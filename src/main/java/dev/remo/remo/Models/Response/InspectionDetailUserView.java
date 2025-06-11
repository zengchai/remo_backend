package dev.remo.remo.Models.Response;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InspectionDetailUserView {
    
    private String id;
    private String date;
    private String time;
    private String motorcycleListingId;
    private String shopName;
    private String address;
    private String shopImageId;
    private String brand;
    private String model;
    private Map<String, Map<String, Integer>> vehicleComponentMap;
    private String status;
    private Map<String, String> extInfo;
}
