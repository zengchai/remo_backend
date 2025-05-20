package dev.remo.remo.Models.Response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewUserView {

    @NotBlank
    private String id;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotBlank
    private String review;
    @NotBlank
    private String reviewImageId;
    @NotBlank
    private String createdAt;

    private String userName;
    private String userImageId;
}
