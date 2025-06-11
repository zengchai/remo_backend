package dev.remo.remo.Models.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MotorcycleListingUserView {

    private String id;
    private String brand;
    private String model;
    private String manufacturedYear;
    private String state;
    private String area;
    private String mileage;
    private int price;
    private String insepctionStatus;
    private String inspectionId;
    private String plateNumber;
    private String phoneNumber;
    private String imagesIds;
    private String createdAt;
    private String status;

}
