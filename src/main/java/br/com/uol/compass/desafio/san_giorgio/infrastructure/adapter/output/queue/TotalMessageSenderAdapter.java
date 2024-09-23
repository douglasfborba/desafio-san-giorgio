package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Getter
@Component("total")
public final class TotalMessageSenderAdapter extends GenericMessageSenderAdapter {

    @Value("${payment.message.queue.total-payment}")
    private String queueName;

    public TotalMessageSenderAdapter(final ObjectMapper objectMapper, final SqsAsyncClient sqsAsyncClient) {
        super(objectMapper, sqsAsyncClient);
    }
}
