package com.fernando.vote.poolget.repository.impl;

import com.fernando.vote.poolget.config.Redis2Config;
import com.fernando.vote.poolget.config.RedisConfig;
import com.fernando.vote.poolget.repository.CacheRepository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;


public class CacheRepositoryImpl implements CacheRepository {


    @Override
    public String getSet(String key) {
//        try(JedisCluster jedis = RedisConfig.getClient()) {
//            return jedis.get(key);
//        }
        try(Jedis jedis = Redis2Config.getResource()) {
            return jedis.get(key);
        }
    }

    @Override
    public String createSet(String key, String value) {
//        try(JedisCluster  jedis = RedisConfig.getClient()) {
//            return jedis.set(key,value);
//        }
        try(Jedis jedis = Redis2Config.getResource()) {
            return jedis.set(key,value);
        }
    }

    @Override
    public String createSet(String key, String value, long ttl) {
//        try(JedisCluster  jedis = RedisConfig.getClient()) {
//            return jedis.setex(key,ttl,value);
//        }
        try(Jedis jedis = Redis2Config.getResource()) {
            return jedis.setex(key,ttl,value);
        }
    }

    @Override
    public Boolean existsKey(String key) {
//        try(JedisCluster  jedis = RedisConfig.getClient()) {
//            return jedis.exists(key);
//        }
        try(Jedis jedis = Redis2Config.getResource()) {
            return jedis.exists(key);
        }
    }
}
