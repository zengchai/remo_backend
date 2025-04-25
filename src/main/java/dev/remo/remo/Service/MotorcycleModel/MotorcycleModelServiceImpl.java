package dev.remo.remo.Service.MotorcycleModel;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;
import dev.remo.remo.Mappers.MotorcycleModelMapper;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Repository.MotorcycleModel.MotorcycleModelRepository;
import dev.remo.remo.Utils.Exception.NotFoundException;

public class MotorcycleModelServiceImpl implements MotorcycleModelService {

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

    public MotorcycleModel getMotorcycleByBrandAndModel(String brand, String model) {
        MotorcycleModelDO motorcycleDO = motorcycleModelRepository.findByBrandAndModel(brand, model)
                .orElseThrow(() -> new NotFoundException(brand + " " + model + " not found"));
        return motorcycleModelMapper.convertModelDOToModel(motorcycleDO);
    }

    public void updateMotorcycleModelReviewList(MotorcycleModel motorcycleModel) {
        motorcycleModelRepository
                .addReviewIdToMotorcycleModel(motorcycleModelMapper.convertModelToModelDO(motorcycleModel));
    }

    public void removeReviewIdListById(String modelId, String reviewId) {

        // Check if the model exists
        MotorcycleModelDO motorcycleModelDO = motorcycleModelRepository.getMotorcycleModelById(new ObjectId(modelId))
                .orElseThrow(() -> new NotFoundException("Motorcycle model not found"));

        // Check if the review exists in the model
        List<String> reviewIds = motorcycleModelDO.getReviews();
        if (!reviewIds.contains(reviewId)) {
            throw new NotFoundException("Review not found in motorcycle model");
        }
        reviewIds.remove(reviewId);
        motorcycleModelDO.setReviews(reviewIds);

        // Save the updated model
        motorcycleModelRepository.addReviewIdToMotorcycleModel(motorcycleModelDO);
    }

}
