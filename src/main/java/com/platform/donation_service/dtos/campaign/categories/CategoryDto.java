package com.platform.donation_service.dtos.campaign.categories;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a category.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    /** Unique identifier for the category. */
    @JsonProperty("id")
    private String id;
    /** Name of the category. */
    @JsonProperty("name")
    private String name;
    /** Description of the category. */
    @JsonProperty("description")
    private String description;
}
