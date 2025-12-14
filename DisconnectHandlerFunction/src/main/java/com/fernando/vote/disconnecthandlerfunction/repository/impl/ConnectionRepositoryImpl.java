package com.fernando.vote.disconnecthandlerfunction.repository.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.fernando.vote.disconnecthandlerfunction.config.DynamoConfig;
import com.fernando.vote.disconnecthandlerfunction.models.ConnectionClient;
import com.fernando.vote.disconnecthandlerfunction.repository.ConnectionRepository;

import java.util.Map;

public class ConnectionRepositoryImpl implements ConnectionRepository {
    private final AmazonDynamoDB dynamoDB;
    private final String tableName;
    public ConnectionRepositoryImpl(){
        dynamoDB= DynamoConfig.getAmazonDynamoDB();
        tableName=System.getenv("DB_TABLE_NAME");
    }

    @Override
    public void deleteConnectionByConnectionId(String connectionId) {
        dynamoDB.deleteItem(new DeleteItemRequest().withTableName(tableName).withKey(Map.of(
                        "connectionId",
                        new AttributeValue().withS(connectionId)
                )));
    }
}
