package dev.remo.remo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.remo.remo.Models.Request.CreateInspectionRequest;
import dev.remo.remo.Models.Request.CreateOrUpdateListingRequest;
import dev.remo.remo.Models.Request.CreateOrUpdateReviewRequest;
import dev.remo.remo.Models.Request.UpdateInspectionRequest;
import dev.remo.remo.Models.Response.GeneralResponse;
import dev.remo.remo.Service.Inspection.InspectionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inspection")
public class InspectionController {

    @Autowired
    InspectionService inspectionService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> createInspection(
            @Valid @RequestBody CreateInspectionRequest createInspectionRequest,
            HttpServletRequest http) {

        inspectionService.createInspection(
                createInspectionRequest, http.getHeader("Authorization").substring(7));

        return ResponseEntity.ok(
                GeneralResponse.builder().success(true).error("").message("Created successfully")
                        .build());

    }

    @PutMapping("/updatestatus/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> acceptInspectionRequest(
            @PathVariable String id,
            @Valid @RequestParam String status,
            @Valid @RequestParam String remark,
            HttpServletRequest http) {

        inspectionService.updateInspectionStatus(
                id, status, remark, http.getHeader("Authorization").substring(7));

        return ResponseEntity.ok(
                GeneralResponse.builder().success(true).error("").message("Updated successfully")
                        .build());

    }

    
    
}
