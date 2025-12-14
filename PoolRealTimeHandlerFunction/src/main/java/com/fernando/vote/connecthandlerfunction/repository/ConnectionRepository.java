package com.fernando.vote.connecthandlerfunction.repository;

import com.fernando.vote.connecthandlerfunction.models.ConnectionClient;

import java.util.List;

public interface ConnectionRepository {
    List<ConnectionClient> getConnections(String poolId);

}
