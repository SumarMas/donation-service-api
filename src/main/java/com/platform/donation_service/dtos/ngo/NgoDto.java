package com.platform.donation_service.dtos.ngo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.donation_service.dtos.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing an NGO (Non-Governmental Organization).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NgoDto {
    /**
     * Unique identifier for the NGO.
     */
    @JsonProperty("ngoId")
    private String id;
    /**
     * The user who created the NGO entry.
     */
    @JsonProperty("userCreator")
    private UserDto userCreator;
    /**
     * Name of the NGO.
     */
    @JsonProperty("name")
    private String name;
    /**
     * Description of the NGO.
     */
    @JsonProperty("description")
    private String description;
    /**
     * The profile file ID of the NGO.
     */
    @JsonProperty("profileFileId")
    private String profileFileId;

    /**
     * The banner file ID of the NGO.
     */
    @JsonProperty("bannerFileId")
    private String bannerFileId;
    /**
     * List of document IDs associated with the NGO.
     */
    @JsonProperty("documentsId")
    private List<String> documentsId;
    /**
     * List of image IDs associated with the NGO.
     */
    @JsonProperty("images")
    private List<NgoImageDto> images;

    /**
     * The status of the NGO.
     */
    @JsonProperty("status")
    private String status;

    /**
     * The creation date and time of the NGO entry.
     */
    @JsonProperty("createdDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime;
}
