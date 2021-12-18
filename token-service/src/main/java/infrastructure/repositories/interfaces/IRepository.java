/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package infrastructure.repositories.interfaces;

import exceptions.CustomerAlreadyRegisteredException;

public interface IRepository<T> {
    void add(T obj) throws CustomerAlreadyRegisteredException;
}