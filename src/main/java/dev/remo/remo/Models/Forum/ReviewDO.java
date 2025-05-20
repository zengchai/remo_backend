package dev.remo.remo.Models.Forum;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Document(collection = "review")
@Data
@Builder
public class ReviewDO {
        
    @Id
    private ObjectId id;

    @NotBlank
    private String motorcycleModelId;

    @NotBlank
    private String userId;

    @NotBlank
    private String review;
    
    @NotBlank
    private String imageId;

    @NotBlank
    private String createdAt;
}
