package dev.remo.remo.Models.Inspection;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "inspection")
@Data
public class InspectionDO {
    
    @Id
    private ObjectId id;
    private int date;
    private int accident;
}
