package com.client;

import dto.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * primary-author: Daniel (s151641)
 * co-author: Troels (s161791)
 */
public class DTUPayClient {

    private final WebTarget baseUrl;

    public DTUPayClient() {
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/api/v1/");
    }

    public String registerAccount(UserRegistrationDTO registrationDTO) {
        Response r = baseUrl.path("account")
                .request()
                .post(Entity.entity(registrationDTO, MediaType.APPLICATION_JSON_TYPE));

        if (r.getStatus() == 200) {
            String result = r.readEntity(new GenericType<>() {
            });

            return result.length() == 0 ? null : result;
        }

        return null;
    }

    public UserAccountDTO getAccount(String id) {
        Response r = baseUrl.path("account/" + id)
                .request()
                .get();

        if (r.getStatus() == 200) {
            return r.readEntity(new GenericType<>() {
            });
        }

        return null;
    }

    public UserAccountDTO getAccountByCpr(String cpr) {
        Response r = baseUrl.path("account/by-cpr/" + cpr)
                .request()
                .get();

        if (r.getStatus() == 200) {
            return r.readEntity(new GenericType<>() {
            });
        }

        return null;
    }

    public boolean retireAccount(String id) {
        Response r = baseUrl.path("account/" + id)
                .request()
                .delete(new GenericType<>() {
                });
        return r.getStatus() == Response.Status.OK.getStatusCode();
    }

    public boolean retireAccountByCpr(String cpr) {
        Response r = baseUrl.path("account/by-cpr/" + cpr)
                .request()
                .delete(new GenericType<>() {
                });
        return r.getStatus() == Response.Status.OK.getStatusCode();
    }

    public List<UserAccountDTO> getAllAccounts() {
        Response r = baseUrl.path("account")
                .request()
                .get();

        if (r.getStatus() == 200) {
            return r.readEntity(new GenericType<>() {
            });
        }

        return null;
    }

    public boolean processPayment(PaymentRequest paymentRequest) {
        Response r = baseUrl.path("payment")
                .request()
                .post(Entity.entity(paymentRequest, MediaType.APPLICATION_JSON_TYPE));
        return r.getStatus() == Response.Status.OK.getStatusCode();
    }

    public boolean requestTokens(String id, int amount) {
        TokenRequest request = new TokenRequest();
        request.setAmount(amount);
        request.setId(id);

        Response r = baseUrl.path("token")
                .request()
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        return r.getStatus() == Response.Status.OK.getStatusCode();
    }

    public Token getToken(String id) {
        Response r = baseUrl.path("token/" + id).request().get();
        Token t = r.readEntity(Token.class);
        return t;
    }

    public boolean validateToken(String tokenId) {
        Response r = baseUrl.path("token/validate/" + tokenId)
                .request()
                .post(null);
        return r.getStatus() == Response.Status.OK.getStatusCode();
    }

    public boolean retireCustomerTokens(String id) {
        Response r = baseUrl.path("token/" + id).request().delete();
        return r.getStatus() == Response.Status.OK.getStatusCode();
    }

    public boolean refund(PaymentRequest paymentRequest) {
        Response r = baseUrl.path("payment/refund")
                .request()
                .post(Entity.entity(paymentRequest, MediaType.APPLICATION_JSON_TYPE));
        return r.getStatus() == Response.Status.OK.getStatusCode();
    }
}