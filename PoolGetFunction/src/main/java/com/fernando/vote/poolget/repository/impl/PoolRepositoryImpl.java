package com.fernando.vote.poolget.repository.impl;

//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.model.*;
import com.fernando.vote.poolget.config.DynamoConfig;
import com.fernando.vote.poolget.models.Option;
import com.fernando.vote.poolget.models.Pool;
import com.fernando.vote.poolget.models.PoolItem;
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
    public Pool  getPoolByIdWithDetails(String pk) {
        // ASUMIMOS que tienes inicializado el enhancedClient
        DynamoDbTable<PoolItem> poolTable = enhancedClient.table(tableName, TableSchema.fromBean(PoolItem.class));

        // 1. Definir la condición de consulta (Solo por PK)
        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(pk).build()
        );

        // 2. Ejecutar la consulta y paginar automáticamente (Lazy loading)
        // El método query() regresa un PageIterable que maneja la paginación internamente.
        // Solo se realizan llamadas a DynamoDB cuando se itera sobre los resultados.
        Iterable<PoolItem> items = poolTable.query(r -> r.queryConditional(queryConditional)).items();

        // 3. Procesar los resultados
        Pool pool = null;
        Set<Option> list = new HashSet<>();

        for (PoolItem item : items) {
            if (item.getSk().equals("METADATA")) {
                // Mapear el ítem de metadata al objeto Pool
                pool = Pool.builder()
                        .poolId(item.getPk())
                        .question(item.getQuestion())
                        .active(item.getActive()) // si lo mapeaste
                        .dateClosed(item.getDateClosed())
                        .build();
            } else if (item.getSk().startsWith("OP") && !item.getSk().endsWith("VOTE")) {
                // Mapear el ítem de opción
                list.add(Option.builder()
                        .optionId(item.getSk())
                        .text(item.getText()) // Asumiendo que el texto está en 'question'
                        .build());
            }
        }

        if (pool == null) {
            throw new NoSuchElementException("Pool not found: " + pk);
        }

        pool.setOptions(list.stream().toList());
        return pool;
    }
}