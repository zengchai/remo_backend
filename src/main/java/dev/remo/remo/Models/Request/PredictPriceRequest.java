package dev.remo.remo.Models.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PredictPriceRequest {
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
}
