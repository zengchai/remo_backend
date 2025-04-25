package dev.remo.remo.Models.Request;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateInspectionRequest {
    
    @NotBlank(message = "Status is required")
    private String status;
    
    @NotBlank(message = "Remark is required")
    private String remark;
    
    @NotNull(message = "Component scores are required")
    private Map<String, Integer> componentScores;

    private Boolean retry = false;

}
