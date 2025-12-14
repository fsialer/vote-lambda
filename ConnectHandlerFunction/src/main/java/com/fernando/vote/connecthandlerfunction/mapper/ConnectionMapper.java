package com.fernando.vote.connecthandlerfunction.mapper;

import com.fernando.vote.connecthandlerfunction.models.ConnectionClient;

public class ConnectionMapper {
    public ConnectionClient connectIdPoolIdToConnectionClient(String connectionId,String poolId){
        return ConnectionClient.builder()
                .connectionId(connectionId)
                .poolId(poolId)
                .build();
    }
}
