package dev.remo.remo.Utils.Enum;

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
}
