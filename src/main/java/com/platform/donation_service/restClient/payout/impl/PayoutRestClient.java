package com.platform.donation_service.restClient.payout.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.donation_service.controllers.manageExceptions.CustomException;
import com.platform.donation_service.dtos.common.ErrorApi;
import com.platform.donation_service.dtos.payout.PayoutRequestDto;
import com.platform.donation_service.restClient.payout.IPayoutRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
/**
 * Implementation of the IPayoutRestClient interface
 * for interacting with the Payout service.
 */
@Service
@Slf4j
@SuppressWarnings("PMD.LooseCoupling")
public class PayoutRestClient implements IPayoutRestClient {
    /**
     * RestTemplate instance for making HTTP requests.
     */
    private final RestTemplate restTemplate;
    /**
     * ObjectMapper instance for JSON processing.
     */
    private final ObjectMapper objectMapper;
    /**
     * Base URL for the Payout service.
     */
    private final String rootUrl;

    /**
     * Application name for internal request header.
     */
    private final String applicationName;
    /**
     * Constructs a PayoutRestClient with the specified RestTemplate and root URL.
     *
     * @param restTemplateParam the RestTemplate instance for making HTTP requests
     * @param rootUrlParam      the base URL for the Payout service,
     *                          injected from application properties
     * @param applicationNameParam the application name,
     *                             injected from application properties
     * @param objectMapperParam the ObjectMapper instance for JSON processing
     */
    public PayoutRestClient(RestTemplate restTemplateParam,
                            @Value("${pool.payout.url}") String rootUrlParam,
                            @Value("${spring.application.name}") String applicationNameParam,
                            ObjectMapper objectMapperParam) {
        this.rootUrl = rootUrlParam;
        this.applicationName = applicationNameParam;
        this.restTemplate = restTemplateParam;
        this.objectMapper = objectMapperParam;
    }

    /**
     * Retrieves a PayoutRequestDto by its unique identifier.
     *
     * @param payoutId The unique identifier of the payout request.
     * @return A ResponseEntity containing the PayoutRequestDto
     * corresponding to the provided ID.
     */
    @Override
    public ResponseEntity<PayoutRequestDto> getPayoutById(UUID payoutId) {
        String getUrl = rootUrl + "/api/v1/payouts/{requestId}";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Internal-Request", applicationName);
            HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
            log.trace("Sending GET request to URL: {}", getUrl);
            return restTemplate.exchange(
                    getUrl,
                    HttpMethod.GET,
                    requestEntity,
                    PayoutRequestDto.class,
                    payoutId
            );
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("HTTP error during get data payout: {}", ex.getMessage());
            handleError(ex);
            return null;
        }
    }

    private void handleError(HttpStatusCodeException ex) {
        try {
            ErrorApi error = objectMapper.readValue(ex.getResponseBodyAsString(), ErrorApi.class);
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new CustomException(error.getMessage(), HttpStatus.BAD_REQUEST);
            }
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new CustomException(error.getMessage(), HttpStatus.NOT_FOUND);
            }
            throw new CustomException("Unexpected error from payout-service ", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException parseEx) {
            throw new CustomException("Unexpected error from payout-service: " + ex.getMessage(),
                    HttpStatus.valueOf(ex.getStatusCode().value()), parseEx);
        }
    }
}
