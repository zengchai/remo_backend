package dev.remo.remo.Service.User;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.UpdateUserRequest;
import dev.remo.remo.Models.Response.UserProfileResponse;
import dev.remo.remo.Models.Users.User;

public interface UserService {
    void updateUser(String id, MultipartFile image, UpdateUserRequest userRequest);

    void deleteUser(String id);

    Boolean favouriteMotorcycleListing(String motorcycleListingId);

    User getUserById(String id);

    UserProfileResponse getMyProfile();

    void removeFavouriteMotorcycleListing(String motorcycleListingId);

    Resource getUserImageById(String id);
}
