package com.platform.donation_service.dtos.donation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.donation_service.dtos.campaign.CampaignDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * Data Transfer Object (DTO) for detailed Donation information,
 * extending the basic DonationDto to include associated campaign data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DonationDetailDto extends DonationDto {
    /** Campaign associated with the donation. */
    @JsonProperty("campaign_data")
    private CampaignDto campaign;
}
