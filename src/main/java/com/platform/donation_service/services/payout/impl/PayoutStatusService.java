package com.platform.donation_service.services.payout.impl;

import com.platform.donation_service.dtos.payout.PayoutMessageDto;
import com.platform.donation_service.enums.PayoutStatus;
import com.platform.donation_service.services.donation.IDonationPaidService;
import com.platform.donation_service.services.payout.IPayoutStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * Implementation of the IPayoutStatusService interface
 * for handling payout status updates.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PayoutStatusService implements IPayoutStatusService {
    /** Service for handling donation paid events. */
    private final IDonationPaidService donationPaidService;
    /**
     * Handles the update of payout status
     * based on the provided payout request data.
     *
     * @param messageDto the payout message
     *                         data transfer object.
     */
    @Override
    public void handlePayoutStatusUpdate(PayoutMessageDto messageDto) {
        log.trace("handlePayoutStatusUpdate payoutRequestDto={}", messageDto);
        if ((messageDto.getPreviousPayoutStatus() == null || messageDto.getPreviousPayoutStatus().equals(PayoutStatus.PENDING))
                && messageDto.getPayoutStatus() == PayoutStatus.PENDING) {
            log.trace("Managing donation paid event for payout status update: {}", messageDto);
            donationPaidService.processDonationPaidEvent(messageDto);
        } else {
            log.debug("No action taken for payout status update: {}", messageDto);
        }
    }
}
