package br.com.uol.compass.desafio.san_giorgio.application.service;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;
import br.com.uol.compass.desafio.san_giorgio.application.domain.service.PaymentMessageSenderService;
import br.com.uol.compass.desafio.san_giorgio.application.factory.model.PaymentItemFactory;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.SendMessageAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.PartialMessageSenderAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.SurplusMessageSenderAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.TotalMessageSenderAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentMessageSenderServiceTest {

    @Mock
    private PartialMessageSenderAdapter partialMessageSenderAdapter;

    @Mock
    private TotalMessageSenderAdapter totalMessageSenderAdapter;

    @Mock
    private SurplusMessageSenderAdapter surplusMessageSenderAdapter;

    @Mock
    private Map<String, SendMessageAdapter> sendMessageAdapters;

    @InjectMocks
    private PaymentMessageSenderService paymentMessageSenderService;

    @ParameterizedTest
    @EnumSource(PaymentStatus.class)
    @DisplayName("Should send payments successfully.")
    void shouldSendPaymentsSuccessfully(final PaymentStatus status) {
        var senderAdapter = switch(status) {
            case PARTIAL -> partialMessageSenderAdapter;
            case TOTAL -> totalMessageSenderAdapter;
            default -> surplusMessageSenderAdapter;
        };

        doNothing()
                .when(senderAdapter)
                .send(any(PaymentItem.class));

        when(sendMessageAdapters.get(status.name().toLowerCase()))
                .thenReturn(senderAdapter);

        var item = switch(status) {
            case PARTIAL -> PaymentItemFactory.createPartialPaymentItem();
            case TOTAL -> PaymentItemFactory.createTotalPaymentItem();
            default -> PaymentItemFactory.createSurplusPaymentItem();
        };

        paymentMessageSenderService.send(status, item);

        verify(senderAdapter, times(1))
                .send(any(PaymentItem.class));
        verify(sendMessageAdapters, times(1))
                .get(status.name().toLowerCase());
    }
}
