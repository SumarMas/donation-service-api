package com.platform.donation_service.restClient.campaign;

import com.platform.donation_service.dtos.campaign.CampaignDto;
import org.springframework.http.ResponseEntity;

import java.util.Set;
import java.util.UUID;
/**
 * REST client interface for interacting with the Campaign service.
 */
public interface ICampaignRestClient {
    /**
     * Retrieves campaigns by their IDs.
     *
     * @param campaignIds the set of campaign IDs to retrieve
     * @return a ResponseEntity containing the CampaignDto objects
     * corresponding to the provided IDs
     */
    ResponseEntity<CampaignDto[]> getCampaignsByIds(Set<UUID> campaignIds);
}
