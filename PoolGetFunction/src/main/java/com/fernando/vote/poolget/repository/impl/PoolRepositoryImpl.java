package com.fernando.vote.poolget.repository.impl;

import com.fernando.vote.poolget.config.DynamoConfig;
import com.fernando.vote.poolget.dto.Option;
import com.fernando.vote.poolget.dto.Poll;
import com.fernando.vote.poolget.models.PollItem;
import com.fernando.vote.poolget.repository.PoolRepository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.*;

public class PoolRepositoryImpl implements PoolRepository {
    private final DynamoDbEnhancedClient enhancedClient;
    private final String tableName;
    private final long currentTime=System.currentTimeMillis();

    public PoolRepositoryImpl() {
        this.enhancedClient = DynamoConfig.getClient();
        this.tableName = getTableName();
    }

    protected String getTableName() {
        return System.getenv("DB_TABLE_NAME");
    }

    @Override
    public Poll getPoolByIdWithDetails(String pk) {
        // ASUMIMOS que tienes inicializado el enhancedClient
        DynamoDbTable<PollItem> poolTable = enhancedClient.table(tableName, TableSchema.fromBean(PollItem.class));

        // 1. Definir la condición de consulta (Solo por PK)
        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(pk).build()
        );

        // 2. Ejecutar la consulta y paginar automáticamente (Lazy loading)
        // El método query() regresa un PageIterable que maneja la paginación internamente.
        // Solo se realizan llamadas a DynamoDB cuando se itera sobre los resultados.
        Iterable<PollItem> items = poolTable.query(r -> r.queryConditional(queryConditional)).items();

        // 3. Procesar los resultados
        Poll poll = null;
        Map<String, Option> optionMap = new HashMap<>();

        for (PollItem item : items) {
            String sk = item.getSk();
            if (item.getSk().equals("METADATA")) {
                // Mapear el ítem de metadata al objeto Pool
                poll = Poll.builder()
                        .pollId(item.getPk())
                        .question(item.getQuestion())
                        .active(item.getActive()) // si lo mapeaste
                        .dateClosed(item.getDateClosed())
                        .build();
            }else if (item.getSk().startsWith("OP") && !item.getSk().endsWith("VOTE")) {
                // Mapear el ítem de opción
                optionMap.put(
                        sk,
                        Option.builder()
                                .optionId(sk)
                                .text(item.getText())
                                .votes(0)
                                .build()
                );
            }else if (sk != null && sk.endsWith("VOTE")) {
                String optionId = sk.replace("_VOTE", "");
                optionMap.computeIfPresent(
                        optionId,
                        (k, opt) -> opt.toBuilder().votes(item.getVotes()).build()
                );
            }
        }

        if(poll != null && !optionMap.isEmpty()){
            poll.setOptions(new ArrayList<>(optionMap.values()));
        }

        return poll;
    }
}