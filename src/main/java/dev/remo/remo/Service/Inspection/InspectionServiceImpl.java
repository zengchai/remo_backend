package dev.remo.remo.Service.Inspection;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import dev.remo.remo.Models.Request.UpdateInspectionRequest;
import dev.remo.remo.Models.Response.InspectionDetailUserView;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Repository.Inspection.InspectionRepository;
import dev.remo.remo.Repository.Shop.ShopRepository;
import dev.remo.remo.Service.Auth.AuthService;
import dev.remo.remo.Service.Listing.MotorcycleListingService;
import dev.remo.remo.Utils.Enum.StatusEnum;
import dev.remo.remo.Utils.Enum.VehicleComponentEnum;
import dev.remo.remo.Utils.Exception.InternalServerErrorException;
import dev.remo.remo.Utils.Exception.InvalidStatusException;
import dev.remo.remo.Utils.Exception.NotFoundResourceException;
import dev.remo.remo.Utils.General.ExtInfoUtil;

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
        shop.setImageId(shopId);

        shopRepository.addShop(inspectionMapper.convertShopToShopDO(shop));
        logger.info("Created shop: " + shop.getName());
    }

    public void deleteInspection(String id) {
        logger.info("Deleting inspection: " + id);
        Inspection inspection = getInspectionById(id);
        MotorcycleListing motorcycleListing = motorcycleListingService
                .getMotorcycleListingById(inspection.getMotorcycleListing().getId());
        authService.validateUser(motorcycleListing.getUser().getId());

        if (inspection.getStatus().getPriority() >= StatusEnum.COMPLETED.getPriority()) {
            throw new InvalidStatusException("Inspection already completed: " + id);
        }

        inspectionRepository.deleteInspection(new ObjectId(inspection.getId()));
        logger.info("Deleted inspection: " + inspection.getId());
    }

    public Map<String, String> getInspectionStatusByIds(List<String> id) {
        logger.info("Getting inspection status for IDs: " + id);
        List<ObjectId> objectIds = id.stream().map(ObjectId::new).toList();
        Map<ObjectId, String> map = inspectionRepository.getInspectionStatusList(objectIds).orElseThrow(() -> {
            return new NotFoundResourceException("Inspection not found for IDs: " + id);
        }).stream()
                .collect(Collectors.toMap(InspectionDO::getId, InspectionDO::getStatus));
        return map.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
    }

    public InspectionDetailUserView getInspectionDetailUserViewById(String id) {
        logger.info("Getting inspection detail view for ID: " + id);
        Inspection inspection = getInspectionById(id);
        return inspectionMapper.convertInspectionToDetailView(inspection);
    }
}
