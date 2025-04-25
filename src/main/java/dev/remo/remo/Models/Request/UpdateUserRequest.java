package dev.remo.remo.Models.Request;

import java.util.ArrayList;

import dev.remo.remo.Models.Users.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "phoneNumber is required")
    private String phoneNum;
    @NotBlank(message = "nric is required")
    private String nric;
    @NotBlank(message = "dob is required")
    private String dob;

}
