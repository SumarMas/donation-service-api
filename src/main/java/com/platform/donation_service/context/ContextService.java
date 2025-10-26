package com.platform.donation_service.context;

import com.platform.donation_service.enums.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * ContextService is responsible for handling user context related operations.
 */
@Service
@AllArgsConstructor
public class ContextService implements IContextService {
    /**
     * validate if the userId is the same as the one in the context.
     *
     * @param userId the user id UUID to validate
     * @return true if the userId is the same as the one in the context
     */
    @Override
    public boolean isThisUserId(UUID userId) {
        return isThisUserId(userId.toString());
    }

    /**
     * validate if the userId is the same as the one in the context.
     *
     * @param userId the user id String to validate
     * @return true if the userId is the same as the one in the context
     */
    @Override
    public boolean isThisUserId(String userId) {
        final RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx == null || ctx.getUserId() == null) {
            return false;
        }
        return ctx.getUserId().equals(userId);
    }

    /**
     * Check if the current user has admin role.
     *
     * @return true if the user has admin role, false otherwise
     */
    @Override
    public boolean isAdmin() {
        return hasRole(UserRole.ADMIN);
    }

    /**
     * Check if the current user has donor role.
     *
     * @return true if the user has donor role, false otherwise
     */
    @Override
    public boolean isDonor() {
        return hasRole(UserRole.DONOR);
    }

    /**
     * Check if the current user has organization role.
     *
     * @return true if the user has organization role, false otherwise
     */
    @Override
    public boolean isOrganization() {
        return hasRole(UserRole.ORGANIZATION);
    }

    /**
     * Get the user ID from the current context.
     *
     * @return the user ID as UUID
     */
    @Override
    public UUID getUserId() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx == null || ctx.getUserId() == null) {
            return null;
        }
        return UUID.fromString(ctx.getUserId());
    }

    private boolean hasRole(UserRole role) {
        final RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx == null || ctx.getRoles() == null) {
            return false;
        }
        return ctx.getRoles().contains(role.name());
    }
}
