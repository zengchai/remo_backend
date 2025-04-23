package dev.remo.remo.Repository.User;

import org.bson.types.ObjectId;

import dev.remo.remo.Models.Users.UserDO;

public interface UserRepository {
    
    Boolean checkByName(String name);
    Boolean saveUser(UserDO user);
    Boolean deleteUser(ObjectId id);
    UserDO findByEmail(String email);
    void updateUser(UserDO user);
}
