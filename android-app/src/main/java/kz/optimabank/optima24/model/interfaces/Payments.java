package kz.optimabank.optima24.model.interfaces;

import android.content.Context;
import org.json.JSONObject;
import kz.optimabank.optima24.model.service.PaymentsImpl;
import kz.optimabank.optima24.model.service.TransferImpl;

/**
  Created by Timur on 17.05.2017.
 */

public interface Payments {
    void checkPayments(Context context,boolean isCheckBalance, JSONObject body);
    void confirmPayments(Context context,JSONObject body);
    void getTaxDict(Context context);
    void registerCheckCallBack(PaymentsImpl.CallbackCheck callback);
    void registerPaymentConfirmCallBack(PaymentsImpl.CallbackConfirmPayment callback);

}
