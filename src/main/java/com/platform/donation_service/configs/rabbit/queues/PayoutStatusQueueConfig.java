package com.platform.donation_service.configs.rabbit.queues;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the Payout Status Queue.
 * Extends the QueueAbstractConfig to set up the exchange and queue names.
 */
@Configuration
public class PayoutStatusQueueConfig extends QueueAbstractConfig {
    /**
     * Constructs a QueueAbstractConfig with
     * the specified exchange and queue names.
     *
     * @param exchangeNameParam the name of the exchange
     * @param queueNameParam    the name of the queue
     */
    public PayoutStatusQueueConfig(@Value("${queues.payout-status.exchange}") String exchangeNameParam,
                                   @Value("${queues.payout-status.queue}") String queueNameParam) {
        super(exchangeNameParam, queueNameParam);
    }
}
