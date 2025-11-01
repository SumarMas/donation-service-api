package com.platform.donation_service.services.donation.Impl;

import com.platform.donation_service.dtos.donation.DonationMessageDto;
import com.platform.donation_service.entities.DonationEntity;
import com.platform.donation_service.enums.DonationStatus;
import com.platform.donation_service.messaging.producer.DonationProducer;
import com.platform.donation_service.services.donation.IDonationPublishEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service implementation for publishing donation events.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DonationPublishEventService implements IDonationPublishEventService {
    /** Producer for publishing donation events to RabbitMQ. */
    private final DonationProducer donationProducer;
    /**
     * Publishes a donation event.
     *
     * @param donation       The donation entity.
     * @param status         The current status of the donation.
     * @param previousStatus The previous status of the donation.
     */
    @Override
    public void publishDonationEvent(DonationEntity donation, DonationStatus status, DonationStatus previousStatus) {
        log.trace("Publishing event for donation ID: {}", donation.getDonationId());
        DonationMessageDto donationMessageDto = DonationMessageDto.builder()
                .donationId(donation.getDonationId())
                .campaignId(donation.getCampaignId())
                .amount(donation.getAmount())
                .donationStatus(status)
                .userId(donation.getDonorId())
                .previousDonationStatus(previousStatus)
                .build();
        donationProducer.publishDonationStateChangeEvent(donationMessageDto);
    }
}
