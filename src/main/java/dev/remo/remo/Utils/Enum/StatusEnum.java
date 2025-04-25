package dev.remo.remo.Utils.Enum;

public enum StatusEnum {

    // When listing is submitted
    SUBMITTED("Submitted", 1),

    // When listing is rejected by the admin
    NON_ACTIVE("Non Active", 0),

    // When listing is approved by the admin
    ACTIVE("Active", 2),
    
    // When inspection is not created by the user
    NOT_STARTED("Not Started", 3),

    // When inspection is created by the user
    PENDING("Pending", 1),

    // When inspection is approved by the admin
    APPROVED("Approved", 4),

    // When inspection is completed by the technician
    COMPLETED("Completed", 5);

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