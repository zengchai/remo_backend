package dev.remo.remo.Service.User;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Mappers.UserMapper;
import dev.remo.remo.Models.Request.UpdateUserRequest;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Models.Users.UserDO;
import dev.remo.remo.Repository.User.UserRepository;
import dev.remo.remo.Service.Auth.AuthService;
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

        UserDO userDO = userMapper.convertToUserDO(updatedUser);
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
}
