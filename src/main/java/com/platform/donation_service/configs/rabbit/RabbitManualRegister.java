package com.platform.donation_service.configs.rabbit;

import com.platform.donation_service.configs.rabbit.exchanges.ExchangeAbstractConfig;
import com.platform.donation_service.configs.rabbit.queues.QueueAbstractConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


/**
 * Manually registers all fanout exchanges,
 * queues, and bindings declared in configuration classes.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class RabbitManualRegister {
    /** RabbitAdmin to manage RabbitMQ infrastructure. */
    private final RabbitAdmin rabbitAdmin;
    /** Application context to retrieve exchange and queue configurations. */
    private final ApplicationContext applicationContext;

    /**
     * Registers all exchanges, queues, and bindings on application startup.
     */
    @PostConstruct
    public void registerRabbitEntities() {
        log.info("🐇 Registering RabbitMQ exchanges, queues and bindings manually...");
        Iterable<ExchangeAbstractConfig> exchanges = applicationContext.getBeansOfType(ExchangeAbstractConfig.class).values();
        for (ExchangeAbstractConfig exchangeConfig : exchanges) {
            String exchangeName = exchangeConfig.getExchangeName();
            log.info("➡️ Declaring fanout exchange: {}", exchangeName);
            rabbitAdmin.declareExchange(new FanoutExchange(exchangeName, true, false));
        }
        Iterable<QueueAbstractConfig> queues = applicationContext.getBeansOfType(QueueAbstractConfig.class).values();
        for (QueueAbstractConfig queueConfig : queues) {
            String queueName = queueConfig.getQueueName();
            String exchangeName = queueConfig.getExchangeName();

            log.info("➡️ Declaring queue: {}", queueName);
            rabbitAdmin.declareQueue(new Queue(queueName, true));

            log.info("➡️ Binding queue '{}' to exchange '{}'", queueName, exchangeName);
            rabbitAdmin.declareBinding(
                    BindingBuilder.bind(new Queue(queueName, true))
                            .to(new FanoutExchange(exchangeName, true, false))
            );
        }

        log.info("✅ RabbitMQ entities successfully declared.");
    }
}
