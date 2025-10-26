package com.platform.donation_service.services.donation.Impl;

import com.mercadopago.resources.preference.Preference;
import com.platform.donation_service.context.IContextService;
import com.platform.donation_service.controllers.manageExceptions.CustomException;
import com.platform.donation_service.dtos.donation.DonationCreateDto;
import com.platform.donation_service.entities.DonationEntity;
import com.platform.donation_service.enums.DonationStatus;
import com.platform.donation_service.repositories.DonationRepository;
import com.platform.donation_service.services.donation.IDonationCreateService;
import com.platform.donation_service.services.mercadoPago.IMercadoPagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service implementation for creating donations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DonationCreateService implements IDonationCreateService {
    /** Repository for managing donations in the database. */
    private final DonationRepository donationRepository;
    /** Context service for retrieving user and request context information. */
    private final IContextService contextService;
    /** Service for interacting with MercadoPago API. */
    private final IMercadoPagoService mercadoPagoService;
    /**
     * Creates a new donation.
     *
     * @param donationCreateDto Data Transfer Object containing donation details.
     * @return The initPoint of MercadoPago.
     */
    @Override
    @Transactional
    public String createDonation(DonationCreateDto donationCreateDto) {
        log.trace("Entering createDonation");
        // get user id from context
        UUID userId = getUserId();
        // create donation entity and save to database
        DonationEntity donationEntity = builDonationEntity(donationCreateDto, userId);
        saveDonationEntity(donationEntity);
        // Call MercadoPago service to create payment preference
        Preference preference = createMercadoPagoPreference(donationCreateDto, donationEntity.getDonationId());
        donationEntity.setPaymentId(preference.getId());
        saveDonationEntity(donationEntity);
        log.trace("Donation created successfully with ID: {}", donationEntity.getDonationId());
        return preference.getInitPoint();
        //return preference.getSandboxInitPoint();
    }

    private UUID getUserId() {
        return contextService.getUserId();
    }

    private DonationEntity builDonationEntity(DonationCreateDto donationCreateDto, UUID userId) {
        return DonationEntity.builder()
                .donorId(userId)
                .campaignId(donationCreateDto.getCampaignId())
                .amount(donationCreateDto.getAmount())
                .currency("ARS")
                .status(DonationStatus.CREATED)
                .donationId(UUID.randomUUID())
                .createdUser(userId)
                .build();
    }
    private void saveDonationEntity(DonationEntity donationEntity) {
        try {
            log.trace("Entering saveDonationEntity");
            donationRepository.save(donationEntity);
        } catch (DataAccessException ex) {
            log.error("Error saving donation to the database", ex);
            throw new CustomException("Failed to create donation. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }
    private Preference createMercadoPagoPreference(DonationCreateDto donationCreateDto, UUID donationId) {
        log.trace("Entering createMercadoPagoPreference");
        return mercadoPagoService.createPreference(donationCreateDto.getAmount(),
                donationCreateDto.getTitle(), donationId.toString());
    }
}
