package votecreate.services.impl;

import redis.clients.jedis.JedisPooled;
import votecreate.event.QueueRepository;
import votecreate.event.impl.QueueRepositoryImpl;
import votecreate.models.OptionEvent;
import votecreate.repository.OptionRepository;
import votecreate.repository.impl.OptionRepositoryImpl;
import votecreate.services.SaveVoteUseCase;

public class VoteService implements SaveVoteUseCase {
    private final OptionRepository optionRepository;
    private final QueueRepository queueRepository;
    public VoteService(){
        optionRepository= new OptionRepositoryImpl();
        queueRepository=new QueueRepositoryImpl();
    }
    @Override
    public Long saveVote(String key) {
        return optionRepository.saveVote(key);
    }

    @Override
    public void sendMessage(OptionEvent event) {
        queueRepository.sendMessage(event);
    }
}
