package com.fernando.vote.workerfunction.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {
    private static JedisPool jedisPool;

    private static void init() {
        String host = System.getenv("REDIS_HOST");
        int port = Integer.parseInt(System.getenv("REDIS_PORT"));
        String password = System.getenv("REDIS_PASSWORD");

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);

        if (password == null || password.isBlank()) {
            jedisPool = new JedisPool(poolConfig, host, port, 5000);
        } else {
            jedisPool = new JedisPool(poolConfig, host, port, 5000, password, true);
        }
    }

    public static Jedis getResource(){
        if (jedisPool == null) {
            init();
        }
        return jedisPool.getResource();
    }
}
