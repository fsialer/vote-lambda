package com.fernando.vote.connecthandlerfunction.repository.impl;

import com.fernando.vote.connecthandlerfunction.config.RedisConfig;
import com.fernando.vote.connecthandlerfunction.repository.CacheRepository;
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
