package dev.remo.remo.Repository.Inspection;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;

import dev.remo.remo.Models.Inspection.InspectionDO;

public interface InspectionRepository {
    Optional<InspectionDO> createInspection(InspectionDO inspectionDO);
    Optional<InspectionDO> getInspectionById(ObjectId inspectionId);
    Optional<List<InspectionDO>> getInspectionStatusList(List<ObjectId> listingId);
    void updateInspectionStatus(ObjectId inspectionId, String status, Map<String,String> extInfo);
    void updateInspectionReport(ObjectId inspectionId, String status, Map<String,Map<String,Integer>> componentScore, Map<String,String> extInfo);
    void deleteInspection(ObjectId inspectionId);
}
