package dev.remo.remo.Models.Listing.Motorcycle;

import dev.remo.remo.Models.Enum.StatusEnum;
import dev.remo.remo.Models.Inspection.Inspection;
import dev.remo.remo.Models.Listing.Listing;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MotorcycleListing extends Listing {

    private MotorcycleModel motorcycleModel;
    private Inspection inspection;
    private String date;
    private StatusEnum status;
    private String manufacturedYear;
    private String mileage;
    private String cubicCapacity;
    private String transmission;
    private String phoneNumber;
    private String state;
    private String area;

}
