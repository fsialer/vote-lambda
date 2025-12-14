package com.fernando.vote.connecthandlerfunction.services.impl;

import com.fernando.vote.connecthandlerfunction.models.ConnectionClient;
import com.fernando.vote.connecthandlerfunction.repository.ConnectionRepository;
import com.fernando.vote.connecthandlerfunction.repository.impl.ConnectionRepositoryImpl;
import com.fernando.vote.connecthandlerfunction.services.ConnectionService;

public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionRepository connectionRepository;

    public ConnectionServiceImpl(){
        connectionRepository=new ConnectionRepositoryImpl();
    }
    @Override
    public void registerConnection(ConnectionClient connectionClient) {
        connectionRepository.saveConnection(connectionClient);
    }
}
