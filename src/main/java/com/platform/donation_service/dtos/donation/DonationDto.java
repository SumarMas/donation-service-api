package com.platform.donation_service.dtos.donation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.donation_service.enums.DonationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for Donation information.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonationDto {
    /** Unique identifier for the donation. */
    @JsonProperty("donation_id")
    private UUID donationId;
    /** Campaign associated with the donation. */
    @JsonProperty("campaign_id")
    private UUID campaignId;
    /** Donor who made the donation. */
    @JsonProperty("donor_id")
    private UUID donorId;
    /** Payment ID from MercadoPago. */
    @JsonProperty("payment_id")
    private String paymentId;
    /** Proof of payment details or URL. */
    @JsonProperty("payment_proof")
    private String paymentProof;
    /** Amount donated. */
    @JsonProperty("amount")
    private BigDecimal amount;
    /** Currency of the donation amount. */
    @JsonProperty("currency")
    private String currency;
    /** Status of the donation. */
    @JsonProperty("status")
    private DonationStatus status;
    /** Payment method used for the donation. */
    @JsonProperty("payment_method")
    private String paymentMethod;
    /** Date and time when the payment was made. */
    @JsonProperty("payment_datetime")
    private LocalDateTime paymentDateTime;
}
