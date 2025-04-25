package dev.remo.remo.Mappers;

import org.springframework.stereotype.Component;

import dev.remo.remo.Models.Inspection.Inspection;
import dev.remo.remo.Models.Inspection.InspectionDO;
import dev.remo.remo.Models.Inspection.Shop.Shop;
import dev.remo.remo.Models.Inspection.Shop.ShopDO;
import dev.remo.remo.Models.Request.CreateInspectionRequest;
import dev.remo.remo.Utils.Enum.StatusEnum;

@Component
public class InspectionMapper {

    public Inspection toDomain(CreateInspectionRequest inspectionRequest) {
        return Inspection.builder()
                .shop(Shop.builder().id(inspectionRequest.getShopId()).build())
                .date(inspectionRequest.getDate())
                .time(inspectionRequest.getTime())
                .status(StatusEnum.PENDING)
                .build();
    }

    public InspectionDO convertInspectionToInspectionDO(Inspection inspection) {
        return InspectionDO.builder()
                .date(inspection.getDate())
                .time(inspection.getTime())
                .locationId(inspection.getShop().getId())
                .status(inspection.getStatus().getCode())
                .build();
    }

    public Shop convertShopDOToShop(ShopDO shopDO) {
        return Shop.builder()
                .id(shopDO.getId().toString())
                .name(shopDO.getName())
                .address(shopDO.getAddress())
                .mapUrl(shopDO.getMapUrl())
                .imageUrl(shopDO.getImageUrl())
                .build();
    }

    public Inspection convertInspectionDOToInspection(InspectionDO inspectionDO) {
        return Inspection.builder()
                .id(inspectionDO.getId().toString())
                .status(StatusEnum.fromCode(inspectionDO.getStatus()))
                .build();
    }
}