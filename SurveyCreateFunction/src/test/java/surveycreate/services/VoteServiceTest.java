package surveycreate.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import surveycreate.repository.OptionRepository;
import surveycreate.repository.QuestionRepository;
import surveycreate.services.impl.VoteService;
import surveycreate.utils.VoteTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VoteServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private OptionRepository optionRepository;

    @Test
    public void When_SendSurveyWithOptions_Expect_SaveSurvey(){
        VoteService voteService = new VoteService(questionRepository, optionRepository);
        
        doNothing().when(questionRepository).saveTransactQuestion(any(), any());
        
        voteService.saveQuestionAndOptions(VoteTestUtils.voteMockBuild(), "SUV#5874558");
        
        verify(questionRepository, times(1)).saveTransactQuestion(any(), eq("SUV#5874558"));
    }

    @Test
    public void When_SaveQuestionAndOptions_Expect_CallRepository(){
        VoteService voteService = new VoteService(questionRepository, optionRepository);
        
        doNothing().when(questionRepository).saveTransactQuestion(any(), any());
        
        voteService.saveQuestionAndOptions(VoteTestUtils.voteMockBuild(), "TEST#123");
        
        verify(questionRepository).saveTransactQuestion(any(), eq("TEST#123"));
        verifyNoInteractions(optionRepository);
    }
}