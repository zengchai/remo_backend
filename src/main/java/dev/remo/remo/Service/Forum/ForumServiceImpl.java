package dev.remo.remo.Service.Forum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private void updateMotorcycleModelReviews(MotorcycleModel model, ReviewDO review) {
        model.getReviews().add(forumMapper.convertReviewDOToReview(review));
        motorcycleModelService.updateMotorcycleModelReviewList(model);
    }

    @Transactional
    public void createOrUpdateReview(MultipartFile image, CreateOrUpdateReviewRequest createOrUpdateReviewRequest,
            String accessToken) {
        Review review = ForumMapper.toDomain(createOrUpdateReviewRequest);
        logger.info(review.toString());

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

        logger.info("Uploading image: " + image.getOriginalFilename());
        String newImageId = forumRepository.uploadFiles(image);
        review.setImageId(newImageId);

        ReviewDO reviewDO = forumRepository.createOrUpdateReview(forumMapper.convertReviewToReviewDO(review));

        updateMotorcycleModelReviews(motorcycleModel, reviewDO);
        logger.info("Review saved: " + reviewDO.toString());
    }

    @Transactional
    public void deleteReviewById(String reviewId, String accessToken) {

        User currentUser = userService.getUserByAccessToken(accessToken);
        ReviewDO existingReview = getReviewbyId(reviewId);

        validateReviewAndOwnership(existingReview, currentUser.getId());

        logger.info("Deleting review: " + existingReview.toString());
        forumRepository.deleteReviewImage(existingReview.getImageId());
        forumRepository.deleteReviewById(new ObjectId(reviewId));
        motorcycleModelService.removeReviewIdListById(existingReview.getMotorcycleModelId(), reviewId);
    }
}
