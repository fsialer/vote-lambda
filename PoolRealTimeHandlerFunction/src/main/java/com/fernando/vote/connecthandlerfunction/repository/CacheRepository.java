package com.fernando.vote.connecthandlerfunction.repository;

import java.util.Map;

public interface CacheRepository {
    Map<String,String> getHashSet(String key);
}
