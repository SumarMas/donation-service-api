package com.platform.donation_service.dtos.donation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * Data Transfer Object representing multiple donations.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationsDto {
    /** donation map where the ID is from the associated campaign. */
    private Map<UUID, List<DonationDto>> donations;
}
