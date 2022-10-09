package kz.optimabank.optima24.model.service;

import android.content.Context;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.InvoiceContainerItem;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.interfaces.Invoice;
import kz.optimabank.optima24.model.manager.GeneralManager;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 17.07.2017.
 */

public class InvoiceImpl extends GeneralService implements Invoice {
    Callback callback;

    @Override
    public void getInvoice(Context context, long invoiceId) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getInvoice(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                invoiceId, new NetworkResponse.SuccessRequestListener<InvoiceContainerItem>() {
                    @Override
                    public void onSuccess(InvoiceContainerItem response, ResponseBody errorBody, int httpStatusCode) {
                        if(httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        callback.jsonInvoiceResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonInvoiceResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void registerCallBack(InvoiceImpl.Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonInvoiceResponse(int statusCode, String errorMessage,InvoiceContainerItem response);
    }
}
