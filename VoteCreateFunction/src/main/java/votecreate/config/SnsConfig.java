package votecreate.config;

import lombok.Getter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;

public class SnsConfig {
    @Getter
    private static final SnsClient snsClient;

    static {
        String snsEndpoint = System.getenv("SNS_ENDPOINT");     // EJ: http://localhost:4566
        String region = System.getenv("REGION");                // EJ: us-east-1
        String accessKey = System.getenv("AWS_SNS_ACCESS_KEY_ID");  // EJ: test
        String secretKey = System.getenv("AWS_SNS_SECRET_ACCESS_KEY"); // EJ: test

        snsClient=SnsClient.builder()
                .endpointOverride(URI.create(snsEndpoint))
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }
}
