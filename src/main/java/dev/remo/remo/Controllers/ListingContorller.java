package dev.remo.remo.Controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import dev.remo.remo.Models.Listing.Motorcycle.MotorcycleListing;
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
                System.err.println(predictRequest.toString());
        String response = motorcycleListingService.predictPrice(predictRequest);
        return ResponseEntity
                .ok(GeneralResponse.builder().success(true).error("").message(response).build());
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> createListing(@Valid @RequestBody CreateOrUpdateListingRequest createListingRequest,
            HttpServletRequest http) {
        motorcycleListingService.createOrUpdateMotorcycleListing(createListingRequest,
                http.getHeader("Authorization").substring(7));

        return ResponseEntity
                .ok(GeneralResponse.builder().success(true).error("").message("Created successfully").build());
    }
}
