package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.InvoiceImpl;

/**
  Created by Timur on 17.07.2017.
 */

public interface Invoice {
    void getInvoice(Context context, long invoiceId);
    void registerCallBack(InvoiceImpl.Callback callback);
}
