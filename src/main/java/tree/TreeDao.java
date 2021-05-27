package tree;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ivan.yuriev
 */
public interface TreeDao<T> extends Dao<T> {

    List<T> getByName(String name);

    Map<Integer, List<T>> getAllChildren(T t);

    Map<Integer, T> getAllParents(T t);

    String getPath(T t);

    T getRoot(T t);

    void add(T parentNode, List<T> nodes);

    void move(T parentNode, T subNode);
}
