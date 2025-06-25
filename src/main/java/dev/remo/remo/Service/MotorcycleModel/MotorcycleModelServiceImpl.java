package dev.remo.remo.Service.MotorcycleModel;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;
import dev.remo.remo.Models.Request.FilterForumRequest;
import dev.remo.remo.Mappers.MotorcycleModelMapper;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Repository.MotorcycleModel.MotorcycleModelRepository;
import dev.remo.remo.Utils.Exception.InvalidStatusException;
import dev.remo.remo.Utils.Exception.NotFoundResourceException;
import io.micrometer.common.util.StringUtils;

public class MotorcycleModelServiceImpl implements MotorcycleModelService {

    private static final Logger logger = LoggerFactory.getLogger(MotorcycleModelServiceImpl.class);

    @Autowired
    MotorcycleModelRepository motorcycleModelRepository;

    @Autowired
    MotorcycleModelMapper motorcycleModelMapper;

    public List<MotorcycleModel> getMotorcycleList() {
        List<MotorcycleModel> motorcycles = new ArrayList<>();
        for (MotorcycleModelDO motorcycleDO : motorcycleModelRepository.getMotorcycleList()) {
            motorcycles.add(motorcycleModelMapper.convertModelDOToModel(motorcycleDO));
        }
        return motorcycles;
    }

    public List<MotorcycleModel> getMotorcycleByBrand(String brand) {
        return motorcycleModelRepository.findByBrand(brand)
                .stream()
                .map(motorcycleModelMapper::convertModelDOToModel)
                .toList();
    }

    public MotorcycleModel getMotorcycleByBrandAndModel(String brand, String model) {
        MotorcycleModelDO motorcycleDO = motorcycleModelRepository.findByBrandAndModel(brand, model)
                .orElseThrow(() -> new NotFoundResourceException(brand + " " + model + " not found"));
        return motorcycleModelMapper.convertModelDOToModel(motorcycleDO);
    }

    public MotorcycleModel getMotorcycleModelById(String id) {
        MotorcycleModelDO motorcycleDO = motorcycleModelRepository.getMotorcycleModelById(new ObjectId(id))
                .orElseThrow(() -> new NotFoundResourceException("Motorcycle model not found"));
        return motorcycleModelMapper.convertModelDOToModel(motorcycleDO);
    }

    public void updateMotorcycleModelReviewList(MotorcycleModel motorcycleModel) {

        logger.info("Updating motorcycle model review list for model: {}", motorcycleModel.getId());
        
        motorcycleModelRepository
                .addOrUpdateMotorcycleModel(motorcycleModelMapper.convertModelToModelDO(motorcycleModel));
    }

    public void removeReviewIdListById(String modelId, String reviewId) {

        logger.info("Removing review ID: {} from motorcycle model ID: {}", reviewId, modelId);

        // Check if the model exists
        MotorcycleModelDO motorcycleModelDO = motorcycleModelRepository.getMotorcycleModelById(new ObjectId(modelId))
                .orElseThrow(() -> new NotFoundResourceException("Motorcycle model not found"));

        // Check if the review exists in the model
        List<String> reviewIds = motorcycleModelDO.getReviewIds();
        if (!reviewIds.contains(reviewId)) {
            throw new NotFoundResourceException("Review not found in motorcycle model");
        }

        reviewIds.remove(reviewId);
        motorcycleModelDO.setReviewIds(reviewIds);
        motorcycleModelRepository.addOrUpdateMotorcycleModel(motorcycleModelDO);

        logger.info("Review ID: {} removed from motorcycle model ID: {}", reviewId, modelId);
    }

    public void createMotorcycleModel(String brand, String model, MultipartFile image) {

        logger.info("Creating motorcycle model with brand: {}, model: {}", brand, model);

        if (motorcycleModelRepository.findByBrandAndModel(brand, model).isPresent()) {
            throw new InvalidStatusException("Motorcycle model already exists");
        }

        String imageId = motorcycleModelRepository.uploadFiles(image);
        MotorcycleModel motorcycleModel = motorcycleModelMapper.toDomain(brand, model, imageId);
        MotorcycleModelDO motorcycleModelDO = motorcycleModelMapper.convertModelToModelDO(motorcycleModel);
        motorcycleModelRepository.addOrUpdateMotorcycleModel(motorcycleModelDO);

        logger.info("Motorcycle model created with ID: {}", motorcycleModelDO.getId());
    }

    public Resource getMotorcycleModelImageById(String id) {
        return motorcycleModelRepository.getMotorcycleModelImageById(new ObjectId(id))
                .orElseThrow(() -> new NotFoundResourceException("Motorcycle model not found"));
    }

    public Page<MotorcycleModel> getMotorcycleModelByFilter(FilterForumRequest filterForumRequest, int page, int size) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "_id"));
        List<Criteria> criteriaList = new ArrayList<>();

        if (StringUtils.isNotBlank(filterForumRequest.getBrand())) {
            criteriaList.add(Criteria.where("brand").is(filterForumRequest.getBrand()));
        }

        if (StringUtils.isNotBlank(filterForumRequest.getModel())) {
            criteriaList.add(Criteria.where("model").is(filterForumRequest.getModel()));
        }

        Page<MotorcycleModelDO> motorcycleModelDOPage = motorcycleModelRepository
                .getMotorcycleModelByFilter(criteriaList, pageable);

        return motorcycleModelMapper.convertModelDOToModel(motorcycleModelDOPage);
    }

}
