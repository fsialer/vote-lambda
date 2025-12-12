package com.fernando.vote.poolcreate.services.impl;


import com.fernando.vote.poolcreate.models.Option;
import com.fernando.vote.poolcreate.models.Pool;
import com.fernando.vote.poolcreate.repository.PoolRepository;
import com.fernando.vote.poolcreate.repository.impl.PoolRepositoryImpl;
import com.fernando.vote.poolcreate.services.IPoolService;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class IPoolServiceImpl implements IPoolService {
    private static final String CODE_SURVEY="POOL_";
    private static final String CODE_OPTION="OP_";
    private final PoolRepository poolRepository;

    public IPoolServiceImpl() {
        this.poolRepository = new PoolRepositoryImpl();
    }

    @Override
    public Pool createPool(Pool pool) {
        String generatedPk= UUID.randomUUID().toString();

        pool.setPoolId(CODE_SURVEY+generatedPk);
        Set<Option> lstOpt=pool.getOptions().stream().map(option ->{
            option.setOptionId(CODE_OPTION+UUID.randomUUID());
            return option;
        } ).collect(Collectors.toSet());
        pool.setOptions(lstOpt);
        poolRepository.savePoolTransact(pool);
        return pool;
    }
}
