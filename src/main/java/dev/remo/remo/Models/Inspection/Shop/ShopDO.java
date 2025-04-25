package dev.remo.remo.Models.Inspection.Shop;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "shop")
@Data
@Builder
public class ShopDO {
    
    @Id
    private ObjectId id;
    private String name;
    private String address;
    private String mapUrl;
    private String imageId;
}
