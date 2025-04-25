package dev.remo.remo.Repository.User.MongoDb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListingDO;
import dev.remo.remo.Models.Users.UserDO;
import dev.remo.remo.Repository.User.UserRepository;
import dev.remo.remo.Utils.Exception.InternalServerErrorException;

public class UserRespositoryMongoDb implements UserRepository {

    @Autowired
    UserMongoDb userMongoDb;

    @Autowired
    MongoDatabase mongoDatabase;

    @Autowired
    MongoTemplate mongoTemplate;

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
        GridFSBucket bucket = GridFSBuckets.create(mongoDatabase, "user");
        bucket.delete(id);
    }

    public String uploadImage(MultipartFile file) {
        GridFSBucket bucket = GridFSBuckets.create(mongoDatabase, "user");
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            ObjectId fileId = bucket.uploadFromStream(fileName, inputStream);
            return fileId.toString();
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to upload file: " + file.getOriginalFilename());
        }
    }
}
