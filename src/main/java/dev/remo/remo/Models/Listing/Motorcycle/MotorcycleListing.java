package dev.remo.remo.Models.Listing.Motorcycle;

import dev.remo.remo.Models.Inspection.Inspection;
import dev.remo.remo.Models.Listing.Lisitng;
import dev.remo.remo.Models.Motorcycle.Motorcycle;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MotorcycleListing extends Lisitng {

    private Motorcycle motorcycle;
    private Inspection inspection;
    private String location;
    private String date;
    private String status;
    private String type;
}
