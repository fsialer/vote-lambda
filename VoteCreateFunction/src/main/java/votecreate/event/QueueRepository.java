package votecreate.event;

import votecreate.models.OptionEvent;

public interface QueueRepository {

    void sendMessage(OptionEvent event);
}
