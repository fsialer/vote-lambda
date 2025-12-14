package com.fernando.vote.connecthandlerfunction.services.impl;

import com.fernando.vote.connecthandlerfunction.events.WebSocketRepository;
import com.fernando.vote.connecthandlerfunction.events.impl.WebSocketRepositoryImpl;
import com.fernando.vote.connecthandlerfunction.models.ConnectionClient;
import com.fernando.vote.connecthandlerfunction.repository.ConnectionRepository;
import com.fernando.vote.connecthandlerfunction.repository.impl.ConnectionRepositoryImpl;
import com.fernando.vote.connecthandlerfunction.services.ConnectionService;

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
            webSocketRepository.postConnection(conn,result);
        }
    }
}
