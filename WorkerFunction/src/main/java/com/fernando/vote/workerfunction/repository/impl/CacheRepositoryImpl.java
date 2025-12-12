package com.fernando.vote.workerfunction.repository.impl;

import com.fernando.vote.workerfunction.config.RedisConfig;
import com.fernando.vote.workerfunction.repository.CacheRepository;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class CacheRepositoryImpl implements CacheRepository {
    @Override
    public Map<String, String> getHashSet(String key) {
        try(Jedis jedis= RedisConfig.getResource()) {
            return jedis.hgetAll(key);
        }
    }
}
