package dev.remo.remo.Repository.MotorcycleListing.MongoDb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;
import dev.remo.remo.Repository.MotorcycleListing.MotorListingRepository;
import dev.remo.remo.Utils.Exception.MultipartfileUploadException;

public class MotorListingRepositoryMongoDb implements MotorListingRepository {

    @Autowired
    MotorListingMongoDb motorListingMongoDb;

    private MongoDatabase mongoDatabase;

    public List<String> uploadFiles(MultipartFile[] files) {
        GridFSBucket bucket = GridFSBuckets.create(mongoDatabase, "motorcycle_listing");
        List<String> storedFileIds = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                InputStream inputStream = file.getInputStream();
                String fileName = file.getOriginalFilename();

                ObjectId fileId = bucket.uploadFromStream(fileName, inputStream);
                storedFileIds.add(fileId.toString());
            } catch (IOException e) {
                throw new MultipartfileUploadException("Failed to upload file: " + file.getOriginalFilename());
            }
        }

        return storedFileIds;
    }

    public void deleteMotorcycleListingImage(List<String> imageIds) {
        GridFSBucket bucket = GridFSBuckets.create(mongoDatabase, "motorcycle_listing");

        for (String imageId : imageIds) {
            // gridFsOperations.delete(new Query(Criteria.where("_id").is(imageId)));
        }
    }

    public Optional<MotorcycleListingDO> getListingById(ObjectId id) {
        return motorListingMongoDb.findById(id);
    }

    public MotorcycleListingDO createOrUpdateListing(MotorcycleListingDO listing) {
        return motorListingMongoDb.save(listing);
    }

    public void deleteMotorcycleListingById(ObjectId listingId) {
        motorListingMongoDb.deleteById(listingId);
    }
}
