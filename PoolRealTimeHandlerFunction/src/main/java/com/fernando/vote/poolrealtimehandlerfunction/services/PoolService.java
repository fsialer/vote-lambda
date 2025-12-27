package com.fernando.vote.poolrealtimehandlerfunction.services;

import com.fernando.vote.poolrealtimehandlerfunction.models.Vote;

import java.util.List;

public interface PoolService {
    List<Vote> getVotesByPoolId(String poolId);
}
