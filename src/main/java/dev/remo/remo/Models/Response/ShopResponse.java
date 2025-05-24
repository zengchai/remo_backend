package dev.remo.remo.Models.Response;

import org.springframework.core.io.Resource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopResponse {
    
    private String id;
    private String name;
    private String address;
    private String mapUrl;
    private String imageId;
}
