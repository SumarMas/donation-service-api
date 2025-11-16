package com.platform.donation_service.services.payout;

import com.platform.donation_service.dtos.payout.PayoutRequestDto;

import java.util.UUID;
/**
 * Service interface for retrieving payout requests.
 */
public interface IPayoutGetService {
    /**
     * Retrieves a payout request by its unique identifier.
     *
     * @param payoutId The unique identifier of the payout request.
     * @return The PayoutRequestDto corresponding to the provided ID.
     */
    PayoutRequestDto getPayoutById(UUID payoutId);
}
