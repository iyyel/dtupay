/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 *
 * Repository for transactions
 */

package infrastructure.repositories;

import dto.AccountInformation;
import dto.Transaction;
import dto.TransactionDTO;
import exceptions.transaction.TransactionException;
import infrastructure.repositories.interfaces.ITransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepository implements ITransactionRepository {

    private List<TransactionDTO> transactions;

    private static TransactionRepository instance = null;

    private TransactionRepository() {
        transactions = new ArrayList<>();
    }

    public static TransactionRepository getInstance() {
        if (instance == null)
            instance = new TransactionRepository();
        return instance;
    }

    @Override
    public void add(TransactionDTO obj) throws TransactionException {
        if (obj.getCreditor() != null || obj.getDebtor() != null || obj.getAmount() != null) {
            transactions.add(obj);
        }
    }

    @Override
    public List<TransactionDTO> getAll() {
        return transactions;
    }

    @Override
    public void dropEverything() {
        transactions = new ArrayList<>();
    }



}