package com.fernando.vote.poolget;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.vote.poolget.dto.Poll;
import com.fernando.vote.poolget.exceptions.PollNotFoundException;
import com.fernando.vote.poolget.services.IPoolService;
import com.fernando.vote.poolget.services.impl.IPoolServiceImpl;

import java.util.Map;

/**
 * Hello world!
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Map<String, String> COMMON_HEADERS = Map.of(
            "Content-Type", "application/json",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
            "Access-Control-Allow-Methods", "OPTIONS,GET"
    );
    private final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        Map<String, String> pathParams = apiGatewayProxyRequestEvent.getPathParameters();

        if (pathParams == null || !pathParams.containsKey("pollId")) {
            throw new RuntimeException("Missing path parameter 'pollId'");
        }
        String poolId = pathParams.get("pollId");

        try {
            IPoolService poolService=new IPoolServiceImpl();
            Poll resp= poolService.getPoolById(poolId);
            return buildResponse(200,resp);
        } catch (Exception e) {
            context.getLogger().log(e.getMessage());
            if(e instanceof PollNotFoundException){
                return buildResponse(404,Map.of("error",e.getMessage()));
            }
            return buildResponse(500,Map.of("error",e.getMessage()));
        }

    }

    private APIGatewayProxyResponseEvent buildResponse(int statusCode, Object body) {
        try {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(statusCode)
                    .withHeaders(COMMON_HEADERS)
                    .withBody(objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }
}
