package dev.remo.remo.Service.Forum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Mappers.ForumMapper;
import dev.remo.remo.Models.Forum.Review;
import dev.remo.remo.Models.Forum.ReviewDO;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Request.CreateOrUpdateReviewRequest;
import dev.remo.remo.Models.Response.ReviewCategoryUserViewResponse;
import dev.remo.remo.Models.Response.ReviewCategoryViewResponse;
import dev.remo.remo.Models.Response.ReviewUserView;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Repository.Forum.ForumRepository;
import dev.remo.remo.Service.Auth.AuthService;
import dev.remo.remo.Service.MotorcycleModel.MotorcycleModelService;
import dev.remo.remo.Service.User.UserService;
import dev.remo.remo.Utils.Exception.NotFoundResourceException;
import io.micrometer.common.util.StringUtils;

public class ForumServiceImpl implements ForumService {

    private static final Logger logger = LoggerFactory.getLogger(ForumServiceImpl.class);

    @Autowired
    ForumRepository forumRepository;

    @Autowired
    MotorcycleModelService motorcycleModelService;

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

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

        User currentUser = authService.getCurrentUser();

        // Update operation if ID is present
        if (StringUtils.isNotBlank(review.getId())) {
            ReviewDO existingReview = getReviewbyId(review.getId());
            authService.validateUser(review.getId());

            String existingImageId = createOrUpdateReviewRequest.getExistingImageIds();
            String oldImageId = existingReview.getImageId();

            if (StringUtils.isBlank(existingImageId)) {
                forumRepository.deleteReviewImage(new ObjectId(oldImageId));
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
        authService.validateUser(existingReview.getUserId());

        forumRepository.deleteReviewImage(new ObjectId(existingReview.getImageId()));
        logger.info("Deleted reviewImage: " + existingReview.getImageId());
        motorcycleModelService.removeReviewIdListById(existingReview.getMotorcycleModelId(), reviewId);
        logger.info(
                "Review ID removed from motorcycle model (" + existingReview.getMotorcycleModelId() + "): " + reviewId);
        forumRepository.deleteReviewById(new ObjectId(reviewId));
        logger.info("Deleted review: " + reviewId);
    }

    public ReviewUserView getReviewById(String reviewId) {
        ReviewDO reviewDO = getReviewbyId(reviewId);
        MotorcycleModel motorcycleModel = motorcycleModelService
                .getMotorcycleModelById(reviewDO.getMotorcycleModelId());
        User user = userService.getUserById(reviewDO.getUserId());
        return forumMapper.convertReviewDOToUserDTO(reviewDO, motorcycleModel, user);
    }

    public Resource getForumImageById(String id) {
        return motorcycleModelService.getMotorcycleModelImageById(id);
    }

    public Resource getReviewImageById(String id) {
        return forumRepository.getReviewImageById(new ObjectId(id))
                .orElseThrow(() -> new NotFoundResourceException("Image not found"));
    }

    public ReviewCategoryUserViewResponse getAllReviewsByMotorcycleModelId(String motorcycleModelId, int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size,
        Sort.by(Sort.Direction.DESC, "_id"));
        MotorcycleModel motorcycleModel = motorcycleModelService
                .getMotorcycleModelById(motorcycleModelId);
        Page<ReviewUserView> reviewUserViews = forumRepository
                .getReviewsByMotorcycleModelIdWithPaging(motorcycleModelId, pageable).map(
                        reviewDO -> {
                            User user = userService.getUserById(reviewDO.getUserId());
                            return forumMapper.convertReviewDOToUserDTO(reviewDO, motorcycleModel, user);
                        });
        return forumMapper.convertToReviewCategoryUserViewResponse(reviewUserViews, motorcycleModel);
    }

    public List<ReviewUserView> getMyReviews() {
        User currentUser = authService.getCurrentUser();
        return forumRepository.getReviewsByUserId(currentUser.getId()).stream()
                .map(reviewDO -> {
                    MotorcycleModel motorcycleModel = motorcycleModelService
                            .getMotorcycleModelById(reviewDO.getMotorcycleModelId());
                    return forumMapper.convertReviewDOToUserDTO(reviewDO, motorcycleModel, currentUser);
                }).toList();
    }

    public ReviewCategoryViewResponse getAllReview() {
        return forumMapper.convertToReviewCategoryViewResponse(motorcycleModelService.getMotorcycleList());
    }

    public Page<ReviewUserView> getAllReview(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "_id"));
        User currentUser = authService.getCurrentUser();
        Map<String, MotorcycleModel> motorcycleModelCache = new HashMap<String, MotorcycleModel>();

        return forumRepository.getAllReviewsByPage(pageable).map(reviewDO -> {
            MotorcycleModel motorcycleModel = motorcycleModelCache.computeIfAbsent(reviewDO.getMotorcycleModelId(),
                    id -> motorcycleModelService.getMotorcycleModelById(id));
            return forumMapper.convertReviewDOToUserDTO(reviewDO,
                    motorcycleModel, currentUser);
        });
    }
}
