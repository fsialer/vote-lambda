package com.fernando.vote.connecthandlerfunction;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fernando.vote.connecthandlerfunction.config.WebSocketConfig;
import com.fernando.vote.connecthandlerfunction.services.ConnectionService;
import com.fernando.vote.connecthandlerfunction.services.PoolService;
import com.fernando.vote.connecthandlerfunction.services.impl.ConnectionServiceImpl;
import com.fernando.vote.connecthandlerfunction.services.impl.PoolServiceImpl;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.GoneException;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.PostToConnectionRequest;

import java.util.List;

/**
 * Hello world!
 */
public class App implements RequestHandler<SNSEvent, Void> {


    @Override
    public Void handleRequest(SNSEvent snsEvent, Context context) {
        String poolId = snsEvent.getRecords().get(0)
                .getSNS().getMessage();
        try{
            // obtener conexiones del pool
            ConnectionService connectionService=new ConnectionServiceImpl();
            PoolService poolService=new PoolServiceImpl();
            List<String> connections = connectionService.findConnections(poolId);

            String resultJson = poolService.getVotesByPoolId(poolId);

            connectionService.sendConnections(connections,resultJson);

        } catch (RuntimeException e) {
            context.getLogger().log(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }
}
