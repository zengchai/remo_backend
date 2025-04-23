package dev.remo.remo.Service.MotorcycleModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Review.Review;
import dev.remo.remo.Repository.MotorcycleModel.MotorcycleModelRepository;
import dev.remo.remo.Service.Forum.ReviewService;

public class MotorcycleModelServiceImpl implements MotorcycleModelService {

    @Autowired
    MotorcycleModelRepository motorcycleRepository;

    @Autowired
    ReviewService reviewService;

    public List<MotorcycleModel> getMotorcycleList(){

        List<MotorcycleModel> motorcycles = new ArrayList<>();
        for(MotorcycleModelDO motorcycleDO : motorcycleRepository.getMotorcycleList()){
            motorcycles.add(convertToMotorcycle(motorcycleDO));
        }
        return motorcycles;

    }

    public MotorcycleModel getMotorcycleByBrandAndModel(String brand,String model){
        return convertToMotorcycle(motorcycleRepository.getMotorcycleDO(brand, model));
    }

    public MotorcycleModel convertToMotorcycle(MotorcycleModelDO motorcycleDO) {

        List<Review> reviews = reviewService.getReviewbyIds(motorcycleDO.getReviews());

        return MotorcycleModel.builder().id(motorcycleDO.getId().toString()).brand(motorcycleDO.getBrand())
                .model(motorcycleDO.getModel()).reviews(reviews).build();
    }
}
