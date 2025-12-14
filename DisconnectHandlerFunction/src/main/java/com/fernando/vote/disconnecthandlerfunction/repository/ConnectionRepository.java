package com.fernando.vote.disconnecthandlerfunction.repository;


import com.fernando.vote.disconnecthandlerfunction.models.ConnectionClient;

public interface ConnectionRepository {
    void deleteConnectionByConnectionId(String connectionId);
}
