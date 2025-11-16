package com.platform.donation_service.services.donation;

import com.platform.donation_service.dtos.payout.PayoutMessageDto;
/**
 * Service interface for handling donation paid events.
 */
public interface IDonationPaidService {
    /**
     * Process the event when a donation has been marked as paid.
     *
     * @param messageDto the payout message event.
     */
    void processDonationPaidEvent(PayoutMessageDto messageDto);
}
