package com.platform.donation_service.repositories;

import com.platform.donation_service.entities.DonationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing DonationEntity instances.
 */
@Repository
public interface DonationRepository extends JpaRepository<DonationEntity, UUID> {
}
