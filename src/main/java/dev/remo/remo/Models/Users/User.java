package dev.remo.remo.Models.Users;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import dev.remo.remo.Utils.Enum.UserRole;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class User implements UserDetails {

    private String id;

    private String name;

    private String email;

    private String password;

    private String nric;

    private String phoneNum;

    private String dob;

    private String imageId;

    private String createdAt;

    private List<String> favouriteListingIds;

    private List<String> motorcycleListingIds;

    private List<UserRole> role;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream()
                .map(role -> new SimpleGrantedAuthority(role.getCode()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }
}
