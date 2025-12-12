package votecreate.event.impl;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import votecreate.config.SqsConfig;
import votecreate.event.QueueRepository;

public class QueueRepositoryImpl implements QueueRepository {
    private static final String QUEUE_URL = System.getenv("QUEUE_URL");
    private static final SqsClient client = SqsConfig.getSqsClient();


    @Override
    public void sendMessage(String message) {
        SendMessageRequest request=SendMessageRequest.builder()
                    .queueUrl(QUEUE_URL)
                    .messageBody(message)
                    .build();
        client.sendMessage(request);
    }
}
