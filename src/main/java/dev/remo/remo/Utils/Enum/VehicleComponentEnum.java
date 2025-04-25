package dev.remo.remo.Utils.Enum;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum VehicleComponentEnum {
    // Identity
    VIN_VERIFICATION("VIN Verification"),
    REGISTRATION("Registration"),
    OWNERSHIP("Ownership"),
    
    // Electrical
    BATTERY("Battery"),
    LIGHTS("Lights"),
    WIRING("Wiring"),
    INSTRUMENTS("Instruments"),
    
    // Mechanical
    BRAKES("Brakes"),
    SUSPENSION("Suspension"),
    TRANSMISSION("Transmission"),
    
    // Safety
    SEAT_BELTS("Seat Belts"),
    AIRBAGS("Airbags"),
    TIRES("Tires"),
    
    // Engine
    OIL_LEVEL("Oil Level"),
    COOLANT("Coolant"),
    BELTS("Belts"),
    NOISE("Noise"),
    
    // Comfort
    AC_HEATING("AC/Heating"),
    SEATS("Seats"),
    WINDOWS("Windows"),
    
    // Accident History
    BODY_PANELS("Body Panels"),
    FRAME("Frame"),
    PAINT("Paint"),
    
    // Appearance
    EXTERIOR("Exterior"),
    INTERIOR("Interior"),
    UPHOLSTERY("Upholstery");

    private final String displayName;

    VehicleComponentEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static final Map<String, VehicleComponentEnum> DISPLAY_NAME_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(
                VehicleComponentEnum::getDisplayName,
                    Function.identity()
            ));

    public static boolean isValidComponent(String displayName) {
        return DISPLAY_NAME_MAP.containsKey(displayName);
    }

    public static VehicleComponentEnum fromDisplayName(String displayName) {
        return DISPLAY_NAME_MAP.get(displayName);
    }

    public static void validateComponentAndScore(String componentName, int score) {
        if (!isValidComponent(componentName)) {
            throw new IllegalArgumentException("Invalid component: " + componentName);
        }
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException(
                String.format("Score %d for %s is invalid. Must be 1-5", score, componentName)
            );
        }
    }
} 