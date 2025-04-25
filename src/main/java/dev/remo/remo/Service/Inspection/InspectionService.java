package dev.remo.remo.Service.Inspection;

import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.CreateInspectionRequest;
import dev.remo.remo.Models.Request.CreateShopRequest;
import dev.remo.remo.Models.Request.UpdateInspectionRequest;

public interface InspectionService {
    void createInspection(CreateInspectionRequest createOrUpdateInspectionRequest, String accessToken);

    void updateInspectionStatus(String inspectionId, String status, String remark, String accessToken);

    void updateInspectionReport(String id, UpdateInspectionRequest updateInspectionRequest, String accessToken);

    void createShop(MultipartFile image, CreateShopRequest createShopRequest, String accessToken);

}
