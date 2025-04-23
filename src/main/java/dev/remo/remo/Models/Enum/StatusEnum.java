package dev.remo.remo.Models.Enum;

public enum StatusEnum {
    NON_ACTIVE("Non Active"),
    ACTIVE("Active"),
    NOT_STARTED("Not Started"),
    PENDING("Pending"),
    APPROVED("Approved"),
    COMPLETED("Completed");

    private final String code;

    StatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}