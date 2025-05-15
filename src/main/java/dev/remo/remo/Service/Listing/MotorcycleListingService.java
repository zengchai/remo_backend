package dev.remo.remo.Service.Listing;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Request.PredictPriceRequest;
import dev.remo.remo.Models.Response.MotorcycleListingDetailUserView;
import dev.remo.remo.Models.Response.MotorcycleListingUserView;

public interface MotorcycleListingService {

    void createOrUpdateMotorcycleListing(MultipartFile[] images,
            CreateOrUpdateListingRequest createOrUpdateListingRequest);

    void deleteMotorcycleListingById(String listingId);

    void updateMotorcycleListingInspection(MotorcycleListing listing, String inspectionId);

    void updateMotorcycleListingStatus(String listingId, String status, String remark);

    String predictPrice(PredictPriceRequest predictPriceRequest);

    MotorcycleListing getMotorcycleListingById(String listingId);

    Page<MotorcycleListingUserView> getMotorcycleListingListUserView(int page, int size);

    MotorcycleListingDetailUserView getMotorcycleListingDetailUserView(String listingId);

    Resource getMotorcycleListingImageById(String id);

    Page<MotorcycleListingUserView> getMyMotorcycleListing(int page,int size);

    Page<MotorcycleListingUserView> getMyFavouriteListings(int page, int size);

}
