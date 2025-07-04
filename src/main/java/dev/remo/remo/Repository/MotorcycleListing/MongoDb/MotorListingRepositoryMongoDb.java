package dev.remo.remo.Repository.MotorcycleListing.MongoDb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;

import dev.remo.remo.Models.General.ModelCount;
import dev.remo.remo.Models.General.MonthCount;
import dev.remo.remo.Models.General.StatusCount;
import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;
import dev.remo.remo.Repository.MotorcycleListing.MotorListingRepository;
import dev.remo.remo.Utils.Enum.StatusEnum;
import dev.remo.remo.Utils.Exception.InternalServerErrorException;
import jakarta.annotation.PostConstruct;

public class MotorListingRepositoryMongoDb implements MotorListingRepository {

    @Autowired
    MotorListingMongoDb motorListingMongoDb;

    @Autowired
    MongoDatabase mongoDatabase;

    @Autowired
    MongoTemplate mongoTemplate;

    private GridFSBucket gridFSBucket;

    @PostConstruct
    public void init() {
        this.gridFSBucket = GridFSBuckets.create(mongoDatabase, "motorcycle_listing");
    }

    public List<String> uploadFiles(MultipartFile[] files) {
        
        List<String> storedFileIds = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                InputStream inputStream = file.getInputStream();
                String fileName = file.getOriginalFilename();

                ObjectId fileId = gridFSBucket.uploadFromStream(fileName, inputStream);
                storedFileIds.add(fileId.toString());
            } catch (IOException e) {
                throw new InternalServerErrorException("Failed to upload file: " + file.getOriginalFilename());
            }
        }

        return storedFileIds;
    }

    public void deleteMotorcycleListingImage(List<ObjectId> imageIds) {
        for (ObjectId imageId : imageIds) {
            gridFSBucket.delete(imageId);
        }
    }

    public Page<MotorcycleListingDO> getListingsById(List<ObjectId> listingId, Pageable pageable) {
        Query query = new Query(Criteria.where("_id").in(listingId));
        long total = mongoTemplate.count(query, MotorcycleListingDO.class);
        query.with(pageable);
        return new PageImpl<>(mongoTemplate.find(query, MotorcycleListingDO.class), Pageable.unpaged(), total);
    }

    public Optional<MotorcycleListingDO> getListingById(ObjectId id) {
        return motorListingMongoDb.findById(id);
    }

    public void updateMotorcycleListingInspection(ObjectId id, String inspectionId) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.updateFirst(query, new Update().set("inspectionId", inspectionId), MotorcycleListingDO.class);
    }

    public MotorcycleListingDO createOrUpdateListing(MotorcycleListingDO listing) {
        return motorListingMongoDb.save(listing);
    }

    public void deleteMotorcycleListingById(ObjectId listingId) {
        motorListingMongoDb.deleteById(listingId);
    }

    public void updateMotorcycleListingStatus(ObjectId objectId, String status, Map<String, String> extInfo) {
        Query query = new Query(Criteria.where("_id").is(objectId));
        Update update = new Update()
                .set("status", status)
                .set("extInfo", extInfo);
        mongoTemplate.updateFirst(query, update, MotorcycleListingDO.class);
    }

    public List<MotorcycleListingDO> getAllListingsForAdmin(Pageable pageable) {
        Query query = new Query();
        query.with(pageable);
        return mongoTemplate.find(query, MotorcycleListingDO.class);
    }

    public List<MotorcycleListingDO> getAllListingsForUser(Pageable pageable) {
        Query query = new Query(Criteria.where("status").is(StatusEnum.ACTIVE.toString()));
        query.with(pageable);
        return mongoTemplate.find(query, MotorcycleListingDO.class);
    }

    public Optional<Resource> getMotorcycleListingImageById(ObjectId id) {
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(id);
        return Optional.ofNullable(new GridFsResource(downloadStream.getGridFSFile(), downloadStream));
    }

    public Page<MotorcycleListingDO> getMotorcycleListingByUserId(String id, Pageable pageable) {
        Query query = new Query(Criteria.where("userId").is(id));
        long total = mongoTemplate.count(query, MotorcycleListingDO.class);
        query.with(pageable);
        return new PageImpl<>(mongoTemplate.find(query, MotorcycleListingDO.class), pageable, total);
    }

    public List<MotorcycleListingDO> getMotorcycleListingByUserId(String id) {
        Query query = new Query(Criteria.where("userId").is(id));
        return mongoTemplate.find(query, MotorcycleListingDO.class);
    }

    public Page<MotorcycleListingDO> getMotorcycleListingByFilter(List<Criteria> criteriaList, Pageable pageable) {

        Query query = new Query();

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, MotorcycleListingDO.class);
        query.with(pageable);

        return new PageImpl<>(mongoTemplate.find(query, MotorcycleListingDO.class), pageable, total);
    }

    public int getMotorcycleListingCount() {
        return (int) motorListingMongoDb.count();
    }

    public List<MonthCount> getNewListingsPerMonth() {

        // Step 1: Convert the Date field directly to "YYYY-MM"
        ProjectionOperation projectToMonth = Aggregation.project()
                .andExpression("{ $dateToString: { format: \"%Y-%m\", date: \"$createdAt\" } }")
                .as("month");

        // Step 2: Group by month and count
        GroupOperation groupByMonth = Aggregation.group("month").count().as("count");

        // Step 3: Rename _id to month
        ProjectionOperation projectFinal = Aggregation.project()
                .and("_id").as("month")
                .and("count").as("count");

        // Step 4: Sort
        SortOperation sort = Aggregation.sort(Sort.Direction.ASC, "month");

        Aggregation aggregation = Aggregation.newAggregation(
                projectToMonth,
                groupByMonth,
                projectFinal,
                sort);

        AggregationResults<MonthCount> results = mongoTemplate.aggregate(aggregation, "motorcycle_listing",
                MonthCount.class);

        return results.getMappedResults();
    }

    public List<StatusCount> getListingCountByStatus() {

        GroupOperation groupByStatus = Aggregation.group("status").count().as("count");
        ProjectionOperation project = Aggregation.project()
                .and("_id").as("status")
                .and("count").as("count");
        Aggregation aggregation = Aggregation.newAggregation(groupByStatus, project);
        AggregationResults<StatusCount> results = mongoTemplate.aggregate(aggregation, "motorcycle_listing",
                StatusCount.class);

        return results.getMappedResults();
    }

    public List<ModelCount> getListingCountAndAvgPriceByMotorcycleId() {

        // Step 1: Match listings where status is NOT "SUBMITTED"
        MatchOperation matchNotSubmitted = Aggregation.match(Criteria.where("status").ne("SUBMITTED"));

        // Step 2: Group by motorcycleId, count, and average price
        GroupOperation groupByModel = Aggregation.group("motorcycleId")
                .count().as("count")
                .avg("price").as("avgPrice");

        // Step 3: Project fields
        ProjectionOperation project = Aggregation.project()
                .and("_id").as("motorcycleId")
                .and("count").as("count")
                .and("avgPrice").as("avgPrice");

        Aggregation aggregation = Aggregation.newAggregation(
                matchNotSubmitted,
                groupByModel,
                project);

        AggregationResults<ModelCount> results = mongoTemplate.aggregate(
                aggregation, "motorcycle_listing", ModelCount.class);

        return results.getMappedResults();
    }
}
