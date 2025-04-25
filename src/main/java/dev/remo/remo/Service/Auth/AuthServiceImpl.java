package dev.remo.remo.Service.Auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import dev.remo.remo.Mappers.UserMapper;
import dev.remo.remo.Models.Request.SignInRequest;
import dev.remo.remo.Models.Request.SignUpRequest;
import dev.remo.remo.Models.Response.JwtResponse;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Models.Users.UserDO;
import dev.remo.remo.Repository.User.UserRepository;
import dev.remo.remo.Utils.Exception.InvalidStatusException;
import dev.remo.remo.Utils.Exception.NotFoundResourceException;
import dev.remo.remo.Utils.Exception.OwnershipNotMatchException;
import dev.remo.remo.Utils.JWTAuth.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;

public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserMapper userMapper;

    @Autowired
    JwtUtils jwtUtils;

    public void registerUser(SignUpRequest user) {
        logger.info("Registering user: {}", user.getEmail());
        User newUser = userMapper.convertSignUpRequestToUser(user);
        UserDO userDO = userRepository.findByEmail(newUser.getEmail()).orElse(null);

        if (userDO != null) {
            throw new InvalidStatusException("Email already exists");
        }

        newUser.setPassword(encoder.encode(newUser.getPassword()));
        userRepository.saveUser(userMapper.convertToUserDO(newUser));
        logger.info("User registered successfully: {}", user.getEmail());
    }

    public JwtResponse signIn(SignInRequest signInRequest, HttpServletResponse response,
            Authentication authentication) {
        logger.info("Signing in user: {}", signInRequest.getEmail());

        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);
        jwtUtils.setJwtCookie(response, refreshToken);

        User user = (User) authentication.getPrincipal();
        logger.info("User signed in successfully: {}", signInRequest.getEmail());
        return userMapper.convertToJwtResponse(user, accessToken);
    }

    public JwtResponse refreshToken(String refreshToken, HttpServletResponse response) {
        logger.info("Refreshing token for user");
        if (refreshToken == null || !jwtUtils.validateRefreshToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        User user = (User) loadUserByUsername(jwtUtils.getEmailFromRefreshToken(refreshToken));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
        String newAccessToken = jwtUtils.generateAccessToken(authentication);
        String newRefreshToken = jwtUtils.generateRefreshToken(authentication);

        jwtUtils.setJwtCookie(response, newRefreshToken);
        logger.info("New refresh token set in cookie");
        return userMapper.convertToJwtResponse(user, newAccessToken);
    }

    public void signOut(HttpServletResponse response) {
        logger.info("Signing out user");
        jwtUtils.cleanJwtCookie(response);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        UserDO userDO = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundResourceException("User Not Found with email: " + email));
        return userMapper.convertToUser(userDO);
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return user;
    }

    public User validateUser(String userId) {
        User user = getCurrentUser();
        
        if (!user.getId().equals(userId) && user.getRole().stream().noneMatch(role -> role.equals("ROLE_ADMIN"))) {
            throw new OwnershipNotMatchException("You don't own this resource");
        }
        return user;
    }

}
