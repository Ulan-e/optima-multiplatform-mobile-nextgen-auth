package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.InvoicePaymentImpl;

/**
  Created by Timur on 31.07.2017.
 */

public interface InvoicePayment {
    void invoicePayment(Context context, JSONObject body);
    void registerCallBack(InvoicePaymentImpl.Callback callback);
}
