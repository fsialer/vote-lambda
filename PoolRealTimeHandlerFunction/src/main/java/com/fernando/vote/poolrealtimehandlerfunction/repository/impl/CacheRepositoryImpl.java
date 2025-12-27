package com.fernando.vote.poolrealtimehandlerfunction.repository.impl;

import com.fernando.vote.poolrealtimehandlerfunction.config.RedisConfig;
import com.fernando.vote.poolrealtimehandlerfunction.repository.CacheRepository;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class CacheRepositoryImpl implements CacheRepository {
    @Override
    public Map<String, String> getHashSet(String key) {
        try(Jedis jedis = RedisConfig.getResource()) {
            return jedis.hgetAll(key);
        }
    }
}
