/**
 * @author Kasper (s141250)
 * @author Sebastian (s135243)
 */

package infrastructure.repositories.interfaces;

import dto.AccountInformation;

public interface IAccountRepository extends IRepository<AccountInformation> {
    void clear();
    AccountInformation getById(String id);
    AccountInformation getByCpr(String cpr);
    void remove(String id);
}
