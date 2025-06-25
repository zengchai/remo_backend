package dev.remo.remo.Service.Inspection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Mappers.InspectionMapper;
import dev.remo.remo.Models.Inspection.Inspection;
import dev.remo.remo.Models.Inspection.InspectionDO;
import dev.remo.remo.Models.Inspection.Shop.Shop;
import dev.remo.remo.Models.Inspection.Shop.ShopDO;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Request.CreateInspectionRequest;
import dev.remo.remo.Models.Request.CreateShopRequest;
import dev.remo.remo.Models.Request.FilterInspectionRequest;
import dev.remo.remo.Models.Request.UpdateInspectionRequest;
import dev.remo.remo.Models.Response.InspectionDetailAdminView;
import dev.remo.remo.Models.Response.InspectionDetailUserView;
import dev.remo.remo.Models.Response.ShopResponse;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Repository.Inspection.InspectionRepository;
import dev.remo.remo.Repository.Shop.ShopRepository;
import dev.remo.remo.Service.Auth.AuthService;
import dev.remo.remo.Service.Listing.MotorcycleListingService;
import dev.remo.remo.Utils.Enum.StatusEnum;
import dev.remo.remo.Utils.Enum.UserRole;
import dev.remo.remo.Utils.Enum.VehicleComponentEnum;
import dev.remo.remo.Utils.Exception.InternalServerErrorException;
import dev.remo.remo.Utils.Exception.InvalidStatusException;
import dev.remo.remo.Utils.Exception.NotFoundResourceException;
import dev.remo.remo.Utils.General.ExtInfoUtil;
import dev.remo.remo.Utils.General.ObjectIdUtil;
import io.micrometer.common.util.StringUtils;

public class InspectionServiceImpl implements InspectionService {

    private static final Logger logger = LoggerFactory.getLogger(InspectionServiceImpl.class);

    @Autowired
    InspectionRepository inspectionRepository;

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    AuthService authService;

    @Autowired
    MotorcycleListingService motorcycleListingService;

    @Autowired
    InspectionMapper inspectionMapper;

    public ShopDO getShopById(String shopId) {
        return shopRepository.getShopById(new ObjectId(shopId)).orElseThrow(() -> {
            return new NotFoundResourceException("Shop not found for ID: " + shopId);
        });
    }

    public Inspection getInspectionById(String inspectionId) {
        return inspectionMapper
                .convertInspectionDOToInspection(inspectionRepository.getInspectionById(new ObjectId(inspectionId))
                        .orElseThrow(
                                () -> new NotFoundResourceException("Inspection not found for ID: " + inspectionId)));
    }

    @Transactional
    public void createInspection(CreateInspectionRequest request) {

        logger.info("Creating inspection: " + request.toString());

        Inspection inspection = inspectionMapper.toDomain(request);
        ShopDO shop = getShopById(request.getShopId());
        MotorcycleListing motorcycleListing = motorcycleListingService
                .getMotorcycleListingById(request.getMotorcycleListingId());

        if (motorcycleListing.getInspection() != null) {
            throw new InvalidStatusException(
                    "Motorcycle listing already has an inspection: " + request.getMotorcycleListingId());
        }

        inspection.setShop(inspectionMapper.convertShopDOToShop(shop));
        inspection.setMotorcycleListing(motorcycleListing);
        inspection.setStatus(StatusEnum.PENDING);

        InspectionDO inspectionDO = inspectionRepository
                .createInspection(inspectionMapper.convertInspectionToInspectionDO(inspection))
                .orElseThrow(() -> new InternalServerErrorException("Failed to create inspection"));
        logger.info("Created inspection: " + inspectionDO.getId());

        motorcycleListingService.updateMotorcycleListingInspection(motorcycleListing, inspectionDO.getId().toString());
        logger.info("Updated motorcycle listing: " + motorcycleListing.getId());
    }

    public void updateInspectionStatus(String inspectionId, String status, String remark) {

        logger.info("Updating inspection status: " + inspectionId + " to " + status);

        Inspection inspection = getInspectionById(inspectionId);
        User currentUser = authService.getCurrentUser();
        StatusEnum statusEnum = StatusEnum.fromCode(status);
        Map<String, String> extInfo = ExtInfoUtil.buildExtInfo(currentUser, remark);

        inspectionRepository.updateInspectionStatus(new ObjectId(inspection.getId()),
                statusEnum.getCode(), extInfo);

        logger.info("Updated inspection status: " + inspection.getId() + " to " + statusEnum.getCode());
    }

