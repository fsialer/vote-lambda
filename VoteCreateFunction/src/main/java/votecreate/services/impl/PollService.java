package votecreate.services.impl;

import votecreate.exceptions.LimitTimeVoteException;
import votecreate.exceptions.PollNotFoundException;
import votecreate.mapper.VoteMapper;
import votecreate.dto.Poll;
import votecreate.repository.CacheRepository;
import votecreate.repository.PollRepository;
import votecreate.repository.impl.CacheRepositoryImpl;
import votecreate.repository.impl.PollRepositoryImpl;
import votecreate.services.IPollService;

import java.time.LocalDateTime;

public class PollService implements IPollService {
    private final PollRepository pollRepository;
    private final CacheRepository cacheRepository;
    private final VoteMapper voteMapper;
    public PollService(){
        pollRepository=new PollRepositoryImpl();
        cacheRepository=new CacheRepositoryImpl();
        voteMapper=new VoteMapper();
    }

     @Override
    public void verifyDateClosed(String pollId) {
        String key="pool:"+pollId;
        Poll poll=null;

        if(Boolean.TRUE.equals(cacheRepository.existsKey(key))){
            String strPool=cacheRepository.getSet(key);
            poll=voteMapper.stringToPoll(strPool);
        }else{
            poll=pollRepository.getPoolByPk(pollId);
            if(poll==null){
                throw new PollNotFoundException("Pool not found: " + pollId);
            }
        }
        LocalDateTime now= LocalDateTime.now();
        if(now.isAfter(LocalDateTime.parse(poll.getDateClosed()))){
            throw new LimitTimeVoteException("The poll sis closed.");
        }
    }
}
