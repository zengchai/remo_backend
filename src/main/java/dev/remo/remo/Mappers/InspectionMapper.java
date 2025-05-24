package dev.remo.remo.Mappers;

import org.springframework.stereotype.Component;

import dev.remo.remo.Models.Inspection.Inspection;
import dev.remo.remo.Models.Inspection.InspectionDO;
import dev.remo.remo.Models.Inspection.Shop.Shop;
import dev.remo.remo.Models.Inspection.Shop.ShopDO;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Request.CreateInspectionRequest;
import dev.remo.remo.Models.Request.CreateShopRequest;
import dev.remo.remo.Models.Response.InspectionDetailAdminView;
import dev.remo.remo.Models.Response.InspectionDetailUserView;
import dev.remo.remo.Utils.Enum.StatusEnum;
import dev.remo.remo.Utils.Enum.VehicleComponentEnum;

@Component
public class InspectionMapper {

        public Inspection toDomain(CreateInspectionRequest inspectionRequest) {
                return Inspection.builder()
                                .shop(Shop.builder().id(inspectionRequest.getShopId()).build())
                                .date(inspectionRequest.getDate())
                                .time(inspectionRequest.getTime())
                                .build();
        }

        public InspectionDO convertInspectionToInspectionDO(Inspection inspection) {
                return InspectionDO.builder()
                                .date(inspection.getDate())
                                .time(inspection.getTime())
                                .shopId(inspection.getShop().getId())
                                .motorcycleListingId(inspection.getMotorcycleListing().getId())
                                .status(inspection.getStatus().getCode())
                                .build();
        }

        public InspectionDO initializInspectionDO(Inspection inspection) {
                return InspectionDO.builder()
                                .status(inspection.getStatus().getCode())
                                .build();
        }

        public Shop convertShopDOToShop(ShopDO shopDO) {
                return Shop.builder()
                                .id(shopDO.getId().toString())
                                .name(shopDO.getName())
                                .address(shopDO.getAddress())
                                .mapUrl(shopDO.getMapUrl())
                                .imageId(shopDO.getId().toString())
                                .build();
        }

        public Shop convertRequestToDomain(CreateShopRequest createShopRequest) {
                return Shop.builder()
                                .name(createShopRequest.getName())
                                .address(createShopRequest.getAddress())
                                .mapUrl(createShopRequest.getMapUrl())
                                .build();
        }

        public ShopDO convertShopToShopDO(Shop shop) {
                return ShopDO.builder()
                                .name(shop.getName())
                                .address(shop.getAddress())
                                .mapUrl(shop.getMapUrl())
                                .imageId(shop.getImageId())
                                .build();
        }

        public Inspection convertInspectionDOToInspection(InspectionDO inspectionDO) {
                Inspection.InspectionBuilder builder = Inspection.builder();

                if (inspectionDO.getComponentScores() != null) {
                        builder.vehicleComponentMap(
                                        VehicleComponentEnum.convertToEnumGroupedMap(
                                                        inspectionDO.getComponentScores()));
                }

                if (inspectionDO.getExtInfo() != null) {
                        builder.extInfo(inspectionDO.getExtInfo());
                }

                return builder.id(inspectionDO.getId().toString())
                                .date(inspectionDO.getDate())
                                .time(inspectionDO.getTime())
                                .motorcycleListing(MotorcycleListing.builder()
                                                .id(inspectionDO.getMotorcycleListingId())
                                                .build())
                                .shop(Shop.builder()
                                                .id(inspectionDO.getShopId().toString())
                                                .build())
                                .status(StatusEnum.fromCode(inspectionDO.getStatus()))
                                .build();
        }

        public InspectionDetailUserView convertInspectionToDetailView(Inspection inspection, ShopDO shop) {
                InspectionDetailUserView.InspectionDetailUserViewBuilder inspectionDetailUserViewBuilder = InspectionDetailUserView
                                .builder();

                if (inspection.getVehicleComponentMap() != null) {
                        inspectionDetailUserViewBuilder.vehicleComponentMap(
                                        VehicleComponentEnum.convertToStringGroupedMap(
                                                        inspection.getVehicleComponentMap()));
                }

                return inspectionDetailUserViewBuilder
                                .id(inspection.getId())
                                .date(inspection.getDate())
                                .brand(inspection.getMotorcycleListing().getMotorcycleModel().getBrand())
                                .model(inspection.getMotorcycleListing().getMotorcycleModel().getModel())
                                .shopName(shop.getName())
                                .address(shop.getAddress())
                                .shopImageId(shop.getImageId())
                                .time(inspection.getTime())
                                .status(inspection.getStatus().getCode())
                                .extInfo(inspection.getExtInfo() != null ? inspection.getExtInfo() : null)
                                .build();

        }

        public InspectionDetailAdminView convertToAdminView(InspectionDO inspectionDO, ShopDO shop) {
                return InspectionDetailAdminView.builder()
                                .id(inspectionDO.getId().toString())
                                .motorcycleLisitngId(inspectionDO.getMotorcycleListingId())
                                .status(inspectionDO.getStatus())
                                .date(inspectionDO.getDate())
                                .time(inspectionDO.getTime())
                                .shopName(shop.getName())
                                .build();
        }
}