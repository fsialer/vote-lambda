package com.fernando.vote.poolcreate.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class DynamoConfig {
    private static AmazonDynamoDB amazonDynamoDB;

    private static void init() {
        String region = System.getenv("REGION");
        String localstackConnection = System.getenv("LOCALSTACK_CONNECTION");
        String accessKey=System.getenv("ACCESS_KEY");
        String secretKey=System.getenv("SECRET_KEY");

        if (localstackConnection == null || localstackConnection.isBlank()) {
            amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(Regions.valueOf(region))
                    .build();
        } else {
            amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(localstackConnection, region)
                    )
                    .withCredentials(
                            new AWSStaticCredentialsProvider(
                                    new BasicAWSCredentials(accessKey, secretKey)
                            )
                    )
                    .build();
        }
    }

    public static AmazonDynamoDB getClient() {
        if (amazonDynamoDB == null) {
            init();
        }
        return amazonDynamoDB;
    }

}
