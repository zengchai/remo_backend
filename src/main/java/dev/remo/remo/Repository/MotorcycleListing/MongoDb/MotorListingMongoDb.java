package dev.remo.remo.Repository.MotorcycleListing.MongoDb;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;

public interface MotorListingMongoDb extends MongoRepository<MotorcycleListingDO,ObjectId> {

}
