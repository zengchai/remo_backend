package dev.remo.remo.Repository.User.MongoDb;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;

import dev.remo.remo.Models.Users.UserDO;
import dev.remo.remo.Repository.User.UserRepository;
import dev.remo.remo.Utils.Exception.InternalServerErrorException;
import jakarta.annotation.PostConstruct;

public class UserRespositoryMongoDb implements UserRepository {

    @Autowired
    UserMongoDb userMongoDb;

    @Autowired
    MongoDatabase mongoDatabase;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoOperations mongoOps;

    private GridFSBucket gridFSBucket;

    @PostConstruct
    public void init() {
        this.gridFSBucket = GridFSBuckets.create(mongoDatabase, "user");
    }

    public void saveUser(UserDO user) {
        userMongoDb.save(user);
    }

    public void deleteUser(ObjectId id) {
        userMongoDb.deleteById(id);
    }

    public Optional<UserDO> findByEmail(String email) {
        return Optional.ofNullable(userMongoDb.findByEmail(email));
    }

    public void updateUser(UserDO user) {
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update()
                .set("name", user.getName())
                .set("nric", user.getNric())
                .set("phoneNum", user.getPhoneNum())
                .set("imageId", user.getImageId())
                .set("dob", user.getDob());
        mongoTemplate.updateFirst(query, update, UserDO.class);
    }

    public void deleteImage(ObjectId id) {
        gridFSBucket.delete(id);
    }

    public String uploadImage(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            ObjectId fileId = gridFSBucket.uploadFromStream(fileName, inputStream);
            return fileId.toString();
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to upload file: " + file.getOriginalFilename());
        }
    }

    public void favourite(ObjectId userId, List<String> motorcycleListingIds) {
        Query query = new Query(Criteria.where("_id").is(userId));
        Update update = new Update().set("favouriteListingIds", motorcycleListingIds);
        mongoTemplate.updateFirst(query, update, UserDO.class);
    }

    public void updateResetToken(ObjectId userId, String resetToken, LocalDateTime resetTokenExpiry) {
        Query query = new Query(Criteria.where("_id").is(userId));
        Update update = new Update()
                .set("resetToken", resetToken)
                .set("resetTokenExpiry", resetTokenExpiry);
        mongoTemplate.updateFirst(query, update, UserDO.class);
    }

    public void deleteResetToken(ObjectId userId) {
        Query query = new Query(Criteria.where("_id").is(userId));
        Update update = new Update()
                .unset("resetToken")
                .unset("resetTokenExpiry");
        mongoTemplate.updateFirst(query, update, UserDO.class);
    }

    public void updatePassword(ObjectId userId, String password) {
        Query query = new Query(Criteria.where("_id").is(userId));
        Update update = new Update()
                .set("password", password);
        mongoTemplate.updateFirst(query, update, UserDO.class);
    }

    public Optional<UserDO> findByToken(String resetToken) {
        Query query = new Query(Criteria.where("resetToken").is(resetToken));
        return Optional.ofNullable(mongoTemplate.findOne(query, UserDO.class));
    }

    public Optional<UserDO> findById(ObjectId id) {
        return userMongoDb.findById(id);
    }

    public void removeFavouriteListingById(Query query, Update update) {
        mongoOps.updateMulti(query, update, UserDO.class);
    }

    public Optional<Resource> getUserImageById(ObjectId id) {
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(id);
        return Optional.ofNullable(new GridFsResource(downloadStream.getGridFSFile(), downloadStream));
    }

}
