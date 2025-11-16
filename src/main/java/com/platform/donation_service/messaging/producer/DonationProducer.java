package com.platform.donation_service.messaging.producer;


import com.platform.donation_service.configs.rabbit.exchanges.ExchangeAbstractConfig;
import com.platform.donation_service.dtos.donation.DonationMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;

/**
 * Producer for publishing donation state change events to RabbitMQ.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DonationProducer {
    /** RabbitTemplate for sending messages to RabbitMQ. */
    private final RabbitTemplate rabbitTemplate;
    /** Configuration for donation events exchanges. */
    private final ExchangeAbstractConfig donationStatusExchangeConfig;

    /**
     * Publishes a donation state change event to the RabbitMQ exchange.
     *
     * @param donationMessageDto the donation message DTO containing donation details.
     */
    public void publishDonationStateChangeEvent(DonationMessageDto donationMessageDto) {
        try {
            log.debug("Entering publishDonationStateChangeEvent with donationMessageDto: {}", donationMessageDto);
            rabbitTemplate.convertAndSend(donationStatusExchangeConfig.getExchangeName(), "", donationMessageDto);
            log.debug("Donation state change event published successfully");
        } catch (AmqpConnectException e) {
            log.error("❌ Failed to connect to RabbitMQ. The broker might be down.", e);
        } catch (MessageConversionException e) {
            log.error("❌ Error serializing donation message: {}", donationMessageDto, e);
        } catch (AmqpException e) {
            log.error("❌ General error publishing message to RabbitMQ.", e);
        }
    }
}
