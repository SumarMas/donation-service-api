package com.platform.donation_service.configs.rabbit;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Ensures RabbitAdmin declares all exchanges, queues,
 * and bindings before any listener starts.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class RabbitInitializer {

    /** RabbitAdmin to manage RabbitMQ infrastructure. */
    private final RabbitAdmin rabbitAdmin;

    /**
     * Initializes RabbitMQ infrastructure on application startup.
     */
    @PostConstruct
    public void init() {
        log.info("🔹 Declaring RabbitMQ infrastructure (queues, exchanges, bindings)...");
        rabbitAdmin.initialize();
    }
}
