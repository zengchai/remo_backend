package dev.remo.remo.Models.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// This class represents a request to predict the price of a motorcycle based on its attributes.
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
