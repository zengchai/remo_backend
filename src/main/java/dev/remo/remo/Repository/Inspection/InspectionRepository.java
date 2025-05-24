package dev.remo.remo.Repository.Inspection;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;

import dev.remo.remo.Models.Inspection.InspectionDO;

public interface InspectionRepository {
    Optional<InspectionDO> createInspection(InspectionDO inspectionDO);
    Optional<InspectionDO> getInspectionById(ObjectId inspectionId);
    List<InspectionDO> getInspectionStatusList(List<ObjectId> ids);
    List<InspectionDO> getAllInspection(Query query);
    List<InspectionDO> getInspectionByListingIds(List<String> listingIds);
    void updateInspectionStatus(ObjectId inspectionId, String status, Map<String,String> extInfo);
    void updateInspectionReport(ObjectId inspectionId, String status, Map<String,Map<String,Integer>> componentScore, Map<String,String> extInfo);
    void deleteInspection(ObjectId inspectionId);
}
