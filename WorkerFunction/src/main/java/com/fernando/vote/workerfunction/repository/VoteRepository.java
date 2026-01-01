package com.fernando.vote.workerfunction.repository;

import java.util.Map;

public interface VoteRepository {
    void batchVote(Map<String,Map<String,String>> votes);
}
