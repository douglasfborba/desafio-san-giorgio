package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue;

import br.com.uol.compass.desafio.san_giorgio.application.factory.model.PaymentItemFactory;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.exception.SendMessageErrorException;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.message.PaymentMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@ExtendWith(SpringExtension.class)
class PartialMessageSenderAdapterTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private SqsAsyncClient sqsAsyncClient;

    @InjectMocks
    private PartialMessageSenderAdapter partialMessageSender;

    @Test
    @DisplayName("Should send payment message successfully.")
    void shouldSendPaymentMessageSuccessfully() throws Exception {
        var item = PaymentItemFactory.createPartialPaymentItem();
        var message = mapper.writeValueAsString(item);

        when(mapper.writeValueAsString(any(PaymentMessage.class)))
                .thenReturn(message);

        when(sqsAsyncClient.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(any());

        partialMessageSender.send(item);

        verify(mapper, times(1))
                .writeValueAsString(any(PaymentMessage.class));
        verify(sqsAsyncClient, times(1))
                .sendMessage(any(SendMessageRequest.class));
    }

    @Test
    @DisplayName("Should throw SendMessageErrorException when catch unexpected error.")
    void shouldThrowSendMessageErrorExceptionWhenCachUnexpectedError() throws Exception {
        var item = PaymentItemFactory.createPartialPaymentItem();

        when(mapper.writeValueAsString(any(PaymentMessage.class)))
                .thenThrow(new NullPointerException());

        var exception = assertThrows(
                SendMessageErrorException.class,
                () -> partialMessageSender.send(item)
        );

        verify(mapper, times(1))
                .writeValueAsString(any(PaymentMessage.class));
        verify(sqsAsyncClient, never())
                .sendMessage(any(SendMessageRequest.class));

        assertThat(exception).isNotNull();
        assertThat(exception.getStatus()).isEqualTo(PRECONDITION_FAILED);
        assertThat(exception.getMessage()).contains("Error to send message to the queue.");
    }
}
