package com.fernando.vote.poolcreate.mapper;

import com.fernando.vote.poolcreate.models.Option;
import com.fernando.vote.poolcreate.models.Pool;
import com.fernando.vote.poolcreate.models.PoolRequest;

import java.util.stream.Collectors;

public class PoolMapper {
    public Pool poolRequestToPool(PoolRequest poolRequest){
        return Pool.builder()
                .question(poolRequest.getQuestion())
                .options(poolRequest.getOptions().stream().map(option-> Option.builder().text(option).build()).collect(Collectors.toSet()))
                .build();
    }
}
