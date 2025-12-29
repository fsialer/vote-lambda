package votecreate.services.impl;

import votecreate.event.QueueRepository;
import votecreate.event.SnsRepository;
import votecreate.event.impl.QueueRepositoryImpl;
import votecreate.event.impl.SnsRepositoryImpl;
import votecreate.dto.Vote;
import votecreate.repository.CacheRepository;
import votecreate.repository.impl.CacheRepositoryImpl;
import votecreate.services.IVoteService;

public class VoteService implements IVoteService {
    private final CacheRepository cacheRepository;
    private final QueueRepository queueRepository;
    private final SnsRepository snsRepository;
    public VoteService(){
        cacheRepository = new CacheRepositoryImpl();
        queueRepository=new QueueRepositoryImpl();
        snsRepository=new SnsRepositoryImpl();
    }
    @Override
    public void saveVote(Vote vote) {
        String key="votes:"+vote.getPoolId();
        cacheRepository.hashIncrement(key,1,vote.getOptionId());
    }

    @Override
    public void sendMessage(Vote vote) {
        queueRepository.sendMessage(vote.getPoolId());
    }

    @Override
    public void publishMessage(Vote vote) {
        snsRepository.sendMessage(vote.getPoolId());
    }
}
