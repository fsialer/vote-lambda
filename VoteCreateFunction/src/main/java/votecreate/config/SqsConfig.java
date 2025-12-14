package votecreate.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

public class SqsConfig {
    private static SqsClient sqsClient;

    private static void init() {
        String localstackConnection = System.getenv("LOCALSTACK_CONNECTION");
        String region = System.getenv("REGION");                // EJ: us-east-1
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");  // EJ: test
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY"); // EJ: test

        if(localstackConnection==null|| localstackConnection.isEmpty()){
            sqsClient = SqsClient.builder()
                    .region(Region.of(region))
                    .build();
        }else{
            sqsClient = SqsClient.builder()
                    .region(Region.of(region != null ? region : "us-east-1"))
                    .endpointOverride(URI.create(
                            localstackConnection
                    ))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(
                                            accessKey ,
                                            secretKey
                                    )
                            )
                    )
                    .build();
        }
    }

    public static SqsClient getClient(){
        if (sqsClient == null) {
            init();
        }
        return sqsClient;
    }

}
