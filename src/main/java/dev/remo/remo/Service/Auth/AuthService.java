package dev.remo.remo.Service.Auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import dev.remo.remo.Models.Request.SignInRequest;
import dev.remo.remo.Models.Request.SignUpRequest;
import dev.remo.remo.Models.Response.JwtResponse;
import dev.remo.remo.Models.Users.User;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService extends UserDetailsService {

    void registerUser(SignUpRequest user);

    User getCurrentUser();

    User validateUser(String userId);

    JwtResponse refreshToken(String refreshToken, HttpServletResponse response);

    JwtResponse signIn(SignInRequest signInRequest, HttpServletResponse response, Authentication authentication);

    void signOut(HttpServletResponse response);

    UserDetails loadUserByUsername(String email);

    void initiateResetPassword(String email);

    void verifyResetToken(String token);

    void resetPassword(String token, String newPassword);
}
