package com.fernando.vote.poolrealtimehandlerfunction.events;

public interface WebSocketRepository {
    void postConnection(String connectionId,String payload);
}
