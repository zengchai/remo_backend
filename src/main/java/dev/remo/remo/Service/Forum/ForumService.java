package dev.remo.remo.Service.Forum;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
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

    Page<ReviewUserView> getAllReview(int page, int size);

    List<ReviewUserView> getMyReviews();

    ReviewCategoryUserViewResponse getAllReviewsByMotorcycleModelId(String motorcycleModelId, int page,
            int size);
}
