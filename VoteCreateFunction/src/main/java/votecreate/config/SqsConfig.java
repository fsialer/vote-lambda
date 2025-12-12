package votecreate.config;

import lombok.Getter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

public class SqsConfig {
    @Getter
    private static final SqsClient sqsClient;
    static {
        String sqsEndpoint = System.getenv("SQS_ENDPOINT");     // EJ: http://localhost:4566
        String region = System.getenv("REGION");                // EJ: us-east-1
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");  // EJ: test
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY"); // EJ: test

        sqsClient = SqsClient.builder()
                .region(Region.of(region != null ? region : "us-east-1"))
                .endpointOverride(URI.create(
                        sqsEndpoint != null ?
                                sqsEndpoint :
                                "http://localstack:4566"
                ))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        accessKey != null ? accessKey : "test",
                                        secretKey != null ? secretKey : "test"
                                )
                        )
                )
                .build();

        System.out.println("✔ SqsClient inicializado (singleton)");
        System.out.println("Endpoint: " + sqsEndpoint);
    }
}
