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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.CreateInspectionRequest;
import dev.remo.remo.Models.Request.CreateShopRequest;
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

        @PostMapping("/shop/create")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> createShop(
                        @RequestPart("metadata") @Valid CreateShopRequest createShopRequest,
                        @RequestPart(value = "file", required = true) MultipartFile newImage,
                        HttpServletRequest http) {

                inspectionService.createShop(newImage,
                                createShopRequest, http.getHeader("Authorization").substring(7));

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("").message("Created successfully")
                                                .build());

        }

        @PutMapping("/updatestatus/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> updateInspectionStatus(
                        @PathVariable String id,
                        @Valid @RequestPart(required = true) String status,
                        @Valid @RequestPart(required = true) String remark,
                        HttpServletRequest http) {

                inspectionService.updateInspectionStatus(
                                id, status, remark, http.getHeader("Authorization").substring(7));

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("").message("Updated successfully")
                                                .build());
        }

        @PutMapping("/updateinspectionreport/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> updateInspectionReport(
                        @PathVariable String id,
                        @Valid @RequestBody UpdateInspectionRequest updateInspectionRequest,
                        HttpServletRequest http) {

                inspectionService.updateInspectionReport(
                                id, updateInspectionRequest, http.getHeader("Authorization").substring(7));

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("").message("Updated successfully")
                                                .build());
        }

}
