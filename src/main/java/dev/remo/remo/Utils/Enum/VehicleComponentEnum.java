package dev.remo.remo.Utils.Enum;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum VehicleComponentEnum {
    // Identity
    VIN_VERIFICATION("VIN Verification", "identity"),
    REGISTRATION("Registration", "identity"),
    OWNERSHIP("Ownership", "identity"),

    // Electrical
    BATTERY("Battery", "electrical"),
    LIGHTS("Lights", "electrical"),
    WIRING("Wiring", "electrical"),
    INSTRUMENTS("Instruments", "electrical"),

    // Mechanical
    BRAKES("Brakes", "mechanical"),
    SUSPENSION("Suspension", "mechanical"),
    TRANSMISSION("Transmission", "mechanical"),

    // Safety
    SEAT_BELTS("Seat Belts", "safety"),
    AIRBAGS("Airbags", "safety"),
    TIRES("Tires", "safety"),

    // Engine
    OIL_LEVEL("Oil Level", "engine"),
    COOLANT("Coolant", "engine"),
    BELTS("Belts", "engine"),
    NOISE("Noise", "engine"),

    // Comfort
    AC_HEATING("AC/Heating", "comfort"),
    SEATS("Seats", "comfort"),
    WINDOWS("Windows", "comfort"),

    // Accident History
    BODY_PANELS("Body Panels", "accidentHistory"),
    FRAME("Frame", "accidentHistory"),
    PAINT("Paint", "accidentHistory"),

    // Appearance
    EXTERIOR("Exterior", "appearance"),
    INTERIOR("Interior", "appearance"),
    UPHOLSTERY("Upholstery", "appearance");

    private final String displayName;
    private final String category;

    VehicleComponentEnum(String displayName, String category) {
        this.displayName = displayName;
        this.category = category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCategory() {
        return category;
    }

    private static final Map<String, VehicleComponentEnum> DISPLAY_NAME_MAP =
        Arrays.stream(values()).collect(Collectors.toMap(VehicleComponentEnum::getDisplayName, Function.identity()));

    public static boolean isValidComponent(String displayName) {
        return DISPLAY_NAME_MAP.containsKey(displayName);
    }

    public static VehicleComponentEnum fromDisplayName(String displayName) {
        return DISPLAY_NAME_MAP.get(displayName);
    }

    public static void validateFlatMap(Map<String, Integer> scores) {
        List<String> missing = DISPLAY_NAME_MAP.keySet().stream()
            .filter(name -> !scores.containsKey(name))
            .toList();

        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("Missing components: " + String.join(", ", missing));
        }

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (!isValidComponent(entry.getKey())) {
                throw new IllegalArgumentException("Invalid component name: " + entry.getKey());
            }

            int score = entry.getValue();
            if (score < 1 || score > 5) {
                throw new IllegalArgumentException(
                    String.format("Invalid score %d for %s. Must be between 1 and 5.", score, entry.getKey()));
            }
        }
    }

    public static Map<String, Map<String, Integer>> groupByCategory(Map<String, Integer> flatScores) {
        Map<String, Map<String, Integer>> grouped = new HashMap<>();

        for (Map.Entry<String, Integer> entry : flatScores.entrySet()) {
            VehicleComponentEnum component = fromDisplayName(entry.getKey());
            if (component == null) continue;

            String category = component.getCategory();
            grouped.computeIfAbsent(category, k -> new HashMap<>())
                   .put(component.getDisplayName(), entry.getValue());
        }

        return grouped;
    }
}
