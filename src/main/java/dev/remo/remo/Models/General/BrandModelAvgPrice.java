package dev.remo.remo.Models.General;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandModelAvgPrice {

    private String motorcycleId;

    private String brand;

    private String model;
    
    private double avgPrice;
}
