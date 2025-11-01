package com.platform.donation_service.schedules;

import com.platform.donation_service.entities.DonationEntity;
import com.platform.donation_service.enums.DonationStatus;
import com.platform.donation_service.repositories.DonationRepository;
import com.platform.donation_service.services.donation.IDonationPublishEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Scheduler that automatically cancels donations
 * that have been in CREATED status for more than 24 hours.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DonationAutoCancelScheduler {
    /** Threshold in hours to determine old confirmed donations. */
    private static final int HOURS_THRESHOLD = 24;
    /** Repository for accessing donation data. */
    private final DonationRepository donationRepository;
    /** Service for publishing donation events. */
    private final IDonationPublishEventService donationPublishEventService;

    /**
     * Cancels donations that have been in CREATED status for more than 24 hours.
     * This method runs at a fixed rate defined in the application properties.
     */
    @Transactional
    @Scheduled(fixedRateString =  "${scheduler.donation-auto-close.rate-ms}")
    public void cancelOldConfirmedDonations() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(HOURS_THRESHOLD);
        log.info("🕒 Running Donation Auto-Cancel job. Checking CREATED donations before {}", cutoff);

        List<DonationEntity> oldConfirmed = donationRepository.findAllByStatusAndCreatedDatetimeBefore(
                DonationStatus.CREATED, cutoff
        );

        if (oldConfirmed.isEmpty()) {
            log.trace("✅ No CREATED donations older than 24h found.");
            return;
        }

        oldConfirmed.forEach(donation -> {
            donationPublishEventService.publishDonationEvent(donation, DonationStatus.CANCELLED, donation.getStatus());
            donation.setStatus(DonationStatus.CANCELLED);
            donation.setLastUpdatedDatetime(LocalDateTime.now());
        });

        donationRepository.saveAll(oldConfirmed);
        log.trace("🚫 {} donations marked as CANCELLED (older than 24h).", oldConfirmed.size());
    }
}
