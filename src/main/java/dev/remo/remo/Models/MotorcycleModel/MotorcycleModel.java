package dev.remo.remo.Models.MotorcycleModel;

import java.util.List;

import dev.remo.remo.Models.Review.Review;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MotorcycleModel {

    private String id;
    private String brand;
    private String model;
    private List<Review> reviews;

}
