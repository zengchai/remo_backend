package dev.remo.remo.Models.Response;

import lombok.Builder;
import lombok.Data;

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
