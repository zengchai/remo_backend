package dev.remo.remo.Repository.MotorcycleListing;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.General.ModelCount;
import dev.remo.remo.Models.General.MonthCount;
import dev.remo.remo.Models.General.StatusCount;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;

public interface MotorListingRepository {

    /**
     * Creates or updates a motorcycle listing.
     *
     * @param listing the motorcycle listing to create or update
     * @return the created or updated motorcycle listing
     */
    MotorcycleListingDO createOrUpdateListing(MotorcycleListingDO listing);

    /**
     * Retrieves a paginated list of motorcycle listings by their IDs.
     *
     * @param listingId the list of motorcycle listing IDs
     * @param pageable  pagination information
     * @return a paginated list of motorcycle listings
     */
    Page<MotorcycleListingDO> getListingsById(List<ObjectId> listingId, Pageable pageable);

    /**
     * Retrieves a motorcycle listing by its ID.
     *
     * @param listingId the ID of the motorcycle listing to retrieve
     * @return an Optional containing the motorcycle listing if found, or empty if not found
     */
    Optional<MotorcycleListingDO> getListingById(ObjectId listingId);

    /**
     * Updates the inspection ID of a motorcycle listing.
     *
     * @param id          the ID of the motorcycle listing to update
     * @param inspectionId the new inspection ID to set
     */
    void updateMotorcycleListingInspection(ObjectId id, String inspectionId);

    /**
     * Deletes a motorcycle listing by its ID.
     * 
     * @param listingId the ID of the motorcycle listing to delete
     */
    void deleteMotorcycleListingById(ObjectId listingId);

    /**
     * Deletes motorcycle listings images.
     *
     * @param imageIds the list of motorcycle listing image IDs
     */
    void deleteMotorcycleListingImage(List<ObjectId> imageIds);

    /**
     * Uploads images associated with a motorcycle listing.
     *
     * @param files the images to upload
     * @return a list of strings indicating the ids of images
     */
    List<String> uploadFiles(MultipartFile[] files);

    /**
     * Updates the status of a motorcycle listing.
     *
     * @param objectId the ID of the motorcycle listing to update
     * @param status   the new status to set
     * @param extInfo  additional information to store with the listing
     */
    void updateMotorcycleListingStatus(ObjectId objectId, String status, Map<String, String> extInfo);

    
    /**
     * Retrieves all motorcycle listings for admin view.
     * 
     * @param pageable pagination information
     * @return a paginated list of motorcycle listings
     */
    List<MotorcycleListingDO> getAllListingsForAdmin(Pageable pageable);

    /**
     * Retrieves all motorcycle listings for a user.
     *
     * @param pageable pagination information
     * @return a paginated list of motorcycle listings for the user
     */
    List<MotorcycleListingDO> getAllListingsForUser(Pageable pageable);

    /**
     * Retrieves a motorcycle listing image by its ID.
     *
     * @param id the ID of the motorcycle listing image to retrieve
     * @return an Optional containing the motorcycle listing image if found, or empty if not found
     */
    Optional<Resource> getMotorcycleListingImageById(ObjectId id);

    /**
     * Retrieves a paginated list of motorcycle listings by user ID.
     *
     * @param id       the ID of the user whose motorcycle listings to retrieve
     * @param pageable pagination information
     * @return a paginated list of motorcycle listings associated with the specified user ID
     */
    Page<MotorcycleListingDO> getMotorcycleListingByUserId(String id, Pageable pageable);

    /**
     * Retrieves a list of motorcycle listings by user ID.
     *
     * @param id the ID of the user whose motorcycle listings to retrieve
     * @return a list of motorcycle listings associated with the specified user ID
     */
    List<MotorcycleListingDO> getMotorcycleListingByUserId(String id);

    /**
     * Retrieves motorcycle listings based on the provided filter criteria.
     *
     * @param query    the list of criteria to filter motorcycle listings
     * @param pageable pagination information
     * @return a paginated list of motorcycle listings matching the filter criteria
     */
    Page<MotorcycleListingDO> getMotorcycleListingByFilter(List<Criteria> query, Pageable pageable);

    /**
     * Retrieves the count of all motorcycle listings.
     *
     * @return the total count of motorcycle listings
     */
    int getMotorcycleListingCount();

    /**
     * Retrieves the count of motorcycle listings created in each month.
     *
     * @return a list of MonthCount objects containing the month and count of new listings
     */
    List<MonthCount> getNewListingsPerMonth();

    /**
     * Retrieves the count of motorcycle listings by their status.
     *
     * @return a list of StatusCount objects containing the status and count of listings
     */
    List<StatusCount> getListingCountByStatus();

    /**
     * Retrieves the count and average price of motorcycle listings grouped by motorcycle ID.
     *
     * @return a list of ModelCount objects containing the motorcycle ID, count, and average price
     */
    List<ModelCount> getListingCountAndAvgPriceByMotorcycleId();
}
