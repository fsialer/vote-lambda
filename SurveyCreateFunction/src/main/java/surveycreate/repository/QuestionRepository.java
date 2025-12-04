package surveycreate.repository;

import surveycreate.models.VoteEvent;

public interface QuestionRepository {
    void saveQuestion(VoteEvent voteEvent, String generatedId);

    void saveTransactQuestion(VoteEvent voteEvent, String generatedId);
}
