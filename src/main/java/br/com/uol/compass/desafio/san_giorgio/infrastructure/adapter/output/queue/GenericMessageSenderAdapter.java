package br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue;

import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.exception.SendMessageErrorException;
import br.com.uol.compass.desafio.san_giorgio.application.domain.model.PaymentItem;
import br.com.uol.compass.desafio.san_giorgio.application.port.output.SendMessageAdapter;
import br.com.uol.compass.desafio.san_giorgio.infrastructure.adapter.output.queue.message.PaymentMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public abstract class GenericMessageSenderAdapter implements SendMessageAdapter {

    protected final ObjectMapper mapper;
    protected final SqsAsyncClient sqsAsyncClient;

    @Override
    public void send(final PaymentItem domain) {
        log.info("Sending payload {} to queue {}", domain, getQueueName());
        try {
            var message = PaymentMessage.fromDomain(domain);
            var result = sqsAsyncClient.sendMessage(
                    SendMessageRequest.builder()
                        .queueUrl(getQueueName())
                        .messageBody(mapper.writeValueAsString(message))
                        .build()
            );
            log.info("Payload sent to queue successfully {}.", result);
        } catch (Exception e) {
            log.error("Error to send message to queue.");
            throw new SendMessageErrorException();
        }
    }
}
