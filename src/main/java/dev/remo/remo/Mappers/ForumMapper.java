package dev.remo.remo.Mappers;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import dev.remo.remo.Models.Forum.Review;
import dev.remo.remo.Models.Forum.ReviewDO;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Request.CreateOrUpdateReviewRequest;
import dev.remo.remo.Models.Response.ReviewCategoryUserViewResponse;
import dev.remo.remo.Models.Response.ReviewCategoryViewResponse;
import dev.remo.remo.Models.Response.ReviewUserView;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Utils.General.DateUtil;
import io.micrometer.common.util.StringUtils;

@Component
public class ForumMapper {

    public static Review toDomain(CreateOrUpdateReviewRequest request) {
        Review.ReviewBuilder builder = Review.builder();

        if (StringUtils.isNotBlank(request.getId())) {
            builder.id(request.getId());
        }

        return builder
                .motorcycleModel(MotorcycleModel.builder()
                        .brand(request.getBrand())
                        .model(request.getModel())
                        .build())
                .review(request.getReview())
                .build();
    }

    public ReviewDO convertReviewToReviewDO(Review review) {
        ReviewDO.ReviewDOBuilder builder = ReviewDO.builder();

        if (StringUtils.isNotBlank(review.getId())) {
            builder.id(new ObjectId(review.getId()));
        }

        return builder
                .motorcycleModelId(review.getMotorcycleModel().getId())
                .imageId(review.getImageId())
                .userId(review.getUser().getId())
                .review(review.getReview())
                .createdAt(DateUtil.getCurrentDateTime())
                .build();
    }

    public Review convertReviewDOToReview(ReviewDO reviewDO) {

        return Review.builder().id(reviewDO.getId().toString())
                .motorcycleModel(MotorcycleModel.builder().id(reviewDO.getMotorcycleModelId()).build())
                .user(User.builder().id(reviewDO.getUserId()).build())
                .review(reviewDO.getReview())
                .createdAt(reviewDO.getCreatedAt())
                .build();
    }

    public ReviewUserView convertReviewDOToUserDTO(ReviewDO reviewDO,MotorcycleModel motorcycleModel, User user) {

        ReviewUserView.ReviewUserViewBuilder builder = ReviewUserView.builder();
        if (StringUtils.isNotBlank(user.getName())) {
            builder.userName(user.getName());
        }
        if (StringUtils.isNotBlank(user.getImageId())) {
            builder.userImageId(user.getImageId());
        }

        return builder
                .id(reviewDO.getId().toString())
                .review(reviewDO.getReview())
                .brand(motorcycleModel.getBrand())
                .model(motorcycleModel.getModel())
                .reviewImageId(reviewDO.getImageId())
                .createdAt(reviewDO.getCreatedAt())
                .build();
    }

    public ReviewCategoryUserViewResponse convertToReviewCategoryUserViewResponse(Page<ReviewUserView> reviewUserViews,
            MotorcycleModel motorcycleModel) {
        return ReviewCategoryUserViewResponse.builder()
                .reviews(reviewUserViews)
                .brand(motorcycleModel.getBrand())
                .model(motorcycleModel.getModel())
                .imageId(motorcycleModel.getImageId())
                .build();
    }

    public ReviewCategoryViewResponse convertToReviewCategoryViewResponse(List<MotorcycleModel> motorcycleModel) {
        
        List<ReviewCategoryUserViewResponse> reviewCategoryUserViewResponseList = new ArrayList<>();
        for (MotorcycleModel model : motorcycleModel) {
            int reviewsCount = 0;
            if (model.getReviews() != null) {
                reviewsCount = model.getReviews().size();
            }
            ReviewCategoryUserViewResponse reviewCategoryUserViewResponse = ReviewCategoryUserViewResponse.builder()
                    .brand(model.getBrand())
                    .model(model.getModel())
                    .imageId(model.getImageId())
                    .reviewsCount(reviewsCount)
                    .build();
            reviewCategoryUserViewResponseList.add(reviewCategoryUserViewResponse);
        }

        return ReviewCategoryViewResponse.builder().reviews(reviewCategoryUserViewResponseList).build();
    }
}
