package dev.remo.remo.Models.Request;

import lombok.Builder;
import lombok.Data;

// This class represents a request to filter forum posts based on motorcycle brand and model.
@Data
@Builder
public class FilterForumRequest {

    private String brand;

    private String model;
}
