/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 */

package infrastructure.repositories.interfaces;

import dto.TransactionDTO;
import exceptions.transaction.TransactionException;

public interface ITransactionRepository extends IRepository<TransactionDTO> {
    void add(TransactionDTO obj) throws TransactionException;
    void dropEverything();
}