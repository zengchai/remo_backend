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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.CreateOrUpdateReviewRequest;
import dev.remo.remo.Models.Request.FilterForumRequest;
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

                forumService.createOrUpdateReview(newImage, createOrUpdateReviewRequest);

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

        @GetMapping("/motorcyclemodel/get/{id}/{page}/{size}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getReviewByMotorcycleModelId(
                        @PathVariable String id,
                        @PathVariable int page,
                        @PathVariable int size,
                        HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Fetched successfully")
                                .data(forumService.getAllReviewsByMotorcycleModelId(id, page, size))
                                .build());
        }

        @GetMapping("/review/get/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getReviewById(
                        @PathVariable String id,
                        HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Fetched successfully")
                                .data(forumService.getReviewById(id))
                                .build());
        }

        @PostMapping("/filter/{page}/{size}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getForumByFilter(@RequestBody FilterForumRequest request, @PathVariable int page,
                        @PathVariable int size, HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Fetched successfully")
                                .data(forumService.getForumByFilter(request, page, size))
                                .build());
        }

        @GetMapping("/getmyreviews/{page}/{size}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getMyReview(
                        @PathVariable int page,
                        @PathVariable int size, HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Fetched successfully")
                                .data(forumService.getMyReviews(page, size))
                                .build());
        }

        @GetMapping("/images/{id}")
        public ResponseEntity<Resource> getForumImage(@PathVariable String id) {

                return ResponseEntity.ok()
                                .contentType(MediaType.IMAGE_JPEG)
                                .body(forumService.getForumImageById(id));
        }

        @GetMapping("/review/images/{id}")
        public ResponseEntity<Resource> getReviewImage(@PathVariable String id) {

                return ResponseEntity.ok()
                                .contentType(MediaType.IMAGE_JPEG)
                                .body(forumService.getReviewImageById(id));
        }

        @GetMapping("/getallreviews/{page}/{size}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> getAllReview(@PathVariable int page, @PathVariable int size,
                        HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Fetched successfully")
                                .data(forumService.getAllReview(page, size))
                                .build());
        }

        
        @DeleteMapping("/delete/user/{userId}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> deleteForumByUserId(@PathVariable String userId, HttpServletRequest http) {
                forumService.deleteReviewByUserId(userId);
                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Deleted all inspection for user " + userId)
                                .build());
        }
}
