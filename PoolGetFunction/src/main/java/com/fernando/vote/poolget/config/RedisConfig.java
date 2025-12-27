package com.fernando.vote.poolget.config;

import redis.clients.jedis.*;

import java.util.Set;

public class RedisConfig {
    private static JedisCluster jedisCluster;

    private static void init() {
        String host = System.getenv("REDIS_HOST");
        int port = Integer.parseInt(System.getenv("REDIS_PORT"));
        String password = System.getenv("REDIS_PASSWORD");

        HostAndPort node = new HostAndPort(host, port);
        Set<HostAndPort> nodes = Set.of(node);

        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                //.password(password)
                .ssl(true)                 // 🔥 OBLIGATORIO
                .timeoutMillis(2000)       // 🔥 EVITA BLOQUEOS
                .build();

        jedisCluster = new JedisCluster(nodes, clientConfig);
    }

    public static JedisCluster getClient() {
        if (jedisCluster == null) {
            init();
            System.out.println("Redis client initialized");
        }
        return jedisCluster;
    }
}
