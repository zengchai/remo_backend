package dev.remo.remo.Utils.Enum;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum VehicleComponentEnum {
    // Identity
    VIN_VERIFICATION("VIN Verification", "Identity"),
    REGISTRATION("Registration", "Identity"),
    OWNERSHIP("Ownership", "Identity"),

    // Electrical
    BATTERY("Battery", "Electrical"),
    LIGHTS("Lights", "Electrical"),
    WIRING("Wiring", "Electrical"),
    INSTRUMENTS("Instruments", "Electrical"),

    // Mechanical
    BRAKES("Brakes", "Mechanical"),
    SUSPENSION("Suspension", "Mechanical"),
    TRANSMISSION("Transmission", "Mechanical"),
    CHAIN_BELT_DRIVE("Chain/Belt Drive", "Mechanical"),

    // Safety
    TIRES("Tires", "Safety"),
    HORN("Horn", "Safety"),
    MIRRORS("Mirrors", "Safety"),

    // Engine
    OIL_LEVEL("Oil Level", "Engine"),
    COOLANT("Coolant", "Engine"),
    BELTS("Belts", "Engine"),
    NOISE("Noise", "Engine"),

    // Comfort/Controls
    SEAT("Seat", "Comfort/Controls"),
    HANDLEBAR("Handlebar/Grips", "Comfort/Controls"),
    FOOTPEGS("Foot Pegs", "Comfort/Controls"),
    CONTROLS("Controls (Levers/Pedals)", "Comfort/Controls"),

    // Body/Frame
    BODY_PANELS("Body Panels", "Body/Frame"),
    FRAME("Frame", "Body/Frame"),
    PAINT("Paint", "Body/Frame"),

    // Appearance
    EXTERIOR("Exterior", "Appearance"),
    ACCESSORIES("Accessories", "Appearance");

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

    private static final Map<String, VehicleComponentEnum> DISPLAY_NAME_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(VehicleComponentEnum::getDisplayName, Function.identity()));

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

    public static Map<String, Map<VehicleComponentEnum, Integer>> convertToEnumGroupedMap(
            Map<String, Map<String, Integer>> grouped) {
        Map<String, Map<VehicleComponentEnum, Integer>> converted = new HashMap<>();

        for (Map.Entry<String, Map<String, Integer>> categoryEntry : grouped.entrySet()) {
            String category = categoryEntry.getKey();
            Map<String, Integer> components = categoryEntry.getValue();

            Map<VehicleComponentEnum, Integer> enumComponents = new HashMap<>();
            for (Map.Entry<String, Integer> componentEntry : components.entrySet()) {
                VehicleComponentEnum componentEnum = VehicleComponentEnum.fromDisplayName(componentEntry.getKey());
                if (componentEnum != null) {
                    enumComponents.put(componentEnum, componentEntry.getValue());
                } else {
                    throw new IllegalArgumentException("Unknown component: " + componentEntry.getKey());
                }
            }

            converted.put(category, enumComponents);
        }
        return converted;
    }

    public static Map<String, Map<String, Integer>> groupByCategory(Map<String, Integer> flatScores) {
        Map<String, Map<String, Integer>> grouped = new HashMap<>();

        for (Map.Entry<String, Integer> entry : flatScores.entrySet()) {
            VehicleComponentEnum component = fromDisplayName(entry.getKey());
            if (component == null)
                continue;

            String category = component.getCategory();
            grouped.computeIfAbsent(category, k -> new HashMap<>())
                    .put(component.getDisplayName(), entry.getValue());
        }

        return grouped;
    }

    public static Map<String, Map<String, Integer>> convertToStringGroupedMap(
            Map<String, Map<VehicleComponentEnum, Integer>> grouped) {
        Map<String, Map<String, Integer>> converted = new HashMap<>();

        for (Map.Entry<String, Map<VehicleComponentEnum, Integer>> categoryEntry : grouped.entrySet()) {
            String category = categoryEntry.getKey();
            Map<VehicleComponentEnum, Integer> enumComponents = categoryEntry.getValue();

            Map<String, Integer> stringComponents = new HashMap<>();
            for (Map.Entry<VehicleComponentEnum, Integer> componentEntry : enumComponents.entrySet()) {
                stringComponents.put(componentEntry.getKey().getDisplayName(), componentEntry.getValue());
            }

            converted.put(category, stringComponents);
        }

        return converted;
    }

}
