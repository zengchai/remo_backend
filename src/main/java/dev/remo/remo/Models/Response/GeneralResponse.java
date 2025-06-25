package dev.remo.remo.Models.Response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

// This class represents a general response structure used across the application.
@Data
@SuperBuilder
public class GeneralResponse {

    Boolean success;

    String error;

    String message;

    Object data;
}
