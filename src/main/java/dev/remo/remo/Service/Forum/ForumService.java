package dev.remo.remo.Service.Forum;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.CreateOrUpdateReviewRequest;
import dev.remo.remo.Models.Request.FilterForumRequest;
import dev.remo.remo.Models.Response.ReviewCategoryUserViewResponse;
import dev.remo.remo.Models.Response.ReviewUserView;

public interface ForumService {

    void createOrUpdateReview(MultipartFile image, CreateOrUpdateReviewRequest createOrUpdateReviewRequest);

    void deleteReviewById(String reviewId);

    void deleteReviewByUserId(String userId);

    Resource getReviewImageById(String id);

    Resource getForumImageById(String id);

    ReviewUserView getReviewById(String reviewId);

    Page<ReviewUserView> getAllReview(int page, int size);

    Page<ReviewUserView> getMyReviews(int page, int size);

    ReviewCategoryUserViewResponse getAllReviewsByMotorcycleModelId(String motorcycleModelId, int page,
            int size);

    Page<ReviewCategoryUserViewResponse> getForumByFilter(FilterForumRequest filterForumRequest, int page, int size);
}