package com.platform.donation_service.entities;

import com.platform.donation_service.enums.DonationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a donation made by a donor to a campaign.
 * All changes are tracked by database triggers in donations_audit.
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "donations")
public class DonationEntity extends AuditEntity {
    /** Constants for column lengths and precision. */
    private static final int LENGTH_PAYMENT_ID = 64;
    /** Constants for column lengths and precision. */
    private static final int LENGTH_PAYMENT_PROOF = 255;
    /** Constants for column lengths and precision. */
    private static final int PRECISION_AMOUNT = 11;
    /** Constants for column lengths and precision. */
    private static final int SCALE_AMOUNT = 2;
    /** Constants for column lengths and precision. */
    private static final int LENGTH_CURRENCY = 5;
    /** Constants for column lengths and precision. */
    private static final int LENGTH_STATUS = 20;
    /** Constants for column lengths and precision. */
    private static final int LENGTH_PAYMENT_METHOD = 50;
    /** Unique identifier for the donation. */
    @Id
    @Column(name = "donation_id", columnDefinition = "BINARY(16)")
    private UUID donationId;

    /** Campaign associated with the donation. */
    @Column(name = "campaign_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID campaignId;

    /** Donor who made the donation. */
    @Column(name = "donor_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID donorId;

    /** Payment ID from MercadoPago. */
    @Column(name = "payment_id", length = LENGTH_PAYMENT_ID)
    private String paymentId;

    /** Proof of payment. */
    @Column(name = "payment_proof", length = LENGTH_PAYMENT_PROOF)
    private String paymentProof;

    /** Amount donated. */
    @Column(name = "amount", precision = PRECISION_AMOUNT, scale = SCALE_AMOUNT, nullable = false)
    private BigDecimal amount;

    /** Currency code (e.g., ARS, USD). */
    @Column(name = "currency", length = LENGTH_CURRENCY, nullable = false)
    private String currency = "ARS";

    /** Donation status. */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = LENGTH_STATUS)
    private DonationStatus status = DonationStatus.CREATED;

    /** Payment method used. */
    @Column(name = "payment_method", length = LENGTH_PAYMENT_METHOD)
    private String paymentMethod;

    /** Payment confirmation date/time. */
    @Column(name = "payment_datetime")
    private LocalDateTime paymentDatetime;
}
