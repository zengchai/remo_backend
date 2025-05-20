package dev.remo.remo.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import dev.remo.remo.Models.Forum.Review;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;
import io.micrometer.common.util.StringUtils;

@Component
public class MotorcycleModelMapper {

    public MotorcycleModel toDomain(String brand, String model, String imageId) {
        return MotorcycleModel.builder()
                .brand(brand)
                .model(model)
                .imageId(imageId)
                .build();
    }

    public MotorcycleModel convertModelDOToModel(MotorcycleModelDO motorcycleModelDO) {

        MotorcycleModel.MotorcycleModelBuilder motorcycleModelBuilder = MotorcycleModel.builder();

        if (!motorcycleModelDO.getReviewIds().isEmpty()) {
            List<Review> reviews = motorcycleModelDO.getReviewIds().stream()
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

        MotorcycleModelDO.MotorcycleModelDOBuilder motorcycleModelDOBuilder = MotorcycleModelDO.builder();

        List<String> reviewIds = new ArrayList<>();

        if (motorcycleModel.getReviews() != null) {
            motorcycleModel.getReviews().stream().forEach(review -> reviewIds.add(review.getId()));
            System.err.println("Review IDs: " + reviewIds);
        }

        if (StringUtils.isNotBlank(motorcycleModel.getId())) {
            motorcycleModelDOBuilder.id(new ObjectId(motorcycleModel.getId()));
        }

        return motorcycleModelDOBuilder
                .brand(motorcycleModel.getBrand())
                .model(motorcycleModel.getModel())
                .imageId(motorcycleModel.getImageId())
                .reviewIds(reviewIds)
                .build();

    }

    public Page<MotorcycleModel> convertModelDOToModel(Page<MotorcycleModelDO> motorcycleModelDOPage,
            Pageable pageable) {
        List<MotorcycleModel> motorcycleModels = motorcycleModelDOPage.getContent()
                .stream()
                .map(this::convertModelDOToModel)
                .collect(Collectors.toList());

        return new PageImpl<>(motorcycleModels, pageable, motorcycleModelDOPage.getTotalElements());
    }

}
