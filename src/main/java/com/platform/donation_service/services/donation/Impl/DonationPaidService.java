package com.platform.donation_service.services.donation.Impl;

import com.platform.donation_service.controllers.manageExceptions.CustomException;
import com.platform.donation_service.dtos.payout.PayoutMessageDto;
import com.platform.donation_service.dtos.payout.PayoutRequestDto;
import com.platform.donation_service.dtos.payout.donation.PayoutRequestDonationDto;
import com.platform.donation_service.entities.DonationEntity;
import com.platform.donation_service.enums.DonationStatus;
import com.platform.donation_service.repositories.DonationRepository;
import com.platform.donation_service.services.donation.IDonationPaidService;
import com.platform.donation_service.services.payout.IPayoutGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
/**
 * Service implementation for handling donation paid events.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DonationPaidService implements IDonationPaidService {
    /** Repository for donation-related operations. */
    private final DonationRepository donationRepository;
    /** Service for retrieving payout information. */
    private final IPayoutGetService payoutGetService;
    /**
     * Process the event when a donation has been marked as paid.
     *
     * @param payoutMessageDto the payout message event.
     */
    @Override
    @Transactional
    public void processDonationPaidEvent(PayoutMessageDto payoutMessageDto) {
        log.trace("processDonationPaidEvent called");
        PayoutRequestDto payoutRequestDto = getPayoutRequestDto(payoutMessageDto.getPayoutId());
        List<PayoutRequestDonationDto> donations = payoutRequestDto.getDonations();
        List<DonationEntity> donationEntities = getDonationsByIds(donations);
        for (DonationEntity donationEntity : donationEntities) {
            donationEntity.setStatus(DonationStatus.PAID);
            donationEntity.setPaymentDatetime(payoutRequestDto.getApprovalDatetime());
        }
        saveAllDonations(donationEntities);
        log.info("Processed donation paid event for payout request ID: {}", payoutRequestDto.getPayoutRequestId());
    }

    private List<DonationEntity> getDonationsByIds(List<PayoutRequestDonationDto> donations) {
        List<UUID> ids = donations.stream().map(PayoutRequestDonationDto::getDonationId).distinct().toList();
        List<DonationEntity> donationEntities = donationRepository.findAllById(ids);
        if (donationEntities.size() != ids.size()) {
            List<UUID> foundIds = donationEntities.stream().map(DonationEntity::getDonationId).toList();
            List<UUID> notFoundIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();
            log.warn("Donations not found for IDs: {}", notFoundIds);
            throw new CustomException("Some donations not found", HttpStatus.CONFLICT);
        }
        return donationEntities;
    }

    private void saveAllDonations(List<DonationEntity> donationEntities) {
        try {
            log.trace("Saving all donations");
            donationRepository.saveAll(donationEntities);
        } catch (DataAccessException ex) {
            log.error("Error saving donation entities: {}", donationEntities, ex);
            throw new CustomException("Error saving donations", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    private PayoutRequestDto getPayoutRequestDto(UUID payoutRequestId) {
        log.trace("getPayoutRequestDto payoutRequestId={}", payoutRequestId);
        return payoutGetService.getPayoutById(payoutRequestId);
    }
}
