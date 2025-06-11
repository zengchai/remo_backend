package dev.remo.remo.Repository.MotorcycleListing;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.General.BrandModelAvgPrice;
import dev.remo.remo.Models.General.ModelCount;
import dev.remo.remo.Models.General.MonthCount;
import dev.remo.remo.Models.General.StatusCount;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;

public interface MotorListingRepository {

    MotorcycleListingDO createOrUpdateListing(MotorcycleListingDO listing);

    Page<MotorcycleListingDO> getListingsById(List<ObjectId> listingId, Pageable pageable);

    Optional<MotorcycleListingDO> getListingById(ObjectId listingId);

    void updateMotorcycleListingInspection(ObjectId id, String inspectionId);

    void deleteMotorcycleListingById(ObjectId listingId);

    void deleteMotorcycleListingImage(List<ObjectId> imageIds);

    List<String> uploadFiles(MultipartFile[] files);

    void updateMotorcycleListingStatus(ObjectId objectId, String status, Map<String, String> extInfo);

    List<MotorcycleListingDO> getAllListingsForAdmin(Pageable pageable);

    List<MotorcycleListingDO> getAllListingsForUser(Pageable pageable);

    Optional<Resource> getMotorcycleListingImageById(ObjectId id);

    Page<MotorcycleListingDO> getMotorcycleListingByUserId(String id, Pageable pageable);

    List<MotorcycleListingDO> getMotorcycleListingByUserId(String id);

    Page<MotorcycleListingDO> getMotorcycleListingByFilter(List<Criteria> query, Pageable pageable);

    int getMotorcycleListingCount();

    List<MonthCount> getNewListingsPerMonth();

    List<StatusCount> getListingCountByStatus();

    List<ModelCount> getListingCountAndAvgPriceByMotorcycleId();
}
