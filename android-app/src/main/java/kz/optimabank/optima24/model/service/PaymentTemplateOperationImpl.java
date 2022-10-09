package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.PaymentTemplateOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 23.05.2017.
 */

public class PaymentTemplateOperationImpl extends GeneralService implements PaymentTemplateOperation {
    private CallbackOperationPayment callback;
    private CallbackChangePayment callbackChangePayment;

    @Override
    public void deletePaymentTemplate(Context context, int templateId) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().deletePaymentTemplate(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                templateId, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        callback.deletePaymentTemplateResponse(code, errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.deletePaymentTemplateResponse(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void changePaymentTemplate(Context context, JSONObject body, int templateId, boolean processAfterSaving) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().changePaymentTemplate(context, HeaderHelper.getOpenSessionHeader(context, sessionId), body,
                templateId, processAfterSaving, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if(callbackChangePayment!=null) {
                            callbackChangePayment.onChangePaymentTemplateResponse(code, errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if(callbackChangePayment!=null) {
                            callbackChangePayment.onChangePaymentTemplateResponse(CONNECTION_ERROR_STATUS, null);
                        }
                    }
                });
    }

    @Override
    public void changeActivePaymentTemplate(Context context, int templateId, boolean IsActive) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().changeActivePaymentTemplate(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                templateId, IsActive, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if (callback!=null){
                            callback.changeActivePaymentTemplate(code,errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callback!=null){
                            callback.changeActivePaymentTemplate(CONNECTION_ERROR_STATUS,null);
                        }
                    }
                });
    }

    @Override
    public void quickPayment(Context context, int templateId) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().quickPayment(context, HeaderHelper.getOpenSessionHeader(context, sessionId), templateId,
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMEssage, int code) {
                        if(callback!=null) {
                            callback.quickPaymentResponse(code, errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if(callback!=null) {
                            callback.quickPaymentResponse(CONNECTION_ERROR_STATUS, null);
                        }
                    }
                });
    }

    @Override
    public void registerCallBack(CallbackOperationPayment callback) {
        this.callback = callback;
    }

    @Override
    public void registerCallBackChange(CallbackChangePayment callback) {
        this.callbackChangePayment = callback;
    }

    public interface CallbackOperationPayment {
        void deletePaymentTemplateResponse(int statusCode, String errorMessage);
        void quickPaymentResponse(int statusCode, String errorMessage);
        void changeActivePaymentTemplate(int statusCode, String errorMessage);
    }

    public interface CallbackChangePayment {
        void onChangePaymentTemplateResponse(int statusCode, String errorMessage);
    }
}
