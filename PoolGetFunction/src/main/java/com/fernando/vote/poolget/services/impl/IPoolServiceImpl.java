package com.fernando.vote.poolget.services.impl;

import com.fernando.vote.poolget.dto.Poll;
import com.fernando.vote.poolget.exceptions.PollNotFoundException;
import com.fernando.vote.poolget.mapper.PollMapper;
import com.fernando.vote.poolget.repository.CacheRepository;
import com.fernando.vote.poolget.repository.PoolRepository;
import com.fernando.vote.poolget.repository.impl.CacheRepositoryImpl;
import com.fernando.vote.poolget.repository.impl.PoolRepositoryImpl;
import com.fernando.vote.poolget.services.IPoolService;

public class IPoolServiceImpl implements IPoolService {
    private final PoolRepository poolRepository;
    private final CacheRepository cacheRepository;
    private final PollMapper pollMapper;

    public IPoolServiceImpl(){
        poolRepository=new PoolRepositoryImpl();
        cacheRepository=new CacheRepositoryImpl();
        pollMapper =new PollMapper();
    }

    @Override
    public Poll getPoolById(String id) {
        String key="pool:"+id;
        Poll poll =null;
        if(Boolean.TRUE.equals(cacheRepository.existsKey(key))){
            String strPool=cacheRepository.getSet(key);
            poll = pollMapper.stringToPoll(strPool);
       }else{
            poll =poolRepository.getPoolByIdWithDetails(id);
            if(poll==null){
                throw new PollNotFoundException("Poll not found: "+id);
            }
            cacheRepository.createSet(key,pollMapper.pollToString(poll),45);
        }
        return poll;
    }
}
