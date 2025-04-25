package dev.remo.remo.Repository.Shop.MongoDb;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import dev.remo.remo.Models.Inspection.Shop.ShopDO;

public interface ShopMongoDb extends MongoRepository<ShopDO, ObjectId> {

    
}
