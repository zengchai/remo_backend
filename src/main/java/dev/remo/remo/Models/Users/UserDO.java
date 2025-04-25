package dev.remo.remo.Models.Users;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Document(collection = "user")
@Data
@Builder
public class UserDO {
    
    @Id
    private ObjectId id;

    @NotBlank
    private String name;
    
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;

    @NotEmpty
    private List<String> role;

    private String nric;
    private String phoneNum;
    private String imageId;
    private String dob;


}
