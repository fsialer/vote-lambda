package votecreate.event.impl;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import votecreate.config.SnsConfig;
import votecreate.event.SnsRepository;

public class SnsRepositoryImpl implements SnsRepository {
    private final SnsClient client;
    private static final String TOPIC_ARN = System.getenv("TOPIC_ARN");
    public SnsRepositoryImpl(){
        client = SnsConfig.getClient();
    }
    @Override
    public void sendMessage(String message) {
        client.publish(PublishRequest.builder()
                .topicArn(TOPIC_ARN)
                .message("""
              {
                "poolId": "%s"
              }
            """.formatted(message))
                .build());
    }
}
