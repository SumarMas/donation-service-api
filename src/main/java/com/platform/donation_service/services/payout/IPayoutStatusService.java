package com.platform.donation_service.services.payout;

import com.platform.donation_service.dtos.payout.PayoutMessageDto;
/**
 * Service interface for handling payout status updates.
 */
public interface IPayoutStatusService {
    /**
     * Handles the update of payout status
     * based on the provided payout request data.
     *
     * @param payoutMessageDto the payout message
     *                         data transfer object.
     */
    void handlePayoutStatusUpdate(PayoutMessageDto payoutMessageDto);
}
