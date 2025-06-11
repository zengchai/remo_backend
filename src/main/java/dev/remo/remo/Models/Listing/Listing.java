package dev.remo.remo.Models.Listing;

import dev.remo.remo.Models.Users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Listing {
    
    private String id;
    private int price;
    private User user;

}
