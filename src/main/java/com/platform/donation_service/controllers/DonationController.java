package com.platform.donation_service.controllers;

import com.platform.donation_service.dtos.donation.DonationCreateDto;
import com.platform.donation_service.services.donation.IDonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @PostMapping
    public ResponseEntity<String> createDonation(@RequestBody @Valid DonationCreateDto donationCreateDto) {
        LOG.trace("Creating a new donation");
        String initPoint = donationService.createDonation(donationCreateDto);
        return ResponseEntity.ok(initPoint);

    }

}
