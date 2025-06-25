package dev.remo.remo.Repository.Inspection;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;

import dev.remo.remo.Models.Inspection.InspectionDO;

public interface InspectionRepository {

    /**
     * Creates a new inspection.
     *
     * @param inspectionDO the inspection data to create
     * @return the created inspection data
     */
    Optional<InspectionDO> createInspection(InspectionDO inspectionDO);

    /**
     * Retrieves an inspection by its ID.
     *
     * @param inspectionId the ID of the inspection to retrieve
     * @return an Optional containing the inspection if found, or empty if not found
     */
    Optional<InspectionDO> getInspectionById(ObjectId inspectionId);

    /**
     * Retrieves a list of inspection statuses by their IDs.
     *
     * @param ids the list of inspection IDs
     * @return a list of InspectionDO objects containing the status information
     */
    List<InspectionDO> getInspectionStatusList(List<ObjectId> ids);

    /**
     * Retrieves all inspections based on the provided criteria and pagination.
     *
     * @param criterias the list of criteria to filter inspections
     * @param pageable  pagination information
     * @return a paginated list of InspectionDO objects
     */
    Page<InspectionDO> getAllInspection(List<Criteria> criterias, Pageable pageable);

    /**
     * Retrieves inspections by listing IDs.
     *
     * @param listingIds the list of listing IDs to filter inspections
     * @return a list of InspectionDO objects associated with the specified listing
     *         IDs
     */
    List<InspectionDO> getInspectionByListingIds(List<String> listingIds);

    /**
     * Updates the status of an inspection.
     *
     * @param inspectionId the ID of the inspection to update
     * @param status       the new status to set
     * @param extInfo      additional information to store with the inspection
     */
    void updateInspectionStatus(ObjectId inspectionId, String status, Map<String, String> extInfo);

    /**
     * Updates the inspection report.
     *
     * @param inspectionId   the ID of the inspection to update
     * @param status         the new status to set
     * @param componentScore a map containing component scores
     * @param extInfo        additional information to store with the inspection
     */
    void updateInspectionReport(ObjectId inspectionId, String status, Map<String, Map<String, Integer>> componentScore,
            Map<String, String> extInfo);

    /**
     * Deletes an inspection by its ID.
     * 
     * @param inspectionId the ID of the inspection to delete
     */
    void deleteInspection(ObjectId inspectionId);
}
