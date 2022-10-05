package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.HistoryImpl;

/**
  Created by Timur on 04.06.2017.
 */

public interface History {
    void getPaymentHistory(Context context, String fromDate, String toDate);
    void getTransferHistory(Context context, String fromDate, String toDate);
    void registerPaymentCallBack(HistoryImpl.CallbackPaymentHistory callback);
    void registerTransferCallBack(HistoryImpl.CallbackTransferHistory callback);
}
