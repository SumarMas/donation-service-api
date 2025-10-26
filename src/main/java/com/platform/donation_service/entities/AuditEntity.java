package com.platform.donation_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base class for entities that require audit fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuperBuilder
public abstract class AuditEntity {

    /**
     * The date and time when the entity was created.
     * This field is not insertable or updatable.
     */
    @Column(name = "created_datetime", insertable = false, updatable = false)
    private LocalDateTime createdDatetime;

    /**
     * The user who created the entity.
     * This field is insertable but not updatable.
     */
    @Column(name = "created_user", insertable = true, updatable = false)
    private UUID createdUser;

    /**
     * The date and time when the entity was last updated.
     * This field is not insertable or updatable.
     */
    @Column(name = "last_updated_datetime", insertable = false, updatable = false)
    private LocalDateTime lastUpdatedDatetime;

    /**
     * The user who last updated the entity.
     * This field is insertable and updatable.
     */
    @Column(name = "last_updated_user", insertable = true, updatable = true)
    private UUID lastUpdatedUser;

    /**
     * Indicates whether the entity is enabled.
     * This field is not insertable but is updatable.
     */
    @Column(name = "enabled", insertable = false, updatable = true)
    private Boolean enabled = true;
}
