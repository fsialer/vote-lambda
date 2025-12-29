package votecreate.repository.impl;

import redis.clients.jedis.Jedis;
import votecreate.config.RedisConfig;
import votecreate.repository.CacheRepository;

public class CacheRepositoryImpl implements CacheRepository {
    @Override
    public Long hashIncrement(String key, long value, String attribute) {
        try(Jedis jedis = RedisConfig.getResource()) {
            return jedis.hincrBy(key,attribute,value);
        }
    }

    @Override
    public String getSet(String key) {
        try(Jedis jedis = RedisConfig.getResource()) {
            return jedis.get(key);
        }
    }

    @Override
    public Boolean existsKey(String key) {
        try(Jedis jedis = RedisConfig.getResource()) {
            return jedis.exists(key);
        }
    }
}
