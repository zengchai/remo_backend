package dev.remo.remo.Models.Request;

import lombok.Data;

@Data
public class FilterListingRequest {
    String brand;
    String model;
    String minYear;
    String maxYear;
    String minPrice;
    String maxPrice;
    String status;
}
