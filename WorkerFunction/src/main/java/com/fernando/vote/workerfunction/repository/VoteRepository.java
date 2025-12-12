package com.fernando.vote.workerfunction.repository;

import com.fernando.vote.workerfunction.models.PoolId;

import java.util.Map;

public interface VoteRepository {
    void batchVote(Map<String,Map<String,String>> votes);
}
