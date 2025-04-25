package dev.remo.remo.Repository.Inspection.MongoDb;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import dev.remo.remo.Models.Inspection.InspectionDO;

public interface InspectionMongoDb extends MongoRepository<InspectionDO, ObjectId> {
}
