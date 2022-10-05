package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.interfaces.InvoicePayment;
import kz.optimabank.optima24.model.manager.GeneralManager;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 31.07.2017.
 */

public class InvoicePaymentImpl extends GeneralService implements InvoicePayment {
    Callback callback;

    @Override
    public void invoicePayment(Context context, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().invoicePayment(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                body, new NetworkResponse.SuccessRequestListener<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody response, ResponseBody errorBody, int httpStatusCode) {
                        if(httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        callback.jsonInvoicePaymentResponse(httpStatusCode, errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonInvoicePaymentResponse(CONNECTION_ERROR_STATUS, errorMessage);
                    }
                });
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonInvoicePaymentResponse(int statusCode, String errorMessage);
    }
}
