package com.fernando.vote.connecthandlerfunction;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fernando.vote.connecthandlerfunction.mapper.ConnectionMapper;
import com.fernando.vote.connecthandlerfunction.services.ConnectionService;
import com.fernando.vote.connecthandlerfunction.services.impl.ConnectionServiceImpl;

import java.util.Map;

/**
 * Hello world!
 */
public class App implements RequestHandler<Map<String,Object>,Void> {

    @Override
    public Void handleRequest(Map<String, Object> event, Context context) {
        String connectionId = (String)
                ((Map)event.get("requestContext")).get("connectionId");

        String poolId = (String)
                ((Map)event.get("queryStringParameters")).get("poolId");

        ConnectionService connectionService=new ConnectionServiceImpl();
        ConnectionMapper connectionMapper=new ConnectionMapper();
        connectionService.registerConnection(connectionMapper.connectIdPoolIdToConnectionClient(connectionId,poolId));

        return null;
    }
}
