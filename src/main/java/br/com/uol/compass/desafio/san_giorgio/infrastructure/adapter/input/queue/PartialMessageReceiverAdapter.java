package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.input.queue;

import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus;
import br.com.uol.compass.desafio.san_giorgio.application.port.input.UpdatePaymentStatusUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

import static br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentStatus.PARTIAL;

@Component
public final class PartialMessageReceiverAdapter extends GenericMessageReceiverAdapter {

    public PartialMessageReceiverAdapter(final ObjectMapper objectMapper, final UpdatePaymentStatusUseCase updatePaymentStatusUseCase) {
        super(objectMapper, updatePaymentStatusUseCase);
    }

    @Override
    @SqsListener(value = "${payment.message.queue.partial-payment}")
    public void receive(final String payload) {
        super.receive(payload);
    }

    @Override
    public PaymentStatus getPaymentStatus() {
        return PARTIAL;
    }
}
