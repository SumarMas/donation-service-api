package com.platform.donation_service.context;

import org.springframework.stereotype.Service;

import java.util.UUID;
/**
 * Service interface for accessing and validating user context information.
 */
@Service
public interface IContextService {
    /** validate if the userId is the same as the one in the context.
     * @param userId the user id UUID to validate
     * @return true if the userId is the same as the one in the context
     */
    boolean isThisUserId(UUID userId);
    /** validate if the userId is the same as the one in the context.
     * @param userId the user id String to validate
     * @return true if the userId is the same as the one in the context
     */
    boolean isThisUserId(String userId);
    /**
     * Check if the current user has admin role.
     * @return true if the user has admin role, false otherwise
     */
    boolean isAdmin();
    /**
     * Check if the current user has donor role.
     * @return true if the user has donor role, false otherwise
     */
    boolean isDonor();
    /**
     * Check if the current user has organization role.
     * @return true if the user has organization role, false otherwise
     */
    boolean isOrganization();
    /**
     * Get the user ID from the current context.
     * @return the user ID as UUID
     */
    UUID getUserId();
}
