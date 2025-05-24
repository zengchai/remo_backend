package dev.remo.remo.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Request.FilterListingRequest;
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
                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("").data(response).build());
        }

        @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
                motorcycleListingService.createOrUpdateMotorcycleListing(newImages, request);

                return ResponseEntity.ok(
                                GeneralResponse.builder()
                                                .success(true)
                                                .message("Updated successfully")
                                                .build());
        }

        @PostMapping(value = "/updateStatus/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> updateListingStatus(
                        @PathVariable String id,
                        @RequestBody Map<String, String> body,
                        HttpServletRequest http) {

                motorcycleListingService.updateMotorcycleListingStatus(id, body.get("status"), body.get("remark"));

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

        @GetMapping("/getmylisting/{page}/{size}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getAllMyListing(
                        @PathVariable int page,
                        @PathVariable int size,
                        HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder()
                                .success(true)
                                .error("")
                                .data(motorcycleListingService.getMyMotorcycleListing(page, size))
                                .build());
        }

        @GetMapping("/getmyfavourite/{page}/{size}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getMyFavouriteListings(
                        @PathVariable int page,
                        @PathVariable int size,
                        HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder()
                                .success(true)
                                .error("")
                                .data(motorcycleListingService.getMyFavouriteListings(page, size))
                                .build());
        }

        @GetMapping("/get/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getListingDetailById(@PathVariable String id, HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder()
                                .success(true)
                                .error("")
                                .data(motorcycleListingService.getMotorcycleListingDetailUserView(id))
                                .build());
        }
        @GetMapping("/motorcyclemodel/getall")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getMotorcycleModelList(HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder()
                                .success(true)
                                .error("")
                                .data(motorcycleListingService.getMotorcycleModelList())
                                .build());
        }

        @GetMapping("/getAll/{page}/{size}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getAllListings(
                        @PathVariable int page,
                        @PathVariable int size,
                        HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder()
                                .success(true)
                                .error("")
                                .data(motorcycleListingService.getMotorcycleListingListUserView(page, size))
                                .build());

        }

        @GetMapping("/images/{id}")
        public ResponseEntity<Resource> getImage(@PathVariable String id) {

                return ResponseEntity.ok()
                                .contentType(MediaType.IMAGE_JPEG)
                                .body(motorcycleListingService.getMotorcycleListingImageById(id));
        }

        @PostMapping("/filter/{page}/{size}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> filterListings(
                        @RequestBody FilterListingRequest filterRequest,
                        @PathVariable int page,
                        @PathVariable int size,
                        HttpServletRequest http) {

                return ResponseEntity.ok(GeneralResponse.builder()
                                .success(true)
                                .error("")
                                .data(motorcycleListingService.filterListings(filterRequest, page, size))
                                .build());
        }

        @PostMapping("/createMotorcycleModel")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> createMotorcycleModel(
                        @RequestPart(value = "brand", required = true) String brand,
                        @RequestPart(value = "model", required = true) String model,
                        @RequestPart(value = "file", required = true) MultipartFile image,
                        HttpServletRequest http) {

                motorcycleListingService.createMotorcycleModel(brand, model, image);

                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Created successfully").build());
        }

        @PutMapping("/favourite/{listingId}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> favouriteListing(@PathVariable String listingId, HttpServletRequest http) {

                String message = motorcycleListingService.favouriteMotorcycleListing(listingId)
                                ? "Unfavourite successfully"
                                : "Favourite successfully";

                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message(message)
                                .build());
        }
}
