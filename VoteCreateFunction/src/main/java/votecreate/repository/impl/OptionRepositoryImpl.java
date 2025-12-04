package votecreate.repository.impl;

import redis.clients.jedis.JedisPooled;
import votecreate.models.OptionEvent;
import votecreate.repository.OptionRepository;

public class OptionRepositoryImpl implements OptionRepository {
    @Override
    public Long saveVote(String key) {
        try(JedisPooled jedis = new JedisPooled(
                System.getenv("REDIS_HOST"),
                Integer.parseInt(System.getenv("REDIS_PORT")),
                Boolean.parseBoolean(System.getenv("REDIS_SSL"))
        )) {
            return jedis.incr(key);
        }
    }
}
