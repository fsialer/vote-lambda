package com.fernando.vote.poolrealtimehandlerfunction.services;

import java.util.List;

public interface ConnectionService {
    List<String> findConnections(String poolId);
    void sendConnections(List<String> connectios,String result);
}
