package dev.remo.remo.Service.Listing;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import dev.remo.remo.Mappers.MotorcycleListingMapper;
import dev.remo.remo.Models.Enum.StatusEnum;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;
import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Request.PredictPriceRequest;
import dev.remo.remo.Repository.MotorcycleListing.MotorListingRepository;
import dev.remo.remo.Service.Ai.AiChatbotService;
import dev.remo.remo.Service.MotorcycleModel.MotorcycleModelService;
import dev.remo.remo.Service.User.UserService;
import io.micrometer.common.util.StringUtils;

public class MotorcycleListingServiceImpl implements MotorcycleListingService {

    @Autowired
    MotorcycleModelService motorcycleModelService;

    @Autowired
    UserService userService;

    @Autowired
    MotorListingRepository motorListingRepository;

    @Autowired
    AiChatbotService aiChatbotService;

    @Autowired
    MotorcycleListingMapper motorcycleListingMapper;

    public Boolean createOrUpdateMotorcycleListing(CreateOrUpdateListingRequest createOrUpdateListingRequest,
            String accessToken) {

        MotorcycleListing motorcycleListing = MotorcycleListingMapper.toDomain(createOrUpdateListingRequest);

        if (StringUtils.isBlank(accessToken)) {
            return false;
        }

        MotorcycleModel motorcycle = motorcycleModelService.getMotorcycleByBrandAndModel(
                motorcycleListing.getMotorcycleModel().getBrand(), motorcycleListing.getMotorcycleModel().getModel());

        if (motorcycle == null) {
            return false;
        }

        motorcycleListing.setUser(userService.getUserByAccessToken(accessToken));
        motorcycleListing.setMotorcycleModel(motorcycle);
        motorcycleListing.setStatus(StatusEnum.PENDING);

        MotorcycleListingDO motorcycleListingDO = motorcycleListingMapper
                .convertToMotorcycleListingDO(motorcycleListing);

        if (StringUtils.isNotBlank(motorcycleListing.getId())) {
            motorcycleListingDO.setId(new ObjectId(motorcycleListing.getId()));
        }

        motorListingRepository.createOrUpdateListing(motorcycleListingDO);

        return true;
    }

    public String predictPrice(PredictPriceRequest request) {
        return aiChatbotService.extractFromResponse(aiChatbotService.buildPrompt(request));
    }

}
