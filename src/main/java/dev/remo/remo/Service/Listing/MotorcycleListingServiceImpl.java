package dev.remo.remo.Service.Listing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Mappers.MotorcycleListingMapper;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Request.PredictPriceRequest;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Repository.MotorcycleListing.MotorListingRepository;
import dev.remo.remo.Service.Ai.AiChatbotService;
import dev.remo.remo.Service.Auth.AuthService;
import dev.remo.remo.Service.MotorcycleModel.MotorcycleModelService;
import dev.remo.remo.Utils.Enum.StatusEnum;
import dev.remo.remo.Utils.Exception.InvalidStatusException;
import dev.remo.remo.Utils.Exception.NotFoundResourceException;
import dev.remo.remo.Utils.General.ExtInfoUtil;
import io.micrometer.common.util.StringUtils;

public class MotorcycleListingServiceImpl implements MotorcycleListingService {

    private static final Logger logger = LoggerFactory.getLogger(MotorcycleListingServiceImpl.class);

    @Autowired
    MotorcycleModelService motorcycleModelService;

    @Autowired
    AuthService userService;

    @Autowired
    MotorListingRepository motorListingRepository;

    @Autowired
    AiChatbotService aiChatbotService;

    @Autowired
    MotorcycleListingMapper motorcycleListingMapper;

    @Transactional
    public void createOrUpdateMotorcycleListing(MultipartFile[] images, CreateOrUpdateListingRequest request) {
        MotorcycleListing motorcycleListing = MotorcycleListingMapper.toDomain(request);
        List<String> imageIds = new ArrayList<>();
        logger.info(motorcycleListing.toString());

        if (motorcycleListing.getStatus().getPriority() >= StatusEnum.ACTIVE.getPriority()) {
            throw new InvalidStatusException("The listing is already active or sold");
        }

        List<ObjectId> removedImageIds = new ArrayList<>();
        User currentUser;

        if (StringUtils.isNotBlank(motorcycleListing.getId())) {
            MotorcycleListing existingListing = getMotorcycleListingById(motorcycleListing.getId());
            currentUser = userService.validateUser(existingListing.getUser().getId());

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
            currentUser = userService.getCurrentUser();
        }

        MotorcycleModel motorcycle = motorcycleModelService.getMotorcycleByBrandAndModel(
                motorcycleListing.getMotorcycleModel().getBrand(),
                motorcycleListing.getMotorcycleModel().getModel());

        motorcycleListing.setUser(currentUser);
        motorcycleListing.setMotorcycleModel(motorcycle);
        motorcycleListing.setStatus(StatusEnum.PENDING);

        MotorcycleListingDO motorcycleListingDO = motorcycleListingMapper
                .convertToMotorcycleListingDO(motorcycleListing);

        if (images != null && Arrays.stream(images).anyMatch(file -> file != null && !file.isEmpty())) {
            logger.info("Uploading images: " + images);
            List<String> newImageIds = motorListingRepository.uploadFiles(images);
            imageIds.addAll(newImageIds);
        }

        motorcycleListingDO.setImagesIds(imageIds);
        motorListingRepository.createOrUpdateListing(motorcycleListingDO);

        if (!removedImageIds.isEmpty()) {
            logger.info("Deleting images: " + removedImageIds);
            motorListingRepository.deleteMotorcycleListingImage(removedImageIds);
        }

        logger.info("Listing saved: " + motorcycleListingDO.toString());
    }

    public void deleteMotorcycleListingById(String listingId) {
        MotorcycleListing motorcycleListing = getMotorcycleListingById(listingId);
        userService.validateUser(motorcycleListing.getUser().getId());
        motorListingRepository
                .deleteMotorcycleListingImage(motorcycleListing.getImagesIds().stream().map(ObjectId::new).toList());
        motorListingRepository.deleteMotorcycleListingById(new ObjectId(listingId));
    }

    public void updateMotorcycleListingInspection(MotorcycleListing listing, String inspectionId) {
        userService.validateUser(listing.getUser().getId());
        motorListingRepository.updateMotorcycleListingInspection(new ObjectId(listing.getId()), inspectionId);
    }

    public String predictPrice(PredictPriceRequest request) {
        return aiChatbotService.extractFromResponse(aiChatbotService.buildPrompt(request));
    }

    public MotorcycleListing getMotorcycleListingById(String listingId) {
        MotorcycleListingDO motorcycleListingDO = motorListingRepository.getListingById(new ObjectId(listingId))
                .orElseThrow(() -> new NotFoundResourceException("Listing is not found"));
        return motorcycleListingMapper.convertMotorcycleListingDOToModel(motorcycleListingDO);
    }

    public void updateMotorcycleListingStatus(String listingId, String status, String remark) {
        MotorcycleListing motorcycleListing = getMotorcycleListingById(listingId);
        User currentUser = userService.getCurrentUser();
        StatusEnum statusEnum = StatusEnum.fromCode(status);

        if (statusEnum.getPriority() >= StatusEnum.ACTIVE.getPriority()) {
            throw new InvalidStatusException("The listing is already active or sold");
        }

        Map<String, String> extInfo = ExtInfoUtil.buildExtInfo(currentUser, remark);

        motorListingRepository.updateMotorcycleListingStatus(new ObjectId(motorcycleListing.getId()),
                statusEnum.getCode(), extInfo);

    }
}
