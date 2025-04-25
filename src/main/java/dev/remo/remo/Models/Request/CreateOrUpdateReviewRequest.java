package dev.remo.remo.Models.Request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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
