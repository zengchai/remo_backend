package dev.remo.remo.Repository.Inspection.MongoDb;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import dev.remo.remo.Models.Inspection.InspectionDO;

public interface InspectionMongoDb extends MongoRepository<InspectionDO, ObjectId> {
    @Query("{'_id': ?0}")
    @Update("{$set: {'status': ?1, 'extInfo.updatedBy': ?2, 'extInfo.updatedAt': ?3, 'extInfo.remark': ?4}}")
    void updateStatus(ObjectId inspectionId, String status, String updatedBy, String updatedAt, String remark);
}
