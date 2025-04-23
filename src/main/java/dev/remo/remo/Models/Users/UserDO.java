package dev.remo.remo.Models.Users;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Document(collection = "user")
@Data
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
    private String dob;

    public UserDO(@NotBlank String name, @NotBlank String email, @NotBlank String password, @NotBlank List<String> role,
            String nric, String phoneNum, String dob) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.nric = nric;
        this.phoneNum = phoneNum;
        this.dob = dob;
    }

}
