package dev.remo.remo.Mappers;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import dev.remo.remo.Models.Forum.Review;
import dev.remo.remo.Models.Forum.ReviewDO;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Request.CreateOrUpdateReviewRequest;
import dev.remo.remo.Models.Users.User;
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
                .build();
    }

    public Review convertReviewDOToReview(ReviewDO reviewDO) {

        return Review.builder().id(reviewDO.getId().toString())
                .motorcycleModel(MotorcycleModel.builder().id(reviewDO.getMotorcycleModelId()).build())
                .user(User.builder().id(reviewDO.getUserId()).build())
                .review(reviewDO.getReview())
                .build();
    }
}
