package votecreate.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {
    private static JedisPool jedisPool;

    static{
        String host= System.getenv("REDIS_HOST");
        Integer port= Integer.valueOf(System.getenv("REDIS_PORT"));
        String password= System.getenv("REDIS_PASSWORD");

        JedisPoolConfig poolConfig=new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);

        if (password == null || password.isBlank()) {
            jedisPool = new JedisPool(poolConfig, host, port, 5000);
        } else {
            jedisPool = new JedisPool(poolConfig, host, port, 5000, password, true);
        }
    }

    public static Jedis getResource(){
        return jedisPool.getResource();
    }
}
