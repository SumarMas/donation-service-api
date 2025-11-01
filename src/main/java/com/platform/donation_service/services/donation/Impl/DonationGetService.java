package com.platform.donation_service.services.donation.Impl;

import com.platform.donation_service.dtos.donation.DonationDto;
import com.platform.donation_service.dtos.donation.DonationsDto;
import com.platform.donation_service.entities.DonationEntity;
import com.platform.donation_service.enums.DonationStatus;
import com.platform.donation_service.repositories.DonationRepository;
import com.platform.donation_service.services.donation.IDonationGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for retrieving donations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DonationGetService implements IDonationGetService {
    /** Repository for accessing donation data. */
    private final DonationRepository donationRepository;
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
        log.trace("Fetching donations for campaign IDs: {} with status: {}", campaignIds, status);
        // Fetch donations from the repository based on campaign IDs and status
        List<DonationEntity> donationEntities = getDonationsFromRepository(campaignIds, status);
        log.debug("Found {} donations", donationEntities.size());
        // Convert the result to DonationDto and insert into Map, where key is campaignId
        DonationsDto donationsDto = DonationsDto.builder()
                .donations(groupDonationsByCampaign(donationEntities))
                .build();
        log.debug("Fetched {} donations for campaign IDs: {}", donationEntities.size(), campaignIds);
        return donationsDto;
    }

    private DonationDto mapEntityToDto(DonationEntity donationEntity) {
        return DonationDto.builder()
                .donationId(donationEntity.getDonationId())
                .campaignId(donationEntity.getCampaignId())
                .amount(donationEntity.getAmount())
                .status(donationEntity.getStatus())
                .donorId(donationEntity.getDonorId())
                .paymentId(donationEntity.getPaymentId())
                .paymentProof(donationEntity.getPaymentProof())
                .currency(donationEntity.getCurrency())
                .paymentMethod(donationEntity.getPaymentMethod())
                .paymentDateTime(donationEntity.getPaymentDatetime())
                .build();
    }

    private List<DonationEntity> getDonationsFromRepository(Set<UUID> campaignIds, Set<DonationStatus> status) {
        try {
            return donationRepository.findByCampaignIdsAndStatus(campaignIds, status);
        } catch (DataAccessException ex) {
            log.error("Error accessing donation data for campaign IDs: {} and status: {}", campaignIds, status, ex);
           return List.of();
        }
    }

    private Map<UUID, List<DonationDto>> groupDonationsByCampaign(List<DonationEntity> donationEntities) {
        if (donationEntities == null || donationEntities.isEmpty()) {
            return Collections.emptyMap();
        }

        return donationEntities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.groupingBy(DonationDto::getCampaignId));
    }
}
