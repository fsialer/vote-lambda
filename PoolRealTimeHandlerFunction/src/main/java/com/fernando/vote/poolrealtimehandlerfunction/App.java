package com.fernando.vote.poolrealtimehandlerfunction;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.vote.poolrealtimehandlerfunction.models.PollId;
import com.fernando.vote.poolrealtimehandlerfunction.models.Vote;
import com.fernando.vote.poolrealtimehandlerfunction.services.ConnectionService;
import com.fernando.vote.poolrealtimehandlerfunction.services.PoolService;
import com.fernando.vote.poolrealtimehandlerfunction.services.impl.ConnectionServiceImpl;
import com.fernando.vote.poolrealtimehandlerfunction.services.impl.PoolServiceImpl;

import java.util.List;

/**
 * Hello world!
 */
public class App implements RequestHandler<SNSEvent, Void> {


    @Override
    public Void handleRequest(SNSEvent snsEvent, Context context) {
        String poolId = snsEvent.getRecords().get(0)
                .getSNS().getMessage();


        try{
            // obtener conexiones del pool
            ObjectMapper objectMapper = new ObjectMapper();
            PollId pollId1 =objectMapper.readValue(poolId, PollId.class);
            ConnectionService connectionService=new ConnectionServiceImpl();
            PoolService poolService=new PoolServiceImpl();
            List<String> connections = connectionService.findConnections(pollId1.getPollId());
            context.getLogger().log(connections.toString());
            List<Vote> votes = poolService.getVotesByPoolId(pollId1.getPollId());
            String voteStr=objectMapper.writeValueAsString(votes);
            connectionService.sendConnections(connections,voteStr);

        } catch (RuntimeException | JsonProcessingException e) {
            context.getLogger().log(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }
}
