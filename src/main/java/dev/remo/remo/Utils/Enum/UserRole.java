package dev.remo.remo.Utils.Enum;

public enum UserRole {
    
    ROLE_USER("user"),
    ROLE_ADMIN("admin");

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
