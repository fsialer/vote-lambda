package votecreate.services.impl;

import votecreate.event.EventBridgeRepository;
import votecreate.event.impl.EventBridgeRepositoryImpl;
import votecreate.dto.Vote;
import votecreate.repository.CacheRepository;
import votecreate.repository.impl.CacheRepositoryImpl;
import votecreate.services.IVoteService;

public class VoteService implements IVoteService {
    private final CacheRepository cacheRepository;

    private final EventBridgeRepository eventBridgeRepository;
    public VoteService(){
        cacheRepository = new CacheRepositoryImpl();
        eventBridgeRepository=new EventBridgeRepositoryImpl();
    }
    @Override
    public void saveVote(Vote vote) {
        String key="votes:"+vote.getPollId();
        cacheRepository.hashIncrement(key,1,vote.getOptionId());
    }

    @Override
    public void publishVote(Vote vote) {
        eventBridgeRepository.publishVote(vote.getPollId());
    }
}
