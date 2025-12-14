package com.fernando.vote.poolget.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import lombok.Getter;

public class DynamoConfig {
    @Getter
    private static AmazonDynamoDB amazonDynamoDB;

    static {
        String region=System.getenv("REGION");
        String connectionUrl=System.getenv("CONNECTION_URL");
        String accessKey=System.getenv("ACCESS_KEY");
        String secretKey=System.getenv("SECRET_KEY");
        amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(connectionUrl,region)
                )
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(accessKey, secretKey)
                        )
                )
                //.withRegion(region)
                .build();
    }

}
