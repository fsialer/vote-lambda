package votecreate.mapper;

import votecreate.models.Vote;
import votecreate.models.VoteRequest;

public class VoteMapper {
    public Vote voteRequestToVote(VoteRequest rq){
        return Vote.builder()
                .poolId(rq.getPoolId())
                .optionId(rq.getOptionId())
                .build();

    }
}
