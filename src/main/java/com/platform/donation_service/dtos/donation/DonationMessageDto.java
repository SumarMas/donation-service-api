package com.platform.donation_service.dtos.donation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.donation_service.enums.DonationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for donation messages sent via RabbitMQ.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationMessageDto {
    /** The donation ID. */
    @JsonProperty("donation_id")
    private UUID donationId;
    /** The user ID who made the donation. */
    @JsonProperty("user_id")
    private UUID userId;
    /** The amount donated. */
    @JsonProperty("amount")
    private BigDecimal amount;
    /** The status of the donation. */
    @JsonProperty("donation_status")
    private DonationStatus donationStatus;
    /** The previous status of the donation. */
    @JsonProperty("previous_donation_status")
    private DonationStatus previousDonationStatus;
}
