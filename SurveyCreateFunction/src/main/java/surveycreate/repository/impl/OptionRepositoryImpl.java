package surveycreate.repository.impl;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemResult;
import com.amazonaws.services.dynamodbv2.model.PutRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import surveycreate.models.Option;
import surveycreate.repository.OptionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionRepositoryImpl implements OptionRepository {
    private final AmazonDynamoDB dynamoDB= AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration("http://dynamodb-local:8000", "us-east-1")
            )
            .build();
    private static final String TABLE_NAME=System.getenv("DB_TABLE_NAME");
    private static final String CODE_SURVEY="SURV#";
    private static final String CODE_OPTION="OP#";
    private final long currentTime=System.currentTimeMillis();

    @Override
    public void saveOptions(String questionId, List<Option> options) {
        List<WriteRequest> writeRequests=new ArrayList<>();
        options.forEach(option -> {
            Map<String, AttributeValue> map=new HashMap<>();
            map.put("PK",new AttributeValue(CODE_SURVEY+questionId));
            map.put("SK",new AttributeValue(CODE_OPTION+option.getOptionId().toString()));
            map.put("description", new AttributeValue(option.getDescription()));
            //map.put("count_votes", new AttributeValue().withN("0"));
            map.put("ttl", new AttributeValue().withN(String.valueOf(currentTime/1000 + (currentTime/1000)+86400)));
            writeRequests.add(new WriteRequest(new PutRequest(map)));
        });

        // Dividir en bloques de 25
        List<List<WriteRequest>> chunks = chunkList(writeRequests, 25);

        for (List<WriteRequest> chunk : chunks) {
            Map<String, List<WriteRequest>> requestItems = new HashMap<>();
            requestItems.put(TABLE_NAME, chunk);
            BatchWriteItemResult result = dynamoDB.batchWriteItem(requestItems);
            // Reintentar hasta que NO haya unprocessed items
            retryUnprocessed(result);
        }
    }

    private void retryUnprocessed(BatchWriteItemResult result){
        Map<String,List<WriteRequest>> unprocessed=result.getUnprocessedItems();
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
