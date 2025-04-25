package dev.remo.remo.Service.User;

import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.UpdateUserRequest;

public interface UserService {
    void updateUser(String id,MultipartFile image,UpdateUserRequest userRequest);
    void deleteUser(String id);

}
