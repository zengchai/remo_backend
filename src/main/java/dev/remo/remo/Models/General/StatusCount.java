package dev.remo.remo.Models.General;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusCount {

    private String status;

    private long count;
}
