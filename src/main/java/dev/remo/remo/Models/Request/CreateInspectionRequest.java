package dev.remo.remo.Models.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// This class represents a request to create an inspection for a motorcycle listing.
@Data
public class CreateInspectionRequest {

    @NotBlank
    private String shopId;

    @NotBlank
    private String date;

    @NotBlank
    private String time;

    @NotBlank
    private String motorcycleListingId;
}
