/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 */

package infrastructure.repositories.interfaces;

import dto.TransactionDTO;

import java.util.List;

// Simple and basic repository that provides some CRUD functionality.
public interface IRepository<T> {
    List<TransactionDTO> getAll();
}
