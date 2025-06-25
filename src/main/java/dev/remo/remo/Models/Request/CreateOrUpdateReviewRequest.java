package dev.remo.remo.Models.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// This class represents a request to create or update a motorcycle review.
@Data
public class CreateOrUpdateReviewRequest {

    private String id;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String review;

    private String existingImageIds;
}
