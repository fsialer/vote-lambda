package com.fernando.vote.poolcreate.repository;


import com.fernando.vote.poolcreate.models.Pool;

public interface PoolRepository {

    void savePoolTransact(Pool pool);
}
