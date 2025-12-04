package surveycreate.services;

import surveycreate.models.VoteEvent;

public interface SaveQuestionUseCase {
    void saveQuestionAndOptions(VoteEvent voteEvent, String generatedId);
}
