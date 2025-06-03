package dev.remo.remo.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.remo.remo.Models.Request.SignInRequest;
import dev.remo.remo.Models.Request.SignUpRequest;
import dev.remo.remo.Models.Response.GeneralResponse;
import dev.remo.remo.Models.Response.JwtResponse;
import dev.remo.remo.Service.Auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpRequest request) {

        authService.registerUser(request);

        return ResponseEntity.ok(JwtResponse.builder()
                .success(true)
                .error("")
                .token("")
                .message("Register Successful")
                .build());
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(
            @Valid @RequestBody SignInRequest request, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtResponse jwtResponse = authService.signIn(request, response, authentication);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        JwtResponse jwtResponse = authService.refreshToken(refreshToken, response);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {

        authService.signOut(response);

        return ResponseEntity.ok(GeneralResponse.builder()
                .success(true)
                .message("Logged out successfully")
                .build());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {

        authService.initiateResetPassword(email);

        return ResponseEntity.ok(GeneralResponse.builder()
                .success(true)
                .message("Password reset link sent to your email")
                .build());
    }

    @PostMapping("/verify-reset-token")
    public ResponseEntity<?> verifyResetToken(@RequestBody Map<String,String> request) {

        authService.verifyResetToken(request.get("email"), request.get("token"));

        return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                .message("Token is valid")
                .build());

    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String,String> request) {

        authService.resetPassword(request.get("email"), request.get("token"), request.get("newPassword"));

        return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                .message("Password reset successfully").build());

    }

}
