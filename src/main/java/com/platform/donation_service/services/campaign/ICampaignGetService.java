package com.platform.donation_service.services.campaign;

import com.platform.donation_service.dtos.campaign.CampaignDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
/**
 * Service interface for retrieving campaigns by their IDs.
 */
public interface ICampaignGetService {
    /**
     * Retrieves campaigns by their IDs.
     *
     * @param campaignIds A set of campaign IDs to retrieve.
     * @return A map where the key is the campaign
     * ID and the value is the corresponding CampaignDto.
     */
    Map<UUID, CampaignDto> getCampaignsByIds(Set<UUID> campaignIds);
}
