package votecreate.services;

import votecreate.dto.Vote;

public interface IVoteService {
    void saveVote(Vote vote);
    void publishVote(Vote vote);
}
