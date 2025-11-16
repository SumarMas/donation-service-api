package com.platform.donation_service.configs.rabbit.queues;

import lombok.Getter;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Abstract configuration class for RabbitMQ queues.
 */
@Configuration
@Getter
public abstract class QueueAbstractConfig {
    /** Name of the exchange. */
    private final String exchangeName;
    /** Name of the queue. */
    private final String queueName;

    /**
     * Constructs a QueueAbstractConfig with the specified exchange and queue names.
     *
     * @param exchangeNameParam the name of the exchange
     * @param queueNameParam    the name of the queue
     */
    public QueueAbstractConfig(String exchangeNameParam, String queueNameParam) {
        this.exchangeName = exchangeNameParam;
        this.queueName = queueNameParam;
    }

    /**
     * Defines the durable queue bean.
     * A durable queue is one that persists
     * across broker restarts, ensuring message durability.
     *
     * @return A Queue object representing the durable queue.
     */
    @Bean(name = "#{T(java.lang.String).format('%sQueue', queueName)}")
    public Queue queue() {
        return new Queue(queueName, true); // true indicates the queue is durable
    }
}
