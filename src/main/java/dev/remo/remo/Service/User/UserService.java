package dev.remo.remo.Service.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import dev.remo.remo.Models.Users.User;

public interface UserService extends UserDetailsService {

    Boolean checkByName(String name);
    Boolean checkByEmail(String email);
    Boolean registerUser(User user);
    void updateUser(String accessToken,User user);
    Boolean deleteUser(String accessToken);
    User getUserByAccessToken(String accessToken);
    UserDetails loadUserByUsername(String email);

}
