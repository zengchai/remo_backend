package dev.remo.remo.Models.Response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewUserView {

    @NotBlank
    private String id;
    @NotBlank
    private String review;
    @NotBlank
    private String reviewImageId;

    private String userName;
    private String userImageId;
}
