package dev.remo.remo.Models.Listing.Motorcycle;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Document(collection = "motorcycle_listing")
@Data
@Builder
public class MotorcycleListingDO {

    @Id
    private ObjectId id;
    @NotBlank
    private String motorcycleId;
    @NotBlank
    private String userId;
    @NotBlank
    private String manufacturedYear;
    @NotBlank
    private String mileage;
    @NotBlank
    private String cubicCapacity;
    @NotBlank
    private String transmission;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String price;
    @NotBlank
    private String createdAt;
    @NotBlank
    private String state;
    @NotBlank
    private String area;
    @NotBlank
    private String status;
    @NotBlank
    private String plateNumber;
    @NotBlank
    private List<String> imagesIds;
    
    private String inspectionId;
    private Map<String, String> extInfo;

}
