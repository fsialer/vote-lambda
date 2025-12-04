package surveycreate.repository;

import surveycreate.models.Option;

import java.util.List;

public interface OptionRepository {
    void saveOptions(String questionId, List<Option> options);
}
