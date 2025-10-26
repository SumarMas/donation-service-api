package com.platform.donation_service.dtos.donation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
/**
 * Data Transfer Object (DTO) for creating a new donation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationCreateDto {
    /** Campaign identifier for the donation. */
    @JsonProperty("campaign_id")
    @NotNull(message = "Campaign ID is mandatory")
    private UUID campaignId;
    /** Amount of the donation. */
    @JsonProperty("amount")
    private BigDecimal amount;
    /** Title of the donation. */
    @JsonProperty("title")
    @NotBlank(message = "Title is mandatory")
    private String title;

}
