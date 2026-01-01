package votecreate.event.impl;

import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import votecreate.config.EventBridgeConfig;
import votecreate.event.EventBridgeRepository;

public class EventBridgeRepositoryImpl implements EventBridgeRepository {

    @Override
    public void publishVote(String pollId) {
        EventBridgeClient ebc= EventBridgeConfig.getClient();
            PutEventsRequestEntry entry= PutEventsRequestEntry.builder()
                    .source("lambda.vote_create_lambda")
                    .detailType("VoteEmitted")
                    .detail("""
              {
                "pollId": "%s"
              }
            """.formatted(pollId))
                    .eventBusName("default")
                    .build();
            ebc.putEvents(PutEventsRequest.builder().entries(entry).build());

    }
}
