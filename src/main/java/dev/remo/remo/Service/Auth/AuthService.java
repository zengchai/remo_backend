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

    User getCurrentUser();

    User validateUser(String userId);

    UserDetails loadUserByUsername(String email);

    JwtResponse refreshToken(String refreshToken, HttpServletResponse response);

    JwtResponse signIn(SignInRequest signInRequest, HttpServletResponse response, Authentication authentication);

    void registerUser(SignUpRequest user);

    void signOut(HttpServletResponse response);

    void initiateResetPassword(String email);

    void verifyResetToken(String email, String token);

    void resetPassword(String email, String token, String newPassword);
}
