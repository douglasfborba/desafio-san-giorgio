package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Getter
@Component("partial")
public final class PartialMessageSenderAdapter extends GenericMessageSenderAdapter {

    @Value("${payment.message.queue.partial-payment}")
    private String queueName;

    public PartialMessageSenderAdapter(final ObjectMapper objectMapper, final SqsAsyncClient sqsAsyncClient) {
        super(objectMapper, sqsAsyncClient);
    }
}
