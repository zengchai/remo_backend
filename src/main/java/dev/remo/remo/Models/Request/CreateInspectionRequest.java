package dev.remo.remo.Models.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateInspectionRequest {
    
    @NotBlank
    private String shopId;
    @NotBlank 
    private String date;
    @NotBlank
    private String time;
    @NotBlank
    private String motorcycleListingId;
}
