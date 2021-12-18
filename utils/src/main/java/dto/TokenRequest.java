package dto;

import java.io.Serializable;

public class TokenRequest implements Serializable {

    private String id;
    private int amount;

    public TokenRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
