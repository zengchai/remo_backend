package dev.remo.remo.Models.Listing.Motorcycle;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "motorcycle_listing")
@Data
@Builder
public class MotorcycleListingDO {
       
    @Id
    private ObjectId id;
    private String motorcycleId;
    private String inspectionId;
    private String userId;
    private String manufacturedYear;
    private String mileage;
    private String cubicCapacity;
    private String transmission;
    private String phoneNumber;
    private String price;
    private String date;
    private String state;
    private String area;
    private String status;
    private List<String> imagesIds;
    private Map<String, String> extInfo;


}
