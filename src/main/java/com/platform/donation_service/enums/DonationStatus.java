package com.platform.donation_service.enums;
/**
 * Enum representing the status of a donation.
 */
public enum DonationStatus {
   /**  Donor initiated the payment, pending confirmation */
    CREATED,
    /** Payment has been confirmed by MercadoPago */
    CONFIRMED,
    /** Donation liquidated to NGO (by payout-service) */
    PAID,
    /** Payment was cancelled */
    CANCELLED
}
