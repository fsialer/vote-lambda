package com.fernando.vote.disconnecthandlerfunction.services.impl;


import com.fernando.vote.disconnecthandlerfunction.models.ConnectionClient;
import com.fernando.vote.disconnecthandlerfunction.repository.ConnectionRepository;
import com.fernando.vote.disconnecthandlerfunction.repository.impl.ConnectionRepositoryImpl;
import com.fernando.vote.disconnecthandlerfunction.services.ConnectionService;

public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionRepository connectionRepository;

    public ConnectionServiceImpl(){
        connectionRepository=new ConnectionRepositoryImpl();
    }
    @Override
    public void deleteConnection(String connectionId) {
        connectionRepository.deleteConnectionByConnectionId(connectionId);
    }
}
