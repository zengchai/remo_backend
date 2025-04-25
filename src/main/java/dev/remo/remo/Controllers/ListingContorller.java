package dev.remo.remo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Request.PredictPriceRequest;
import dev.remo.remo.Models.Response.GeneralResponse;
import dev.remo.remo.Service.Listing.MotorcycleListingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/listing")
public class ListingContorller {

        @Autowired
        MotorcycleListingService motorcycleListingService;

        @PostMapping("/pricepredict")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> predictPrice(@Valid @RequestBody PredictPriceRequest predictRequest,
                        HttpServletRequest http) {
                String response = motorcycleListingService.predictPrice(predictRequest);
                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("").message(response).build());
        }

        @PostMapping("/create")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> createListing(
                        @RequestPart("metadata") @Valid CreateOrUpdateListingRequest request,
                        @RequestPart(value = "files", required = true) MultipartFile[] newImages,
                        HttpServletRequest http) {

                motorcycleListingService.createOrUpdateMotorcycleListing(newImages, request);

                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Created successfully").build());
        }

        @PutMapping(value = "/update/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> updateListing(
                        @PathVariable String id,
                        @RequestPart("metadata") @Valid CreateOrUpdateListingRequest request,
                        @RequestPart(value = "files", required = false) MultipartFile[] newImages,
                        HttpServletRequest http) {

                request.setId(id);
                motorcycleListingService.createOrUpdateMotorcycleListing(newImages,request);

                return ResponseEntity.ok(
                                GeneralResponse.builder()
                                                .success(true)
                                                .message("Updated successfully")
                                                .build());
        }

        @PutMapping(value = "/updateStatus/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> updateListingStatus(
                        @PathVariable String id,
                        @RequestPart(required = true) String status,
                        @RequestPart(required = true) String remark,
                        HttpServletRequest http) {

                motorcycleListingService.updateMotorcycleListingStatus(id,status, remark);

                return ResponseEntity.ok(
                                GeneralResponse.builder()
                                                .success(true)
                                                .message("Updated successfully")
                                                .build());
        }

        @DeleteMapping("/delete/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> deleteListing(@PathVariable String id, HttpServletRequest http) {

                motorcycleListingService.deleteMotorcycleListingById(id);
                
                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Deleted successfully")
                                .build());

        }

}
