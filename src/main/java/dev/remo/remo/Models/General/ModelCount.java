package dev.remo.remo.Models.General;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModelCount {
    private String motorcycleId;
    private String brand;
    private String model;
    private long count;
    private double avgPrice;
}
