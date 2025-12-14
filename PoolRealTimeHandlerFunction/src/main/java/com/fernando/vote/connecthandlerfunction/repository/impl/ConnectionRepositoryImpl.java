package com.fernando.vote.connecthandlerfunction.repository.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.fernando.vote.connecthandlerfunction.config.DynamoConfig;
import com.fernando.vote.connecthandlerfunction.models.ConnectionClient;
import com.fernando.vote.connecthandlerfunction.repository.ConnectionRepository;

import java.util.*;

public class ConnectionRepositoryImpl implements ConnectionRepository {
    private final AmazonDynamoDB dynamoDB;
    private final String tableName;
    private final long currentTime=System.currentTimeMillis();

    public ConnectionRepositoryImpl() {
        this.dynamoDB = DynamoConfig.getClient();
        this.tableName = getTableName();
    }

    protected String getTableName() {
        return System.getenv("DB_TABLE_NAME");
    }

    @Override
    public List<ConnectionClient> getConnections(String poolId) {
        HashMap<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put(":pk",new AttributeValue().withS(poolId));
        QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withKeyConditionExpression("PK = :pk")
                .withExpressionAttributeValues(keyToGet);
        QueryResult result = dynamoDB.query(queryRequest);

        List<Map<String, AttributeValue>> items = result.getItems();
        if (items == null || items.isEmpty()) {
            throw new NoSuchElementException("Pool not found: " + poolId);
        }

        Set<ConnectionClient> list = new HashSet<>();
        for (Map<String, AttributeValue> item : items) {
            if (item.containsKey("connectionId") && item.containsKey("poolId")){
                ConnectionClient connectionClient=ConnectionClient.builder()
                        .connectionId(item.get("connectionId").getS())
                        .poolId(item.get("poolId").getS())
                        .build();
                list.add(connectionClient);
            }
        }

        return list.stream().toList();
    }


}
