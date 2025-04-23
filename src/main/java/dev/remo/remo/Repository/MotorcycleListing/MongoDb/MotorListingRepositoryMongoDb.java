package dev.remo.remo.Repository.MotorcycleListing.MongoDb;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;
import dev.remo.remo.Repository.MotorcycleListing.MotorListingRepository;

public class MotorListingRepositoryMongoDb implements MotorListingRepository {

    @Autowired
    MotorListingMongoDb motorListingMongoDb;

    public MotorcycleListingDO getListingById(ObjectId id) {
        return motorListingMongoDb.findById(id).orElse(null);
    }

    @Override
    public MotorcycleListingDO createOrUpdateListing(MotorcycleListingDO listing) {
        return motorListingMongoDb.save(listing);
    }

}
