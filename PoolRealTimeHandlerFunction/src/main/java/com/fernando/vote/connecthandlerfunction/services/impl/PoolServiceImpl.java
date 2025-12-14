package com.fernando.vote.connecthandlerfunction.services.impl;

import com.fernando.vote.connecthandlerfunction.repository.CacheRepository;
import com.fernando.vote.connecthandlerfunction.repository.impl.CacheRepositoryImpl;
import com.fernando.vote.connecthandlerfunction.services.PoolService;

public class PoolServiceImpl implements PoolService {
    private final CacheRepository cacheRepository;

    public PoolServiceImpl(){
        cacheRepository=new CacheRepositoryImpl();
    }

    @Override
    public String getVotesByPoolId(String poolId) {
        return cacheRepository.getHashSet(poolId).toString();
    }
}
