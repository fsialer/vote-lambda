package com.fernando.vote.connecthandlerfunction.events.impl;

import com.fernando.vote.connecthandlerfunction.config.WebSocketConfig;
import com.fernando.vote.connecthandlerfunction.events.WebSocketRepository;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.PostToConnectionRequest;

public class WebSocketRepositoryImpl implements WebSocketRepository {
    private final ApiGatewayManagementApiClient apiGw;

    public WebSocketRepositoryImpl(){
        apiGw=WebSocketConfig.getApiGatewayManagementApiClient();
    }

    @Override
    public void postConnection(String connectionId, String payload) {
        apiGw.postToConnection(PostToConnectionRequest.builder()
                .connectionId(connectionId)
                .data(SdkBytes.fromUtf8String(payload))
                .build());
    }
}
