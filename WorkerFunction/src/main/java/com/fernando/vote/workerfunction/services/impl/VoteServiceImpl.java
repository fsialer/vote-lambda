package com.fernando.vote.workerfunction.services.impl;

import com.fernando.vote.workerfunction.models.PollId;
import com.fernando.vote.workerfunction.repository.CacheRepository;
import com.fernando.vote.workerfunction.repository.VoteRepository;
import com.fernando.vote.workerfunction.repository.impl.CacheRepositoryImpl;
import com.fernando.vote.workerfunction.repository.impl.VoteRepositoryImpl;
import com.fernando.vote.workerfunction.services.VoteService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VoteServiceImpl implements VoteService {
    private final CacheRepository  cacheRepository;
    private final VoteRepository voteRepository;

    public VoteServiceImpl(){
        cacheRepository=new CacheRepositoryImpl();
        voteRepository=new VoteRepositoryImpl();
    }

    @Override
    public void syncVote(Set<PollId> pools) {
        Map<String,Map<String,String>> votes=new HashMap<>();
        pools.forEach(pool->{
            var voteHash=cacheRepository.getHashSet("votes:"+pool.getPollId());
            votes.put(pool.getPollId(),voteHash);
        });
        voteRepository.batchVote(votes);
    }
}
