package dev.remo.remo.Models.Response;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InspectionDetailUserView {
    
    private String date;
    private String shopName;
    private String mapUrl;
    private Map<String, Map<String, Integer>> vehicleComponentMap;
    private String status;
}
