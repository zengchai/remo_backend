package dev.remo.remo.Service.Forum;

import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.CreateOrUpdateReviewRequest;

public interface ForumService {
    void createOrUpdateReview(MultipartFile image,CreateOrUpdateReviewRequest createOrUpdateReviewRequest);
    void deleteReviewById(String reviewId);
}
