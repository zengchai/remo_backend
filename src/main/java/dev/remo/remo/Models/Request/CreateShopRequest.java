package dev.remo.remo.Models.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateShopRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Map Url is required")
    private String mapUrl;
}
