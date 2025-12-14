package com.fernando.vote.connecthandlerfunction.events;

public interface WebSocketRepository {
    void postConnection(String connectionId,String payload);
}
