package surveycreate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import surveycreate.models.Option;
import surveycreate.models.VoteEvent;
import surveycreate.services.SaveQuestionUseCase;

import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AppTest {

    @Mock
    private Context mockContext;
    
    @Mock
    private SaveQuestionUseCase mockVoteService;
    
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        
        doNothing().when(mockVoteService).saveQuestionAndOptions(any(VoteEvent.class), anyString());
    }

    private App createAppWithMock() {
        return new App() {
            @Override
            protected SaveQuestionUseCase createVoteService() {
                return mockVoteService;
            }
        };
    }

    @Test
    public void testSuccessfulSurveyCreation() throws Exception {
        VoteEvent voteEvent = VoteEvent.builder()
            .question("What's your favorite programming language?")
            .options(Set.of(
                Option.builder().optionId(1).description("Java").build(),
                Option.builder().optionId(2).description("Python").build()
            ))
            .build();

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setBody(objectMapper.writeValueAsString(voteEvent));

        App app = createAppWithMock();
        APIGatewayProxyResponseEvent response = app.handleRequest(request, mockContext);

        assertEquals(201, response.getStatusCode().intValue());
        assertTrue(response.getBody().contains("Survey created successfully"));
        assertTrue(response.getBody().contains("surveyId"));
        assertEquals("application/json", response.getHeaders().get("Content-Type"));
    }

    @Test
    public void testValidationErrorEmptyTitle() throws Exception {
        VoteEvent voteEvent = VoteEvent.builder()
            .question("")
            .options(Set.of(
                Option.builder().optionId(1).description("Option 1").build(),
                Option.builder().optionId(2).description("Option 2").build()
            ))
            .build();

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setBody(objectMapper.writeValueAsString(voteEvent));

        App app = createAppWithMock();
        APIGatewayProxyResponseEvent response = app.handleRequest(request, mockContext);

        assertEquals(400, response.getStatusCode().intValue());
        assertTrue(response.getBody().contains("Question is required"));
    }

    @Test
    public void testInvalidJsonRequest() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setBody("{invalid json}");

        App app = createAppWithMock();
        APIGatewayProxyResponseEvent response = app.handleRequest(request, mockContext);

        assertEquals(500, response.getStatusCode().intValue());
        assertTrue(response.getBody().contains("error"));
    }
}