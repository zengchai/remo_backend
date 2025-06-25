package dev.remo.remo.Models.Response;

import org.springframework.data.domain.Page;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

// This class represents a response containing review details for a specific motorcycle category,
@Builder
@Data
public class ReviewCategoryUserViewResponse {

    @NotBlank
    private String id;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String imageId;

    @NotBlank
    Page<ReviewUserView> reviews;

    private int reviewsCount;
}
