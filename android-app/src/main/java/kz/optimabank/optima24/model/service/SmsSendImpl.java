package kz.optimabank.optima24.model.service;

import android.content.Context;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.interfaces.SmsSend;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
 * Created by Timur on 04.06.2017.
 */

public class SmsSendImpl extends GeneralService implements SmsSend {

    private TransferCallback transferCallback;
    private PaymentCallback paymentCallback;
    private CreateTemplateCallback createTemplateCallback;
    private ChangePaymentTemplateCallback changePaymentTemplateCallback;


    @Override
    public void sendSms(Context context, int otpKey) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().sendSMS(context,
                HeaderHelper.getOpenSessionHeader(context, sessionId), otpKey,new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if (transferCallback != null) {
                            transferCallback.jsonSmsResponse(code, errorMessage, code);
                        }
                        if (paymentCallback != null) {
                            paymentCallback.onSmsCodeResponse(code, errorMessage, code);
                        }
                        if (createTemplateCallback != null) {
                            createTemplateCallback.onCreateTemplateSmsCodeResponse(code,errorMessage,code);
                        }
                        if (changePaymentTemplateCallback!=null){
                            changePaymentTemplateCallback.onSmSCodeForPaymentTemplate(code,errorMessage,code);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (transferCallback != null) {
                            transferCallback.jsonSmsResponse(CONNECTION_ERROR_STATUS, null, null);
                        }
                        if (paymentCallback != null) {
                            paymentCallback.onSmsCodeResponse(CONNECTION_ERROR_STATUS, null, null);
                        }
                        if (createTemplateCallback!=null){
                            createTemplateCallback.onCreateTemplateSmsCodeResponse(CONNECTION_ERROR_STATUS, null, null);
                        }
                        if (changePaymentTemplateCallback!=null){
                            changePaymentTemplateCallback.onSmSCodeForPaymentTemplate(CONNECTION_ERROR_STATUS,null,null);
                        }
                    }
                });
    }

    @Override
    public void registerSmsCallBackForTransfer(TransferCallback transferCallback) {
        this.transferCallback = transferCallback;
    }

    @Override
    public void registerSmsCallBackForPayment(PaymentCallback callback) {
        this.paymentCallback = callback;
    }

    @Override
    public void registerSmsCallBackForCreateTemplate(CreateTemplateCallback callback) {
        this.createTemplateCallback = callback;
    }

    @Override
    public void registerSmsCallBackForPaymentTemplate(ChangePaymentTemplateCallback callback) {
        this.changePaymentTemplateCallback= callback;
    }


    public interface TransferCallback {
        void jsonSmsResponse(int statusCode, String errorMessage, Integer errorCode);

        void checkMt100TransferResponse(int statusCode, CheckResponse response, String errorMessage);
    }

    public interface PaymentCallback {
        void onSmsCodeResponse(int statusCode, String errorMessage, Integer errorCode);
    }
    public interface ChangePaymentTemplateCallback{
        void onSmSCodeForPaymentTemplate(int statusCode, String errorMessage, Integer errorCode);
    }

    public interface CreateTemplateCallback {
        void onCreateTemplateSmsCodeResponse(int statusCode, String errorMessage, Integer errorCode);
    }
}
