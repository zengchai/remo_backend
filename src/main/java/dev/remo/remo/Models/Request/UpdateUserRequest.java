package dev.remo.remo.Models.Request;

import java.util.ArrayList;

import dev.remo.remo.Models.Users.User;
import lombok.Data;

@Data
public class UpdateUserRequest {
    
    private String name;
    private String phoneNumber;
    private String nric;
    private String dob;

    public User convertToUser(){
        return new User("",name,"","",nric,phoneNumber,dob,new ArrayList<>());
    }
}
