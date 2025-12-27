package com.fernando.vote.poolget;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.vote.poolget.models.Pool;
import com.fernando.vote.poolget.services.IPoolService;
import com.fernando.vote.poolget.services.impl.IPoolServiceImpl;

import java.util.Map;

/**
 * Hello world!
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        // Capturar el parámetro de ruta "id" desde pathParameters
        Map<String, String> pathParams = apiGatewayProxyRequestEvent.getPathParameters();

        if (pathParams == null || !pathParams.containsKey("id")) {
            throw new RuntimeException("Missing path parameter 'id'");
        }
        String poolId = pathParams.get("id");

        try {
             IPoolService poolService=new IPoolServiceImpl();
             //poolService.getPoolById(poolId);
            Pool resp= poolService.getPoolById(poolId);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withHeaders(Map.of("Content-Type","application/json",
                            "Access-Control-Allow-Origin", "*",
                            "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                            "Access-Control-Allow-Methods", "OPTIONS,GET"))
                    .withBody(objectMapper.writeValueAsString(resp));
        } catch (Exception e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withHeaders(Map.of("Content-Type", "application/json",
                            "Access-Control-Allow-Origin", "*",
                            "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                            "Access-Control-Allow-Methods", "OPTIONS,GET"))
                    .withBody("{\"error\": \"" + e.getMessage() + "\"}");
        }

    }
}
