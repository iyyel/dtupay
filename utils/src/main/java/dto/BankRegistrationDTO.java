package dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankRegistrationDTO implements Serializable {

    private BigDecimal balance;

    public BankRegistrationDTO() {

    }

    public BankRegistrationDTO(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BankRegistrationDTO{" +
                "balance=" + balance +
                '}';
    }

}