/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package services.interfaces;

import dto.CustomerTokens;
import dto.Token;
import exceptions.*;
import exceptions.token.InvalidTokenException;

public interface ITokenService {
    void registerCustomer(String customerId) throws CustomerAlreadyRegisteredException;
    String requestTokens(String customerId, int amount) throws CustomerNotFoundException, TooManyTokensException, CustomerAlreadyRegisteredException;
    Token getToken(String customerId) throws CustomerNotFoundException, CustomerHasNoTokensException;
    CustomerTokens getCustomerFromToken(String tokenId) throws TokenNotFoundException, CustomerNotFoundException;
    Token validateToken(String tokenId) throws CustomerNotFoundException, TokenNotFoundException, InvalidTokenException;
    String deleteCustomer(String customerId) throws CustomerNotFoundException;
    CustomerTokens getCustomer(String customerId) throws CustomerNotFoundException;
    boolean customerExists(String customerId);
}
