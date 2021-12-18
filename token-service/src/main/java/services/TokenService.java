/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 *
 *  Main service for token, handles business logic
 */

package services;

import dto.CustomerTokens;
import dto.Token;
import exceptions.CustomerHasNoTokensException;
import exceptions.CustomerNotFoundException;
import exceptions.TokenNotFoundException;
import exceptions.TooManyTokensException;
import infrastructure.repositories.CustomerTokensRepository;
import org.jboss.logmanager.Level;
import services.interfaces.ITokenService;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class TokenService implements ITokenService {

    private final CustomerTokensRepository repo = CustomerTokensRepository.getInstance();

    Logger logger = Logger.getLogger(TokenService.class.getName());

    @Override
    public void registerCustomer(String customerId) {
        CustomerTokens customerToken = new CustomerTokens(customerId);
        repo.add(customerToken);
    }

    @Override
    public String requestTokens(String customerId, int amount) throws CustomerNotFoundException, TooManyTokensException {
        logger.log(Level.SEVERE, "REQUEST TOKENS: CustomerId: " + customerId + ", amount: " + amount);

        if (!customerExists(customerId)) {
            logger.log(Level.SEVERE, "CUSTOMER DID NOT EXIST! REGISTERING NOW!");
            registerCustomer(customerId);
        }

        logger.log(Level.SEVERE, "ADDING TOKENS TO CUSTOMER!");
        addTokens(repo.get(customerId), amount);

        StringBuilder sb = new StringBuilder();
        for (Token token : repo.get(customerId).getTokens()) {
            sb.append(token.getId()).append("\n");
        }
        logger.log(Level.SEVERE, "TOKENS AFTER addTokens (outside method): " + sb.toString());

        return customerId;
    }

    @Override
    public Token getToken(String customerId) throws CustomerNotFoundException, CustomerHasNoTokensException {
        return repo.getTokenFromCustomer(customerId);
    }

    @Override
    public CustomerTokens getCustomerFromToken(String tokenId) throws TokenNotFoundException {
        return repo.getCustomerWithTokenId(tokenId);
    }

    @Override
    public Token validateToken(String tokenId) throws TokenNotFoundException {
        return validateTokenLogic(tokenId, repo.getCustomerWithTokenId(tokenId));
    }

    @Override
    public String deleteCustomer(String customerId) throws CustomerNotFoundException {
        repo.deleteCustomer(customerId);
        return customerId;
    }

    @Override
    public CustomerTokens getCustomer(String customerId) throws CustomerNotFoundException {
        return repo.get(customerId);
    }

    @Override
    public boolean customerExists(String customerId) {
        try {
            repo.get(customerId);
        } catch (CustomerNotFoundException e) {
            return false;
        }
        return true;
    }

    private void addTokens(CustomerTokens customerTokens, Integer amount)
            throws TooManyTokensException {

        StringBuilder sb = new StringBuilder();
        for (Token token : customerTokens.getTokens()) {
            sb.append(token.getId()).append("\n");
        }
        logger.log(Level.SEVERE, "TOKENS BEFORE addTokens: " + sb.toString());


        // Only allowed to request 1-5 tokens if you have one or less tokens
        if (customerTokens.getTokens().size() < 2 && amount < 6 && amount > 0) {
            customerTokens.getTokens().addAll(generateTokens(amount));
        } else {
            throw new TooManyTokensException(customerTokens.getCustomerId(), amount);
        }


        sb = new StringBuilder();
        for (Token token : customerTokens.getTokens()) {
            sb.append(token.getId()).append("\n");
        }
        logger.log(Level.SEVERE, "TOKENS AFTER addTokens (inside method): " + sb.toString());
    }

    private String generateTokenId() {
        return String.valueOf(Math.random() * 6972593);
    }

    private List<Token> generateTokens(Integer amount) {
        List<Token> newTokens = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            newTokens.add(new Token(generateTokenId()));
        }
        return newTokens;
    }

    private Token validateTokenLogic(String tokenId, CustomerTokens customerTokens) {

        StringBuilder sb = new StringBuilder();
        for (Token token : customerTokens.getTokens()) {
            sb.append(token.getId()).append("\n");
        }
        logger.log(Level.SEVERE, "TOKENS inside validateTokenLogic: " + sb.toString());


        Token token = customerTokens.getTokens().stream()
                .filter(obj -> obj.getId().equals(tokenId))
                .findAny()
                .orElse(null);

        customerTokens.getTokens().removeIf(obj -> obj.getId().equals(tokenId));

        return token;
    }

}