package com.platform.donation_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class for the Donation Service Spring Boot application.
 */
@SpringBootApplication
@EnableScheduling
@EnableRabbit
@Slf4j
public class DonationServiceApplication implements CommandLineRunner {
    /** RabbitListenerEndpointRegistry to manage RabbitMQ listeners. */
    private final RabbitListenerEndpointRegistry registry;

    /**
     * Constructor to initialize RabbitListenerEndpointRegistry.
     * @param registryParam RabbitListenerEndpointRegistry instance
     */
    public DonationServiceApplication(RabbitListenerEndpointRegistry registryParam) {
        this.registry = registryParam;
    }
    /**
     * Main method to run the Spring Boot application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(DonationServiceApplication.class, args);
    }

    /**
     * Method to start RabbitMQ listeners on application startup.
     * @param args command-line arguments
     * @throws Exception if an error occurs while starting listeners
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting RabbitMQ Payout Service Application");
        registry.start();
    }
}
