package com.fernando.vote.connecthandlerfunction.repository;


import com.fernando.vote.connecthandlerfunction.models.ConnectionClient;

public interface ConnectionRepository {
    void saveConnection(ConnectionClient connectionClient);
}
