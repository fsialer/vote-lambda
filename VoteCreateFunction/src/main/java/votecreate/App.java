package votecreate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import votecreate.mapper.VoteMapper;
import votecreate.dto.VoteRequest;
import votecreate.services.IPollService;
import votecreate.services.IVoteService;
import votecreate.services.impl.PollService;
import votecreate.services.impl.VoteService;

import java.util.Map;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Map<String, String> COMMON_HEADERS = Map.of(
            "Content-Type", "application/json",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
            "Access-Control-Allow-Methods", "OPTIONS,POST"
    );

    private final ObjectMapper objectMapper;
    private final IVoteService iVoteService;
    private final IPollService iPollService;
    private final VoteMapper voteMapper;

    public App() {
        iVoteService = saveVoteUseCase();
        iPollService=new PollService();
        voteMapper=new VoteMapper();
        objectMapper=new ObjectMapper();
    }

    protected IVoteService saveVoteUseCase() {
        return new VoteService();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        try {
            String body=apiGatewayProxyRequestEvent.getBody();
            VoteRequest rq=voteMapper.bodyToVoteRequest(body);
            iPollService.verifyDateClosed(rq.getPollId());
            iVoteService.saveVote(voteMapper.voteRequestToVote(rq));
            iVoteService.publishVote(voteMapper.voteRequestToVote(rq));
            return buildResponse(204,Map.of("message","Vote sent successfully"));

        }catch (Exception e){
            context.getLogger().log(e.getMessage());
            return buildResponse(500,Map.of("error",e.getMessage()));
        }
    }

    private APIGatewayProxyResponseEvent buildResponse(int statusCode, Object body) {
        try {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(statusCode)
                    .withHeaders(COMMON_HEADERS)
                    .withBody(objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500);
        }
    }
}
