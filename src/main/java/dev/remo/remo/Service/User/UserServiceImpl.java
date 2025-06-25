package dev.remo.remo.Service.User;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Mappers.UserMapper;
import dev.remo.remo.Models.General.MonthCount;
import dev.remo.remo.Models.Request.UpdateUserRequest;
import dev.remo.remo.Models.Response.UserProfileResponse;
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

            if (StringUtils.isNotBlank(existingUser.getImageId())) {
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

        userRepository.deleteImage(new ObjectId(user.getImageId()));
        logger.info("Image deleted: " + user.getImageId());

        userRepository.deleteUser(new ObjectId(user.getId()));
        logger.info("User deleted: " + user.getId());

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

    public void removeFavouriteMotorcycleListing(String motorcycleListingId) {

        logger.info("Removing favourite motorcycle listing: " + motorcycleListingId);

        Query query = new Query(Criteria.where("favouriteListingIds").in(motorcycleListingId));
        Update update = new Update().pull("favouriteListingIds", motorcycleListingId);
        userRepository.removeFavouriteListingById(query, update);

        logger.info("Favourite motorcycle listing removed: " + motorcycleListingId);
    }

    public Resource getUserImageById(String id) {
        return userRepository.getUserImageById(new ObjectId(id))
                .orElseThrow(() -> new NotFoundResourceException("Image not found"));
    }

    public List<MonthCount> getNewUsersPerMonth() {
        return userRepository.getNewUsersPerMonth();
    }

    public UserProfileResponse getMyProfile() {
        User user = authService.getCurrentUser();
        return userMapper.convertToUserProfileResponse(user);
    }

    public long getActiveUsers(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return userRepository.countActiveUsersSince(since);
    }

    public int getUserCount() {
        return userRepository.getUserCount();
    }
}
