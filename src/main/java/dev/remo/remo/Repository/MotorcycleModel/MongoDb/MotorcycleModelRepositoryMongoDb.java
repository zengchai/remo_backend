package dev.remo.remo.Repository.MotorcycleModel.MongoDb;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModelDO;
import dev.remo.remo.Repository.MotorcycleModel.MotorcycleModelRepository;
import dev.remo.remo.Utils.Exception.InternalServerErrorException;
import jakarta.annotation.PostConstruct;

public class MotorcycleModelRepositoryMongoDb implements MotorcycleModelRepository {

    @Autowired
    MotorcycleModelMongoDb motorcycleModelMongoDb;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoDatabase mongoDatabase;

    private GridFSBucket gridFSBucket;

    @PostConstruct
    public void init() {

        this.gridFSBucket = GridFSBuckets.create(mongoDatabase, "motorcycle_models");
    }

    public List<MotorcycleModelDO> getMotorcycleList() {

        return motorcycleModelMongoDb.findAll();
    }

    @Override
    public Optional<MotorcycleModelDO> findByBrandAndModel(String brand, String model) {

        Query query = new Query(Criteria.where("brand").is(brand)
                .and("model").is(model));
        MotorcycleModelDO result = mongoTemplate.findOne(query, MotorcycleModelDO.class);

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<MotorcycleModelDO> getMotorcycleModelById(ObjectId id) {
        return motorcycleModelMongoDb.findById(id);
    }

    @Override
    public void addOrUpdateMotorcycleModel(MotorcycleModelDO motorcycleModelDO) {
        motorcycleModelMongoDb.save(motorcycleModelDO);
    }

    public Page<MotorcycleModelDO> getAllMotorcycleModelByPage(Pageable pageable) {
        return motorcycleModelMongoDb.findAll(pageable);
    }

    public String uploadFiles(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            ObjectId fileId = gridFSBucket.uploadFromStream(fileName, inputStream);
            return fileId.toString();
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to upload file: " + file.getOriginalFilename());
        }
    }

    public Optional<Resource> getMotorcycleModelImageById(ObjectId id) {
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(id);
        return Optional.ofNullable(new GridFsResource(downloadStream.getGridFSFile(), downloadStream));
    }

    public List<MotorcycleModelDO> findByBrand(String brand) {
        Query query = new Query(Criteria.where("brand").is(brand));
        return mongoTemplate.find(query, MotorcycleModelDO.class);
    }

    public Page<MotorcycleModelDO> getMotorcycleModelByFilter(List<Criteria> criteriaList, Pageable pageable) {

        Query query = new Query();

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, MotorcycleModelDO.class);
        query.with(pageable);

        return new PageImpl<>(mongoTemplate.find(query, MotorcycleModelDO.class), pageable, total);
    }
}
