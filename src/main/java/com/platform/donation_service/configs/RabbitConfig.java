package com.platform.donation_service.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for publishing donation events.
 * Uses the same ObjectMapper configured in MappersConfig.
 */
@Configuration
public class RabbitConfig {
    /** Name of the donation exchange. */
    private final String donationExchangeName;
    /**
     * Constructs a RabbitConfig with the specified donation exchange name.
     *
     * @param donationExchangeNameParam the name of the donation exchange,
     *                             injected from application properties
     */
    public RabbitConfig(@Value("${exchange.donation-status}") String donationExchangeNameParam) {
        this.donationExchangeName = donationExchangeNameParam;
    }
    /** Getter for donationExchange.
     * @return the name of the donation exchange
     */
    public String getDonationExchangeName() {
        return donationExchangeName;
    }
    /**
     * Defines a FanoutExchange for donation events.
     *
     * @return the FanoutExchange bean
     */
    @Bean
    public FanoutExchange donationExchange() {
        return new FanoutExchange(donationExchangeName, true, false);
    }

    /**
     * Message converter using the shared ObjectMapper (supports LocalDateTime, etc.).
     * @param objectMapper the shared ObjectMapper bean
     * @return the MessageConverter bean
     */
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    /**
     * RabbitTemplate configured with JSON message converter.
     * @param connectionFactory the RabbitMQ connection factory
     * @param jsonMessageConverter the JSON message converter
     * @return the RabbitTemplate bean
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        template.setChannelTransacted(true);
        return template;
    }
}
