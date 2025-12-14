package com.fernando.vote.workerfunction.repository.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fernando.vote.workerfunction.config.DynamoConfig;
import com.fernando.vote.workerfunction.repository.VoteRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteRepositoryImpl implements VoteRepository {

    private final AmazonDynamoDB dynamoDB;
    private final String tableName;
    private final long currentTime=System.currentTimeMillis();

    public VoteRepositoryImpl() {
        this.dynamoDB = DynamoConfig.getClient();
        this.tableName = getTableName();
    }

    protected String getTableName() {
        return System.getenv("DB_TABLE_NAME");
    }

    @Override
    public void batchVote(Map<String,Map<String,String>> votes) {
        List<WriteRequest> writeRequests=new ArrayList<>();
        votes.forEach((key1, hash) -> {
            hash.forEach((key2, value) -> {
                Map<String, AttributeValue> key = new HashMap<>();
                key.put("PK", new AttributeValue(key1));
                key.put("SK", new AttributeValue(key2+"_VOTE"));
                key.put("votes", new AttributeValue().withN(value));
                writeRequests.add(new WriteRequest(new PutRequest(key)));
            });
        });


        // Dividir en bloques de 25
        List<List<WriteRequest>> chunks = chunkList(writeRequests, 25);

        for (List<WriteRequest> chunk : chunks) {
            Map<String, List<WriteRequest>> requestItems = new HashMap<>();
            requestItems.put(tableName, chunk);
            BatchWriteItemResult result = dynamoDB.batchWriteItem(requestItems);
            // Reintentar hasta que NO haya unprocessed items
            retryUnprocessed(result);
        }
    }

    private void retryUnprocessed(BatchWriteItemResult result){
        Map<String, List<WriteRequest>> unprocessed=result.getUnprocessedItems();
        while (unprocessed!=null && !unprocessed.isEmpty()){
            BatchWriteItemResult newResult=dynamoDB.batchWriteItem(unprocessed);
            unprocessed=newResult.getUnprocessedItems();
        }
    }

    private static <T> List<List<T>> chunkList(List<T> list, int size){
        List<List<T>> chunks=new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            chunks.add(list.subList(i, Math.min(list.size(), i + size)));
        }
        return chunks;
    }


}
