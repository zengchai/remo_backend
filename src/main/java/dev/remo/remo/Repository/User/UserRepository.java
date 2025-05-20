package dev.remo.remo.Repository.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Users.UserDO;

public interface UserRepository {
    
    void saveUser(UserDO user);
    void deleteUser(ObjectId id);
    Optional<UserDO> findByEmail(String email);
    void updateUser(UserDO user);
    void deleteImage(ObjectId id);
    String uploadImage(MultipartFile image);
    void favourite(ObjectId userId, List<String> motorcycleListingIds);
    Optional<UserDO> findByToken(String resetToken);
    Optional<UserDO> findById(ObjectId id);
    Optional<Resource> getUserImageById(ObjectId id);
    void updateResetToken(ObjectId userId, String resetToken, LocalDateTime resetTokenExpiry);
    void deleteResetToken(ObjectId userId);
    void updatePassword(ObjectId userId, String password);
    void removeFavouriteListingById(Query query,Update update);
}
