package com.fernando.vote.poolrealtimehandlerfunction.events.impl;

import com.fernando.vote.poolrealtimehandlerfunction.config.WebSocketConfig;
import com.fernando.vote.poolrealtimehandlerfunction.events.WebSocketRepository;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.ApiGatewayManagementApiException;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.GoneException;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.PostToConnectionRequest;

public class WebSocketRepositoryImpl implements WebSocketRepository {
    private final ApiGatewayManagementApiClient apiGw;

    public WebSocketRepositoryImpl(){
        apiGw=WebSocketConfig.getApiGatewayManagementApiClient();
    }

    @Override
    public void postConnection(String connectionId, String payload) {
        try {
            apiGw.postToConnection(PostToConnectionRequest.builder()
                    .connectionId(connectionId)
                    .data(SdkBytes.fromUtf8String(payload))
                    .build());
            System.out.println("Mensaje enviado con éxito a: " + connectionId);
        } catch (GoneException e) {
            // El usuario ya no está conectado. Aquí deberías borrar el ID de tu base de datos (DynamoDB)
            System.err.println("La conexión " + connectionId + " ya no existe (410 Gone).");
        } catch (ApiGatewayManagementApiException e) {
            // Error de permisos (403) o de formato
            System.err.println("Error de AWS API Gateway: " + e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
//        apiGw.postToConnection(PostToConnectionRequest.builder()
//                .connectionId(connectionId)
//                .data(SdkBytes.fromUtf8String(payload))
//                .build());
    }
}
