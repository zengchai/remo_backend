package dev.remo.remo.Utils.Enum;

public enum StatusEnum {

    // When listing is submitted
    SUBMITTED("SUBMITTED", 0),

    // When listing is approved by the admin
    ACTIVE("ACTIVE", 4),
    
    // When listing is approved by the admin
    NOT_ACTIVE("NOT ACTIVE", 4),

    // When inspection is not created by the user
    NOT_STARTED("NOT STARTED", 2),

    // When inspection is created by the user or when listing is rejected by the admin
    PENDING("PENDING", 3),
    
    // When inspection is approved by the admin
    NOT_APPROVED("NOT APPROVED", 3),

    // When inspection is approved by the admin
    APPROVED("APPROVED", 4),

    // When inspection is complete updated by the technician
    COMPLETED("COMPLETED", 5);

    private final String code;
    private final int priority;

    StatusEnum(String code, int priority) {
        this.code = code;
        this.priority = priority;
    }

    public String getCode() {
        return code;
    }

    public int getPriority() {
        return priority;
    }

    public static StatusEnum fromCode(String code) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

}