package dev.remo.remo.Models.Inspection;

import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Document(collection = "inspection")
@Data
@Builder
public class InspectionDO {
    
    @Id
    private ObjectId id;
    @NotBlank
    private String date;
    @NotBlank
    private String time;
    @NotBlank
    private String shopId;
    @NotBlank
    private String motorcycleListingId;
    @NotBlank
    private String status;
    private Map<String, Map<String, Integer>> vehicleComponentMap;
    private Map<String,String> extInfo;

}
