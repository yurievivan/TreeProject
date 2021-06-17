package tree.dao;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author ivan.yuriev
 */
public interface Dao<T> {

    Optional<T> get(long id);

    List<T> getAll();

    void save(T t);

    void save(List<T> t);

    void delete(T t);
}
