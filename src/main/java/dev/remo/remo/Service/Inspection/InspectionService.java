package dev.remo.remo.Service.Inspection;

import dev.remo.remo.Models.Request.CreateInspectionRequest;

public interface InspectionService {
    void createInspection(CreateInspectionRequest createOrUpdateInspectionRequest, String accessToken);
    void updateInspectionStatus(String inspectionId, String status, String remark, String accessToken);
}
