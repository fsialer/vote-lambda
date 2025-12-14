package votecreate.services;

import votecreate.models.Vote;

public interface IVoteService {
    Long saveVote(Vote vote);
    void sendMessage(Vote vote);
    void publishMessage(Vote vote);
}
