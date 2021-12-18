package com.client;

import dto.MoneySummary;
import dto.TransactionDTO;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * primary-author: Daniel (s151641)
 * co-author: Troels (s161791)
 */
public class ReportClient {

    private final WebTarget baseUrl;

    public ReportClient() {
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8083/api/v1/");
    }

    public MoneySummary getAllTransactions() {
        Response r = baseUrl.path("management").request().get();

        if (r.getStatus() == 200) {
            return r.readEntity(new GenericType<>() {
            });
        }

        return null;
    }

    public List<TransactionDTO> getCustomerReport(String id, Date startDate, Date endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
        String startDateStr = formatter.format(startDate);
        String endDateStr = formatter.format(endDate);

        Response r = baseUrl.path("management/report/customer/" + id)
                .queryParam("start", startDateStr)
                .queryParam("end", endDateStr)
                .request()
                .get();

        if (r.getStatus() == 200) {
            return r.readEntity(new GenericType<>() {
            });
        }

        return null;
    }

    public List<TransactionDTO> getMerchantReport(String id, Date startDate, Date endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
        String startDateStr = formatter.format(startDate);
        String endDateStr = formatter.format(endDate);

        Response r = baseUrl.path("management/report/merchant/" + id)
                .queryParam("start", startDateStr)
                .queryParam("end", endDateStr)
                .request()
                .get();

        if (r.getStatus() == 200) {
            return r.readEntity(new GenericType<>() {
            });
        }

        return null;
    }

}