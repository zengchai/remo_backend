package dev.remo.remo.Repository.Forum.MongoDb;

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

import dev.remo.remo.Models.Forum.ReviewDO;
import dev.remo.remo.Repository.Forum.ForumRepository;
import dev.remo.remo.Utils.Exception.InternalServerErrorException;
import jakarta.annotation.PostConstruct;

public class ForumRepositoryMongoDb implements ForumRepository {

    @Autowired
    ForumMongoDb forumMongoDb;

    @Autowired
    MongoDatabase mongoDatabase;

    @Autowired
    MongoTemplate mongoTemplate;

    private GridFSBucket gridFSBucket;

    @PostConstruct
    public void init() {
        this.gridFSBucket = GridFSBuckets.create(mongoDatabase, "review");
    }

    @Override
    public ReviewDO createOrUpdateReview(ReviewDO reviewDO) {
        return forumMongoDb.save(reviewDO);
    }

    @Override
    public Optional<ReviewDO> getReviewById(ObjectId reviewId) {
        return forumMongoDb.findById(reviewId);
    }

    @Override
    public void deleteReviewById(ObjectId reviewId) {
        forumMongoDb.deleteById(reviewId);
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

    public void deleteReviewImage(ObjectId id) {
        gridFSBucket.delete(id);
    }

    public Page<ReviewDO> getReviewsByUserId(String userId, Pageable pageable) {
        Query query = new Query(Criteria.where("userId").is(userId));
        query.with(pageable);
        long count = mongoTemplate.count(query, ReviewDO.class);
        return new PageImpl<>(mongoTemplate.find(query, ReviewDO.class), pageable, count);
    }

    public Optional<Resource> getReviewImageById(ObjectId id) {
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(id);
        return Optional.ofNullable(new GridFsResource(downloadStream.getGridFSFile(), downloadStream));
    }

    @Override
    public Page<ReviewDO> getReviewsByMotorcycleModelIdWithPaging(String motorcycleModelId, Pageable pageable) {
        Query query = new Query(Criteria.where("motorcycleModelId").is(motorcycleModelId));
        long count = mongoTemplate.count(query, ReviewDO.class);
        List<ReviewDO> reviews = mongoTemplate.find(query.with(pageable), ReviewDO.class);
        return new PageImpl<>(reviews, pageable, count);
    }

    public Page<ReviewDO> getAllReviewsByPage(Pageable pageable) {
        return forumMongoDb.findAll(pageable);
    }
}
