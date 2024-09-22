package br.com.uol.compass.desafio.san_giorgio.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.endpoints.Endpoint;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.endpoints.SqsEndpointProvider;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Configuration
public class AWSClientConfig {
    @Value("${amazon.aws.region}")
    private String region;

    @Value("${amazon.aws.sqs.endpoint}")
    private String endpoint;

    @Value("${amazon.aws.credentials.access-key}")
    private String accessKeyId;

    @Value("${amazon.aws.credentials.secret-key}")
    private String secretAccessKey;

    @Value("${amazon.aws.credentials.session-token}")
    private String sessionToken;

    @Bean ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SqsTemplate sqsTemplate() {
        return SqsTemplate.newTemplate(getAsyncClient());
    }

    @Bean
    public SqsAsyncClient getAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.of(region))
                .endpointProvider(getEndpointProvider())
                .credentialsProvider(getCredentialsProvider())
                .build();
    }

    @Bean
    public SqsEndpointProvider getEndpointProvider() {
        var sqsEndpoint = Endpoint.builder()
                .url(URI.create(endpoint))
                .build();
        return endpointParams -> CompletableFuture.completedFuture(sqsEndpoint);
    }

    @Bean
    public StaticCredentialsProvider getCredentialsProvider() {
        var awsCredentials = AwsSessionCredentials.create(accessKeyId, secretAccessKey, sessionToken);
        return StaticCredentialsProvider.create(awsCredentials);
    }
}
