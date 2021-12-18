/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 *
 *  Repository for CustomerTokens objects
 */
package infrastructure.repositories;

import dto.CustomerTokens;
import dto.Token;
import exceptions.CustomerHasNoTokensException;
import exceptions.CustomerNotFoundException;
import exceptions.TokenNotFoundException;
import infrastructure.repositories.interfaces.ICustomerTokensRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CustomerTokensRepository implements ICustomerTokensRepository {

    private static CustomerTokensRepository instance;
    private final List<CustomerTokens> customerTokens = new ArrayList<>();

    private CustomerTokensRepository() {

    }

    public static CustomerTokensRepository getInstance() {
        if (instance == null) {
            instance = new CustomerTokensRepository();
        }
        return instance;
    }

    @Override
    public void add(CustomerTokens customerToken) {
        this.customerTokens.add(customerToken);
    }

    @Override
    public CustomerTokens get(String customerId) throws CustomerNotFoundException {
        CustomerTokens customerTokens = this.customerTokens.stream()
                .filter(obj -> obj.getCustomerId().equals(customerId))
                .findAny()
                .orElse(null);

        if (customerTokens == null) {
            throw new CustomerNotFoundException(customerId);
        }

        return customerTokens;
    }

    @Override
    public CustomerTokens getCustomerWithTokenId(String tokenId) throws TokenNotFoundException {
        CustomerTokens customerToken = this.customerTokens.stream()
                .filter(obj -> findTokenInList(tokenId, obj))
                .findAny()
                .orElse(null);

        if (customerToken == null) {
            throw new TokenNotFoundException(tokenId);
        }

        return customerToken;
    }

    @Override
    public void deleteCustomer(String id) throws CustomerNotFoundException {
        customerTokens.remove(get(id));
    }

    @Override
    public Token getTokenFromCustomer(String customerId) throws CustomerNotFoundException, CustomerHasNoTokensException {
        Logger logger = Logger.getLogger(CustomerTokensRepository.class.getName());
        if (!get(customerId).getTokens().isEmpty()) {
            logger.severe("get token cus" + get(customerId));
            System.out.println("get token cus" + get(customerId));
            Token t = get(customerId).getTokens().get(0);
            System.out.println("token " + t);
            logger.severe("token " + t);
            return t;
        } else {
            throw new CustomerHasNoTokensException(customerId);
        }
    }

    private boolean findTokenInList(String tokenId, CustomerTokens customerTokens) {
        Token result = customerTokens.getTokens().stream()
                .filter(obj -> obj.getId()
                        .equals(tokenId))
                .findAny()
                .orElse(null);
        return result != null;
    }

}