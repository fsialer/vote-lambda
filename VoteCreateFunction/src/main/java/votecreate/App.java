package votecreate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import votecreate.mapper.VoteMapper;
import votecreate.models.VoteRequest;
import votecreate.services.IVoteService;
import votecreate.services.impl.VoteService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper=new ObjectMapper();
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final IVoteService iVoteService;

    public App() {
        this.iVoteService = saveVoteUseCase();
    }

    protected IVoteService saveVoteUseCase() {
        return new VoteService();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        try {
            String body=apiGatewayProxyRequestEvent.getBody(); // Aqui obtenemos el cuerpo de la solicitud
            VoteRequest rq=objectMapper.readValue(body,VoteRequest.class);
            //Validar campos
            Set<ConstraintViolation<VoteRequest>> violations = validator.validate(rq);
            if(!violations.isEmpty()){
                String errors=violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(","));
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(400)
                        .withHeaders(headers)
                        .withBody("{\"error\": \""+errors+"\"}");
            }
            VoteMapper voteMapper=new VoteMapper();
            Long vote= iVoteService.saveVote(voteMapper.voteRequestToVote(rq));
            iVoteService.sendMessage(voteMapper.voteRequestToVote(rq));
            iVoteService.publishMessage(voteMapper.voteRequestToVote(rq));
            // Respuesta exitosa
            Map<String,String> resp= new HashMap<>();
            resp.put("vote",vote.toString());
            resp.put("message","Vote sent successfully");
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(201)
                    .withHeaders(Map.of("Content-Type","application/json"))
                    .withBody(objectMapper.writeValueAsString(resp));


        }catch (Exception e){
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withHeaders(headers)
                    .withBody("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
