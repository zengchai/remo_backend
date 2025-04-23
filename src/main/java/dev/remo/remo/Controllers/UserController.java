package dev.remo.remo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.remo.remo.Models.Request.UpdateUserRequest;
import dev.remo.remo.Models.Response.GeneralResponse;
import dev.remo.remo.Models.Users.User;
import dev.remo.remo.Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest,
            HttpServletRequest http) {
        String accessToken = http.getHeader("Authorization").substring(7);
        User updateUser = updateUserRequest.convertToUser();
        userService.updateUser(accessToken, updateUser);
        System.err.println("asd");

        return ResponseEntity
                .ok(GeneralResponse.builder().success(true).error("").message("Updated successfully").build());

    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteUser(HttpServletRequest http) {
        try {
            String accessToken = http.getHeader("Authorization").substring(7);
            userService.deleteUser(accessToken);
            return ResponseEntity
                    .ok(GeneralResponse.builder().success(true).error("").message("Deleted Successfully").build());
        } catch (Exception e) {
            return ResponseEntity
                    .ok(GeneralResponse.builder().success(false).error(e.getMessage()).message("").build());
        }
    }
}
