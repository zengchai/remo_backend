package dev.remo.remo.Repository.Inspection.MongoDb;

import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import dev.remo.remo.Models.Inspection.InspectionDO;
import dev.remo.remo.Repository.Inspection.InspectionRepository;

public class InspectionRepositoryMongoDb implements InspectionRepository {

    @Autowired
    InspectionMongoDb inspectionMongoDb;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void updateInspectionStatus(ObjectId inspectionId, String status, Map<String, String> extInfo) {
        Query query = new Query(Criteria.where("_id").is(inspectionId));
        Update update = new Update()
            .set("status", status)
            .set("extInfo", extInfo);
        mongoTemplate.updateFirst(query, update, InspectionDO.class);
    }

    @Override
    public void updateInspectionReport(ObjectId inspectionId, String status, Map<String, Map<String, Integer>> componentScores, Map<String, String> extInfo) {
        Query query = new Query(Criteria.where("_id").is(inspectionId));
        Update update = new Update()
            .set("status", status)
            .set("componentScores", componentScores)
            .set("extInfo", extInfo);
        mongoTemplate.updateFirst(query, update, InspectionDO.class);
    }

    public Optional<InspectionDO> createInspection(InspectionDO inspectionDO) {
        return Optional.ofNullable(inspectionMongoDb.save(inspectionDO));
    }

    public Optional<InspectionDO> getInspectionById(ObjectId inspectionId) {
        return inspectionMongoDb.findById(inspectionId);
    }

}
