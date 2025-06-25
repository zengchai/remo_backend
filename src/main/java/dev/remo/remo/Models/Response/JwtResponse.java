package dev.remo.remo.Models.Response;

import java.util.List;

import lombok.Data;
import lombok.experimental.SuperBuilder;

// This class represents a JWT response containing the token, user email, and roles.
@Data
@SuperBuilder
public class JwtResponse extends GeneralResponse {

	private String token;

	private String email;

	private List<String> roles;
}
