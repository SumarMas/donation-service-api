package com.platform.donation_service.services.payout.impl;

import com.platform.donation_service.controllers.manageExceptions.CustomException;
import com.platform.donation_service.dtos.payout.PayoutRequestDto;
import com.platform.donation_service.restClient.payout.IPayoutRestClient;
import com.platform.donation_service.services.payout.IPayoutGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;
/**
 * Implementation of the IPayoutGetService interface
 * for retrieving payout request details.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PayoutGetService implements IPayoutGetService {
    /** REST client for payout-related operations. */
    private final IPayoutRestClient payoutRestClient;
    /**
     * Retrieves a payout request by its unique identifier.
     *
     * @param payoutId The unique identifier of the payout request.
     * @return The PayoutRequestDto corresponding to the provided ID.
     */
    @Override
    public PayoutRequestDto getPayoutById(UUID payoutId) {
        log.trace("getPayoutById payoutId={}", payoutId);
        try {
            PayoutRequestDto payoutRequestDto = payoutRestClient.getPayoutById(payoutId).getBody();
            if (payoutRequestDto == null) {
                log.error("No payout found with ID: {}", payoutId);
                throw new CustomException("Payout not found", HttpStatus.NOT_FOUND);
            }
            return  payoutRequestDto;
        } catch (Exception e) {
            log.error("Error retrieving payout with ID: {}", payoutId, e);
            throw e;
        }
    }
}
