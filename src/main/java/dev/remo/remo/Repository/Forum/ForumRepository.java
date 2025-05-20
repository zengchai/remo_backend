package dev.remo.remo.Repository.Forum;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Forum.ReviewDO;

public interface ForumRepository {

    ReviewDO createOrUpdateReview(ReviewDO reviewDO);

    Optional<ReviewDO> getReviewById(ObjectId reviewId);

    void deleteReviewById(ObjectId reviewId);

    void deleteReviewImage(ObjectId Id);

    String uploadFiles(MultipartFile file);

    List<ReviewDO> getReviewsByUserId(String userId);

    Optional<Resource> getReviewImageById(ObjectId id);

    Page<ReviewDO> getReviewsByMotorcycleModelIdWithPaging(String motorcycleModelId, Pageable pageable);

    Page<ReviewDO> getAllReviewsByPage(Pageable pageable);
}
