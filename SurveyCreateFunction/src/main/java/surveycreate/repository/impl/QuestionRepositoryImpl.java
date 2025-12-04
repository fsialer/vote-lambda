package surveycreate.repository.impl;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import surveycreate.models.Option;
import surveycreate.models.VoteEvent;
import surveycreate.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionRepositoryImpl implements QuestionRepository {
    private final AmazonDynamoDB dynamoDB;
    private final String tableName;
    private static final String CODE_SURVEY="SURV#";
    private static final String CODE_OPTION="OP#";
    private final long currentTime=System.currentTimeMillis();

    public QuestionRepositoryImpl() {
        this.dynamoDB = getDynamoDBClient();
        this.tableName = getTableName();
    }

    protected AmazonDynamoDB getDynamoDBClient() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://dynamodb-local:8000", "us-east-1")
                )
                .build();
    }

    protected String getTableName() {
        return System.getenv("DB_TABLE_NAME");
    }

    @Override
    public void saveQuestion(VoteEvent voteEvent, String generatedId) {
        Map<String, AttributeValue> item= new HashMap<>();
        item.put("PK",new AttributeValue(CODE_SURVEY+generatedId));
        item.put("SK", new AttributeValue("METADATA"));
        item.put("question", new AttributeValue(voteEvent.getQuestion()));
        item.put("total_votes", new AttributeValue().withN("0"));
        item.put("close", new AttributeValue().withBOOL(false));
        item.put("ttl", new AttributeValue().withN(String.valueOf(currentTime/1000 + (currentTime/1000)+86400)));
        PutItemRequest request=new PutItemRequest();
        request.setTableName(tableName);
        request.setItem(item);
        dynamoDB.putItem(request);
    }

    @Override
    public void saveTransactQuestion(VoteEvent voteEvent, String generatedId) {
        List<TransactWriteItem> actions = new ArrayList<>();

        Map<String, AttributeValue> pollItem = new HashMap<>();
        pollItem.put("PK",new AttributeValue(CODE_SURVEY+generatedId));
        pollItem.put("SK", new AttributeValue("METADATA"));
        pollItem.put("question", new AttributeValue(voteEvent.getQuestion()));
        //pollItem.put("total_votes", new AttributeValue().withN("0"));
        pollItem.put("close", new AttributeValue().withBOOL(false));
        pollItem.put("ttl", new AttributeValue().withN(String.valueOf(currentTime/1000 + (currentTime/1000)+86400)));

        actions.add(new TransactWriteItem()
                .withPut(new Put()
                        .withTableName(tableName)
                        .withItem(pollItem)
                ));

        for (Option opt : voteEvent.getOptions()) {
            Map<String, AttributeValue> optItem = new HashMap<>();
            optItem.put("PK",new AttributeValue(CODE_SURVEY+generatedId));
            optItem.put("SK",new AttributeValue(CODE_OPTION+opt.getOptionId().toString()));
            optItem.put("description", new AttributeValue(opt.getDescription()));
            //optItem.put("count_votes", new AttributeValue().withN("0"));

            actions.add(new TransactWriteItem()
                    .withPut(new Put()
                            .withTableName(tableName)
                            .withItem(optItem)
                    ));
        }

        dynamoDB.transactWriteItems(new TransactWriteItemsRequest()
                .withTransactItems(actions));
    }
}