package dev.remo.remo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.UpdateUserRequest;
import dev.remo.remo.Models.Response.GeneralResponse;
import dev.remo.remo.Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @RequestPart(value = "metadata") UpdateUserRequest updateUserRequest,
            @RequestPart(value = "file", required = false) MultipartFile newImage,
            HttpServletRequest http) {

        userService.updateUser(id, newImage, updateUserRequest);

        return ResponseEntity
                .ok(GeneralResponse.builder().success(true).error("").message("Updated successfully").build());

    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteUser(@PathVariable String id, HttpServletRequest http) {

        userService.deleteUser(id);

        return ResponseEntity
                .ok(GeneralResponse.builder().success(true).error("").message("Deleted Successfully").build());
    }
}
