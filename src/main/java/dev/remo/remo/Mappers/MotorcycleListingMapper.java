package dev.remo.remo.Mappers;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import dev.remo.remo.Models.Inspection.Inspection;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Response.MotorcycleListingDetailUserView;
import dev.remo.remo.Models.Response.MotorcycleListingUserView;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Utils.Enum.StatusEnum;
import dev.remo.remo.Utils.General.DateUtil;
import io.micrometer.common.util.StringUtils;

@Component
public class MotorcycleListingMapper {

    public static MotorcycleListing toDomain(CreateOrUpdateListingRequest request) {
        MotorcycleListing.MotorcycleListingBuilder<?, ?> builder = MotorcycleListing.builder();

        if (StringUtils.isNotBlank(request.getId())) {
            builder.id(request.getId());
        }

        return builder
                .motorcycleModel(MotorcycleModel.builder()
                        .brand(request.getBrand())
                        .model(request.getModel())
                        .build())
                .inspection(Inspection.builder().status(StatusEnum.NOT_STARTED).build())
                .status(StatusEnum.SUBMITTED)
                .manufacturedYear(request.getManufacturedYear())
                .mileage(request.getMileage())
                .cubicCapacity(request.getCubicCapacity())
                .transmission(request.getTransmission())
                .phoneNumber(request.getPhoneNumber())
                .state(request.getState())
                .area(request.getArea())
                .plateNumber(request.getPlateNumber())
                .price(request.getPrice())
                .build();

    }

    public MotorcycleListingDO convertToMotorcycleListingDO(MotorcycleListing motorcycleListing) {

        MotorcycleListingDO.MotorcycleListingDOBuilder builder = MotorcycleListingDO.builder();

        if (StringUtils.isNotBlank(motorcycleListing.getId())) {
            builder.id(new ObjectId(motorcycleListing.getId()));
        }
        if (StringUtils.isBlank(motorcycleListing.getCreatedAt())) {
            builder.createdAt(DateUtil.getCurrentDateTime());
        }

        return builder
                .motorcycleId(motorcycleListing.getMotorcycleModel().getId())
                .inspectionId(motorcycleListing.getInspection().getId())
                .userId(motorcycleListing.getUser().getId())
                .manufacturedYear(motorcycleListing.getManufacturedYear())
                .mileage(motorcycleListing.getMileage())
                .cubicCapacity(motorcycleListing.getCubicCapacity())
                .transmission(motorcycleListing.getTransmission())
                .phoneNumber(motorcycleListing.getPhoneNumber())
                .price(motorcycleListing.getPrice())
                .state(motorcycleListing.getState())
                .plateNumber(motorcycleListing.getPlateNumber())
                .area(motorcycleListing.getArea())
                .status(motorcycleListing.getStatus().getCode())
                .build();
    }

    public MotorcycleListing convertMotorcycleListingDOToModel(MotorcycleListingDO motorcycleListingDO) {
        MotorcycleListing.MotorcycleListingBuilder<?, ?> builder = MotorcycleListing.builder();

        if (StringUtils.isNotBlank(motorcycleListingDO.getInspectionId())) {
            builder.inspection(Inspection.builder().id(motorcycleListingDO.getInspectionId()).build());
        }
        
        if (motorcycleListingDO.getExtInfo() != null) {
            builder.extInfo(motorcycleListingDO.getExtInfo());
        }

        return builder
                .id(motorcycleListingDO.getId().toString())
                .motorcycleModel(MotorcycleModel.builder().id(motorcycleListingDO.getMotorcycleId()).build())
                .user(User.builder().id(motorcycleListingDO.getUserId()).build())
                .manufacturedYear(motorcycleListingDO.getManufacturedYear())
                .mileage(motorcycleListingDO.getMileage())
                .cubicCapacity(motorcycleListingDO.getCubicCapacity())
                .transmission(motorcycleListingDO.getTransmission())
                .state(motorcycleListingDO.getState())
                .area(motorcycleListingDO.getArea())
                .price(motorcycleListingDO.getPrice())
                .phoneNumber(motorcycleListingDO.getPhoneNumber())
                .plateNumber(motorcycleListingDO.getPlateNumber())
                .status(StatusEnum.fromCode(motorcycleListingDO.getStatus()))
                .imagesIds(motorcycleListingDO.getImagesIds())
                .build();

    }

    public MotorcycleListingUserView convertToUserDTOView(MotorcycleListingDO motorcycleListingDO,
            MotorcycleModel motorcycleModel) {

        MotorcycleListingUserView.MotorcycleListingUserViewBuilder motorcycleListingUserViewBuilder = MotorcycleListingUserView
                .builder();

        if (motorcycleListingDO.getInspectionId() == null) {
            motorcycleListingUserViewBuilder
                    .insepctionStatus(StatusEnum.NOT_STARTED.getCode());
        } else {
            motorcycleListingUserViewBuilder.inspectionId(motorcycleListingDO.getInspectionId());
        }

        return motorcycleListingUserViewBuilder
                .id(motorcycleListingDO.getId().toString())
                .brand(motorcycleModel.getBrand())
                .model(motorcycleModel.getModel())
                .manufacturedYear(motorcycleListingDO.getManufacturedYear())
                .state(motorcycleListingDO.getState())
                .area(motorcycleListingDO.getArea())
                .mileage(motorcycleListingDO.getMileage())
                .price(motorcycleListingDO.getPrice())
                .phoneNumber(motorcycleListingDO.getPhoneNumber())
                .status(motorcycleListingDO.getStatus())
                .createdAt(motorcycleListingDO.getCreatedAt())
                .imagesIds(motorcycleListingDO.getImagesIds().get(0))
                .build();

    }

    public MotorcycleListingDetailUserView convertToDetailUserDTOView(MotorcycleListing motorcycleListing,
            MotorcycleModel motorcycleModel, Boolean isFavourite) {

        MotorcycleListingDetailUserView.MotorcycleListingDetailUserViewBuilder motorcycleListingDetailUserViewBuilder = MotorcycleListingDetailUserView
                .builder();

        if (motorcycleListing.getInspection() == null) {
            motorcycleListingDetailUserViewBuilder.inspectionStatus(StatusEnum.NOT_STARTED.getCode());
        } else {
            motorcycleListingDetailUserViewBuilder.inspectionId(motorcycleListing.getInspection().getId());
        }

        return motorcycleListingDetailUserViewBuilder
                .id(motorcycleListing.getId().toString())
                .brand(motorcycleModel.getBrand())
                .model(motorcycleModel.getModel())
                .userId(motorcycleListing.getUser().getId())
                .status(motorcycleListing.getStatus().getCode())
                .cubicCapacity(motorcycleListing.getCubicCapacity())
                .transmission(motorcycleListing.getTransmission())
                .manufacturedYear(motorcycleListing.getManufacturedYear())
                .state(motorcycleListing.getState())
                .area(motorcycleListing.getArea())
                .mileage(motorcycleListing.getMileage())
                .price(motorcycleListing.getPrice())
                .plateNumber(motorcycleListing.getPlateNumber())
                .phoneNumber(motorcycleListing.getPhoneNumber())
                .imagesIds(motorcycleListing.getImagesIds())
                .isFavourite(isFavourite)
                .build();

    }
}
