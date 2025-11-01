package com.platform.donation_service.enums;

/**
 * Enum representing different user roles in the system.
 */
public enum UserRole {
    /**
     * Role for administrative users with elevated privileges.
     */
    ADMIN,
    /**
     * Role for regular users who can donate.
     */
    DONOR,
    /**
     * Role for users representing organizations.
     */
    ORGANIZATION
}
