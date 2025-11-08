package com.platform.donation_service.services.donation.Impl;

import com.platform.donation_service.dtos.donation.DonationCreateDto;
import com.platform.donation_service.dtos.donation.DonationDetailDto;
import com.platform.donation_service.dtos.donation.DonationsDto;
import com.platform.donation_service.enums.DonationStatus;
import com.platform.donation_service.services.donation.IDonationCreateService;
import com.platform.donation_service.services.donation.IDonationGetService;
import com.platform.donation_service.services.donation.IDonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of the IDonationService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DonationService implements IDonationService {
    /** Service for creating donations. */
    private final IDonationCreateService donationCreateService;
    /** Service for retrieving donations. */
    private final IDonationGetService donationGetService;
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

    /**
     * Retrieves donations associated with the specified campaign
     * IDs and filtered by donation status.
     *
     * @param campaignIds A set of campaign IDs to filter the donations.
     * @param status      A set of donation statuses to filter the donations.
     * @return A DonationsDto containing the filtered donations.
     */
    @Override
    public DonationsDto getDonationsByCampaignId(Set<UUID> campaignIds, Set<DonationStatus> status) {
        log.trace("Getting donations by campaign");
        return donationGetService.getDonationsByCampaignId(campaignIds, status);
    }

    /**
     * Retrieves all donations made by the user in the current context.
     *
     * @return A list of DonationDetailDto representing the user's donations.
     */
    @Override
    public List<DonationDetailDto> getAllDonationsByUserContext() {
        log.trace("Getting all donations by user context");
        return donationGetService.getAllDonationsByUserContext();
    }
}
