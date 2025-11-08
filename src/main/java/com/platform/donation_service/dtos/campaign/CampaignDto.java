package com.platform.donation_service.dtos.campaign;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.donation_service.dtos.campaign.categories.CategoryDto;
import com.platform.donation_service.dtos.ngo.NgoDto;
import com.platform.donation_service.enums.CampaignState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object representing a campaign.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDto {
    /** Unique identifier for the campaign. */
    @JsonProperty("id")
    private String id;
    /** Organization that owns the campaign. */
    @JsonProperty("ngo")
    private NgoDto ngo;
    /** Title of the campaign. */
    @JsonProperty("title")
    private String title;
    /** Goal amount for the campaign. */
    @JsonProperty("goal_amount")
    private BigDecimal goalAmount;
    /** Current amount raised for the campaign. */
    @JsonProperty("current_amount")
    private BigDecimal currentAmount;
    /** Description of the campaign. */
    @JsonProperty("description")
    private String description;
    /** End date and time of the campaign. */
    @JsonProperty("end_date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;
    /** Creation date and time of the campaign. */
    @JsonProperty("create_date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDateTime;
    /** State of the campaign. */
    @JsonProperty("campaign_state")
    private CampaignState campaignState;
    /** Categories associated with the campaign. */
    @JsonProperty("categories")
    private List<CategoryDto> categories;
    /** Tags associated with the campaign. */
    @JsonProperty("tags")
    private List<String> tags;
    /** Image Ids associated with the campaign. */
    @JsonProperty("images")
    private List<String> images;
}