    public void updateInspectionReport(String id, UpdateInspectionRequest updateInspectionRequest) {

        logger.info("Updating inspection report: " + id + " to " + updateInspectionRequest.getStatus());

        Inspection inspection = getInspectionById(id);
        User currentUser = authService.getCurrentUser();

        if (inspection.getStatus().getPriority() >= StatusEnum.COMPLETED.getPriority()
                && !updateInspectionRequest.getRetry()) {
            throw new InvalidStatusException("Inspection already completed: " + id);
        }

        Map<String, String> extInfo = ExtInfoUtil.buildExtInfo(currentUser, updateInspectionRequest.getRemark());
        VehicleComponentEnum.validateFlatMap(updateInspectionRequest.getComponentScores());
        Map<String, Map<String, Integer>> componentScores = VehicleComponentEnum
                .groupByCategory(updateInspectionRequest.getComponentScores());

        inspectionRepository.updateInspectionReport(new ObjectId(inspection.getId()),
                StatusEnum.fromCode(updateInspectionRequest.getStatus()).getCode(),
                componentScores, extInfo);

        logger.info("Updated inspection report: " + inspection.getId() + " to " + updateInspectionRequest.getStatus());
    }

    public void createShop(MultipartFile image, CreateShopRequest createShopRequest) {

        logger.info("Creating shop: " + createShopRequest.toString());

        Shop shop = inspectionMapper.convertRequestToDomain(createShopRequest);

        String shopId = shopRepository.uploadFiles(image);
        logger.info("Uploaded shop image with ID: " + shopId);

        shop.setImageId(shopId);
        shopRepository.addShop(inspectionMapper.convertShopToShopDO(shop));
        
        logger.info("Created shop: " + shop.getName());
    }

    public void deleteInspection(String id) {

        logger.info("Deleting inspection: " + id);

        User currentUser = authService.getCurrentUser();
        Inspection inspection = getInspectionById(id);
        MotorcycleListing motorcycleListing = motorcycleListingService
                .getMotorcycleListingById(inspection.getMotorcycleListing().getId());
        authService.validateUser(motorcycleListing.getUser().getId());

        if (inspection.getStatus().getPriority() >= StatusEnum.COMPLETED.getPriority()
                && !currentUser.getRole().contains(UserRole.ADMIN)) {
            throw new InvalidStatusException("Inspection already completed: " + id);
        }

        inspectionRepository.deleteInspection(new ObjectId(inspection.getId()));
        logger.info("Deleted inspection: " + inspection.getId());

        motorcycleListingService.updateMotorcycleListingInspection(motorcycleListing, null);
        logger.info("Updated motorcycle listing: " + motorcycleListing.getId() + " to remove inspection");
        
    }

    public Map<String, String> getInspectionStatusByIds(List<String> id) {

        List<ObjectId> objectIds = id.stream().map(ObjectId::new).toList();
        List<InspectionDO> inspectionDOList = inspectionRepository.getInspectionStatusList(objectIds);
        
        if (inspectionDOList.isEmpty()) {
            throw new NotFoundResourceException("No inspections found for the provided IDs");
        }

        Map<ObjectId, String> map = inspectionDOList.stream()
                .collect(Collectors.toMap(InspectionDO::getId, InspectionDO::getStatus));
        
        return map.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
    }

    public InspectionDetailUserView getInspectionDetailUserViewById(String id) {

        Inspection inspection = getInspectionById(id);
        MotorcycleListing motorcycleListing = motorcycleListingService
                .getMotorcycleListingById(inspection.getMotorcycleListing().getId());

        inspection.setMotorcycleListing(motorcycleListing);

        ShopDO shopDO = getShopById(inspection.getShop().getId());

        return inspectionMapper.convertInspectionToDetailView(inspection, shopDO);
    }

    public List<InspectionDetailUserView> getMyInspection() {

        User currentUser = authService.getCurrentUser();
        List<MotorcycleListing> motorcycleListings = motorcycleListingService
                .getMotorcycleListingByUserId(currentUser.getId());
        Map<String, MotorcycleListing> listingMap = motorcycleListings.stream()
                .collect(Collectors.toMap(MotorcycleListing::getId, m -> m));
        Map<String, ShopDO> shopMap = new HashMap<>();

        List<Inspection> inspectionList = getInspectionsByListingIds(motorcycleListings.stream()
                .map(MotorcycleListing::getId).toList()).stream()
                .map(inspection -> {
                    MotorcycleListing fullListing = listingMap.get(inspection.getMotorcycleListing().getId());
                    inspection.setMotorcycleListing(fullListing);
                    return inspection;
                })
                .collect(Collectors.toList());

        return inspectionList.stream()
                .map(inspection -> {
                    ShopDO shopDO = shopMap.computeIfAbsent(inspection.getShop().getId(),
                            id -> getShopById(id.toString()));
                    return inspectionMapper.convertInspectionToDetailView(inspection, shopDO);
                }).toList();

    }

