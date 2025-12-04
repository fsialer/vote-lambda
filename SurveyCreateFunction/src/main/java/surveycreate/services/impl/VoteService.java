package surveycreate.services.impl;

import surveycreate.models.VoteEvent;
import surveycreate.repository.OptionRepository;
import surveycreate.repository.QuestionRepository;
import surveycreate.repository.impl.OptionRepositoryImpl;
import surveycreate.repository.impl.QuestionRepositoryImpl;
import surveycreate.services.SaveQuestionUseCase;

public class VoteService implements SaveQuestionUseCase {
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public VoteService(QuestionRepository questionRepository, OptionRepository optionRepository) {
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
    }

    public VoteService() {
        this.questionRepository = new QuestionRepositoryImpl();
        this.optionRepository = new OptionRepositoryImpl();
    }

    @Override
    public void saveQuestionAndOptions(VoteEvent voteEvent, String generatedId) {
//        questionRepository.saveQuestion(voteEvent, generatedId);
//        optionRepository.saveOptions(generatedId,voteEvent.getOptions().stream().toList());
        questionRepository.saveTransactQuestion(voteEvent, generatedId);
    }
}
