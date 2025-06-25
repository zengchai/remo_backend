package dev.remo.remo.Models.MotorcycleModel;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Document(collection = "motorcycle_models")
@Data
@Builder
public class MotorcycleModelDO {

    @Id
    private ObjectId id;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String imageId;

    @NotBlank
    private List<String> reviewIds;
}
