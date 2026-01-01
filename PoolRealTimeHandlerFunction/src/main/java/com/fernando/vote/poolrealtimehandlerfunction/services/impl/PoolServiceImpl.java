package com.fernando.vote.poolrealtimehandlerfunction.services.impl;

import com.fernando.vote.poolrealtimehandlerfunction.models.Vote;
import com.fernando.vote.poolrealtimehandlerfunction.repository.CacheRepository;
import com.fernando.vote.poolrealtimehandlerfunction.repository.impl.CacheRepositoryImpl;
import com.fernando.vote.poolrealtimehandlerfunction.services.PoolService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoolServiceImpl implements PoolService {
    private final CacheRepository cacheRepository;

    public PoolServiceImpl(){
        cacheRepository=new CacheRepositoryImpl();
    }

    @Override
    public List<Vote> getVotesByPoolId(String pollId) {
        String key="votes:"+ pollId;
        Map<String,String> votes=cacheRepository.getHashSet(key);
        List<Vote> votesList=new ArrayList<>();
        votes.forEach((k,v)->{
            votesList.add(new Vote(k,Long.parseLong(v)));
        });
        return votesList;
    }
}
