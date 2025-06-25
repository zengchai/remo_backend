package dev.remo.remo.Models.Inspection.Shop;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Shop {

    private String id;

    private String name;

    private String address;

    private String mapUrl;

    private String imageId;
}
