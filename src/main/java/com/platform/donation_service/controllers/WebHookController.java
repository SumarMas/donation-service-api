package com.platform.donation_service.controllers;

import com.platform.donation_service.services.mercadoPago.IMercadoPagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling webhooks from external services.
 */
@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebHookController {

    /** Service for handling MercadoPago interactions. */
    private final IMercadoPagoService mercadoPagoService;

    /**
     * Endpoint to handle MercadoPago webhooks.
     *
     * @param payload the raw JSON payload from the webhook (optional).
     * @param type    the type of the webhook event.
     * @param dataId  the ID of the data related to the event.
     * @return HTTP 200 OK response.
     */
    @PostMapping("/mercado-pago")
    public ResponseEntity<Void> handleMercadoPagoWebHook(@RequestBody(required = false) String payload,
                                                         @RequestParam("type") String type,
                                                         @RequestParam("data.id") String dataId) {
        log.info("Received MercadoPago webhook");
        mercadoPagoService.processDonationPayment(payload, type, dataId);
        return ResponseEntity.ok().build();
    }
}
