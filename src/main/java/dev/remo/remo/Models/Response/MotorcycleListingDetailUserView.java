package dev.remo.remo.Models.Response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MotorcycleListingDetailUserView {
    
    private String id;
    private String brand;
    private String model;
    private String cubicCapacity;
    private String transmission;
    private String manufacturedYear;
    private String state;
    private String area;
    private String mileage;
    private String price;
    private String inspectionId;
    private String insepctionStatus;
    private String platenNumber;
    private String phoneNumber;
    private Boolean isFavourite;
    private List<String> imagesIds;
}
