package dev.remo.remo.Repository.Forum.MongoDb;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import dev.remo.remo.Models.Forum.ReviewDO;

public interface ForumMongoDb extends MongoRepository<ReviewDO, ObjectId> {

    Page<ReviewDO> findAll(Pageable pageable);
}
