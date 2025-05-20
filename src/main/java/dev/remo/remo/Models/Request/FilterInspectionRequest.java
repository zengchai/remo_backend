package dev.remo.remo.Models.Request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilterInspectionRequest {

    private String status;
    private String shopId;
    private String minDate;
    private String maxDate;
    private String minTime;
    private String maxTime;
    
}
