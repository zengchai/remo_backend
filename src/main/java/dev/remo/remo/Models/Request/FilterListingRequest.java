package dev.remo.remo.Models.Request;

import lombok.Data;

// This class represents a request to filter motorcycle listings.
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
