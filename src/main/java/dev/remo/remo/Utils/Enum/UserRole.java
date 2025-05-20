package dev.remo.remo.Utils.Enum;

import java.util.List;
import java.util.stream.Collectors;

public enum UserRole {
    
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String code;

    // Constructor to initialize the code
    UserRole(String code) {
        this.code = code;
    }

    // Method to get the code
    public String getCode() {
        return code;
    }

    public static UserRole fromCode(String code) {
        for (UserRole role : UserRole.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant with code: " + code);
    }

    public static List<UserRole> convertToUserRole(List<String> roles) {
        return roles.stream()
                .map(UserRole::fromCode)
                .collect(Collectors.toList());
    }

    public static List<String> convertToString(List<UserRole> roles) {
        return roles.stream()
                .map(UserRole::getCode)
                .collect(Collectors.toList());
    }
}
