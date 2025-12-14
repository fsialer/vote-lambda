package votecreate.event;

public interface SnsRepository {
    void sendMessage(String message);
}
