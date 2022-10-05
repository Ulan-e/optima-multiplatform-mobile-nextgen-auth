package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kz.optimabank.optima24.model.base.TemplatesPayment;

/**
  Created by Timur on 25.04.2017.
 */

public class PaymentTemplateResponse {
    @SerializedName("Subscriptions")
    public ArrayList<TemplatesPayment> templatesPayments;
    @SerializedName("Invoices")
    public ArrayList<Invoices> invoices;
}
