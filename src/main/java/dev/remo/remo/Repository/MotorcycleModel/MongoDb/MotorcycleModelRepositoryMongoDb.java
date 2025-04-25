package dev.remo.remo.Repository.MotorcycleModel.MongoDb;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;
import dev.remo.remo.Repository.MotorcycleModel.MotorcycleModelRepository;

public class MotorcycleModelRepositoryMongoDb implements MotorcycleModelRepository {

    @Autowired
    MotorcycleModelMongoDb motorcycleModelMongoDb;

    @Autowired
    MongoTemplate mongoTemplate;

    public List<MotorcycleModelDO> getMotorcycleList() {
        return motorcycleModelMongoDb.findAll();
    }

    @Override
    public Optional<MotorcycleModelDO> findByBrandAndModel(String brand, String model) {
        try {
            Query query = new Query(Criteria.where("brand").is(brand)
                    .and("model").is(model));
            MotorcycleModelDO result = mongoTemplate.findOne(query, MotorcycleModelDO.class);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<MotorcycleModelDO> getMotorcycleModelById(ObjectId id) {
        return motorcycleModelMongoDb.findById(id);
    }

    @Override
    public void addReviewIdToMotorcycleModel(MotorcycleModelDO motorcycleModelDO) {
        motorcycleModelMongoDb.save(motorcycleModelDO);
    }

}
