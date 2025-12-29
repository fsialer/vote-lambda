package votecreate.repository;

public interface CacheRepository {
    Long hashIncrement(String key, long value, String attribute);
    String getSet(String key);
    Boolean existsKey(String key);
}
