package dev.remo.remo.Controllers;

import java.util.List;

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

import dev.remo.remo.Models.Request.CreateInspectionRequest;
import dev.remo.remo.Models.Request.CreateShopRequest;
import dev.remo.remo.Models.Request.FilterInspectionRequest;
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

                inspectionService.createInspection(createInspectionRequest);

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

                inspectionService.createShop(newImage, createShopRequest);

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

                inspectionService.updateInspectionStatus(id, status, remark);

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

                inspectionService.updateInspectionReport(id, updateInspectionRequest);

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("").message("Updated successfully")
                                                .build());
        }

        @DeleteMapping("/delete/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> deleteInspection(@PathVariable String id, HttpServletRequest http) {

                inspectionService.deleteInspection(id);

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("").message("Deleted successfully")
                                                .build());
        }

        @GetMapping("/getmyinspections")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getMyInspection(HttpServletRequest http) {

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("")
                                                .data(inspectionService.getMyInspection())
                                                .build());

        }

        @PostMapping("/getstatus")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getInspectionStatus(
                        @RequestBody List<String> ids,
                        HttpServletRequest http) {

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("")
                                                .data(inspectionService.getInspectionStatusByIds(ids))
                                                .build());

        }

        @GetMapping("/get/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getInspectionDetailById(
                        @PathVariable String id,
                        HttpServletRequest http) {

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("")
                                                .data(inspectionService.getInspectionDetailUserViewById(id))
                                                .build());

        }

        @GetMapping("/getallinspection/{page}/{size}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> getAllInspection(
                        @PathVariable int page,
                        @PathVariable int size,
                        HttpServletRequest http) {

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("")
                                                .data(inspectionService.getAllInspectionAdminView(page, size))
                                                .build());

        }

        @GetMapping("/getallshop")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> getAllShop(
                        HttpServletRequest http) {

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("")
                                                .data(inspectionService.getAllShops())
                                                .build());

        }

        @PostMapping("/filter/{page}/{size}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> getAllInspectionByFilters(
                        @RequestBody FilterInspectionRequest inspectionFilterRequest,
                        @PathVariable int page,
                        @PathVariable int size,
                        HttpServletRequest http) {

                return ResponseEntity.ok(
                                GeneralResponse.builder().success(true).error("")
                                                .data(inspectionService.getAllInspectionByFilter(
                                                                inspectionFilterRequest, page, size))
                                                .build());

        }

        @GetMapping("/shop/images/{id}")
        public ResponseEntity<Resource> getImage(@PathVariable String id) {

                return ResponseEntity.ok()
                                .contentType(MediaType.IMAGE_JPEG)
                                .body(inspectionService.getShopImage(id));
        }

        @DeleteMapping("/delete/user/{userId}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
        public ResponseEntity<?> deleteInspectionByUserId(@PathVariable String userId, HttpServletRequest http) {

                inspectionService.deleteInspectionByUserId(userId);

                return ResponseEntity.ok(GeneralResponse.builder().success(true).error("")
                                .message("Deleted all inspection for user " + userId)
                                .build());
        }
}
