package dev.remo.remo.Models.Inspection.Shop;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "location")
@Data
public class ShopDO {
    
    @Id
    private ObjectId id;
    private String name;
    private String address;
    private String mapUrl;
    private String imageUrl;
}
