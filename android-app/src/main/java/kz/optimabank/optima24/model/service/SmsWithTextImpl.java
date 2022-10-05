package kz.optimabank.optima24.model.service;

import android.content.Context;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.SmsWithTextSend;
import kz.optimabank.optima24.model.manager.GeneralManager;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class SmsWithTextImpl implements SmsWithTextSend {
    SmsSendWithTextCallback smsSendWithTextCallback;
    SmsSendWithOperationCodeCallback smsSendWithOperationCodeCallback;
    SmsSendWithTextForPaymentCallback smsSendWithTextForPaymentCallback;

    private IsOtpKeyValidCallback isOtpKeyValidCallback;
    private SendOtpWithTextCallback sendOtpWithTextCallback;

    @Override
    public void sendSmsWithText(Context context, int otpKey, String amountWithCurrency, String operationCode) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().confirmationCodeWithKeyAmountAndCurrencyRequest(context,
                OptimaBank.getInstance().getOpenSessionHeader(sessionId), otpKey, amountWithCurrency, operationCode, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if (smsSendWithTextCallback != null) {
                            smsSendWithTextCallback.onSmsTextResponse(code, errorMessage, code);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (smsSendWithTextCallback != null) {
                            smsSendWithTextCallback.onSmsTextResponse(CONNECTION_ERROR_STATUS, null, null);
                        }

                    }
                });
    }

    @Override
    public void sendSmsWithTextForPayment(Context context, int otpKey, String amount, String operationCode) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().confirmationCodeWithKeyAmountAndCurrencyRequest(context,
                OptimaBank.getInstance().getOpenSessionHeader(sessionId), otpKey, amount, operationCode, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if (smsSendWithTextForPaymentCallback != null) {
                            smsSendWithTextForPaymentCallback.onSmsOperationForPaymentResponse(code, errorMessage, code);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (smsSendWithTextForPaymentCallback != null) {
                            smsSendWithTextForPaymentCallback.onSmsOperationForPaymentResponse(CONNECTION_ERROR_STATUS, null, null);
                        }

                    }
                });
    }


    @Override
    public void sendSmsWithOperationCode(Context context, int otpKey, String amount, String operationCode) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().confirmationCodeWithKeyAmountAndCurrencyRequest(context,
                OptimaBank.getInstance().getOpenSessionHeader(sessionId), otpKey, amount, operationCode, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if (smsSendWithOperationCodeCallback != null) {
                            smsSendWithOperationCodeCallback.onSmsOperationCodeResponse(code, errorMessage, code);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (smsSendWithOperationCodeCallback != null) {
                            smsSendWithOperationCodeCallback.onSmsOperationCodeResponse(CONNECTION_ERROR_STATUS, null, null);
                        }

                    }
                });
    }

    @Override
    public void sendSmsForOtpValidation(Context context, int otpKey, String smsCode) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().checkIsOtpKeyValid(context,
                OptimaBank.getInstance().getOpenSessionHeader(sessionId), otpKey, smsCode,
                (response, errorMessage, code) -> {
                    if (isOtpKeyValidCallback != null) {
                        isOtpKeyValidCallback.setResponseIsOtpKeyValid(code, errorMessage);
                    }
                }, () -> {
                    if (isOtpKeyValidCallback != null) {
                        isOtpKeyValidCallback.setResponseIsOtpKeyValid(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void sendOtpWithText(Context context, int otpKey) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().sendSMS(context,
                OptimaBank.getInstance().getOpenSessionHeader(sessionId), otpKey,
                (response, errorMessage, code) -> {
                    if (sendOtpWithTextCallback != null) {
                        sendOtpWithTextCallback.setResponseSendOtpWithText(code, errorMessage);
                    }
                }, () -> {
                    if (sendOtpWithTextCallback != null) {
                        sendOtpWithTextCallback.setResponseSendOtpWithText(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void registerSmsSendOtpKeyValidation(IsOtpKeyValidCallback callback) {
        this.isOtpKeyValidCallback = callback;
    }

    @Override
    public void registerSmsWithTextForPayment(SmsSendWithTextForPaymentCallback smsSendWithTextForPaymentCallback) {
        this.smsSendWithTextForPaymentCallback = smsSendWithTextForPaymentCallback;
    }

    @Override
    public void registerSmsWithTextCallBack(SmsSendWithTextCallback smsSendWithTextCallback) {
        this.smsSendWithTextCallback = smsSendWithTextCallback;
    }

    @Override
    public void registerSmsWithOperationCodeCallBack(SmsSendWithOperationCodeCallback smsSendWithOperationCodeCallback) {
        this.smsSendWithOperationCodeCallback = smsSendWithOperationCodeCallback;
    }

    @Override
    public void registerOtpKeyValidation(SendOtpWithTextCallback sendOtpWithTextCallback) {
        this.sendOtpWithTextCallback = sendOtpWithTextCallback;
    }

    public interface SmsSendWithTextCallback {
        void onSmsTextResponse(int statusCode, String errorMessage, Integer errorCode);
    }

    public interface SmsSendWithOperationCodeCallback {
        void onSmsOperationCodeResponse(int statusCode, String errorMessage, Integer errorCode);
    }

    public interface SmsSendWithTextForPaymentCallback {
        void onSmsOperationForPaymentResponse(int statusCode, String errorMessage, Integer errorCode);
    }

    public interface SendOtpWithTextCallback {
        void setResponseSendOtpWithText(int statusCode, String errorMessage);
    }

    public interface IsOtpKeyValidCallback {
        void setResponseIsOtpKeyValid(int statusCode, String errorMessage);
    }
}