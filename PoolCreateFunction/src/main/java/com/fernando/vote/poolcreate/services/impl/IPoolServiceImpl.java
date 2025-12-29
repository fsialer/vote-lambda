package com.fernando.vote.poolcreate.services.impl;

import com.fernando.vote.poolcreate.models.Option;
import com.fernando.vote.poolcreate.models.Poll;
import com.fernando.vote.poolcreate.repository.PollRepository;
import com.fernando.vote.poolcreate.repository.impl.PollRepositoryImpl;
import com.fernando.vote.poolcreate.services.IPoolService;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class IPoolServiceImpl implements IPoolService {
    private static final String CODE_SURVEY="POOL_";
    private static final String CODE_OPTION="OP_";
    private final PollRepository pollRepository;

    public IPoolServiceImpl() {
        this.pollRepository = new PollRepositoryImpl();
    }

    @Override
    public Poll createPool(Poll poll) {
        String generatedPk= UUID.randomUUID().toString();

        poll.setPollId(CODE_SURVEY+generatedPk);
        Set<Option> lstOpt= poll.getOptions().stream().map(option ->{
            option.setOptionId(CODE_OPTION+UUID.randomUUID());
            return option;
        } ).collect(Collectors.toSet());
        poll.setOptions(lstOpt);
        pollRepository.savePoolTransact(poll);
        return poll;
    }
}
