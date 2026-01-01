package votecreate.event;

public interface EventBridgeRepository {
    void publishVote(String pollId);
}
