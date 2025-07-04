package dev.remo.remo.Service.Listing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

import dev.remo.remo.Mappers.MotorcycleListingMapper;
import dev.remo.remo.Models.General.ModelCount;
import dev.remo.remo.Models.General.MonthCount;
import dev.remo.remo.Models.General.StatusCount;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Request.FilterListingRequest;
import dev.remo.remo.Models.Request.PredictPriceRequest;
import dev.remo.remo.Models.Response.MotorcycleListingDetailUserView;
import dev.remo.remo.Models.Response.MotorcycleListingUserView;
import dev.remo.remo.Models.Response.MotorcycleModelList;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Repository.MotorcycleListing.MotorListingRepository;
import dev.remo.remo.Service.Ai.AiChatbotService;
import dev.remo.remo.Service.Auth.AuthService;
import dev.remo.remo.Service.MotorcycleModel.MotorcycleModelService;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Utils.Enum.StatusEnum;
import dev.remo.remo.Utils.Enum.UserRole;
import dev.remo.remo.Utils.Exception.InvalidStatusException;
import dev.remo.remo.Utils.Exception.NotFoundResourceException;
import dev.remo.remo.Utils.General.ExtInfoUtil;
import dev.remo.remo.Utils.General.ObjectIdUtil;
import io.micrometer.common.util.StringUtils;

public class MotorcycleListingServiceImpl implements MotorcycleListingService {

    private static final Logger logger = LoggerFactory.getLogger(MotorcycleListingServiceImpl.class);

    @Autowired
    MotorcycleModelService motorcycleModelService;

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    MotorListingRepository motorListingRepository;

    @Autowired
    AiChatbotService aiChatbotService;

    @Autowired
    MotorcycleListingMapper motorcycleListingMapper;

    @Transactional
    public void createOrUpdateMotorcycleListing(MultipartFile[] images, CreateOrUpdateListingRequest request) {

        logger.info("Creating or updating motorcycle listing: " + request.toString());

        MotorcycleListing motorcycleListing = MotorcycleListingMapper.toDomain(request);
        List<String> imageIds = new ArrayList<>();

        if (motorcycleListing.getStatus().getPriority() >= StatusEnum.ACTIVE.getPriority()) {
            throw new InvalidStatusException("The listing is already active or sold");
        }

        List<ObjectId> removedImageIds = new ArrayList<>();
        User currentUser;

        if (StringUtils.isNotBlank(motorcycleListing.getId())) {

            logger.info("Updating listing: " + motorcycleListing.getId());

            MotorcycleListing existingListing = getMotorcycleListingById(motorcycleListing.getId());
            currentUser = authService.validateUser(existingListing.getUser().getId());
            Map<String, String> extInfo = ExtInfoUtil.buildExtInfo(currentUser,
                    "Update listing: " + motorcycleListing.getId());

            if (existingListing.getInspection() != null) {
                motorcycleListing.setInspection(existingListing.getInspection());
            }
            motorcycleListing.setCreatedAt(existingListing.getCreatedAt());
            motorcycleListing.setExtInfo(extInfo);

            List<String> oldImageIds = existingListing.getImagesIds();
            List<String> existingImageIds = Optional.ofNullable(request.getExistingImageIds())
                    .orElse(new ArrayList<>());

            for (String id : existingImageIds) {
                if (!oldImageIds.contains(id)) {
                    throw new NotFoundResourceException("Invalid image ID detected: " + id);
                }
            }

            removedImageIds = oldImageIds.stream()
                    .filter(id -> !existingImageIds.contains(id))
                    .map(ObjectId::new)
                    .toList();
            imageIds.addAll(existingImageIds);
        } else {
            currentUser = authService.getCurrentUser();
        }

        MotorcycleModel motorcycle = motorcycleModelService.getMotorcycleByBrandAndModel(
                motorcycleListing.getMotorcycleModel().getBrand(),
                motorcycleListing.getMotorcycleModel().getModel());

        motorcycleListing.setUser(currentUser);
        motorcycleListing.setMotorcycleModel(motorcycle);

        MotorcycleListingDO motorcycleListingDO = motorcycleListingMapper
                .convertToMotorcycleListingDO(motorcycleListing);

        if (images != null && Arrays.stream(images).anyMatch(file -> file != null && !file.isEmpty())) {

            logger.info("Uploading images for listing: " + motorcycleListingDO.getId());
            List<String> newImageIds = motorListingRepository.uploadFiles(images);
            imageIds.addAll(newImageIds);
        }

        motorcycleListingDO.setImagesIds(imageIds);
        motorListingRepository.createOrUpdateListing(motorcycleListingDO);

        if (!removedImageIds.isEmpty()) {

            motorListingRepository.deleteMotorcycleListingImage(removedImageIds);
            logger.info("Removed images from listing: " + motorcycleListingDO.getId() + " - " + removedImageIds);
        }

        logger.info("Listing saved: " + motorcycleListingDO.toString());
    }

