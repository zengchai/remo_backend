package dev.remo.remo.Models.Inspection;

import org.bson.types.ObjectId;

import dev.remo.remo.Models.Enum.StatusEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Inspection {
    
    private String id;
    private int accident;
    private StatusEnum status;
}
