package com.platform.donation_service.services.donation.Impl;

import com.platform.donation_service.dtos.donation.DonationCreateDto;
import com.platform.donation_service.services.donation.IDonationCreateService;
import com.platform.donation_service.services.donation.IDonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the IDonationService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DonationService implements IDonationService {
    /** Service for creating donations. */
    private final IDonationCreateService donationCreateService;
    /**
     * Creates a new donation.
     *
     * @param donationCreateDto Data Transfer Object containing donation details.
     * @return The initPoint of MercadoPago.
     */
    @Override
    public String createDonation(DonationCreateDto donationCreateDto) {
        log.trace("Creating a new donation");
        return donationCreateService.createDonation(donationCreateDto);
    }
}
