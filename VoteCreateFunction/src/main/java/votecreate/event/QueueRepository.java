package votecreate.event;

public interface QueueRepository {
    void sendMessage(String message);
}
