package dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankAccountDTO implements Serializable {

    private String id;
    private BigDecimal balance;

    public BankAccountDTO() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
