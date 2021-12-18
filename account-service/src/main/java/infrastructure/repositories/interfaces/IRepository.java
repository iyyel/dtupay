package infrastructure.repositories.interfaces;

import java.util.List;

/**
 * @primary-author Troels (s161791)
 * @co-author Daniel (s151641)
 */
public interface IRepository<T> {
    void add(T obj);
    List<T> getAll();
}