package dev.remo.remo.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import dev.remo.remo.Models.Forum.Review;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;

@Component
public class MotorcycleModelMapper {
    public MotorcycleModel convertModelDOToModel(MotorcycleModelDO motorcycleModelDO) {

        MotorcycleModel.MotorcycleModelBuilder motorcycleModelBuilder = MotorcycleModel.builder();
        if (motorcycleModelDO.getReviews() != null) {
            List<Review> reviews = motorcycleModelDO.getReviews().stream()
                    .map(reviewId -> Review.builder().id(reviewId).build())
                    .collect(Collectors.toList());
            motorcycleModelBuilder.reviews(reviews);
        }

        return motorcycleModelBuilder
                .id(motorcycleModelDO.getId().toString())
                .brand(motorcycleModelDO.getBrand())
                .model(motorcycleModelDO.getModel()).build();
    }

    public MotorcycleModelDO convertModelToModelDO(MotorcycleModel motorcycleModel) {
        List<String> reviewIds = new ArrayList<>();
        motorcycleModel.getReviews().stream().forEach(review -> reviewIds.add(review.getId()));
        return MotorcycleModelDO.builder().id(new ObjectId(motorcycleModel.getId()))
                .brand(motorcycleModel.getBrand())
                .model(motorcycleModel.getModel())
                .reviews(reviewIds).build();
    }
}
