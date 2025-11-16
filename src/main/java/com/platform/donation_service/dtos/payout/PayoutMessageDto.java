package com.platform.donation_service.dtos.payout;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.donation_service.enums.PayoutStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object representing a payout message.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayoutMessageDto {
    /** The payout ID. */
    @JsonProperty("payout_id")
    private UUID payoutId;
    /** The NGO ID associated with the payout. */
    @JsonProperty("ngo_id")
    private UUID ngoId;
    /** The datetime when the payout was made. */
    @JsonProperty("payout_datetime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime payoutDatetime;
    /** The previous status of the payout. */
    @JsonProperty("previous_payout_status")
    private PayoutStatus previousPayoutStatus;
    /** The current status of the payout. */
    @JsonProperty("payout_status")
    private PayoutStatus payoutStatus;
    /** The total amount of the payout. */
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
}
