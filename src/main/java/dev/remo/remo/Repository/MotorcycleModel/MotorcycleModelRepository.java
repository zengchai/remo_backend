package dev.remo.remo.Repository.MotorcycleModel;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;

public interface MotorcycleModelRepository {

    /**
     * Retrieves a list of all motorcycle models.
     *
     * @return a list of MotorcycleModelDO objects
     */
    List<MotorcycleModelDO> getMotorcycleList();

    /**
     * Retrieves a motorcycle model by its ID.
     *
     * @param id the ID of the motorcycle model
     * @return an Optional containing the MotorcycleModelDO if found, or empty if not found
     */
    Optional<MotorcycleModelDO> getMotorcycleModelById(ObjectId id);

    /**
     * Finds a motorcycle model by its brand and model name.
     *
     * @param brand the brand of the motorcycle
     * @param model the model name of the motorcycle
     * @return an Optional containing the MotorcycleModelDO if found, or empty if not found
     */
    Optional<MotorcycleModelDO> findByBrandAndModel(String brand, String model);

    /**
     * Adds or updates a motorcycle model.
     *
     * @param motorcycleModelDO the MotorcycleModelDO to add or update
     */
    void addOrUpdateMotorcycleModel(MotorcycleModelDO motorcycleModelDO);

    /**
     * Retrieves all motorcycle models with pagination.
     * 
     * @param pageable the pagination information
     */
    Page<MotorcycleModelDO> getAllMotorcycleModelByPage(Pageable pageable);

    /**
     * Uploads a file associated with a motorcycle model.
     * 
     * @param file the MultipartFile to upload
     */
    String uploadFiles(MultipartFile file);

    /**
     * Retrieves the image of a motorcycle model by its ID.
     * 
     * @param id the ID of the motorcycle model
     * @return an Optional containing the Resource if found, or empty if not found
     */
    Optional<Resource> getMotorcycleModelImageById(ObjectId id);

    /**
     * Retrieves a list of motorcycle models by their brand.
     *
     * @param brand the brand of the motorcycle
     * @return a list of MotorcycleModelDO objects matching the brand
     */
    List<MotorcycleModelDO> findByBrand(String brand);

    /**
     * Retrieves motorcycle models based on a list of criteria and pagination.
     *
     * @param criteriaList the list of criteria to filter motorcycle models
     * @param pageable     pagination information
     * @return a paginated list of MotorcycleModelDO objects matching the criteria
     */
    Page<MotorcycleModelDO> getMotorcycleModelByFilter(List<Criteria> criteriaList, Pageable pageable);

}