    public void createMotorcycleModel(String brand, String model, MultipartFile image) {

        logger.info("Creating motorcycle model: " + brand + " " + model);

        motorcycleModelService.createMotorcycleModel(brand, model, image);

        logger.info("Motorcycle model created: " + brand + " " + model);
    }

    @Transactional
    public void deleteMotorcycleListingById(String listingId) {

        logger.info("Deleting motorcycle listing: " + listingId);

        MotorcycleListing motorcycleListing = getMotorcycleListingById(listingId);
        authService.validateUser(motorcycleListing.getUser().getId());

        motorListingRepository
                .deleteMotorcycleListingImage(motorcycleListing.getImagesIds().stream().map(ObjectId::new).toList());
        logger.info("Deleting images for listing: " + listingId);

        userService.removeFavouriteMotorcycleListing(listingId);
        logger.info("Removing listing from user's favourites: " + listingId);

        motorListingRepository.deleteMotorcycleListingById(new ObjectId(listingId));
        logger.info("Listing deleted: " + listingId);
    }

    public void updateMotorcycleListingInspection(MotorcycleListing listing, String inspectionId) {

        logger.info(
                "Updating motorcycle listing inspection: " + listing.getId() + " with inspection ID: " + inspectionId);

        authService.validateUser(listing.getUser().getId());
        motorListingRepository.updateMotorcycleListingInspection(new ObjectId(listing.getId()), inspectionId);

        logger.info(
                "Motorcycle listing inspection updated: " + listing.getId() + " with inspection ID: " + inspectionId);
    }

    public String predictPrice(PredictPriceRequest request) {
        return aiChatbotService.extractFromResponse(aiChatbotService.buildPrompt(request));
    }

    public MotorcycleListing getMotorcycleListingById(String listingId) {

        MotorcycleListingDO motorcycleListingDO = motorListingRepository
                .getListingById(ObjectIdUtil.validateObjectId(listingId))
                .orElseThrow(() -> new NotFoundResourceException("Listing is not found"));
        MotorcycleModel motorcycleModel = motorcycleModelService
                .getMotorcycleModelById(motorcycleListingDO.getMotorcycleId());
        MotorcycleListing motorcycleListing = motorcycleListingMapper
                .convertMotorcycleListingDOToModel(motorcycleListingDO);
        motorcycleListing.setMotorcycleModel(motorcycleModel);

        return motorcycleListing;
    }

    public void updateMotorcycleListingStatus(String listingId, String status, String remark) {

        logger.info("Updating motorcycle listing status: " + listingId + " to " + status);

        MotorcycleListing motorcycleListing = getMotorcycleListingById(listingId);
        User currentUser = authService.getCurrentUser();
        StatusEnum statusEnum = StatusEnum.fromCode(status);

        if ((motorcycleListing.getStatus().getPriority() >= StatusEnum.ACTIVE.getPriority()
                && !status.equals(StatusEnum.NOT_ACTIVE.getCode())) ||
                motorcycleListing.getStatus().getPriority() >= StatusEnum.NOT_ACTIVE.getPriority()) {
            throw new InvalidStatusException("The listing is already active or sold");
        }

        Map<String, String> extInfo = ExtInfoUtil.buildExtInfo(currentUser, remark);

        motorListingRepository.updateMotorcycleListingStatus(new ObjectId(motorcycleListing.getId()),
                statusEnum.getCode(), extInfo);

        logger.info("Listing status updated: " + motorcycleListing.getId() + " to " + statusEnum.getCode());

    }

