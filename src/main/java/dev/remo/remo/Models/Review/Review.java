package dev.remo.remo.Models.Review;

import dev.remo.remo.Models.MotorcycleModel.MotorcycleModel;
import dev.remo.remo.Models.Users.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {
    
    private String id;
    private MotorcycleModel motorcycle;
    private User user;
    private String review;
    
}
