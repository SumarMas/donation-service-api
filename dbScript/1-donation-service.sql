-- ============================================================
-- DATABASE: donation_service
-- Description: Schema, audit tables and triggers for donation-service
-- ============================================================

CREATE DATABASE IF NOT EXISTS donation_service CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE donation_service;

-- ============================================================
-- 1) TABLE: donations
-- ============================================================
CREATE TABLE IF NOT EXISTS donations (
                                         donation_id BINARY(16) NOT NULL,
    campaign_id BINARY(16) NOT NULL,
    donor_id BINARY(16) NOT NULL,
    payment_id VARCHAR(64),
    amount DECIMAL(11,2) NOT NULL,
    currency VARCHAR(5) NOT NULL DEFAULT 'ARS',
    status ENUM('CREATED','CONFIRMED','PAID','CANCELLED') NOT NULL DEFAULT 'CREATED',
    payment_method VARCHAR(50),
    payment_proof VARCHAR(255),
    payment_datetime DATETIME NULL,

    enabled BOOLEAN DEFAULT TRUE,
    created_datetime DATETIME DEFAULT NOW(),
    created_user BINARY(16),
    last_updated_datetime DATETIME DEFAULT NOW(),
    last_updated_user BINARY(16),

    PRIMARY KEY (donation_id),
    KEY idx_donations_campaign (campaign_id),
    KEY idx_donations_donor (donor_id),
    KEY idx_donations_status (status)
    );

-- ============================================================
-- 2) TABLE: donations_audit (mirror + version)
-- ============================================================
CREATE TABLE IF NOT EXISTS donations_audit LIKE donations;
ALTER TABLE donations_audit
    ADD COLUMN version INT NOT NULL,
DROP PRIMARY KEY,
    ADD PRIMARY KEY (donation_id, version);

-- ============================================================
-- 3) TRIGGERS
--    - after_insert: guarda versión 1 en audit
--    - before_update: mantiene created_*, actualiza last_updated_datetime y guarda nueva versión
-- ============================================================
DELIMITER $$

DROP TRIGGER IF EXISTS after_insert_donations $$
CREATE TRIGGER after_insert_donations
    AFTER INSERT ON donations
    FOR EACH ROW
BEGIN
    INSERT INTO donations_audit
    SELECT d.*, 1 AS version
    FROM donations d
    WHERE d.donation_id = NEW.donation_id;
END $$

DROP TRIGGER IF EXISTS before_update_donations $$
CREATE TRIGGER before_update_donations
    BEFORE UPDATE ON donations
    FOR EACH ROW
BEGIN
    DECLARE last_version INT;

    -- Mantener creado por/fecha originales
    SET NEW.created_user = OLD.created_user;
    SET NEW.created_datetime = OLD.created_datetime;

    -- Actualizar last_updated_datetime
    SET NEW.last_updated_datetime = NOW();

    -- Buscar última versión en audit
    SELECT COALESCE(MAX(da.version), 0)
    INTO last_version
    FROM donations_audit da
    WHERE da.donation_id = NEW.donation_id;

    -- Insertar nueva versión (snapshot) en audit
    INSERT INTO donations_audit
    SELECT d.*, last_version + 1 AS version
    FROM donations d
    WHERE d.donation_id = NEW.donation_id;
END $$

DELIMITER ;

-- ============================================================
-- LISTO
-- ============================================================
