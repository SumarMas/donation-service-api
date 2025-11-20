package com.platform.donation_service.messaging.consumer;

import com.platform.donation_service.controllers.manageExceptions.CustomException;
import com.platform.donation_service.dtos.payout.PayoutMessageDto;
import com.platform.donation_service.services.payout.IPayoutStatusService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 * Consumer class for handling payout status messages from RabbitMQ.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PayoutStatusConsumer {
    /**
     * Service for handling payout status notifications.
     */
    private final IPayoutStatusService payoutStatusService;

    /**
     * Handles incoming payout status messages from the RabbitMQ queue.
     *
     * @param payoutMessageDto The payout message DTO.
     * @param deliveryTag      The delivery tag for manual acknowledgment.
     * @param channel          The RabbitMQ channel.
     */
    @RabbitListener(queues = "${queues.payout-status.queue}", ackMode = "MANUAL")
    public void handlePayoutStatusMessage(@Payload PayoutMessageDto payoutMessageDto,
                                          @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                                          @Header(AmqpHeaders.CHANNEL) Channel channel) {
        try {
            try {
                log.info("Received Payout Status Message from Payout Service");
                payoutStatusService.handlePayoutStatusUpdate(payoutMessageDto);
                channel.basicAck(deliveryTag, false);
            } catch (CustomException ex) {
                log.error("Custom exception processing payout status message: {}", ex.getMessage(), ex);
                channel.basicNack(deliveryTag, false, true);
            } catch (Exception ex) {
                log.error("Unexpected error processing payout status message: {}", ex.getMessage(), ex);
                channel.basicNack(deliveryTag, false, true);
            }
        } catch (IOException e) {
            log.error("IO exception during message acknowledgment: {}", e.getMessage(), e);
        }
    }
}
