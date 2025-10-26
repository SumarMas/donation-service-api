package com.platform.donation_service.services.mercadoPago.Impl;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.platform.donation_service.controllers.manageExceptions.CustomException;
import com.platform.donation_service.services.donation.IDonationProcessPayment;
import com.platform.donation_service.services.mercadoPago.IMercadoPagoService;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for interacting with MercadoPago API.
 */
@Service
public class MercadoPagoService implements IMercadoPagoService {
    /** Logger instance for logging information and errors. */
    private static final Logger LOG = LoggerFactory.getLogger(MercadoPagoService.class);
    /** Service for processing donation payments. */
    private final IDonationProcessPayment donationProcessPayment;
    /** MercadoPago access token for authentication. */
    private final String mercadoPagoAccessToken;
    /** URLs for handling payment outcomes. */
    private final String mercadoPagoSuccessUrl;
    /** URL for handling payment failures. */
    private final String mercadoPagoFailureUrl;
    /** URL for handling pending payments. */
    private final String mercadoPagoPendingUrl;
    /** URL for payment notifications webhook. */
    private final String mercadoPagoNotificationUrl;
    /**
     * Constructs a MercadoPagoService with the specified configuration values.
     *
     * @param mercadoPagoAccessTokenParam the MercadoPago access token,
     *                                    injected from application properties
     * @param mercadoPagoSuccessUrlParam  the success URL for payments,
     *                                    injected from application properties
     * @param mercadoPagoFailureUrlParam  the failure URL for payments,
     *                                    injected from application properties
     * @param mercadoPagoPendingUrlParam  the pending URL for payments,
     *                                    injected from application properties
     * @param mercadoPagoNotificationUrlParam the notification URL for payment webhooks,
     *                                     injected from application properties
     * @param donationProcessPaymentParam  the service for processing donation payments
     */
    public MercadoPagoService(@Value("${mercado-pago.access-token}") String mercadoPagoAccessTokenParam,
                             @Value("${mercado-pago.url.success}") String mercadoPagoSuccessUrlParam,
                             @Value("${mercado-pago.url.failure}") String mercadoPagoFailureUrlParam,
                             @Value("${mercado-pago.url.pending}") String mercadoPagoPendingUrlParam,
                              @Value("${mercado-pago.url.notify}") String mercadoPagoNotificationUrlParam,
                              IDonationProcessPayment donationProcessPaymentParam) {
        this.mercadoPagoSuccessUrl = mercadoPagoSuccessUrlParam;
        this.mercadoPagoFailureUrl = mercadoPagoFailureUrlParam;
        this.mercadoPagoPendingUrl = mercadoPagoPendingUrlParam;
        this.mercadoPagoAccessToken = mercadoPagoAccessTokenParam;
        this.mercadoPagoNotificationUrl = mercadoPagoNotificationUrlParam;
        this.donationProcessPayment = donationProcessPaymentParam;

    }

    /**
     * Creates a MercadoPago payment preference.
     *
     * @param amount     The amount for the donation.
     * @param title      The title of the donation.
     * @param donationId The unique identifier for the donation.
     * @return The created Preference object.
     */
    @Override
    public Preference createPreference(BigDecimal amount, String title, String donationId) {
        LOG.trace("Creating a preference with donationId: {}", donationId);
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(title)
                    .quantity(1)
                    .unitPrice(amount)
                    .currencyId("ARS")
                    .categoryId("donations")
                    .build();
            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(mercadoPagoSuccessUrl)
                    .failure(mercadoPagoFailureUrl)
                    .pending(mercadoPagoPendingUrl)
                    .build();

            OffsetDateTime expirationDateFrom = LocalDateTime.now()
                    .atZone(ZoneId.systemDefault())
                    .toOffsetDateTime();

            OffsetDateTime expirationDateTo = LocalDateTime.now()
                    .plusHours(24)
                    .atZone(ZoneId.systemDefault())
                    .toOffsetDateTime();
            PreferenceRequest request = PreferenceRequest.builder()
                    .items(items)
                    .externalReference(donationId)
                    .backUrls(backUrls)
                    .expirationDateFrom(expirationDateFrom)
                    .expirationDateTo(expirationDateTo)
                    .notificationUrl(mercadoPagoNotificationUrl)
                    .build();
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(request);
            LOG.trace("Preference created with ID: {}", preference.getId());
            return preference;
        } catch (MPException | MPApiException ex) {
            LOG.error("Error creating MercadoPago preference: {}", ex.getMessage());
            throw new CustomException("An error occurred while creating the payment. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    /**
     * Processes a donation payment based on the provided payload.
     *
     * @param payload The payment notification payload from MercadoPago.
     * @param type    The type of the notification.
     * @param dataId  The identifier for the payment data.
     */
    @Override
    public void processDonationPayment(String payload, String type, String dataId) {
        LOG.trace("Processing donation payment with dataId: {}. Type: {}. Payload: {}", dataId, type, payload);
        if (!"payment".equals(type)) {
            LOG.warn("Unsupported notification type: {}", type);
            return;
        }
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);
            com.mercadopago.client.payment.PaymentClient paymentClient = new com.mercadopago.client.payment.PaymentClient();
            com.mercadopago.resources.payment.Payment payment =
                    paymentClient.get(Long.parseLong(dataId));
            donationProcessPayment.processPayment(payment);
            LOG.trace("Donation payment processed for dataId: {}", dataId);
        } catch (MPException | MPApiException ex) {
            LOG.error("Error processing donation payment: {}", ex.getMessage(), ex);
            throw new CustomException("An error occurred while processing the payment. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }
}
