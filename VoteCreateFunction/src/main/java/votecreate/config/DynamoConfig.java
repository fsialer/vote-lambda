package com.fernando.vote.poolget.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

public class DynamoConfig {
    private static DynamoDbEnhancedClient enhancedClient;

    private static void init() {
        String region = System.getenv("REGION");
        String localstackConnection = System.getenv("LOCALSTACK_CONNECTION");
        String accessKey = System.getenv("ACCESS_KEY");
        String secretKey = System.getenv("SECRET_KEY");

        if (localstackConnection == null || localstackConnection.isBlank()) {
            DynamoDbClient standardClient = DynamoDbClient.builder()
                    .region(Region.of(region))
                    .build();
            enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(standardClient).build();
        } else {
            DynamoDbClient standardClient = DynamoDbClient.builder()
                    .region(Region.of(region))
                    .endpointOverride(URI.create(localstackConnection))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)))
                    .build();
            enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(standardClient).build();
        }
    }

    public static DynamoDbEnhancedClient getClient() {
        if (enhancedClient == null) {
            init();
        }
        return enhancedClient;
    }
}