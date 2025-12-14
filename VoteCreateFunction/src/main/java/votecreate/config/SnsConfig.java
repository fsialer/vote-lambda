package votecreate.config;

import lombok.Getter;
import redis.clients.jedis.Jedis;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;

public class SnsConfig {

    private static SnsClient snsClient;

    private static void init() {
        String localstackConnection = System.getenv("LOCALSTACK_CONNECTION");
        String region = System.getenv("REGION");                // EJ: us-east-1
        String accessKey = System.getenv("ACCESS_KEY");  // EJ: test
        String secretKey = System.getenv("SECRET_KEY"); // EJ: test
        if(localstackConnection==null || localstackConnection.isEmpty()){
            snsClient=SnsClient.builder()
                    .region(Region.of(region))
                    .build();
        }else{
            snsClient=SnsClient.builder()
                    .endpointOverride(URI.create(localstackConnection))
                    .region(Region.of(region))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(accessKey, secretKey)
                            )
                    )
                    .build();
        }

    }

    public static SnsClient getClient(){
        if (snsClient == null) {
            init();
        }
        return snsClient;
    }

}
