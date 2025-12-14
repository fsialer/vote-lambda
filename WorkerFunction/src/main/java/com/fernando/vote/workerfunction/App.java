package com.fernando.vote.workerfunction;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
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
        Set<String> pools=new HashSet<>();
        for (SQSEvent.SQSMessage message : sqsEvent.getRecords()) {
            pools.add(message.getBody());
            System.out.println("Received message: " + message.getBody());
        }
        VoteService voteService=new VoteServiceImpl();
        voteService.syncVote(pools);

        return "Processing complete";
    }
}
