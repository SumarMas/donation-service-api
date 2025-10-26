package com.platform.donation_service.services.donation;

import com.platform.donation_service.dtos.donation.DonationCreateDto;
/**
 * Interface for donation services.
 */
public interface IDonationService {
    /**
     * Creates a new donation.
     *
     * @param donationCreateDto Data Transfer Object containing donation details.
     * @return The initPoint of MercadoPago.
     */
    String createDonation(DonationCreateDto donationCreateDto);
}
