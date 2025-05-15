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

        @GetMapping("/motorcyclemodel/get/{id}")
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

        @GetMapping("/getallreviews")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getAllReview(HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Fetched successfully")
                                .data(forumService.getAllReview())
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
}
