package dev.remo.remo.Models.Response;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

// This class represents a list of motorcycle models, where each model is identified by a brand and model name.
@Data
@Builder
public class MotorcycleModelList {

    private Map<String, Map<String, String>> motorcycleModels;
}
