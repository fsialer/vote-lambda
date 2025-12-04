package votecreate.services;

import votecreate.models.OptionEvent;

public interface SaveVoteUseCase {
    Long saveVote(String key);
    void sendMessage(OptionEvent event);
}
