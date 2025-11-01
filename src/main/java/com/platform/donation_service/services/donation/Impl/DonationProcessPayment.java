package com.platform.donation_service.services.donation.Impl;

import com.mercadopago.resources.payment.Payment;
import com.platform.donation_service.controllers.manageExceptions.CustomException;
import com.platform.donation_service.entities.DonationEntity;
import com.platform.donation_service.enums.DonationStatus;
import com.platform.donation_service.repositories.DonationRepository;
import com.platform.donation_service.services.donation.IDonationProcessPayment;
import com.platform.donation_service.services.donation.IDonationPublishEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
/**
 * Implementation of the IDonationProcessPayment
 * interface for processing donation payments.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DonationProcessPayment implements IDonationProcessPayment {
    /** Repository for managing donations in the database. */
    private final DonationRepository donationRepository;
    /** Service for publishing donation events. */
    private final IDonationPublishEventService donationPublishEventService;


    /**
     * Processes the payment for a donation.
     * @param payment The payment to be processed.
     */
    @Override
    @Transactional
    public void processPayment(Payment payment) {
        try {
            log.trace("Entering processPayment with payment ID: {}", payment.getId());
            UUID donationId = UUID.fromString(payment.getExternalReference());
            String status = payment.getStatus();
            handlePaymentStatus(status, donationId, payment);
            log.trace("Payment processed successfully for payment ID: {}", payment.getId());
        } catch (Exception e) {
            log.error("Error processing payment {}: {}", payment, e.getMessage());
            throw e;
        }
    }

    private void handlePaymentStatus(String status, UUID donationId, Payment payment) {
        switch (status) {
            case "approved" -> updateDonationStatus(donationId, DonationStatus.CONFIRMED, payment);
            case "pending" -> updateDonationStatus(donationId, DonationStatus.CREATED, payment);
            case "rejected", "cancelled", "refunded" -> updateDonationStatus(donationId, DonationStatus.CANCELLED, payment);
            default -> {
                log.warn("Unhandled payment status: {}", status);
                throw new CustomException("Unhandled payment status: " + status,
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    private void updateDonationStatus(UUID donationId, DonationStatus status, Payment payment) {
        DonationEntity donation = findDonationById(donationId);
        DonationStatus previousStatus = donation.getStatus();
        log.info("payment.getTransactionDetails().getNetReceivedAmount(): {}", payment.getTransactionDetails().getNetReceivedAmount());
        log.info("payment.getNetAmount() {}", payment.getNetAmount());
        if (status.equals(DonationStatus.CONFIRMED)) {
            donation.setPaymentProof(payment.getId().toString());
            donation.setPaymentMethod(payment.getPaymentMethodId());
        }
        donation.setStatus(status);
        try {
            donationRepository.save(donation);
        } catch (DataAccessException ex) {
            log.error("Error updating donation status for donation ID {}: {}", donationId, ex.getMessage());
            throw new CustomException("Failed to update donation status", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
        publishDonationEvent(donation, status, previousStatus);
    }
    private DonationEntity findDonationById(UUID donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(() -> {
                    log.error("Donation with ID {} not found", donationId);
                    return new CustomException("Donation not found", HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    private void publishDonationEvent(DonationEntity donation, DonationStatus status, DonationStatus previousStatus) {
        // Placeholder for event publishing logic
        donationPublishEventService.publishDonationEvent(donation, status, previousStatus);
    }
}
