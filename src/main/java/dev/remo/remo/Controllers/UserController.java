package dev.remo.remo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

        @PostMapping("/update/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> updateUser(
                        @PathVariable String id,
                        @RequestPart(value = "metadata") UpdateUserRequest updateUserRequest,
                        @RequestPart(value = "file", required = false) MultipartFile newImage,
                        HttpServletRequest http) {

                userService.updateUser(id, newImage, updateUserRequest);

                return ResponseEntity
                                .ok(GeneralResponse.builder().success(true).error("").message("Updated successfully")
                                                .build());
        }

        @GetMapping("/getmyprofile")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getMyProfile(
                        HttpServletRequest http) {

                return ResponseEntity
                                .ok(GeneralResponse.builder().success(true).error("").message("Fetched successfully")
                                                .data(userService.getMyProfile())
                                                .build());
        }

        @DeleteMapping("/delete/{id}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<?> deleteUser(@PathVariable String id, HttpServletRequest http) {

                userService.deleteUser(id);

                return ResponseEntity
                                .ok(GeneralResponse.builder().success(true).error("").message("Deleted Successfully")
                                                .build());
        }

        @GetMapping("/images/{id}")
        public ResponseEntity<Resource> getUserImage(@PathVariable String id) {

                return ResponseEntity.ok()
                                .contentType(MediaType.IMAGE_JPEG)
                                .body(userService.getUserImageById(id));
        }

        @GetMapping("/getNewUsersPerMonth")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> getNewUsersPerMonth() {

                return ResponseEntity
                                .ok(GeneralResponse.builder().success(true).error("").message("Fetched successfully")
                                                .data(userService.getNewUsersPerMonth())
                                                .build());
        }

        @GetMapping("/getActiveUsers/{days}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> getActiveUsers(@PathVariable int days) {

                return ResponseEntity
                                .ok(GeneralResponse.builder().success(true).error("").message("Fetched successfully")
                                                .data(userService.getActiveUsers(days))
                                                .build());
        }

        @GetMapping("/getUserCount")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> getUserCount() {

                return ResponseEntity
                                .ok(GeneralResponse.builder().success(true).error("").message("Fetched successfully")
                                                .data(userService.getUserCount())
                                                .build());
        }
}
