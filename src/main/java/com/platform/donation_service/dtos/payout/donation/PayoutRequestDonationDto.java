package com.platform.donation_service.dtos.payout.donation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data Transfer Object representing a donation associated with a payout request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayoutRequestDonationDto {
    /** Unique identifier for the payout request. */
    @JsonProperty("payout_request_id")
    private UUID payoutRequestId;
    /** Unique identifier for the donation. */
    @JsonProperty("donation_id")
    private UUID donationId;
    /** Campaign associated with the donation. */
    @JsonProperty("campaign_id")
    private UUID campaignId;
    /** Amount donated. */
    @JsonProperty("amount")
    private BigDecimal amount;
}
