package dev.remo.remo.Repository.Inspection;

import java.util.Optional;

import org.bson.types.ObjectId;

import dev.remo.remo.Models.Inspection.InspectionDO;

public interface InspectionRepository {
    void createInspection(InspectionDO inspectionDO);
    Optional<InspectionDO> getInspectionById(ObjectId inspectionId);
    void updateInspectionStatus(ObjectId inspectionId, String status,String updatedBy, String updatedAt, String remark);
}
