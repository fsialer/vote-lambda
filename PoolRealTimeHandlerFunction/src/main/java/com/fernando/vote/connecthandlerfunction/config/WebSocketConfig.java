package com.fernando.vote.connecthandlerfunction.config;

import lombok.Getter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;

import java.net.URI;

public class WebSocketConfig {
    @Getter
    private static ApiGatewayManagementApiClient apiGatewayManagementApiClient;
    static{
        String region=System.getenv("REGION");
        String localstackConnection=System.getenv("LOCALSTACK_CONNECTION");
        String websocketUrl=System.getenv("AWS_WEBSOCKET_URL");
        String accessKey=System.getenv("ACCESS_KEY");
        String secretKey=System.getenv("SECRET_KEY");
        if(localstackConnection==null || localstackConnection.isEmpty()){
            apiGatewayManagementApiClient= ApiGatewayManagementApiClient.builder()
                    .endpointOverride(URI.create(
                            websocketUrl
                    ))
                    .region(Region.of(region))
                    .build();
        }else{
            apiGatewayManagementApiClient= ApiGatewayManagementApiClient.builder()
                    .endpointOverride(URI.create(
                            localstackConnection
                    ))
                    .region(Region.of(region))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(accessKey, secretKey)
                            )
                    )
                    .build();
        }
    }
}
