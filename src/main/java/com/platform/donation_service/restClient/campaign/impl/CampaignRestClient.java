package com.platform.donation_service.restClient.campaign.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.donation_service.controllers.manageExceptions.CustomException;
import com.platform.donation_service.dtos.campaign.CampaignDto;
import com.platform.donation_service.dtos.common.ErrorApi;
import com.platform.donation_service.restClient.campaign.ICampaignRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Set;
import java.util.UUID;
/**
 * REST client implementation for interacting with the Campaign service.
 */
@Service
@Slf4j
public class CampaignRestClient implements ICampaignRestClient {
    /** RestTemplate instance for making HTTP requests. */
    private final RestTemplate restTemplate;
    /** ObjectMapper instance for JSON processing. */
    private final ObjectMapper objectMapper;
    /** Base URL for the auth service. */
    private final String baseUrl;
    /**
     * Constructs a CampaignRestClient with the specified RestTemplate and base URL.
     *
     * @param restTemplateParam the RestTemplate instance for making HTTP requests
     * @param objectMapperParam the ObjectMapper instance for JSON processing
     * @param baseUrlParam      the base URL for the auth service,
     *                          injected from application properties
     */
    public CampaignRestClient(RestTemplate restTemplateParam,
                              ObjectMapper objectMapperParam,
                              @Value("${pool.campaign.url}") String baseUrlParam) {
        this.objectMapper = objectMapperParam;
        this.restTemplate = restTemplateParam;
        this.baseUrl = baseUrlParam;
    }

    /**
     * Retrieves campaigns by their IDs.
     *
     * @param campaignIds the set of campaign IDs to retrieve
     * @return a ResponseEntity containing the CampaignDto objects
     * corresponding to the provided IDs
     */
    @Override
    public ResponseEntity<CampaignDto[]> getCampaignsByIds(Set<UUID> campaignIds) {
        String postUrl = baseUrl + "/api/v1/campaigns/get-by-ids";
        try {
            log.trace("getCampaignsByIds RestClient - start");
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Set<UUID>> entity = new HttpEntity<>(campaignIds, headers);
            return restTemplate.exchange(postUrl, HttpMethod.POST, entity, CampaignDto[].class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("HTTP error during get data ngo: {}", ex.getMessage());
            handleError(ex);
            return null;
        }
    }

    private void handleError(HttpStatusCodeException ex) {
        try {
            ErrorApi error = objectMapper.readValue(ex.getResponseBodyAsString(), ErrorApi.class);
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND || ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new CustomException(error.getMessage(), HttpStatus.valueOf(ex.getStatusCode().value()));
            }
            throw new CustomException("Unexpected error from user-service ", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException parseEx) {
            throw new CustomException("Unexpected error from user-service: " + ex.getMessage(),
                    HttpStatus.valueOf(ex.getStatusCode().value()), parseEx);
        }
    }
}
