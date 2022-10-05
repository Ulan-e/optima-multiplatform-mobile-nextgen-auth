package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
  Created by Timur on 17.07.2017.
 */

public class Invoices implements Serializable {
    @SerializedName("Amount")
    private String amount;
    @SerializedName("SubscriptionId")
    private int subscriptionId;
    @SerializedName("Id")
    private long invoiceId;
    @SerializedName("Status")
    private int status;

    public String getAmount() {
        return amount;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public int getStatus() {
        return status;
    }
}
