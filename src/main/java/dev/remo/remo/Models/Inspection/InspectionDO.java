package dev.remo.remo.Models.Inspection;

import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "inspection")
@Data
@Builder
public class InspectionDO {
    
    @Id
    private ObjectId id;
    private String date;
    private String time;
    private String locationId;
    private Map<String, Map<String, Integer>> vehicleComponentMap;
    private String status;
    private Map<String,String> extInfo;

}
