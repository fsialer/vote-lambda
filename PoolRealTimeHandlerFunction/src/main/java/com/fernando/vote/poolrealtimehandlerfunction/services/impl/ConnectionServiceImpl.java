package com.fernando.vote.poolrealtimehandlerfunction.services.impl;

import com.fernando.vote.poolrealtimehandlerfunction.events.WebSocketRepository;
import com.fernando.vote.poolrealtimehandlerfunction.events.impl.WebSocketRepositoryImpl;
import com.fernando.vote.poolrealtimehandlerfunction.models.ConnectionClient;
import com.fernando.vote.poolrealtimehandlerfunction.repository.ConnectionRepository;
import com.fernando.vote.poolrealtimehandlerfunction.repository.impl.ConnectionRepositoryImpl;
import com.fernando.vote.poolrealtimehandlerfunction.services.ConnectionService;

import java.util.List;

public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final WebSocketRepository webSocketRepository;

    public ConnectionServiceImpl(){
        connectionRepository=new ConnectionRepositoryImpl();
        webSocketRepository=new WebSocketRepositoryImpl();
    }
    @Override
    public List<String> findConnections(String poolId) {
        List<ConnectionClient> connectionClients=connectionRepository.getConnections(poolId);
        return connectionClients.stream().map(ConnectionClient::getConnectionId).toList();
    }

    @Override
    public void sendConnections(List<String> connections, String result) {
        for (String conn : connections) {
            System.out.println("Sending to connection: " + conn+" the result: "+result);
            webSocketRepository.postConnection(conn,result);
        }
    }
}
