package dev.remo.remo.Service.User;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Mappers.UserMapper;
import dev.remo.remo.Models.Request.UpdateUserRequest;
import dev.remo.remo.Models.Response.MotorcycleListingUserView;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Models.Users.UserDO;
import dev.remo.remo.Repository.User.UserRepository;
import dev.remo.remo.Service.Auth.AuthService;
import dev.remo.remo.Utils.Exception.NotFoundResourceException;
import io.micrometer.common.util.StringUtils;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    UserMapper userMapper;

    public void updateUser(String id, MultipartFile image, UpdateUserRequest updateUserRequest) {
        logger.info("Update user with id: " + id);
        User updatedUser = userMapper.convertUpdateRequestUserToUser(updateUserRequest);
        User existingUser = authService.validateUser(id);
        updatedUser.setId(existingUser.getId());

        if (image != null && !image.isEmpty()) {
            if (!StringUtils.isBlank(existingUser.getImageId())) {
                userRepository.deleteImage(new ObjectId(existingUser.getImageId()));
            }
            String imageId = userRepository.uploadImage(image);
            updatedUser.setImageId(imageId);
            logger.info("Image uploaded with id: " + imageId);
        }

        UserDO userDO = userMapper.convertToUserDOForUpdate(updatedUser);
        logger.info("User to be updated: " + userDO.toString());
        userRepository.updateUser(userDO);
        logger.info("User updated: " + userDO.toString());
    }

    public void deleteUser(String id) {
        User user = authService.validateUser(id);

        userRepository.deleteUser(new ObjectId(user.getId()));
        logger.info("User deleted: " + user.getId());

        userRepository.deleteImage(new ObjectId(user.getImageId()));
        logger.info("Image deleted: " + user.getImageId());
    }
    
    public Boolean favouriteMotorcycleListing(String motorcycleListingId) {
        logger.info("Favouriting motorcycle listing: " + motorcycleListingId);
        User user = authService.getCurrentUser();

        Boolean isFavourite = user.getFavouriteListingIds().stream()
                .anyMatch(listingId -> listingId.equals(motorcycleListingId));
        if (isFavourite) {
            user.getFavouriteListingIds().remove(motorcycleListingId);
        } else {
            user.getFavouriteListingIds().add(motorcycleListingId);
        }
        
        userRepository.favourite(new ObjectId(user.getId()), user.getFavouriteListingIds());
        logger.info("User favourite listing updated: " + user.getId() + " to " + user.getFavouriteListingIds());
        return isFavourite;
    }

    public User getUserById(String id) {
        return userMapper.convertToUser(userRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new NotFoundResourceException("User not found with id: " + id)));
    }
}
