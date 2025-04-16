package dev.remo.remo.Models.Listing.Motorcycle;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "listing")
@Data
public class MotorcycleListingDO {
       
    @Id
    private ObjectId id;
    private String motorcycleId;
    private String inspectionId;

}
