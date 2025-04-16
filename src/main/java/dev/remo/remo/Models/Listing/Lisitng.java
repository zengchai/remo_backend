package dev.remo.remo.Models.Listing;

import dev.remo.remo.Models.Users.User;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class Lisitng {
    
    private String id;
    private String price;
    private User user;

}
