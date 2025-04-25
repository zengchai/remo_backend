package dev.remo.remo.Models.Request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrUpdateListingRequest {

    private String id;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotBlank
    private String manufacturedYear;
    @NotBlank
    private String mileage;
    @NotBlank
    private String cubicCapacity;
    @NotBlank
    private String transmission;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String state;
    @NotBlank
    private String area;
    @NotBlank
    private String date;
    @NotBlank
    private String price;
    @NotBlank
    private String status;
    
    private List<String> existingImageIds;

}
