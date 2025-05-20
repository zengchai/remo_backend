package dev.remo.remo.Repository.MotorcycleListing;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    List<MotorcycleListingDO> getAllListingsForAdmin(Pageable pageable);

    List<MotorcycleListingDO> getAllListingsForUser(Pageable pageable);

    Optional<Resource> getMotorcycleListingImageById(String id);

    List<MotorcycleListingDO> getMotorcycleListingByUserId(String id);

    Page<MotorcycleListingDO> getMotorcycleListingByFilter(List<Criteria> query, Pageable pageable);
}
