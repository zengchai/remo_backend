package dev.remo.remo.Models.Response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

// This class represents the details of a motorcycle listing view for a user.
@Data
@Builder
public class MotorcycleListingDetailUserView {

    private String id;

    private String brand;

    private String model;

    private String userId;

    private String cubicCapacity;

    private String transmission;

    private String manufacturedYear;

    private String state;

    private String area;

    private String mileage;

    private int price;

    private String inspectionId;

    private String inspectionStatus;

    private String plateNumber;

    private String phoneNumber;

    private String status;

    private Boolean isFavourite;

    private String remark;

    private String createdAt;

    private String updatedBy;

    private String updatedAt;

    private List<String> imagesIds;
}