    public Page<MotorcycleListingUserView> getMyMotorcycleListing(int page, int size) {

        User currentUser = authService.getCurrentUser();
        Pageable pageRequest = PageRequest.of(page, size);
        Map<String, MotorcycleModel> motorcycleModelMap = new HashMap<String, MotorcycleModel>();
        Page<MotorcycleListingDO> motorcycleListingDOList = motorListingRepository
                .getMotorcycleListingByUserId(currentUser.getId(), pageRequest);

        List<MotorcycleListingUserView> motorcycleListingUserViewList = motorcycleListingDOList.getContent()
                .stream()
                .map(listingDO -> {

                    MotorcycleModel mm = motorcycleModelMap.computeIfAbsent(
                            listingDO.getMotorcycleId(),
                            id -> motorcycleModelService.getMotorcycleModelById(id));

                    return motorcycleListingMapper.convertToUserDTOView(listingDO, mm);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(motorcycleListingUserViewList, motorcycleListingDOList.getPageable(),
                motorcycleListingDOList.getTotalElements());
    }

    public Page<MotorcycleListingUserView> getMyFavouriteListings(int page, int size) {

        User currentUser = authService.getCurrentUser();
        Pageable pageRequest = PageRequest.of(page, size);
        Map<String, MotorcycleModel> motorcycleModelMap = new HashMap<String, MotorcycleModel>();
        List<ObjectId> listingIds = currentUser.getFavouriteListingIds()
                .stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());

        if (currentUser.getFavouriteListingIds() == null || currentUser.getFavouriteListingIds().isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        }

        Page<MotorcycleListingDO> motorcycleListingDO = motorListingRepository.getListingsById(listingIds, pageRequest);

        List<MotorcycleListingUserView> motorcycleListingUserViewList = motorcycleListingDO.getContent()
                .stream()
                .map(listingDO -> {

                    MotorcycleModel mm = motorcycleModelMap.computeIfAbsent(
                            listingDO.getMotorcycleId(),
                            id -> motorcycleModelService.getMotorcycleModelById(id));

                    return motorcycleListingMapper.convertToUserDTOView(listingDO, mm);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(motorcycleListingUserViewList, motorcycleListingDO.getPageable(),
                motorcycleListingDO.getTotalElements());
    }

    public List<MotorcycleListing> getMotorcycleListingByUserId(String userId) {

        List<MotorcycleListing> motorcycleListings = motorListingRepository.getMotorcycleListingByUserId(userId)
                .stream()
                .map(motorcycleListingMapper::convertMotorcycleListingDOToModel)
                .collect(Collectors.toList());
        Map<String, MotorcycleModel> motorcycleModelMap = new HashMap<String, MotorcycleModel>();

        return motorcycleListings.stream()
                .peek(listing -> {
                    MotorcycleModel motorcycleModel = motorcycleModelMap.computeIfAbsent(
                            listing.getMotorcycleModel().getId(),
                            id -> motorcycleModelService.getMotorcycleModelById(id));
                    listing.setMotorcycleModel(motorcycleModel);
                })
                .collect(Collectors.toList());
    }

    public Page<MotorcycleListingUserView> getMotorcycleListingListUserView(int page, int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "_id"));
        User currentUser = authService.getCurrentUser();
        List<MotorcycleListingDO> motorcycleListingDOs = new ArrayList<>();
        List<MotorcycleListingUserView> listingList = new ArrayList<>();

        if (currentUser.getRole().contains(UserRole.ADMIN)) {
            // Admins can see all listings
            motorcycleListingDOs = motorListingRepository.getAllListingsForAdmin(pageable);
        } else {
            // Regular users see only active listings
            motorcycleListingDOs = motorListingRepository.getAllListingsForUser(pageable);
        }

        if (!motorcycleListingDOs.isEmpty()) {
            listingList = motorcycleListingDOs
                    .stream()
                    .map(listingDO -> {
                        MotorcycleModel motorcycleModel = motorcycleModelService
                                .getMotorcycleModelById(listingDO.getMotorcycleId());

                        return motorcycleListingMapper.convertToUserDTOView(listingDO, motorcycleModel);
                    })
                    .collect(Collectors.toList());
        }

        return new PageImpl<>(listingList, pageable, motorcycleListingDOs.size());
    }

    public MotorcycleListingDetailUserView getMotorcycleListingDetailUserView(String listingId) {

        User currentUser = authService.getCurrentUser();
        MotorcycleListing motorcycleListing = getMotorcycleListingById(listingId);
        Boolean isFavourite = false;

        if ( currentUser.getAuthorities().stream().noneMatch(role -> !role.getAuthority().equals("ROLE_ADMIN"))) {
            return motorcycleListingMapper.convertToDetailUserDTOView(motorcycleListing, isFavourite);
        }

        if (currentUser.getFavouriteListingIds() != null) {
            isFavourite = currentUser.getFavouriteListingIds().stream()
                    .anyMatch(listingId::equals);
        }

        List<String> imageIds = motorcycleListing.getImagesIds();
        if (imageIds != null && !imageIds.isEmpty()) {
            imageIds.remove(0);
            Collections.reverse(imageIds);
        }

        return motorcycleListingMapper.convertToDetailUserDTOView(motorcycleListing,
                isFavourite);
    }

    public MotorcycleListingDetailUserView getMotorcycleListingForUpdate(String listingId) {

        User currentUser = authService.getCurrentUser();
        authService.validateUser(currentUser.getId());
        MotorcycleListing motorcycleListing = getMotorcycleListingById(listingId);

        return motorcycleListingMapper.convertToDetailUserDTOView(motorcycleListing,
                false);
    }

    public Resource getMotorcycleListingImageById(String id) {
        ObjectId objectId = ObjectIdUtil.validateObjectId(id);
        return motorListingRepository.getMotorcycleListingImageById(objectId)
                .orElseThrow(() -> new NotFoundResourceException("Image not found"));
    }

    public Page<MotorcycleListingUserView> filterListings(FilterListingRequest filterRequest, int page, int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "_id"));

