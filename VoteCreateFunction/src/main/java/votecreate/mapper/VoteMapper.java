package votecreate.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import votecreate.dto.Vote;
import votecreate.dto.VoteRequest;
import votecreate.dto.Poll;

public class VoteMapper {
    public Vote voteRequestToVote(VoteRequest rq){
        return Vote.builder()
                .pollId(rq.getPollId())
                .optionId(rq.getOptionId())
                .build();
    }

    public VoteRequest bodyToVoteRequest(String body){
        try{
            ObjectMapper obj=new ObjectMapper();
            return obj.readValue(body,VoteRequest.class);
        }catch (Exception e){
            return null;
        }
    }

    public Poll stringToPoll(String str){
        try{
            ObjectMapper obj=new ObjectMapper();

            return obj.readValue(str, Poll.class);
        }catch (JsonProcessingException e){
            return null;
        }
    }
}
