package com.fernando.vote.poolrealtimehandlerfunction.repository;

import com.fernando.vote.poolrealtimehandlerfunction.models.ConnectionClient;

import java.util.List;

public interface ConnectionRepository {
    List<ConnectionClient> getConnections(String poolId);

}
