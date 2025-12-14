package com.fernando.vote.connecthandlerfunction.repository.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fernando.vote.connecthandlerfunction.config.DynamoConfig;
import com.fernando.vote.connecthandlerfunction.models.ConnectionClient;
import com.fernando.vote.connecthandlerfunction.repository.ConnectionRepository;

import java.util.Map;

public class ConnectionRepositoryImpl implements ConnectionRepository {
    private final AmazonDynamoDB dynamoDB;
    private final String tableName;
    public ConnectionRepositoryImpl(){
        dynamoDB= DynamoConfig.getAmazonDynamoDB();
        tableName=System.getenv("DB_TABLE_NAME");
    }

    @Override
    public void saveConnection(ConnectionClient connectionClient) {
        dynamoDB.putItem(tableName, Map.of(
                "connectionId", new AttributeValue().withS(connectionClient.getConnectionId()),
                "poolId", new AttributeValue().withS(connectionClient.getPoolId())
        ));
    }
}
