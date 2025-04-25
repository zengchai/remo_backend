package dev.remo.remo.Service.User;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Models.Users.UserDO;
import dev.remo.remo.Repository.User.UserRepository;
import dev.remo.remo.Utils.JWTAuth.JwtUtils;

public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    public Boolean checkByName(String name) {
        return userRepository.checkByName(name);
    }

    public Boolean checkByEmail(String email) {
        UserDO userDO = userRepository.findByEmail(email);
        if (userDO != null) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean registerUser(User user) {
        // Encode the password
        user.setPassword(encoder.encode(user.getPassword()));

        // Save the user to db
        return userRepository.saveUser(convertToUserDO(user));
    }

    public void updateUser(String accessToken,User updateUser) {

        User user = getUserByAccessToken(accessToken);
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPassword(user.getPassword());
        updateUser.setRole(user.getRole());
        System.err.println(updateUser.toString());

        UserDO userDO = convertToUserDO(updateUser);
        userDO.setId(new ObjectId(updateUser.getId()));

        // Save the user to db
        userRepository.updateUser(userDO);
        System.err.println(userDO.toString());
    }

    public Boolean deleteUser(String accessToken){
        User user = getUserByAccessToken(accessToken);
        userRepository.deleteUser(new ObjectId(user.getId()));
        return true;
    }

    public User getUserByAccessToken(String accessToken){
        String email = jwtUtils.getEmailFromAccessToken(accessToken);
        User user = (User) loadUserByUsername(email);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user by email (instead of username)
        User user = convertToUser(userRepository.findByEmail(email));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Return the User object (which implements UserDetails)
        return user;
    }

    public UserDO convertToUserDO(User user) {
        return new UserDO(user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getNric(),
                user.getPhoneNum(), user.getDob());
    }

    public User convertToUser(UserDO userDO) {
        return new User(userDO.getId().toString(),userDO.getName(), userDO.getEmail(), userDO.getPassword(), userDO.getNric(),
                userDO.getPhoneNum(), userDO.getDob(), userDO.getRole());
    }
    
}
