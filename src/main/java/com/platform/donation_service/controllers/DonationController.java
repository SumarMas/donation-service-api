package com.platform.donation_service.controllers;

import com.platform.donation_service.dtos.donation.DonationCreateDto;
import com.platform.donation_service.dtos.donation.DonationsDto;
import com.platform.donation_service.enums.DonationStatus;
import com.platform.donation_service.services.donation.IDonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

/**
 * Controller for handling donation-related requests.
 */
@RestController
@RequestMapping("/api/v1/donations")
@RequiredArgsConstructor
public class DonationController {
    /** Logger for logging information and errors. */
    private static final Logger LOG = LoggerFactory.getLogger(DonationController.class);
    /** Service for handling donation-related operations. */
    private final IDonationService donationService;
    /**
     * Creates a new donation.
     *
     * @param donationCreateDto the DTO containing donation creation data
     * @return ResponseEntity with the initialization point for the donation
     */
    @PostMapping
    public ResponseEntity<String> createDonation(@RequestBody @Valid DonationCreateDto donationCreateDto) {
        LOG.trace("Creating a new donation");
        String initPoint = donationService.createDonation(donationCreateDto);
        return ResponseEntity.ok(initPoint);

    }
    /**
     * Retrieves donations by campaign IDs and status.
     *
     * @param campaignIds the set of campaign IDs to filter donations
     * @param status      the set of donation statuses to filter donations
     * @return ResponseEntity with the DonationsDto containing the filtered donations
     */
    @GetMapping
    public ResponseEntity<DonationsDto> getDonationsByCampaignsId(
            @RequestParam(required = true, name = "campaign") Set<UUID> campaignIds,
            @RequestParam(required = true, name = "status") Set<DonationStatus> status) {
        LOG.trace("Getting donations by campaign");
        DonationsDto donationsDto = donationService.getDonationsByCampaignId(campaignIds, status);
        return ResponseEntity.ok(donationsDto);
    }

}
