package votecreate.event.impl;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import votecreate.config.SnsConfig;
import votecreate.event.SnsRepository;

public class SnsRepositoryImpl implements SnsRepository {
    private static final SnsClient client = SnsConfig.getSnsClient();
    private static final String TOPIC_ARN = System.getenv("TOPIC_ARN");
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
