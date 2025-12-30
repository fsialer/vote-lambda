package com.fernando.vote.poolget.repository;

import com.fernando.vote.poolget.dto.Poll;

public interface PoolRepository {

    Poll getPoolByIdWithDetails(String pk);
}
