package dev.remo.remo.Models.Response;

import lombok.Builder;
import lombok.Data;

// This class represents a shop response when shop details are requested.
@Data
@Builder
public class ShopResponse {

    private String id;

    private String name;

    private String address;

    private String mapUrl;

    private String imageId;
}
