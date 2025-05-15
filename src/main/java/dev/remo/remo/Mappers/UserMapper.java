package dev.remo.remo.Mappers;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import dev.remo.remo.Models.Request.SignUpRequest;
import dev.remo.remo.Models.Request.UpdateUserRequest;
import dev.remo.remo.Models.Response.JwtResponse;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Models.Users.UserDO;
import io.micrometer.common.util.StringUtils;

@Component
public class UserMapper {

    public User convertUpdateRequestUserToUser(UpdateUserRequest updateUserRequest) {
        return User.builder()
                .name(updateUserRequest.getName())
                .phoneNum(updateUserRequest.getPhoneNum())
                .nric(updateUserRequest.getNric())
                .dob(updateUserRequest.getDob())
                .build();
    }

    public UserDO convertToUserDOForUpdate(User user) {
        return UserDO.builder()
                .id(new ObjectId(user.getId()))
                .name(user.getName())
                .nric(user.getNric())
                .phoneNum(user.getPhoneNum())
                .imageId(user.getImageId())
                .dob(user.getDob())
                .build();
    }

    public UserDO convertToUserDO(User user) {
        UserDO.UserDOBuilder userDOBuilder = UserDO.builder();

        if (StringUtils.isNotBlank(user.getName())) {
            userDOBuilder.nric(user.getNric());
            userDOBuilder.phoneNum(user.getPhoneNum());
            userDOBuilder.imageId(user.getImageId());
            userDOBuilder.dob(user.getDob());
        }

        if (user.getFavouriteListingIds() == null) {
            userDOBuilder.favouriteListingIds(new ArrayList<>());
        } else {
            userDOBuilder.favouriteListingIds(user.getFavouriteListingIds());
        }

        return userDOBuilder
                .password(user.getPassword())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public User convertToUser(UserDO userDO) {
        User.UserBuilder<?, ?> userBuilder = User.builder();

        if (StringUtils.isNotBlank(userDO.getName())) {
            userBuilder.name(userDO.getName());
            userBuilder.nric(userDO.getNric());
            userBuilder.phoneNum(userDO.getPhoneNum());
            userBuilder.imageId(userDO.getImageId());
            userBuilder.dob(userDO.getDob());
        }

        if (StringUtils.isNotBlank(userDO.getResetToken())) {
            userBuilder.resetToken(userDO.getResetToken())
                    .resetTokenExpiry(userDO.getResetTokenExpiry());
        }

        
        return userBuilder.id(userDO.getId().toString())
                .password(userDO.getPassword())
                .email(userDO.getEmail())
                .favouriteListingIds(userDO.getFavouriteListingIds())
                .role(userDO.getRole())
                .build();
    }

    public User convertSignUpRequestToUser(SignUpRequest signUpRequest) {
        return User.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .role(signUpRequest.getRoles())
                .build();
    }

    public JwtResponse convertToJwtResponse(User user, String accessToken) {
        return JwtResponse.builder()
                .token(accessToken)
                .email(user.getEmail())
                .roles(user.getRole())
                .success(true)
                .error("")
                .message("User signed in successfully")
                .build();
    }

}
