package com.fernando.vote.poolget.repository;

public interface CacheRepository {
    String getSet(String key);
    String createSet(String key, String value);
    String createSet(String key, String value,long ttl);
    Boolean existsKey(String key);

}
