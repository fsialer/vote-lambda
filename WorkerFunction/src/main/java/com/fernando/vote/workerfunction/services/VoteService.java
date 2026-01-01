package com.fernando.vote.workerfunction.services;

import com.fernando.vote.workerfunction.models.PollId;

import java.util.Set;

public interface VoteService {
    void syncVote(Set<PollId> pools);
}
