package dev.remo.remo.Models.MotorcycleModel;

import java.util.ArrayList;
import java.util.List;

import dev.remo.remo.Models.Forum.Review;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MotorcycleModel {

    private String id;

    private String brand;

    private String model;

    private String imageId;

    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
}
