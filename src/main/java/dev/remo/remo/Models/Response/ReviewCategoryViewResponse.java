package dev.remo.remo.Models.Response;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewCategoryViewResponse {
    
    @NotBlank
    private List<ReviewCategoryUserViewResponse> reviews;
}
