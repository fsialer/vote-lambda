package votecreate.repository;

import votecreate.models.OptionEvent;

public interface OptionRepository {
    Long saveVote(String key);
}
