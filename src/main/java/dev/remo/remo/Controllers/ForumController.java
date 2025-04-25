package dev.remo.remo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.CreateOrUpdateReviewRequest;
import dev.remo.remo.Models.Response.GeneralResponse;
import dev.remo.remo.Service.Forum.ForumService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

        @Autowired
        ForumService forumService;

        @PostMapping("/save")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> saveReview(
                        @RequestPart("metadata") @Valid CreateOrUpdateReviewRequest createOrUpdateReviewRequest,
                        @RequestPart(value = "file", required = true) MultipartFile newImage,
                        HttpServletRequest http) {

                forumService.createOrUpdateReview(newImage,createOrUpdateReviewRequest);

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("").message("Saved successfully")
                                                .build());

        }

        @DeleteMapping("/delete/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> deleteReview(@PathVariable String id, HttpServletRequest http) {

                forumService.deleteReviewById(id);
                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Deleted successfully")
                                .build());

        }
}
