package dev.remo.remo.Service.Listing;

import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Request.PredictPriceRequest;

public interface MotorcycleListingService {

    Boolean createOrUpdateMotorcycleListing(CreateOrUpdateListingRequest motorcycle, String accessToken);
    String predictPrice(PredictPriceRequest motorcycle);
}
