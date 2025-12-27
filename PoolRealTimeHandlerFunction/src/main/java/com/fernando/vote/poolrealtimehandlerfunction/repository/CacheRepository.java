package com.fernando.vote.poolrealtimehandlerfunction.repository;

import java.util.Map;

public interface CacheRepository {
    Map<String,String> getHashSet(String key);
}
