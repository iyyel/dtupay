/**
 * @author Kasper (s141250)
 * @author Sebastian (s135243)
 */

package infrastructure.repositories;

import dto.AccountInformation;
import infrastructure.repositories.interfaces.IAccountRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AccountRepository implements IAccountRepository {

    private static AccountRepository instance = null;

    private final List<AccountInformation> accountInformations;

    private AccountRepository() {
        accountInformations = new ArrayList<>();
    }

    public static AccountRepository getInstance() {
        if (instance == null)
            instance = new AccountRepository();
        return instance;
    }

    @Override
    public void clear() {
        accountInformations.clear();
    }

    @Override
    public void add(AccountInformation accountInformation) {
        accountInformations.add(accountInformation);
    }

    @Override
    public AccountInformation getById(String id) {
        return accountInformations.stream()
                .filter(a -> a.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public AccountInformation getByCpr(String cpr) {
        return accountInformations.stream()
                .filter(a -> a.getCpr().equals(cpr))
                .findAny()
                .orElse(null);
    }

    @Override
    public void remove(String id) {
        AccountInformation accountInformation = getById(id);
        if (accountInformation != null) {
            accountInformations.remove(accountInformation);
        }
    }

    @Override
    public List<AccountInformation> getAll() {
        return accountInformations;
    }

}
