package com.fernando.vote.disconnecthandlerfunction.mapper;


import com.fernando.vote.disconnecthandlerfunction.models.ConnectionClient;

public class ConnectionMapper {
    public ConnectionClient connectIdPoolIdToConnectionClient(String connectionId, String poolId){
        return ConnectionClient.builder()
                .connectionId(connectionId)
                .poolId(poolId)
                .build();
    }
}
