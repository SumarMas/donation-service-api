package com.platform.donation_service.services.mercadoPago;

import com.mercadopago.resources.preference.Preference;

import java.math.BigDecimal;
/**
 * Interface for MercadoPago service operations.
 */
public interface IMercadoPagoService {
    /**
     * Creates a payment preference in MercadoPago.
     *
     * @param amount     The amount for the donation.
     * @param title      The title or description of the donation.
     * @param donationId The unique identifier for the donation.
     * @return The created Preference object from MercadoPago.
     */
    Preference createPreference(BigDecimal amount, String title, String donationId);
    /**
     * Processes a donation payment based on the provided payload.
     *
     * @param payload The payment notification payload from MercadoPago.
     * @param type    The type of the notification.
     * @param dataId  The identifier for the payment data.
     */
    void processDonationPayment(String payload, String type, String dataId);
}
