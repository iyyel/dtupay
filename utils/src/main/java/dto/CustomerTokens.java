/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 *
 *  Object for a given customer and its tokens
 */
package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerTokens implements Serializable {

    private List<Token> tokens;
    private String customerId;

    public CustomerTokens() {

    }

    public CustomerTokens(String customerId) {
        this.customerId = customerId;
        this.tokens = new ArrayList<>();
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Token t : tokens)
            sb.append(t).append("\n");

        return "CustomerTokens{" +
                "tokens=" + sb.toString() +
                ", customerId='" + customerId + '\'' +
                '}';
    }

}