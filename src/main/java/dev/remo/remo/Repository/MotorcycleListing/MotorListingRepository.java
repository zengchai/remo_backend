package dev.remo.remo.Repository.MotorcycleListing;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;

public interface MotorListingRepository {

    MotorcycleListingDO createOrUpdateListing(MotorcycleListingDO listing);

    Optional<MotorcycleListingDO> getListingById(ObjectId listingId);

    void updateMotorcycleListingInspection(ObjectId id, String inspectionId);

    void deleteMotorcycleListingById(ObjectId listingId);

    void deleteMotorcycleListingImage(List<ObjectId> imageIds);

    List<String> uploadFiles(MultipartFile[] files);

    void updateMotorcycleListingStatus(ObjectId objectId, String status, Map<String, String> extInfo);

}
