package dev.remo.remo.Service.Forum;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Mappers.ForumMapper;
import dev.remo.remo.Models.Forum.Review;
import dev.remo.remo.Models.Forum.ReviewDO;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Request.CreateOrUpdateReviewRequest;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Repository.Forum.ForumRepository;
import dev.remo.remo.Service.Auth.AuthService;
import dev.remo.remo.Service.MotorcycleModel.MotorcycleModelService;
import dev.remo.remo.Utils.Exception.NotFoundResourceException;
import io.micrometer.common.util.StringUtils;

public class ForumServiceImpl implements ForumService {

    private static final Logger logger = LoggerFactory.getLogger(ForumServiceImpl.class);

    @Autowired
    ForumRepository forumRepository;

    @Autowired
    MotorcycleModelService motorcycleModelService;

    @Autowired
    AuthService userService;

    @Autowired
    ForumMapper forumMapper;

    public ReviewDO getReviewbyId(String reviewId) {
        return forumRepository.getReviewById(new ObjectId(reviewId)).orElseThrow(() -> {
            return new NotFoundResourceException("Review not found");
        });
    }

    @Transactional
    public void createOrUpdateReview(MultipartFile image, CreateOrUpdateReviewRequest createOrUpdateReviewRequest) {
        logger.info("Creating or updating review: " + createOrUpdateReviewRequest.toString());
        Review review = ForumMapper.toDomain(createOrUpdateReviewRequest);

        User currentUser = userService.getCurrentUser();

        // Update operation if ID is present
        if (StringUtils.isNotBlank(review.getId())) {
            ReviewDO existingReview = getReviewbyId(review.getId());
            userService.validateUser(review.getId());

            String existingImageId = createOrUpdateReviewRequest.getExistingImageIds();
            String oldImageId = existingReview.getImageId();

            if (StringUtils.isBlank(existingImageId)) {
                forumRepository.deleteReviewImage(oldImageId);
            } else if (!existingImageId.equals(oldImageId)) {
                throw new NotFoundResourceException("Invalid image ID detected: " + existingImageId);
            } else {
                review.setImageId(existingImageId);
            }
        } 

        MotorcycleModel motorcycleModel = motorcycleModelService.getMotorcycleByBrandAndModel(
                review.getMotorcycleModel().getBrand(),
                review.getMotorcycleModel().getModel());

        review.setUser(currentUser);
        review.setMotorcycleModel(motorcycleModel);

        String newImageId = forumRepository.uploadFiles(image);
        logger.info("New image ID: " + newImageId);
        review.setImageId(newImageId);

        ReviewDO reviewDO = forumRepository.createOrUpdateReview(forumMapper.convertReviewToReviewDO(review));
        motorcycleModel.getReviews().add(forumMapper.convertReviewDOToReview(reviewDO));
        motorcycleModelService.updateMotorcycleModelReviewList(motorcycleModel);

        logger.info("Review saved: " + reviewDO.toString());
    }

    @Transactional
    public void deleteReviewById(String reviewId) {
        logger.info("Deleting review by ID: " + reviewId);
        ReviewDO existingReview = getReviewbyId(reviewId);
        userService.validateUser(existingReview.getUserId());

        forumRepository.deleteReviewImage(existingReview.getImageId());
        logger.info("Deleted reviewImage: " + existingReview.getImageId());
        forumRepository.deleteReviewById(new ObjectId(reviewId));
        logger.info("Deleted review: " + reviewId);
        motorcycleModelService.removeReviewIdListById(existingReview.getMotorcycleModelId(), reviewId);
        logger.info(
                "Review ID removed from motorcycle model (" + existingReview.getMotorcycleModelId() + "): " + reviewId);
    }
}
