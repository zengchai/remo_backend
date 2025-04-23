package dev.remo.remo.Mappers;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import dev.remo.remo.Models.Enum.StatusEnum;
import dev.remo.remo.Models.Inspection.Inspection;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import io.micrometer.common.util.StringUtils;

@Component
public class MotorcycleListingMapper {

    public static MotorcycleListing toDomain(CreateOrUpdateListingRequest request) {
        MotorcycleListing.MotorcycleListingBuilder<?, ?> builder = MotorcycleListing.builder();

        if (StringUtils.isNotBlank(request.getId())) {
            builder.id(request.getId());
        }

        return builder
                .inspection(Inspection.builder().status(StatusEnum.NOT_STARTED).build())
                .date(request.getDate())
                .manufacturedYear(request.getManufacturedYear())
                .mileage(request.getMileage())
                .price(request.getPrice())
                .motorcycleModel(MotorcycleModel.builder()
                        .brand(request.getBrand())
                        .model(request.getModel())
                        .build())
                .transmission(request.getTransmission())
                .cubicCapacity(request.getCubicCapacity())
                .state(request.getState())
                .area(request.getArea())
                .phoneNumber(request.getPhoneNumber())
                .build();

    }

    public MotorcycleListingDO convertToMotorcycleListingDO(MotorcycleListing motorcycleListing) {

        MotorcycleListingDO.MotorcycleListingDOBuilder builder = MotorcycleListingDO.builder();

        if (StringUtils.isNotBlank(motorcycleListing.getId())) {
            builder.id(new ObjectId(motorcycleListing.getId()));
        }

        return builder.motorcycleId(motorcycleListing.getMotorcycleModel().getId())
                .inspectionId(motorcycleListing.getInspection().getId())
                .manufacturedYear(motorcycleListing.getManufacturedYear()).mileage(motorcycleListing.getMileage())
                .cubicCapacity(motorcycleListing.getCubicCapacity()).transmission(motorcycleListing.getTransmission())
                .phoneNumber(motorcycleListing.getPhoneNumber()).price(motorcycleListing.getPrice())
                .date(motorcycleListing.getDate())
                .state(motorcycleListing.getState()).area(motorcycleListing.getArea()).build();

    }
}
