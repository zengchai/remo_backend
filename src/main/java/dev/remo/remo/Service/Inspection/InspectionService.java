package dev.remo.remo.Service.Inspection;

import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Request.CreateInspectionRequest;
import dev.remo.remo.Models.Request.CreateShopRequest;
import dev.remo.remo.Models.Request.UpdateInspectionRequest;

public interface InspectionService {
    void createInspection(CreateInspectionRequest createOrUpdateInspectionRequest);

    void updateInspectionStatus(String inspectionId, String status, String remark);

    void updateInspectionReport(String id, UpdateInspectionRequest updateInspectionRequest);

    void createShop(MultipartFile image, CreateShopRequest createShopRequest);

    void deleteInspection(String id);

}
