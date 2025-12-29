package com.fernando.vote.poolcreate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.vote.poolcreate.mapper.PollMapper;
import com.fernando.vote.poolcreate.models.Poll;
import com.fernando.vote.poolcreate.dto.PollRequest;
import com.fernando.vote.poolcreate.services.IPoolService;
import com.fernando.vote.poolcreate.services.impl.IPoolServiceImpl;

import java.util.Map;


/**
 * Hello world!
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Map<String, String> COMMON_HEADERS = Map.of(
            "Content-Type", "application/json",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
            "Access-Control-Allow-Methods", "OPTIONS,POST"
    );

    private final ObjectMapper objectMapper;
    private final IPoolService iPoolService;
    private final PollMapper mapper;

    public App() {
        this.iPoolService = new IPoolServiceImpl();
        this.objectMapper=new ObjectMapper();
        this.mapper=new PollMapper();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        try {
            if (apiGatewayProxyRequestEvent.getBody() == null || apiGatewayProxyRequestEvent.getBody().isEmpty()) {
                return buildResponse(400, Map.of("error", "Request body is empty"));
            }
            String body=apiGatewayProxyRequestEvent.getBody();
            PollRequest rq=mapper.bodyToPollRequest(body);
            Poll resp= iPoolService.createPool(mapper.poolRequestToPool(rq));
            return buildResponse(201,resp);

        } catch (RuntimeException|JsonProcessingException e) {
            context.getLogger().log(e.getMessage());
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
