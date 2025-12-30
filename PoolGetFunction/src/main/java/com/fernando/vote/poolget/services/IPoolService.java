package com.fernando.vote.poolget.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fernando.vote.poolget.dto.Poll;

public interface IPoolService {
    Poll getPoolById(String id) throws JsonProcessingException;
}
