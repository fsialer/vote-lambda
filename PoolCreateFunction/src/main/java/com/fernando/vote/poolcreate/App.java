package com.fernando.vote.poolcreate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.vote.poolcreate.mapper.PoolMapper;
import com.fernando.vote.poolcreate.models.Pool;
import com.fernando.vote.poolcreate.models.PoolRequest;
import com.fernando.vote.poolcreate.services.IPoolService;
import com.fernando.vote.poolcreate.services.impl.IPoolServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final ObjectMapper objectMapper=new ObjectMapper();
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final IPoolService iPoolService;

    public App() {
        this.iPoolService = createVoteService();
    }

    protected IPoolService createVoteService() {
        return new IPoolServiceImpl();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        try {
            // Deserializar JSON a objeto VoteEvent
            String body=apiGatewayProxyRequestEvent.getBody(); // Aqui obtenemos el cuerpo de la solicitud
            PoolRequest rq=objectMapper.readValue(body, PoolRequest.class);
            //Validar campos
            Set<ConstraintViolation<PoolRequest>> violations = validator.validate(rq);
            if(!violations.isEmpty()){
                String errors=violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(","));
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(400)
                        .withHeaders(headers)
                        .withBody("{\"error\": \""+errors+"\"}");
            }
            // Map para DynamoDB
            PoolMapper mapper=new PoolMapper();
            Pool resp= iPoolService.createPool(mapper.poolRequestToPool(rq));
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(201)
                    .withHeaders(Map.of("Content-Type","application/json",
                            "Access-Control-Allow-Origin", "*",
                            "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                            "Access-Control-Allow-Methods", "OPTIONS,POST"))
                    .withBody(objectMapper.writeValueAsString(resp));

        } catch (JsonProcessingException|RuntimeException e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withHeaders(Map.of("Content-Type", "application/json",
                            "Access-Control-Allow-Origin", "*",
                            "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
                            "Access-Control-Allow-Methods", "OPTIONS,POST"))
                    .withBody("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
