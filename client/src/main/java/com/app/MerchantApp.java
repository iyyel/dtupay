package com.app;

import com.client.DTUPayClient;
import dto.PaymentRequest;
import dto.Token;

/**
 * primary-author: Troels (s161791)
 * co-author: Daniel (s151641)
 */
public class MerchantApp {

    private final DTUPayClient dtuPay = new DTUPayClient();
    private final CustomerApp customerApp = new CustomerApp();

    public Token requestTokenFromCustomer(String customerId) {
        return customerApp.getToken(customerId);
    }

    public boolean processPayment(PaymentRequest paymentRequest) {
        return dtuPay.processPayment(paymentRequest);
    }

    public boolean refund(PaymentRequest paymentRequest) {
        return dtuPay.refund(paymentRequest);
    }

}