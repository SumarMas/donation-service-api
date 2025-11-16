package com.platform.donation_service.configs.rabbit.exchanges;

import lombok.Getter;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Abstract configuration class for RabbitMQ exchanges.
 */
@Configuration
public abstract class ExchangeAbstractConfig {
    /** Name of the exchange. */
    @Getter
    private final String exchangeName;
    /**
     * Constructs an ExchangeAbstractConfig with the specified exchange name.
     *
     * @param exchangeNameParam the name of the exchange
     */
    public ExchangeAbstractConfig(String exchangeNameParam) {
        this.exchangeName = exchangeNameParam;
    }

    /**
     * Defines a FanoutExchange for the exchange.
     *
     * @return the FanoutExchange bean
     */
    @Bean(name = "#{T(java.lang.String).format('%sExchange', exchangeName)}")
    public FanoutExchange createExchange() {
        return new FanoutExchange(exchangeName, true, false);
    }
}
