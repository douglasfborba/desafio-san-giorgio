package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.queue;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.Payment;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;
import br.com.uol.compass.desafio.san_giorgio.application.factory.model.PaymentFactory;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.UpdatePaymentStatusUseCase;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.exception.ReceiveMessageErrorException;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.factory.message.PaymentMessageFactory;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.message.PaymentMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SurplusMessageReceiverAdapterTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private UpdatePaymentStatusUseCase updatePaymentStatusUseCase;

    @InjectMocks
    private SurplusMessageReceiverAdapter surplusMessageReceiver;

    @Test
    @DisplayName("Should receive payment message successfully.")
    void shouldReceivePaymentMessageSuccessfully() throws Exception {
        var message = PaymentMessageFactory.createSurplusPaymentMessage();
        var payload = mapper.writeValueAsString(message);

        when(mapper.readValue(payload, PaymentMessage.class))
                .thenReturn(message);

        when(updatePaymentStatusUseCase.update(any(PaymentStatus.class), any(Payment.class)))
                .thenReturn(PaymentFactory.createSurplusPayment());

        surplusMessageReceiver.receive(payload);

        verify(mapper, times(1))
                .readValue(payload, PaymentMessage.class);
        verify(updatePaymentStatusUseCase, times(1))
                .update(any(PaymentStatus.class), any(Payment.class));
    }

    @Test
    @DisplayName("Should throw ReceiveMessageErrorException when catch unexpected error.")
    void shouldThrowReceiveMessageErrorExceptionWhenCatchUnexpectedError() throws Exception {
        var message = PaymentMessageFactory.createSurplusPaymentMessage();
        var payload = mapper.writeValueAsString(message);

        when(mapper.readValue(payload, PaymentMessage.class))
                .thenThrow(new NullPointerException());

        var exception = assertThrows(
                ReceiveMessageErrorException.class,
                () -> surplusMessageReceiver.receive(payload)
        );

        assertNotNull(exception);
        assertThat(exception.getMessage())
                .contains("Error to receive message from the queue.");
        verify(mapper, times(1))
                .readValue(payload, PaymentMessage.class);
        verify(updatePaymentStatusUseCase, never())
                .update(any(PaymentStatus.class), any(Payment.class));
    }
}
