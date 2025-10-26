package com.platform.donation_service.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    /**
     * Unique identifier for the user.
     */
    @JsonProperty("userId")
    private String id;
    /**
     * First name of the user.
     */
    @JsonProperty("firstName")
    private String firstName;
    /**
     * Last name of the user.
     */
    @JsonProperty("lastName")
    private String lastName;
    /**
     * Email address of the user.
     */
    @JsonProperty("email")
    private String email;
    /**
     * Profile file identifier for the user's profile image.
     */
    @JsonProperty("profileFileId")
    private String profileFileId;
    /**
     * Status of the user account.
     */
    @JsonProperty("status")
    private String status;
    /**
     * List of roles assigned to the user.
     */
    @JsonProperty("roles")
    private List<String> roles;
}
