package com.platform.donation_service.services.donation.Impl;

import com.platform.donation_service.dtos.donation.DonationCreateDto;
import com.platform.donation_service.services.donation.IDonationCreateService;
import com.platform.donation_service.services.donation.IDonationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of the IDonationService interface.
 */
@Service
@RequiredArgsConstructor
public class DonationService implements IDonationService {
    private static final Logger LOG = LoggerFactory.getLogger(DonationService.class);
    private final IDonationCreateService donationCreateService;
    /**
     * Creates a new donation.
     *
     * @param donationCreateDto Data Transfer Object containing donation details.
     * @return The initPoint of MercadoPago.
     */
    @Override
    public String createDonation(DonationCreateDto donationCreateDto) {
        LOG.trace("Creating a new donation");
        return donationCreateService.createDonation(donationCreateDto);
    }
}
