package com.fernando.vote.poolrealtimehandlerfunction.config;

import lombok.Getter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
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
            String cleanUrl = websocketUrl.replace("wss://", "https://");

            // 2. IMPORTANTE: Quitar la barra final si existe
            if (cleanUrl.endsWith("/")) {
                cleanUrl = cleanUrl.substring(0, cleanUrl.length() - 1);
            }
            System.out.println("WebSocket URL limpia: " + cleanUrl);
            apiGatewayManagementApiClient= ApiGatewayManagementApiClient.builder()
                    .endpointOverride(URI.create(
                            cleanUrl
                    ))
                    .region(Region.of(region))
                    .credentialsProvider(DefaultCredentialsProvider.create())
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
