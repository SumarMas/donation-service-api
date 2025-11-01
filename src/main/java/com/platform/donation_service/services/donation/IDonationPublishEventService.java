package com.platform.donation_service.services.donation;

import com.platform.donation_service.entities.DonationEntity;
import com.platform.donation_service.enums.DonationStatus;
/**
 * Service interface for publishing donation events.
 */
public interface IDonationPublishEventService {
    /**
     * Publishes a donation event.
     *
     * @param donation       The donation entity.
     * @param status         The current status of the donation.
     * @param previousStatus The previous status of the donation.
     */
    void publishDonationEvent(DonationEntity donation, DonationStatus status, DonationStatus previousStatus);
}
