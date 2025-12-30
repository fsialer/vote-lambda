package votecreate.repository.impl;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import votecreate.config.DynamoConfig;
import votecreate.dto.Poll;
import votecreate.models.PollItem;
import votecreate.repository.PollRepository;

public class PollRepositoryImpl implements PollRepository {
    private final DynamoDbEnhancedClient enhancedClient;
    private final String tableName;

    public PollRepositoryImpl() {
        this.enhancedClient = DynamoConfig.getClient();
        this.tableName = getTableName();
    }
    protected String getTableName() {
        return System.getenv("DB_TABLE_NAME");
    }
    @Override
    public Poll getPoolByPk(String pk) {

        DynamoDbTable<PollItem> poolTable = enhancedClient.table(tableName, TableSchema.fromBean(PollItem.class));

        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(pk).build()
        );
        Iterable<PollItem> items = poolTable.query(r -> r.queryConditional(queryConditional)).items();
        Poll poll = null;

        for (PollItem item : items) {
            if (item.getSk().equals("METADATA")) {
                // Mapear el ítem de metadata al objeto Pool
                poll = Poll.builder()
                        .pollId(item.getPk())
                        .question(item.getQuestion())
                        .active(item.getActive()) // si lo mapeaste
                        .dateClosed(item.getDateClosed())
                        .build();
            }
        }

        return poll;
    }
}
