package com.fernando.vote.poolget.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.vote.poolget.config.DynamoConfig;
import com.fernando.vote.poolget.models.Pool;
import com.fernando.vote.poolget.repository.CacheRepository;
import com.fernando.vote.poolget.repository.PoolRepository;
import com.fernando.vote.poolget.repository.impl.CacheRepositoryImpl;
import com.fernando.vote.poolget.repository.impl.PoolRepositoryImpl;
import com.fernando.vote.poolget.services.IPoolService;

import java.util.NoSuchElementException;

public class IPoolServiceImpl implements IPoolService {
    private final PoolRepository poolRepository;
    private final CacheRepository cacheRepository;
    public IPoolServiceImpl(){
        poolRepository=new PoolRepositoryImpl();
        cacheRepository=new CacheRepositoryImpl();
    }
    @Override
    public Pool getPoolById(String id) throws JsonProcessingException {
        String key="pool:"+id;
        Pool pool=null;
        ObjectMapper obj=new ObjectMapper();

        if(Boolean.TRUE.equals(cacheRepository.existsKey(key))){
            String strPool=cacheRepository.getSet(key);
            pool=obj.readValue(strPool,Pool.class);
       }else{
            pool=poolRepository.getPoolByIdWithDetails(id);
            cacheRepository.createSet(key,obj.writeValueAsString(pool),3600);
        }
        return pool;
    }
}