        List<Criteria> criteriaList = new ArrayList<>();
        Map<String, MotorcycleModel> motorcycleModelMap = new HashMap<String, MotorcycleModel>();
        String brand = filterRequest.getBrand();
        String model = filterRequest.getModel();
        String minManufacturedYear = filterRequest.getMinYear();
        String maxManufacturedYear = filterRequest.getMaxYear();
        String minPrice = filterRequest.getMinPrice();
        String maxPrice = filterRequest.getMaxPrice();
        String status = filterRequest.getStatus();

        if (StringUtils.isNotBlank(brand)) {

            List<MotorcycleModel> motorcycleModels = new ArrayList<>();
            if (StringUtils.isNotBlank(model)) {
                motorcycleModels.add(motorcycleModelService
                        .getMotorcycleByBrandAndModel(brand, model));
            } else {
                motorcycleModels = motorcycleModelService
                        .getMotorcycleByBrand(brand);
            }
            System.err.println("Motorcycle Models: " + motorcycleModels);
            criteriaList.add(Criteria.where("motorcycleId").in(
                    motorcycleModels.stream()
                            .map(MotorcycleModel::getId)
                            .collect(Collectors.toList())));

            motorcycleModelMap.putAll(motorcycleModels.stream()
                    .collect(Collectors.toMap(MotorcycleModel::getId, m -> m)));
        }

        if (StringUtils.isNotBlank(minManufacturedYear) && StringUtils.isNotBlank(maxManufacturedYear)) {
            criteriaList.add(Criteria.where("manufacturedYear").gte(minManufacturedYear).lte(maxManufacturedYear));
        } else if (minManufacturedYear != null) {
            criteriaList.add(Criteria.where("manufacturedYear").gte(minManufacturedYear));
        } else if (maxManufacturedYear != null) {
            criteriaList.add(Criteria.where("manufacturedYear").lte(maxManufacturedYear));
        }

