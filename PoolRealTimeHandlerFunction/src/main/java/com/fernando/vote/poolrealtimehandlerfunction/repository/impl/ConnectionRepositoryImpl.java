package com.fernando.vote.poolrealtimehandlerfunction.repository.impl;

import com.fernando.vote.poolrealtimehandlerfunction.config.DynamoConfig;
import com.fernando.vote.poolrealtimehandlerfunction.models.ConnectionClient;
import com.fernando.vote.poolrealtimehandlerfunction.repository.ConnectionRepository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.*;

public class ConnectionRepositoryImpl implements ConnectionRepository {
    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public ConnectionRepositoryImpl() {
        this.dynamoDbClient = DynamoConfig.getStandardClient();
        this.tableName = getTableName();
    }

    protected String getTableName() {
        return System.getenv("DB_TABLE_NAME");
    }

    @Override
    public List<ConnectionClient> getConnections(String poolId) {
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(tableName)
                .indexName("PoolIdIndex")
                .keyConditionExpression("poolId = :v_poolId")
                .expressionAttributeValues(Map.of(":v_poolId", AttributeValue.builder().s(poolId).build()))
                .build();

        QueryResponse result = dynamoDbClient.query(queryRequest);
        return mapQueryResultToConnections(result);
    }

    private List<ConnectionClient> mapQueryResultToConnections(QueryResponse result) {
        List<ConnectionClient> connections = new ArrayList<>();
        result.items().forEach(item -> {
            connections.add(ConnectionClient.builder()
                .connectionId(item.get("connectionId").s())
                .poolId(item.get("poolId").s())
                .build());
        });
        return connections;
    }
}