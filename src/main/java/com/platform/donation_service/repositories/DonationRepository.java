package com.platform.donation_service.repositories;

import com.platform.donation_service.entities.DonationEntity;
import com.platform.donation_service.enums.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Repository interface for managing DonationEntity instances.
 */
@Repository
public interface DonationRepository extends JpaRepository<DonationEntity, UUID> {
    /**
     * Finds donations by campaign IDs and donation statuses.
     *
     * @param campaignIds      A set of campaign IDs to filter the donations.
     * @param donationStatuses A set of donation statuses to filter the donations.
     * @return A list of DonationEntity objects that match the provided campaign IDs and donation statuses.
     */
    @Query("SELECT d FROM DonationEntity d WHERE d.campaignId IN :campaignIds AND d.status IN :donationStatuses")
    List<DonationEntity> findByCampaignIdsAndStatus(Set<UUID> campaignIds, Set<DonationStatus> donationStatuses);
}
