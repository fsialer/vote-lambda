package com.fernando.vote.workerfunction;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.vote.workerfunction.models.PollId;
import com.fernando.vote.workerfunction.services.VoteService;
import com.fernando.vote.workerfunction.services.impl.VoteServiceImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Hello world!
 */
public class App implements RequestHandler<SQSEvent, String> {

    @Override
    public String handleRequest(SQSEvent sqsEvent, Context context) {
        // Iterar sobre cada mensaje recibido de SQS
        Set<PollId> pools=new HashSet<>();
        ObjectMapper objectMapper=new ObjectMapper();
        for (SQSEvent.SQSMessage message : sqsEvent.getRecords()) {
            try {
                pools.add(objectMapper.readValue(message.getBody(), PollId.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            context.getLogger().log("Received message: " + message.getBody());
        }
        VoteService voteService=new VoteServiceImpl();
        voteService.syncVote(pools);

        return "Processing complete";
    }
}
