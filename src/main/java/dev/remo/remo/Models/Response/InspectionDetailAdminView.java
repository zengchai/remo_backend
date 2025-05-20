package dev.remo.remo.Models.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InspectionDetailAdminView {
    
    private String id;
    private String motorcycleLisitngId;
    private String createdAt;
    private String status;
    private String date;
    private String time;
    private String shopName;

}
