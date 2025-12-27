package com.fernando.vote.connecthandlerfunction;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fernando.vote.connecthandlerfunction.mapper.ConnectionMapper;
import com.fernando.vote.connecthandlerfunction.services.ConnectionService;
import com.fernando.vote.connecthandlerfunction.services.impl.ConnectionServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class App implements RequestHandler<Map<String,Object>,Map<String, Object>> {

    @Override
    public Map<String, Object>  handleRequest(Map<String, Object> event, Context context) {
        String connectionId = (String)
                ((Map)event.get("requestContext")).get("connectionId");

        String poolId = (String)
                ((Map)event.get("queryStringParameters")).get("poolId");

        ConnectionService connectionService=new ConnectionServiceImpl();
        ConnectionMapper connectionMapper=new ConnectionMapper();
        System.out.println("Registering connection: "+connectionId+" to pool: "+poolId);
        connectionService.registerConnection(connectionMapper.connectIdPoolIdToConnectionClient(connectionId,poolId));

        return createSuccessResponse("Connection successful!");
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 400);
        response.put("body", message);
        return response;
    }

    private Map<String, Object> createSuccessResponse(String message) {
        System.out.println("Creating success response");
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 200);
        response.put("body", message);
        return response;
    }
}
