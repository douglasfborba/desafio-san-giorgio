package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.queue;

import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.exception.ReceiveMessageErrorException;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.UpdatePaymentStatusUseCase;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.ReceiveMessageAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.message.PaymentMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public abstract class GenericMessageReceiverAdapter implements ReceiveMessageAdapter {
    protected final ObjectMapper objectMapper;
    protected final UpdatePaymentStatusUseCase updatePaymentStatusUseCase;

    @Override
    public void receive(final String payload) {
        try {
            var message = objectMapper.readValue(payload, PaymentMessage.class);
            log.info("Message received from queue {}", message);
            updatePaymentStatusUseCase.update(getPaymentStatus(), message.toDomain());
            log.info("Payment status updated with success.");
        } catch (Exception e) {
            log.error("Error to receive message from queue.");
            throw new ReceiveMessageErrorException();
        }
    }
}
