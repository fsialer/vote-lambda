package votecreate.repository;

import votecreate.dto.Poll;

public interface PollRepository {
    Poll getPoolByPk(String pk);
}
