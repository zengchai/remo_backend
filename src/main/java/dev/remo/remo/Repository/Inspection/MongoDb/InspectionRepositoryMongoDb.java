package dev.remo.remo.Repository.Inspection.MongoDb;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import dev.remo.remo.Models.Inspection.InspectionDO;
import dev.remo.remo.Repository.Inspection.InspectionRepository;

public class InspectionRepositoryMongoDb implements InspectionRepository {

    @Autowired
    InspectionMongoDb inspectionMongoDb;

    public void createInspection(InspectionDO inspectionDO) {
        inspectionMongoDb.save(inspectionDO);
    }

    public Optional<InspectionDO> getInspectionById(ObjectId inspectionId) {
        return inspectionMongoDb.findById(inspectionId);
    }

    public void updateInspectionStatus(ObjectId inspectionId, String status, String updatedBy, String updatedAt, String remark){
        inspectionMongoDb.updateStatus(inspectionId, status, updatedBy, updatedAt, remark);
    }

}
