package com.platform.donation_service.configs.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for the User Service.
 * Uses the same ObjectMapper configured in MappersConfig.
 */
@Configuration
public class RabbitConfig {
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

    /**
     * Defines a RabbitAdmin bean that will automatically declare queues,
     * exchanges and bindings defined in the Spring context.
     *
     * @param connectionFactory the RabbitMQ connection factory
     * @return the RabbitAdmin bean
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true); // 🔹 ensures declarations run on startup
        return admin;
    }
}
