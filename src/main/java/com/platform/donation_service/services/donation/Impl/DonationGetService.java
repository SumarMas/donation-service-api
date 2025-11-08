package com.platform.donation_service.services.donation.Impl;

import com.platform.donation_service.context.IContextService;
import com.platform.donation_service.dtos.campaign.CampaignDto;
import com.platform.donation_service.dtos.donation.DonationDetailDto;
import com.platform.donation_service.dtos.donation.DonationDto;
import com.platform.donation_service.dtos.donation.DonationsDto;
import com.platform.donation_service.entities.DonationEntity;
import com.platform.donation_service.enums.DonationStatus;
import com.platform.donation_service.repositories.DonationRepository;
import com.platform.donation_service.services.campaign.ICampaignGetService;
import com.platform.donation_service.services.donation.IDonationGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    /** Service for accessing context information. */
    private final IContextService contextService;
    /** Service for accessing campaign information. */
    private final ICampaignGetService campaignGetService;
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

    /**
     * Retrieves all donations made by the user in the current context.
     *
     * @return A list of DonationDetailDto representing the user's donations.
     */
    @Override
    public List<DonationDetailDto> getAllDonationsByUserContext() {
        List<DonationDetailDto> result = new ArrayList<>();
        UUID userId = getUserContextId();
        List<DonationEntity> donationEntities = getDonationsByUserId(userId);
        log.debug("Found {} donations for user ID: {}", donationEntities.size(), userId);
        if (donationEntities.isEmpty()) {
            return result;
        }
        Set<UUID> campaignIds = donationEntities.stream()
                .map(DonationEntity::getCampaignId)
                .collect(Collectors.toSet());
        Map<UUID, CampaignDto> campaigns = campaignGetService.getCampaignsByIds(campaignIds);
        Map<UUID, List<DonationEntity>> donationsByCampaign = donationEntities.stream()
                .collect(Collectors.groupingBy(DonationEntity::getCampaignId));
        for (Map.Entry<UUID, List<DonationEntity>> entry : donationsByCampaign.entrySet()) {
            UUID campaignId = entry.getKey();
            CampaignDto campaignDto = campaigns.get(campaignId);
            if (campaignDto == null) {
                log.warn("CampaignDto is null for campaignId: {}", campaignId);
                campaignDto = new CampaignDto();
            }
            final CampaignDto  finalCampaignDto = campaignDto;
            result.addAll(entry.getValue().stream()
                    .map(donationEntity -> mapEntityToDetailDto(donationEntity, finalCampaignDto))
                    .toList());
        }
        return result;
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

    private DonationDetailDto mapEntityToDetailDto(DonationEntity donationEntity, CampaignDto campaignDto) {
        return DonationDetailDto.builder()
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
                .campaign(campaignDto)
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

    private UUID getUserContextId() {
        return contextService.getUserId();
    }

    private List<DonationEntity> getDonationsByUserId(UUID userId) {
        try {
            return donationRepository.findAllByDonorIdIs(userId);
        } catch (DataAccessException ex) {
            log.error("Error accessing donation data for user ID: {}", userId, ex);
            return List.of();
        }
    }
}
