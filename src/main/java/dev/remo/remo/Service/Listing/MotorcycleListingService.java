package dev.remo.remo.Service.Listing;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.General.ModelCount;
import dev.remo.remo.Models.General.MonthCount;
import dev.remo.remo.Models.General.StatusCount;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Request.FilterListingRequest;
import dev.remo.remo.Models.Request.PredictPriceRequest;
import dev.remo.remo.Models.Response.MotorcycleListingDetailUserView;
import dev.remo.remo.Models.Response.MotorcycleListingUserView;
import dev.remo.remo.Models.Response.MotorcycleModelList;

public interface MotorcycleListingService {

    void createOrUpdateMotorcycleListing(MultipartFile[] images,
            CreateOrUpdateListingRequest createOrUpdateListingRequest);

    void createMotorcycleModel(String brand, String model, MultipartFile image);

    void deleteMotorcycleListingById(String listingId);

    void updateMotorcycleListingInspection(MotorcycleListing listing, String inspectionId);

    void updateMotorcycleListingStatus(String listingId, String status, String remark);

    String predictPrice(PredictPriceRequest predictPriceRequest);

    MotorcycleListing getMotorcycleListingById(String listingId);

    Page<MotorcycleListingUserView> getMotorcycleListingListUserView(int page, int size);

    MotorcycleListingDetailUserView getMotorcycleListingDetailUserView(String listingId);

    MotorcycleListingDetailUserView getMotorcycleListingForUpdate(String listingId);

    Resource getMotorcycleListingImageById(String id);

    Page<MotorcycleListingUserView> getMyMotorcycleListing(int page, int size);

    Page<MotorcycleListingUserView> getMyFavouriteListings(int page, int size);

    List<MotorcycleListing> getMotorcycleListingByUserId(String userId);

    Page<MotorcycleListingUserView> filterListings(FilterListingRequest filterRequest, int page, int size);

    Boolean favouriteMotorcycleListing(String motorcycleListingId);

    MotorcycleModelList getMotorcycleModelList();

    int getMotorcycleListingCount();

    void deleteMotorcycleListingsByUserId(String userId);

    List<MonthCount> getNewListingsPerMonth();

    List<StatusCount> getMotorcycleListingStatusCount();

    List<ModelCount> getListingCountAndAvgPriceByMotorcycleId();
}
