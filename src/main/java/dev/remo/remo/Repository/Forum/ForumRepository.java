package dev.remo.remo.Repository.Forum;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Forum.ReviewDO;

public interface ForumRepository{

    /**
     * Creates or updates a review.
     *
     * @param reviewDO the review to create or update
     * @return the created or updated review
     */
    ReviewDO createOrUpdateReview(ReviewDO reviewDO);

    /**
     * Retrieves a review by its ID.
     *
     * @param reviewId the ID of the review to retrieve
     * @return an Optional containing the review if found, or empty if not found
     */
    Optional<ReviewDO> getReviewById(ObjectId reviewId);

    /**
     * Deletes a review by its ID.
     *
     * @param reviewId the ID of the review to delete
     */
    void deleteReviewById(ObjectId reviewId);

    /**
     * Deletes a review image by its ID.
     *
     * @param Id the ID of the review image to delete
     */
    void deleteReviewImage(ObjectId Id);

    /**
     * Uploads image associated with a review.
     *
     * @param file the image to upload
     * @return a string indicating the id of the uploaded image
     */
    String uploadFiles(MultipartFile file);

    /**
     * Retrieves a paginated list of reviews by user ID.
     *
     * @param userId the ID of the user whose reviews to retrieve
     * @param pageable pagination information
     * @return a page of reviews associated with the specified user ID
     */
    Page<ReviewDO> getReviewsByUserId(String userId,Pageable pageable);

    /**
     * Retrieves a review image by its ID.
     *
     * @param id the ID of the review image to retrieve
     * @return an Optional containing the review image if found, or empty if not found
     */
    Optional<Resource> getReviewImageById(ObjectId id);

    /**
     * Retrieves a paginated list of reviews by motorcycle model ID.
     *
     * @param motorcycleModelId the ID of the motorcycle model whose reviews to retrieve
     * @param pageable pagination information
     * @return a page of reviews associated with the specified motorcycle model ID
     */
    Page<ReviewDO> getReviewsByMotorcycleModelIdWithPaging(String motorcycleModelId, Pageable pageable);

    /**
     * Retrieves all reviews by motorcycle model ID.
     *
     * @param motorcycleModelId the ID of the motorcycle model whose reviews to retrieve
     * @return a list of reviews associated with the specified motorcycle model ID
     */
    Page<ReviewDO> getAllReviewsByPage(Pageable pageable);

    /**
     * Retrieves all reviews by user ID.
     *
     * @param userId the ID of the user whose reviews to retrieve
     * @return a list of reviews associated with the specified user ID
     */
    List<ReviewDO> getReviewsByUserId(String userId);
}
