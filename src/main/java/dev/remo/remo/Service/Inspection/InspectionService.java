package dev.remo.remo.Service.Inspection;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import dev.remo.remo.Models.Inspection.Inspection;
import dev.remo.remo.Models.Request.CreateInspectionRequest;
import dev.remo.remo.Models.Request.CreateShopRequest;
import dev.remo.remo.Models.Request.FilterInspectionRequest;
import dev.remo.remo.Models.Request.UpdateInspectionRequest;
import dev.remo.remo.Models.Response.InspectionDetailAdminView;
import dev.remo.remo.Models.Response.InspectionDetailUserView;

public interface InspectionService {
    void createInspection(CreateInspectionRequest createOrUpdateInspectionRequest);

    void updateInspectionStatus(String inspectionId, String status, String remark);

    void updateInspectionReport(String id, UpdateInspectionRequest updateInspectionRequest);

    void createShop(MultipartFile image, CreateShopRequest createShopRequest);

    void deleteInspection(String id);

    Inspection getInspectionById(String id);

    List<InspectionDetailUserView> getMyInspection();

    Map<String, String> getInspectionStatusByIds(List<String> ids);

    InspectionDetailUserView getInspectionDetailUserViewById(String id);

    Page<InspectionDetailAdminView> getAllInspectionAdminView(int page, int size);

    Page<InspectionDetailAdminView> getAllInspectionByFilter(FilterInspectionRequest filterInspectionRequest,int page, int size);

}
