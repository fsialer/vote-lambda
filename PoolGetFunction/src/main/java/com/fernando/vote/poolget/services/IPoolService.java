package com.fernando.vote.poolget.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fernando.vote.poolget.models.Pool;

public interface IPoolService {
    Pool getPoolById(String id) throws JsonProcessingException;
}
