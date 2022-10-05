package kz.optimabank.optima24.model.gson.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentRegions;
import kz.optimabank.optima24.db.entry.PaymentService;

/**
  Created by Timur on 14.04.2017.
 */

public class PaymentContextResponse {
    @SerializedName("Categories")
    public ArrayList<PaymentCategory> paymentContext;
    @SerializedName("Services")
    public ArrayList<PaymentService> services;
    @SerializedName("Regions")
    public ArrayList<PaymentRegions> regions;
    @SerializedName("Countryes")
    public ArrayList<Country> countries;
}
