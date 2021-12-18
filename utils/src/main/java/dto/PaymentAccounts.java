package dto;

import java.io.Serializable;

public class PaymentAccounts implements Serializable {

    private UserAccountDTO customer;
    private UserAccountDTO merchant;
    private String token;
    private int amount;

    public PaymentAccounts() {

    }

    public UserAccountDTO getCustomer() {
        return customer;
    }

    public void setCustomer(UserAccountDTO customer) {
        this.customer = customer;
    }

    public UserAccountDTO getMerchant() {
        return merchant;
    }

    public void setMerchant(UserAccountDTO merchant) {
        this.merchant = merchant;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}