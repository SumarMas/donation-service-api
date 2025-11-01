package com.platform.donation_service.services.donation;

import com.platform.donation_service.dtos.donation.DonationsDto;
import com.platform.donation_service.enums.DonationStatus;

import java.util.Set;
import java.util.UUID;

/**
 * Service interface for retrieving
 * donations based on campaign IDs and donation status.
 */
public interface IDonationGetService {
    /**
     * Retrieves donations associated with the specified campaign
     * IDs and filtered by donation status.
     *
     * @param campaignIds A set of campaign IDs to filter the donations.
     * @param status      A set of donation statuses to filter the donations.
     * @return A DonationsDto containing the filtered donations.
     */
    DonationsDto getDonationsByCampaignId(Set<UUID> campaignIds, Set<DonationStatus> status);
}
