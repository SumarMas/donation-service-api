package com.platform.donation_service.services.donation;

import com.mercadopago.resources.payment.Payment;
/**
 * Interface for processing donation payments.
 */
public interface IDonationProcessPayment {
    /**
     * Processes a payment.
     *
     * @param payment The payment to be processed.
     */
    void processPayment(Payment payment);
}
