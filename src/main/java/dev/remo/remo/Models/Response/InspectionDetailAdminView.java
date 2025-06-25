package dev.remo.remo.Models.Response;

import lombok.Builder;
import lombok.Data;

// This class represents the details of an inspection view for an admin.
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
