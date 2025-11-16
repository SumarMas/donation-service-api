package com.platform.donation_service.configs.rabbit.exchanges;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the "donation status" RabbitMQ exchange.
 */
@Configuration
public class DonationStatusExchangeConfig extends ExchangeAbstractConfig {
    /**
     * Constructs an ExchangeAbstractConfig with the specified exchange name.
     *
     * @param exchangeNameParam the name of the exchange
     */
    public DonationStatusExchangeConfig(@Value("${queues.donation.exchange}") String exchangeNameParam) {
        super(exchangeNameParam);
    }
}
