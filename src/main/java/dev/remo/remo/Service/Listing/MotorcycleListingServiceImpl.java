package dev.remo.remo.Service.Listing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import dev.remo.remo.Service.MotorcycleModel.MotorcycleModelService;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Utils.Enum.StatusEnum;
import dev.remo.remo.Utils.Exception.InvalidStatusException;
import dev.remo.remo.Utils.Exception.NotFoundException;
import dev.remo.remo.Utils.Exception.OwnershipNotMatchException;
import io.micrometer.common.util.StringUtils;

public class MotorcycleListingServiceImpl implements MotorcycleListingService {

    private static final Logger logger = LoggerFactory.getLogger(MotorcycleListingServiceImpl.class);

    @Autowired
    MotorcycleModelService motorcycleModelService;

    @Autowired
    UserService userService;

    @Autowired
    MotorListingRepository motorListingRepository;

    @Autowired
    AiChatbotService aiChatbotService;

    @Autowired
    MotorcycleListingMapper motorcycleListingMapper;

    private void verifyOwnership(String listingUserId, String currentUserId) {
        if (!listingUserId.equals(currentUserId)) {
            logger.info(currentUserId + " is not the owner of the listing " + listingUserId);
            throw new OwnershipNotMatchException("You are not the owner of this listing");
        }
        logger.info(currentUserId + " is the owner of the listing ");
    }

    @Transactional
    public void createOrUpdateMotorcycleListing(MultipartFile[] images, CreateOrUpdateListingRequest request,
            String accessToken) {
        MotorcycleListing motorcycleListing = MotorcycleListingMapper.toDomain(request);
        List<String> imageIds = new ArrayList<>();
        logger.info(motorcycleListing.toString());

        User currentUser = userService.getUserByAccessToken(accessToken);

        if (motorcycleListing.getStatus().getPriority() >= StatusEnum.ACTIVE.getPriority()) {
            throw new InvalidStatusException("The listing is already active or sold");
        }

        List<String> removedImageIds = new ArrayList<>();

        if (StringUtils.isNotBlank(motorcycleListing.getId())) {
            MotorcycleListing existingListing = getMotorcycleListingById(motorcycleListing.getId());
            verifyOwnership(existingListing.getUser().getId(), currentUser.getId());

            List<String> oldImageIds = existingListing.getImagesIds();
            List<String> existingImageIds = Optional.ofNullable(request.getExistingImageIds())
                    .orElse(new ArrayList<>());

            for (String id : existingImageIds) {
                if (!oldImageIds.contains(id)) {
                    throw new NotFoundException("Invalid image ID detected: " + id);
                }
            }

            removedImageIds = oldImageIds.stream()
                    .filter(id -> !existingImageIds.contains(id))
                    .toList();

            imageIds.addAll(existingImageIds);
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

    public void deleteMotorcycleListingById(String listingId, String accessToken) {
        User currentUser = userService.getUserByAccessToken(accessToken);
        MotorcycleListing motorcycleListing = getMotorcycleListingById(listingId);
        verifyOwnership(motorcycleListing.getUser().getId(), currentUser.getId());
        motorListingRepository.deleteMotorcycleListingImage(motorcycleListing.getImagesIds());
        motorListingRepository.deleteMotorcycleListingById(new ObjectId(listingId));
    }

    public String predictPrice(PredictPriceRequest request) {
        return aiChatbotService.extractFromResponse(aiChatbotService.buildPrompt(request));
    }

    public MotorcycleListing getMotorcycleListingById(String listingId) {
        MotorcycleListingDO motorcycleListingDO = motorListingRepository.getListingById(new ObjectId(listingId))
                .orElseThrow(() -> new NotFoundException("Listing is not found"));
        return motorcycleListingMapper.convertMotorcycleListingDOToModel(motorcycleListingDO);
    }
}
