package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BankReference;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.gson.response.CheckPaymentsResponse;
import kz.optimabank.optima24.model.gson.response.TaxDictResponse;
import kz.optimabank.optima24.model.interfaces.Payments;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 17.05.2017.
 */

public class PaymentsImpl extends GeneralService implements Payments {
    CallbackCheck callback;
    CallbackConfirmPayment callbackConfirmPayment;

    @Override
    public void checkPayments(Context context, final boolean isCheckBalance, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().checkPayments(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                body, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<CheckPaymentsResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<CheckPaymentsResponse> response, String errorMessage, int code) {
                        if(response != null) {
                            callback.checkPaymentResponse(code, response.data, errorMessage, isCheckBalance);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.checkPaymentResponse(CONNECTION_ERROR_STATUS, null, null, isCheckBalance);
                    }
                });
    }

    @Override
    public void confirmPayments(Context context, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().confirmPayments(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                body, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<BankReference>>() {
                    @Override
                    public void onSuccess(BaseResponse<BankReference> response, String errorMessage, int code) {
                        if(response != null) {
                            if (callbackConfirmPayment != null) {
                                callbackConfirmPayment.onConfirmPaymentResponse(code, response.data, errorMessage);
                            }
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callbackConfirmPayment.onConfirmPaymentResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }


    @Override
    public void getTaxDict(Context context) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getTaxDict(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<TaxDictResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<TaxDictResponse> response, String errorMessage, int code) {
                        callback.taxDictResponse(code, response.data, errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.taxDictResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void registerCheckCallBack(CallbackCheck callback) {
        this.callback = callback;
    }

    @Override
    public void registerPaymentConfirmCallBack(CallbackConfirmPayment callback) {
        this.callbackConfirmPayment = callback;
    }

    public interface CallbackCheck {
        void checkPaymentResponse(int statusCode, CheckPaymentsResponse response, String errorMessage,boolean isBalance);
        void taxDictResponse(int statusCode, TaxDictResponse response, String errorMessage);
    }

    public interface CallbackConfirmPayment {
        void onConfirmPaymentResponse(int statusCode, BankReference response, String errorMessage);
    }
}
