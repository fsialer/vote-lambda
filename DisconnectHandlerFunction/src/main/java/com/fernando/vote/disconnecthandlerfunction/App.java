package com.fernando.vote.disconnecthandlerfunction;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fernando.vote.disconnecthandlerfunction.services.ConnectionService;
import com.fernando.vote.disconnecthandlerfunction.services.impl.ConnectionServiceImpl;

import java.util.Map;

/**
 * Hello world!
 */
public class App implements RequestHandler<Map<String, Object>, Void> {

    private final ConnectionService connectionService =
            new ConnectionServiceImpl();
    @Override
    public Void handleRequest(Map<String, Object>  event, Context context) {
        String connectionId = (String)
                ((Map<?, ?>) event.get("requestContext"))
                        .get("connectionId");

        connectionService.deleteConnection(connectionId);
        System.out.println("Conexion cerrada: " + connectionId);
        return null;
    }


}