    public List<Inspection> getInspectionsByListingIds(List<String> listingIds) {
        return inspectionRepository.getInspectionByListingIds(listingIds).stream()
                .map(inspectionMapper::convertInspectionDOToInspection)
                .collect(Collectors.toList());
    }

    public Page<InspectionDetailAdminView> getAllInspectionAdminView(int page, int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "_id"));
        List<InspectionDO> inspectionDOList = new ArrayList<>();
        Map<String, ShopDO> shopMap = new HashMap<>();
        List<InspectionDetailAdminView> inspectionDetailAdminViews = inspectionDOList.stream().map(inspectionDO -> {
            ShopDO shopDO = shopMap.computeIfAbsent(inspectionDO.getShopId(),
                    id -> getShopById(id.toString()));
            return inspectionMapper.convertToAdminView(inspectionDO, shopDO);
        }).collect(Collectors.toList());

        return new PageImpl<InspectionDetailAdminView>(
                inspectionDetailAdminViews, pageable, inspectionDetailAdminViews.size());
    }

    public Page<InspectionDetailAdminView> getAllInspectionByFilter(FilterInspectionRequest filterInspectionRequest,
            int page, int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "_id"));

        List<Criteria> criteriaList = new ArrayList<>();

        String shopId = filterInspectionRequest.getShopId();
        String status = filterInspectionRequest.getStatus();
        String minDate = filterInspectionRequest.getMinDate();
        String maxDate = filterInspectionRequest.getMaxDate();
        String time = filterInspectionRequest.getTime();

        if (StringUtils.isNotBlank(shopId)) {
            criteriaList.add(Criteria.where("shopId").is(shopId));
        }

        if (StringUtils.isNotBlank(minDate) && StringUtils.isNotBlank(maxDate)) {
            criteriaList.add(Criteria.where("date").gte(minDate).lte(maxDate));
        } else if (StringUtils.isNotBlank(minDate)) {
            criteriaList.add(Criteria.where("date").gte(minDate));
        } else if (StringUtils.isNotBlank(maxDate)) {
            criteriaList.add(Criteria.where("date").lte(maxDate));
        }

        if (StringUtils.isNotBlank(time)) {
            criteriaList.add(Criteria.where("time").is(time));
        }

        if (StringUtils.isNotBlank(status)) {
            criteriaList.add(Criteria.where("status").is(StatusEnum.fromCode(status).getCode()));
        }

        Page<InspectionDO> inspectionDOList = inspectionRepository.getAllInspection(criteriaList, pageable);

        Map<String, ShopDO> shopMap = new HashMap<>();
        
        List<InspectionDetailAdminView> inspectionDetailAdminViews = inspectionDOList.getContent().stream().map(inspectionDO -> {
            ShopDO shopDO = shopMap.computeIfAbsent(inspectionDO.getShopId(),
                    id -> getShopById(id.toString()));
            return inspectionMapper.convertToAdminView(inspectionDO, shopDO);
        }).collect(Collectors.toList());

        return new PageImpl<InspectionDetailAdminView>(
                inspectionDetailAdminViews, inspectionDOList.getPageable(), inspectionDOList.getTotalElements());
    }

    public Resource getShopImage(String id) {
        ObjectId objectId = ObjectIdUtil.validateObjectId(id);
        return shopRepository.getShopImageById(objectId)
                .orElseThrow(() -> new NotFoundResourceException("Shop image not found for ID: " + id));
    }

    public List<ShopResponse> getAllShops() {
        List<ShopDO> shopDOList = shopRepository.getAllShops();
        return shopDOList.stream()
                .map(shopDO -> {
                    return ShopResponse.builder()
                            .id(shopDO.getId().toString())
                            .name(shopDO.getName())
                            .address(shopDO.getAddress())
                            .mapUrl(shopDO.getMapUrl())
                            .imageId(shopDO.getImageId())
                            .build();
                }).collect(Collectors.toList());

    }

    public void deleteInspectionByUserId(String userId) {

        logger.info("Deleting all inspections for user: " + userId);

        User currentUser = authService.validateUser(userId);
        List<MotorcycleListing> motorcycleListings = motorcycleListingService
                .getMotorcycleListingByUserId(currentUser.getId());
        if (motorcycleListings.isEmpty()) {
            logger.info("No motorcycle listings found for user: " + userId);
            return;
        }

        List<String> listingIds = motorcycleListings.stream()
                .map(MotorcycleListing::getId).toList();
        List<Inspection> inspections = getInspectionsByListingIds(listingIds);

        if (inspections.isEmpty()) {
            logger.info("No inspections found for user: " + userId);
            return;
        }

        for (Inspection inspection : inspections) {
            deleteInspection(inspection.getId());
            logger.info("Deleted inspection: " + inspection.getId());
        }
        
        logger.info("All inspections deleted for user: " + userId);
    }
}
