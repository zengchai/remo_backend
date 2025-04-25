package dev.remo.remo.Repository.MotorcycleModel.MongoDb;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;

public interface MotorcycleModelMongoDb extends MongoRepository<MotorcycleModelDO, ObjectId> {
}
