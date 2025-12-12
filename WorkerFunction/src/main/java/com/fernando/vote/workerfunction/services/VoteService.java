package com.fernando.vote.workerfunction.services;

import java.util.Set;

public interface VoteService {
    void syncVote(Set<String> pools);
}
