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
import dev.remo.remo.Service.MotorcycleModel.MotorcycleModelService;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Utils.Exception.NotFoundException;
import dev.remo.remo.Utils.Exception.OwnershipNotMatchException;
import io.micrometer.common.util.StringUtils;

public class ForumServiceImpl implements ForumService {

    private static final Logger logger = LoggerFactory.getLogger(ForumServiceImpl.class);

    @Autowired
    ForumRepository forumRepository;

    @Autowired
    MotorcycleModelService motorcycleModelService;

    @Autowired
    UserService userService;

    @Autowired
    ForumMapper forumMapper;

    private void validateReviewAndOwnership(ReviewDO existingReview, String currentUserId) {
        if (!existingReview.getUserId().equals(currentUserId)) {
            logger.warn("User {} attempted to modify review {} owned by {}",
                    currentUserId, existingReview.getId(), existingReview.getUserId());
            throw new OwnershipNotMatchException("You don't own this review");
        }
    }

    public ReviewDO getReviewbyId(String reviewId) {
        return forumRepository.getReviewById(new ObjectId(reviewId)).orElseThrow(() -> {
            return new NotFoundException("Review not found");
        });
    }

    @Transactional
    public void createOrUpdateReview(MultipartFile image, CreateOrUpdateReviewRequest createOrUpdateReviewRequest,
            String accessToken) {
        logger.info("Creating or updating review: " + createOrUpdateReviewRequest.toString());
        Review review = ForumMapper.toDomain(createOrUpdateReviewRequest);

        User currentUser = userService.getUserByAccessToken(accessToken);

        // Update operation if ID is present
        if (StringUtils.isNotBlank(review.getId())) {
            ReviewDO existingReview = getReviewbyId(review.getId());
            validateReviewAndOwnership(existingReview, currentUser.getId());

            String existingImageId = createOrUpdateReviewRequest.getExistingImageIds();
            String oldImageId = existingReview.getImageId();

            if (StringUtils.isBlank(existingImageId)) {
                forumRepository.deleteReviewImage(oldImageId);
            } else if (!existingImageId.equals(oldImageId)) {
                throw new NotFoundException("Invalid image ID detected: " + existingImageId);
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
    public void deleteReviewById(String reviewId, String accessToken) {
        logger.info("Deleting review by ID: " + reviewId);
        User currentUser = userService.getUserByAccessToken(accessToken);
        ReviewDO existingReview = getReviewbyId(reviewId);

        validateReviewAndOwnership(existingReview, currentUser.getId());

        forumRepository.deleteReviewImage(existingReview.getImageId());
        logger.info("Deleted reviewImage: " + existingReview.getImageId());
        forumRepository.deleteReviewById(new ObjectId(reviewId));
        logger.info("Deleted review: " + reviewId);
        motorcycleModelService.removeReviewIdListById(existingReview.getMotorcycleModelId(), reviewId);
        logger.info(
                "Review ID removed from motorcycle model (" + existingReview.getMotorcycleModelId() + "): " + reviewId);
    }
}
