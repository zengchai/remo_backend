package dev.remo.remo.Models.Response;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MotorcycleModelList {
    
    private Map<String, Map<String, String>> motorcycleModels;
}
