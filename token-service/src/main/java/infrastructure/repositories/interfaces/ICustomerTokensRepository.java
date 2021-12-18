/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package infrastructure.repositories.interfaces;

import dto.CustomerTokens;
import dto.Token;
import exceptions.CustomerAlreadyRegisteredException;
import exceptions.CustomerHasNoTokensException;
import exceptions.CustomerNotFoundException;
import exceptions.TokenNotFoundException;

public interface ICustomerTokensRepository extends IRepository<CustomerTokens> {
    void add(CustomerTokens customerTokens) throws CustomerAlreadyRegisteredException;
    CustomerTokens get(String customerId) throws CustomerNotFoundException;
    CustomerTokens getCustomerWithTokenId(String tokenId) throws TokenNotFoundException, CustomerNotFoundException;
    void deleteCustomer(String customerId) throws CustomerNotFoundException;
    Token getTokenFromCustomer(String customerId) throws CustomerNotFoundException, CustomerHasNoTokensException;
}