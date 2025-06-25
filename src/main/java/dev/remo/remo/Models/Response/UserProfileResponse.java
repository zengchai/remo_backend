package dev.remo.remo.Models.Response;

import lombok.Builder;
import lombok.Data;

// This class represents a user profile response when user details are requested.
@Data
@Builder
public class UserProfileResponse {

    private String id;

    private String name;

    private String email;

    private String phoneNumber;

    private String dob;

    private String imageId;
}
