package dev.remo.remo.Repository.User;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Users.UserDO;

public interface UserRepository {
    
    void saveUser(UserDO user);
    void deleteUser(ObjectId id);
    Optional<UserDO> findByEmail(String email);
    void updateUser(UserDO user);
    void deleteImage(ObjectId id);
    String uploadImage(MultipartFile image);
}
