package com.platform.donation_service.services.campaign.impl;

import com.platform.donation_service.controllers.manageExceptions.CustomException;
import com.platform.donation_service.dtos.campaign.CampaignDto;
import com.platform.donation_service.restClient.campaign.ICampaignRestClient;
import com.platform.donation_service.services.campaign.ICampaignGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for retrieving campaigns by their IDs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignGetService implements ICampaignGetService {
    /** REST client for interacting with the Campaign service. */
    private final ICampaignRestClient campaignRestClient;

    /**
     * Retrieves campaigns by their IDs.
     *
     * @param campaignIds A set of campaign IDs to retrieve.
     * @return A map where the key is the campaign ID
     * and the value is the corresponding CampaignDto.
     */
    @Override
    public Map<UUID, CampaignDto> getCampaignsByIds(Set<UUID> campaignIds) {
        log.trace("getCampaignsByIds {}", campaignIds);
        CampaignDto[] campaignDtos;
        try {
            campaignDtos = campaignRestClient.getCampaignsByIds(campaignIds).getBody();
            if (campaignDtos == null) {
                log.error("CampaignDto array is null for campaignIds: {}", campaignIds);
                campaignDtos = new CampaignDto[0];
            }
        } catch (CustomException ex) {
            log.error("Error retrieving campaigns: {}", ex.getMessage(), ex);
            campaignDtos = new CampaignDto[0];
        }
        return Arrays.stream(campaignDtos).collect(Collectors.toMap(dto -> UUID.fromString(dto.getId()), dto -> dto, (dto1, dto2) -> dto1));
    }
}
