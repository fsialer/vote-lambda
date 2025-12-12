package com.fernando.vote.poolcreate.repository.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fernando.vote.poolcreate.config.DynamoConfig;
import com.fernando.vote.poolcreate.exceptions.PoolRepositoryException;
import com.fernando.vote.poolcreate.models.Option;
import com.fernando.vote.poolcreate.models.Pool;
import com.fernando.vote.poolcreate.repository.PoolRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoolRepositoryImpl implements PoolRepository {
    private final AmazonDynamoDB dynamoDB;
    private final String tableName;
    private final long currentTime=System.currentTimeMillis();

    public PoolRepositoryImpl() {
        this.dynamoDB = DynamoConfig.getAmazonDynamoDB();
        this.tableName = getTableName();
    }

    protected String getTableName() {
        return System.getenv("DB_TABLE_NAME");
    }

    @Override
    public void savePoolTransact(Pool pool) {
        try {
            List<TransactWriteItem> actions = new ArrayList<>();
            Map<String, AttributeValue> pollItem = new HashMap<>();
            pollItem.put("PK",new AttributeValue(pool.getPoolId()));
            pollItem.put("SK", new AttributeValue("METADATA"));
            pollItem.put("question", new AttributeValue(pool.getQuestion()));
            pollItem.put("active", new AttributeValue().withBOOL(true));
            pollItem.put("ttl", new AttributeValue().withN(String.valueOf(currentTime/1000 + (currentTime/1000)+86400)));

            actions.add(new TransactWriteItem()
                    .withPut(new Put()
                            .withTableName(tableName)
                            .withItem(pollItem)
                    ));

            for (Option option : pool.getOptions()) {
                Map<String, AttributeValue> optItem = new HashMap<>();
                optItem.put("PK",new AttributeValue(pool.getPoolId()));
                optItem.put("SK",new AttributeValue(option.getOptionId()));
                optItem.put("text", new AttributeValue(option.getText()));
                actions.add(new TransactWriteItem()
                        .withPut(new Put()
                                .withTableName(tableName)
                                .withItem(optItem)
                        ));
            }
            dynamoDB.transactWriteItems(new TransactWriteItemsRequest()
                    .withTransactItems(actions));
        }catch (Exception e){
            throw new PoolRepositoryException(e.getMessage());
        }

    }
}