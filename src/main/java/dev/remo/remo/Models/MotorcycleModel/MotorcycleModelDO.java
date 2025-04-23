package dev.remo.remo.Models.MotorcycleModel;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "motorcycle_models")
@Data
public class MotorcycleModelDO {
    
    @Id
    private ObjectId id;
    private String brand;
    private String model;
    private List<String> reviews;
}
