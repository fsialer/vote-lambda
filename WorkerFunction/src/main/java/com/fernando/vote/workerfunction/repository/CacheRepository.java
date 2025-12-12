package com.fernando.vote.workerfunction.repository;

import java.util.Map;

public interface CacheRepository {
    Map<String,String> getHashSet(String key);
}
