package com.fernando.vote.disconnecthandlerfunction.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import lombok.Getter;

public class DynamoConfig {
    @Getter
    private static AmazonDynamoDB amazonDynamoDB;

    static {
        String region=System.getenv("REGION");
        String localstackConnection=System.getenv("LOCALSTACK_CONNECTION");
        String accessKey=System.getenv("ACCESS_KEY");
        String secretKey=System.getenv("SECRET_KEY");
        if(localstackConnection==null || localstackConnection.isEmpty()){
            amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(Regions.valueOf(region))
                    .build();
        }else{
            amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(localstackConnection,region)
                    )
                    .withCredentials(
                            new AWSStaticCredentialsProvider(
                                    new BasicAWSCredentials(accessKey, secretKey)
                            )
                    )
                    .build();
        }
    }

}
