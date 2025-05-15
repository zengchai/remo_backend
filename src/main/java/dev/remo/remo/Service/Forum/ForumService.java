package dev.remo.remo.Service.Forum;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.CreateOrUpdateReviewRequest;
import dev.remo.remo.Models.Response.ReviewCategoryUserViewResponse;
import dev.remo.remo.Models.Response.ReviewCategoryViewResponse;
import dev.remo.remo.Models.Response.ReviewUserView;

public interface ForumService {
    void createOrUpdateReview(MultipartFile image, CreateOrUpdateReviewRequest createOrUpdateReviewRequest);

    void deleteReviewById(String reviewId);

    ReviewUserView getReviewById(String reviewId);

    ReviewCategoryViewResponse getAllReview();

    Resource getReviewImageById(String id);

    Resource getForumImageById(String id);

    ReviewCategoryUserViewResponse getAllReviewsByMotorcycleModelId(String motorcycleModelId, int page,
            int size);
}
