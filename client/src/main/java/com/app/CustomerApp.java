package com.app;

import com.client.DTUPayClient;
import dto.Token;

/**
 * primary-author: Troels (s161791)
 * co-author: Daniel (s151641)
 */
public class CustomerApp {

    private final DTUPayClient dtuPay = new DTUPayClient();

    public Token getToken(String customerId) {
        return dtuPay.getToken(customerId);
    }

    public boolean requestTokens(String customerId, int amount) {
        return dtuPay.requestTokens(customerId, amount);
    }

}
