package votecreate.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;

import java.net.URI;

public class EventBridgeConfig {
    private static EventBridgeClient client;

    private static void init() {
        String region = System.getenv("REGION");
        String localstackConnection = System.getenv("LOCALSTACK_CONNECTION");
        String accessKey = System.getenv("ACCESS_KEY");
        String secretKey = System.getenv("SECRET_KEY");

        if (localstackConnection == null || localstackConnection.isBlank()) {
            client = EventBridgeClient.builder()
                    .region(Region.of(region))
                    .build();
        } else {
            client = EventBridgeClient.builder()
                    .region(Region.of(region))
                    .endpointOverride(URI.create(localstackConnection))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)))
                    .build();
        }
    }

    public static EventBridgeClient getClient() {
        if (client == null) {
            init();
        }
        return client;
    }
}
