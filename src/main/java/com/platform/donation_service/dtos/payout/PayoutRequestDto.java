package com.platform.donation_service.dtos.payout;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.donation_service.dtos.payout.donation.PayoutRequestDonationDto;
import com.platform.donation_service.enums.PayoutStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object representing a payout request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayoutRequestDto {
    /** Unique identifier for the payout request. */
    @JsonProperty("payout_request_id")
    private UUID payoutRequestId;
    /** Unique identifier for the NGO requesting the payout. */
    @JsonProperty("ngo_id")
    private UUID ngoId;
    /** Amount requested for the payout. */
    @JsonProperty("amount")
    private BigDecimal amount;
    /** Status of the payout request. */
    @JsonProperty("status")
    private PayoutStatus status;
    /** Unique identifier for the proof file associated with the payout request. */
    @JsonProperty("proof_file_id")
    private UUID proofFileId;
    /** Date and time when the payout request was made. */
    @JsonProperty("request_datetime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestDateTime;
    /** Date and time when the payout request was approved. */
    @JsonProperty("approval_datetime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvalDatetime;
    /** List of donations associated with the payout request. */
    @JsonProperty("donations")
    private List<PayoutRequestDonationDto> donations;
}