        if (StringUtils.isNotBlank(minPrice) && StringUtils.isNotBlank(maxPrice)) {
            criteriaList.add(Criteria.where("price").gte(Integer.parseInt(minPrice)).lte(Integer.parseInt(maxPrice)));
        } else if (StringUtils.isNotBlank(minPrice)) {
            criteriaList.add(Criteria.where("price").gte(Integer.parseInt(minPrice)));
        } else if (StringUtils.isNotBlank(maxPrice)) {
            criteriaList.add(Criteria.where("price").lte(Integer.parseInt(maxPrice)));
        }

        if (authService.getCurrentUser().getRole().contains(UserRole.ADMIN)) {

            if (StringUtils.isNotBlank(status)) {
                criteriaList.add(Criteria.where("status").is(StatusEnum.fromCode(status).getCode()));
            }
        } else {
            criteriaList.add(Criteria.where("status").is(StatusEnum.ACTIVE.getCode()));
        }

        Page<MotorcycleListingDO> motorcycleListingPage = motorListingRepository
                .getMotorcycleListingByFilter(criteriaList, pageable);
        List<MotorcycleListingUserView> motorcycleListingUserView = motorcycleListingPage.getContent()
                .stream()
                .map(listingDO -> {

                    MotorcycleModel mm = motorcycleModelMap.computeIfAbsent(
                            listingDO.getMotorcycleId(),
                            id -> motorcycleModelService.getMotorcycleModelById(id));

                    return motorcycleListingMapper.convertToUserDTOView(listingDO, mm);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(motorcycleListingUserView,
                motorcycleListingPage.getPageable(),
                motorcycleListingPage.getTotalElements());
    }

    public Boolean favouriteMotorcycleListing(String motorcycleListingId) {
        MotorcycleListing motorcycleListing = getMotorcycleListingById(motorcycleListingId);
        return userService.favouriteMotorcycleListing(motorcycleListing.getId());
    }

    public MotorcycleModelList getMotorcycleModelList() {

        List<MotorcycleModel> motorcycleModels = motorcycleModelService.getMotorcycleList();
        Map<String, Map<String, String>> motorcycleModelMap = new HashMap<>();

        for (MotorcycleModel motorcycleModel : motorcycleModels) {
            String brand = motorcycleModel.getBrand();
            String model = motorcycleModel.getModel();
            String id = motorcycleModel.getId();

            if (!motorcycleModelMap.containsKey(id)) {
                motorcycleModelMap.put(id, new HashMap<>());
            }
            motorcycleModelMap.get(id).put(brand, model);
        }

        return MotorcycleModelList.builder()
                .motorcycleModels(motorcycleModelMap)
                .build();
    }

    public int getMotorcycleListingCount() {
        return motorListingRepository.getMotorcycleListingCount();
    }

    public void deleteMotorcycleListingsByUserId(String userId) {

        logger.info("Deleting all motorcycle listings for user: " + userId);

        User user = authService.validateUser(userId);
        List<MotorcycleListing> motorcycleListings = getMotorcycleListingByUserId(user.getId());

        if (motorcycleListings.isEmpty()) {
            logger.info("No listings found for user: " + user.getId());
            return;
        }

        for (MotorcycleListing listing : motorcycleListings) {
            deleteMotorcycleListingById(listing.getId());
            logger.info("Deleted listing: " + listing.getId());
        }

        logger.info("All listings deleted for user: " + user.getId());
    }

    public List<MonthCount> getNewListingsPerMonth() {
        return motorListingRepository.getNewListingsPerMonth();
    }

    public List<StatusCount> getMotorcycleListingStatusCount() {
        return motorListingRepository.getListingCountByStatus();
    }

    public List<ModelCount> getListingCountAndAvgPriceByMotorcycleId() {

        List<ModelCount> modelCounts = motorListingRepository.getListingCountAndAvgPriceByMotorcycleId();

        for (ModelCount modelCount : modelCounts) {
            MotorcycleModel motorcycleModel = motorcycleModelService
                    .getMotorcycleModelById(modelCount.getMotorcycleId());
            modelCount.setBrand(motorcycleModel.getBrand());
            modelCount.setModel(motorcycleModel.getModel());
        }

        return modelCounts.stream()
                .filter(modelCount -> modelCount.getCount() > 0)
                .collect(Collectors.toList());
    }
}
