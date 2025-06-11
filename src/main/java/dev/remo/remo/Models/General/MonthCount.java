package dev.remo.remo.Models.General;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthCount {
    private String month;
    private int count;
}
