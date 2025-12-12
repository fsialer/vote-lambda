package com.fernando.vote.poolget.repository.impl;

import com.fernando.vote.poolget.config.RedisConfig;
import com.fernando.vote.poolget.repository.CacheRepository;
import redis.clients.jedis.Jedis;


public class CacheRepositoryImpl implements CacheRepository {


    @Override
    public String getSet(String key) {
        try(Jedis jedis = RedisConfig.getResource()) {
            return jedis.get(key);
        }
    }

    @Override
    public String createSet(String key, String value) {
        try(Jedis jedis = RedisConfig.getResource()) {
            return jedis.set(key,value);
        }
    }

    @Override
    public String createSet(String key, String value, long ttl) {
        try(Jedis jedis = RedisConfig.getResource()) {
            return jedis.setex(key,ttl,value);
        }
    }

    @Override
    public Boolean existsKey(String key) {
        try(Jedis jedis = RedisConfig.getResource()) {
            return jedis.exists(key);
        }
    }
}
