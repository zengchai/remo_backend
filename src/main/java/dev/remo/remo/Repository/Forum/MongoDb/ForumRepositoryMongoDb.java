package dev.remo.remo.Repository.Forum.MongoDb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

import dev.remo.remo.Models.Forum.ReviewDO;
import dev.remo.remo.Repository.Forum.ForumRepository;
import dev.remo.remo.Utils.Exception.InternalServerErrorException;

public class ForumRepositoryMongoDb implements ForumRepository {

    @Autowired
    ForumMongoDb forumMongoDb;

    @Autowired
    MongoDatabase mongoDatabase;

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
        GridFSBucket bucket = GridFSBuckets.create(mongoDatabase, "review");
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();

            ObjectId fileId = bucket.uploadFromStream(fileName, inputStream);
            return fileId.toString();
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to upload file: " + file.getOriginalFilename());
        }
    }

    public void deleteReviewImage(String Id) {
        GridFSBucket bucket = GridFSBuckets.create(mongoDatabase, "review");
        bucket.delete(new ObjectId(Id));
    }
}
