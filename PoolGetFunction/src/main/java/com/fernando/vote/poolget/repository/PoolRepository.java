package com.fernando.vote.poolget.repository;

import com.fernando.vote.poolget.models.Pool;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PoolRepository {

    Pool getPoolByIdWithDetails(String pk);
}
