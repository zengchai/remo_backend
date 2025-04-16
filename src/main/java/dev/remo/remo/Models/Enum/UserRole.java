package dev.remo.remo.Models.Enum;

public enum UserRole {
    
    USER("user"),
    ADMIN("admin");

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
