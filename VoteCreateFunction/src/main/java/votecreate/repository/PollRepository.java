package votecreate.repository;

import redis.clients.jedis.util.Pool;
import votecreate.models.Poll;

public interface PollRepository {
    Poll getPoolByPk(String pk);
}
