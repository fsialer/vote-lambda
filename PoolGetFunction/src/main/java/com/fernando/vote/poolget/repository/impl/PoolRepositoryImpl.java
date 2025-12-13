package com.fernando.vote.poolget.repository.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fernando.vote.poolget.config.DynamoConfig;
import com.fernando.vote.poolget.models.Option;
import com.fernando.vote.poolget.models.Pool;
import com.fernando.vote.poolget.repository.PoolRepository;

import java.util.*;

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
    public Pool  getPoolByIdWithDetails(String pk) {
        // Crear el objeto que representa la clave primaria (PK) y la clave secundaria (SK)
        HashMap<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put(":pk",new AttributeValue().withS(pk));
        QueryRequest queryRequest = new QueryRequest()
                .withTableName(tableName)
                .withKeyConditionExpression("PK = :pk")
                .withExpressionAttributeValues(keyToGet);

        QueryResult result = dynamoDB.query(queryRequest);

        List<Map<String, AttributeValue>> items = result.getItems();
        if (items == null || items.isEmpty()) {
            throw new NoSuchElementException("Pool not found: " + pk);
        }
        Pool pool=Pool.builder().build();
        Set<Option> list = new HashSet<>();
        for (Map<String, AttributeValue> item : items) {
            if (item.containsKey("SK")){
                if(item.get("SK").getS().equals("METADATA")){
                    pool.setPoolId(item.get("PK").getS());
                    pool.setQuestion(item.get("question").getS());
                    pool.setActive(item.get("active").getBOOL());
                }
                if(item.get("SK").getS().startsWith("OP_") && !item.get("SK").getS().endsWith("_VOTE")){
                    list.add(Option.builder()
                            .optionId(item.get("SK").getS())
                            .text(item.get("text").getS())
                            .build());
                }
            }
        }
        pool.setOptions(list.stream().toList());
        return pool;
    }
}