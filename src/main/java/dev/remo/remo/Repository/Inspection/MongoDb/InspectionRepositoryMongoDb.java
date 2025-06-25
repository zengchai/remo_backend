package dev.remo.remo.Repository.Inspection.MongoDb;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public void updateInspectionReport(ObjectId inspectionId, String status,
            Map<String, Map<String, Integer>> componentScores, Map<String, String> extInfo) {
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

    public void deleteInspection(ObjectId inspectionId) {
        inspectionMongoDb.deleteById(inspectionId);
    }

    public List<InspectionDO> getInspectionStatusList(List<ObjectId> ids) {
        return inspectionMongoDb.findAllById(ids);
    }

    public List<InspectionDO> getInspectionByListingIds(List<String> listingIds) {
        Query query = new Query(Criteria.where("motorcycleListingId").in(listingIds));
        return mongoTemplate.find(query, InspectionDO.class);
    }

    public Page<InspectionDO> getAllInspection(List<Criteria> criteriaList, Pageable pageable) {

        Query query = new Query();

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, InspectionDO.class);
        query.with(pageable);

        return new PageImpl<>(mongoTemplate.find(query, InspectionDO.class), pageable, total);
    }
}
