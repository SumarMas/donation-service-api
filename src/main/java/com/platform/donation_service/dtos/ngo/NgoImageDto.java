package com.platform.donation_service.dtos.ngo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an image associated with an NGO (used for the image carousel).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NgoImageDto {
    /**
     * The file ID of the image.
     */
    @JsonProperty("imageId")
    private String imageId;
    /**
     * The order index of the image in the carousel.
     */
    @JsonProperty("orderIndex")
    private Integer orderIndex;
}
