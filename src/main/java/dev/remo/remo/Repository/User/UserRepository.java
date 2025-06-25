package dev.remo.remo.Repository.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.General.MonthCount;
import dev.remo.remo.Models.Users.UserDO;

public interface UserRepository {

    /**
     * Saves a user to the database.
     * 
     * @param user the UserDO object to save
     */
    void saveUser(UserDO user);

    /**
     * Deletes a user by their ID.
     * 
     * @param id the ID of the user to delete
     */
    void deleteUser(ObjectId id);

    /**
     * Finds a user by their email address.
     * 
     * @param email the email address of the user
     * @return an Optional containing the UserDO if found, or empty if not found
     */
    Optional<UserDO> findByEmail(String email);

    /**
     * Updates an existing user in the database.
     * 
     * @param user the UserDO object with updated information
     */
    void updateUser(UserDO user);

    /**
     * Deletes an image by its ID.
     * 
     * @param id the ID of the image to delete
     */
    void deleteImage(ObjectId id);

    /**
     * Uploads an image and returns its ID.
     * 
     * @param image the MultipartFile image to upload
     * @return the ID of the uploaded image
     */
    String uploadImage(MultipartFile image);

    /**
     * Adds the favourite motorcycle listings for a user.
     * 
     * @param userId               the ID of the user
     * @param motorcycleListingIds the list of motorcycle listing IDs to add to
     *                             favourites
     */
    void favourite(ObjectId userId, List<String> motorcycleListingIds);

    /**
     * Finds a user by their reset token.
     * 
     * @param resetToken the reset token of the user
     * @return an Optional containing the UserDO if found, or empty if not found
     */
    Optional<UserDO> findByToken(String resetToken);

    /**
     * Finds a user by their ID.
     * 
     * @param id the ID of the user
     * @return an Optional containing the UserDO if found, or empty if not found
     */
    Optional<UserDO> findById(ObjectId id);

    /**
     * Get the image of a user by their ID.
     * 
     * @param id the ID of the user
     * @return an Optional containing the Resource if found, or empty if not found
     */
    Optional<Resource> getUserImageById(ObjectId id);

    /**
     * Updates the reset token and its expiry time for a user.
     * 
     * @param userId           the ID of the user
     * @param resetToken       the reset token to set
     * @param resetTokenExpiry the expiry time of the reset token
     */
    void updateResetToken(ObjectId userId, String resetToken, LocalDateTime resetTokenExpiry);

    /**
     * Deletes the reset token for a user.
     * 
     * @param userId the ID of the user
     */
    void deleteResetToken(ObjectId userId);

    /**
     * Updates the password for a user.
     * 
     * @param userId   the ID of the user
     * @param password the new password to set
     */
    void updatePassword(ObjectId userId, String password);

    /**
     * Removes a motorcycle listing from the user's favourites by its ID.
     * 
     * @param query the query to find the user
     */
    void removeFavouriteListingById(Query query, Update update);

    /**
     * Retrieves a list of users who have registered in the last month.
     * 
     * @return a list of UserDO objects representing new users
     */
    List<MonthCount> getNewUsersPerMonth();

    /**
     * Updates the last login time for a user.
     * 
     * @param userId      the ID of the user
     * @param lastLoginAt the time of the last login
     */
    void updateLastLoginAt(ObjectId userId, LocalDateTime lastLoginAt);

    /**
     * Counts the number of active users since a specified date.
     * 
     * @param sinceDate the date from which to count active users
     * @return the count of active users
     */
    long countActiveUsersSince(LocalDateTime sinceDate);

    /**
     * Gets the count of all users in the database.
     * 
     * @return the total number of users
     */
    int getUserCount();
}
