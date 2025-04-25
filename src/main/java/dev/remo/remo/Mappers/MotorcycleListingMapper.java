package dev.remo.remo.Mappers;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import dev.remo.remo.Models.Inspection.Inspection;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Utils.Enum.StatusEnum;
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
                .date(request.getDate())
                .status(StatusEnum.SUBMITTED)
                .manufacturedYear(request.getManufacturedYear())
                .mileage(request.getMileage())
                .cubicCapacity(request.getCubicCapacity())
                .transmission(request.getTransmission())
                .phoneNumber(request.getPhoneNumber())
                .state(request.getState())
                .area(request.getArea())
                .price(request.getPrice())
                .build();

    }

    public MotorcycleListingDO convertToMotorcycleListingDO(MotorcycleListing motorcycleListing) {

        MotorcycleListingDO.MotorcycleListingDOBuilder builder = MotorcycleListingDO.builder();

        if (StringUtils.isNotBlank(motorcycleListing.getId())) {
            builder.id(new ObjectId(motorcycleListing.getId()));
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
                .date(motorcycleListing.getDate())
                .state(motorcycleListing.getState())
                .area(motorcycleListing.getArea())
                .status(motorcycleListing.getStatus().getCode())
                .build();

    }

    public MotorcycleListing convertMotorcycleListingDOToModel(MotorcycleListingDO motorcycleListingDO) {

        MotorcycleListing.MotorcycleListingBuilder<?, ?> builder = MotorcycleListing.builder();

        if (StringUtils.isNotBlank(motorcycleListingDO.getInspectionId())) {
            builder.inspection(Inspection.builder().id(motorcycleListingDO.getInspectionId()).build());
        }

        return builder.id(motorcycleListingDO.getId().toString())
                .user(User.builder().id(motorcycleListingDO.getUserId()).build())
                .imagesIds(motorcycleListingDO.getImagesIds())
                .build();

    }
}
