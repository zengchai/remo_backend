package dev.remo.remo.Models.Request;

import lombok.Builder;
import lombok.Data;

// This class represents a request to filter inspections.
@Data
@Builder
public class FilterInspectionRequest {

    private String status;

    private String shopId;

    private String minDate;

    private String maxDate;

    private String time;
}
