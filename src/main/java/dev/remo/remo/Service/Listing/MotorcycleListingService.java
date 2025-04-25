package dev.remo.remo.Service.Listing;

import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Request.PredictPriceRequest;

public interface MotorcycleListingService {

    void createOrUpdateMotorcycleListing(MultipartFile[] images,
            CreateOrUpdateListingRequest createOrUpdateListingRequest);

    void deleteMotorcycleListingById(String listingId);

    void updateMotorcycleListingInspection(MotorcycleListing listing, String inspectionId);

    void updateMotorcycleListingStatus(String listingId, String status, String remark);

    String predictPrice(PredictPriceRequest predictPriceRequest);

    MotorcycleListing getMotorcycleListingById(String listingId);
}
