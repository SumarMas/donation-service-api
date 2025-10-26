package com.platform.donation_service.controllers;

import com.platform.donation_service.services.mercadoPago.IMercadoPagoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
public class WebHookController {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(WebHookController.class);
    private final IMercadoPagoService mercadoPagoService;

    @PostMapping("/mercado-pago")
    public ResponseEntity<Void> handleMercadoPagoWebHook(@RequestBody(required = false) String payload,
                                                         @RequestParam("type") String type,
                                                         @RequestParam("data.id") String dataId) {
        LOG.info("Received MercadoPago webhook");
        mercadoPagoService.processDonationPayment(payload, type, dataId);
        return ResponseEntity.ok().build();
    }
}
