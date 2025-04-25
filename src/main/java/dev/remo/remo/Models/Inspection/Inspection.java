package dev.remo.remo.Models.Inspection;

import java.util.Map;

import dev.remo.remo.Models.Inspection.Shop.Shop;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Utils.Enum.StatusEnum;
import dev.remo.remo.Utils.Enum.VehicleComponentEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Inspection {
    
    private String id;
    private String date;
    private String time;
    private MotorcycleListing motorcycleListing;
    private Shop shop;
    private Map<String, Map<VehicleComponentEnum, Integer>> vehicleComponentMap;
    private StatusEnum status;
    private Map<String, String> extInfo;
}
