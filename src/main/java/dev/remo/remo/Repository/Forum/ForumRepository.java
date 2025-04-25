package dev.remo.remo.Repository.Forum;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Forum.ReviewDO;

public interface ForumRepository {
    ReviewDO createOrUpdateReview(ReviewDO reviewDO);
    Optional<ReviewDO> getReviewById(ObjectId reviewId);
    void deleteReviewById(ObjectId reviewId);
    void deleteReviewImage(String Id);
    String uploadFiles(MultipartFile file);
}
