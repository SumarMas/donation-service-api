package com.platform.donation_service.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for publishing donation events.
 * Uses the same ObjectMapper configured in MappersConfig.
 */
@Configuration
public class RabbitConfig {

    public static final String DONATION_EXCHANGE = "donation.exchange";

    @Bean
    public FanoutExchange donationExchange() {
        return new FanoutExchange(DONATION_EXCHANGE, true, false);
    }

    /**
     * Message converter using the shared ObjectMapper (supports LocalDateTime, etc.)
     */
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    /**
     * RabbitTemplate configured with JSON message converter.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        template.setChannelTransacted(true);
        return template;
    }
}
