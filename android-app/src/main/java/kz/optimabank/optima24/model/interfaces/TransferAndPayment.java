package kz.optimabank.optima24.model.interfaces;

import android.content.Context;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;

/**
  Created by Timur on 14.04.2017.
 */

public interface TransferAndPayment {
    void getPaymentContext(Context context);
    void getPaymentSubscriptions(Context context);
    void getTransferTemplate(Context context);
    void registerCallBack(TransferAndPaymentImpl.Callback callback);
    void registerUpdateCallBack(TransferAndPaymentImpl.UpdateCallback callback);
}
