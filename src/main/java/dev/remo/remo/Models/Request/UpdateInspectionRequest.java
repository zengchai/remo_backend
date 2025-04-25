package dev.remo.remo.Models.Request;

import java.util.Map;

import dev.remo.remo.Models.Inspection.Shop.Shop;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Utils.Enum.StatusEnum;
import dev.remo.remo.Utils.Enum.VehicleComponentEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateInspectionRequest {
  
    @NotBlank(message = "Inspection ID is required")
    private String inspectionId;
    
    @NotBlank(message = "Status is required")
    private String status;
    
    private String remark;
    
    @NotNull(message = "Component scores are required")
    private Map<VehicleComponentEnum, Integer> componentScores;

}
