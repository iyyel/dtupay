package dto;
import java.io.Serializable;

public class PaymentRequest implements Serializable {

    private String customerId;
    private String merchantId;
    private int amount;
    private String token;
    private boolean isRefund;

    public PaymentRequest() {

    }

    public PaymentRequest(String customerId, String merchantId, int amount, String token, boolean isRefund) {
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.token = token;
        this.isRefund = isRefund;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRefund() {
        return isRefund;
    }

    public void setRefund(boolean refund) {
        isRefund = refund;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "customerId='" + customerId + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", amount=" + amount +
                ", token='" + token + '\'' +
                ", isRefund=" + isRefund +
                '}';
    }

}
