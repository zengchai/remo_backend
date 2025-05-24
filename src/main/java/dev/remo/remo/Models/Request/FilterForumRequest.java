package dev.remo.remo.Models.Request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilterForumRequest {
    
    private String brand;
    private String model;
}
