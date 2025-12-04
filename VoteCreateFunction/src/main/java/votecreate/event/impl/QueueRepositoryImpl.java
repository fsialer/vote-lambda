package votecreate.event.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import votecreate.event.QueueRepository;
import votecreate.models.OptionEvent;

import java.net.URI;

public class QueueRepositoryImpl implements QueueRepository {
    private static final SqsClient sqs = SqsClient.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create(System.getenv().getOrDefault(
                    "SQS_ENDPOINT",
                    "http://localstack:4566"   // si corre dentro de Docker
            )))
            .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                            System.getenv().getOrDefault("AWS_ACCESS_KEY_ID", "test"),
                            System.getenv().getOrDefault("AWS_SECRET_ACCESS_KEY", "test")
                    )
            ))
            .build();

    private static final String QUEUE_URL = System.getenv("QUEUE_URL");

    private ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public void sendMessage(OptionEvent event) {
        try{
            SendMessageRequest request=SendMessageRequest.builder()
                    .queueUrl(QUEUE_URL)
                    .messageBody(objectMapper.writeValueAsString(event))
                    .build();
            sqs.sendMessage(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
