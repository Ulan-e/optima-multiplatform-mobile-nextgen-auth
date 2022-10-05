package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.SmsSendImpl;
import kz.optimabank.optima24.model.service.SmsWithTextImpl;

public interface SmsWithTextSend {
    void sendSmsWithText(Context context, int otpKey, String amount, String operationCode);

    void sendSmsWithTextForPayment(Context context, int otpKey, String amount, String operationCode);

    void registerSmsWithTextForPayment(SmsWithTextImpl.SmsSendWithTextForPaymentCallback smsSendWithTextForPaymentCallback);

    void sendSmsWithOperationCode(Context context, int otpKey, String amount, String operationCode);

    void registerSmsWithTextCallBack(SmsWithTextImpl.SmsSendWithTextCallback smsSendWithTextCallback);

    void registerSmsWithOperationCodeCallBack(SmsWithTextImpl.SmsSendWithOperationCodeCallback smsSendWithOperationCodeCallback);

    // запрос на потверждения нового номера телефона если пользователь сменил номер телефона
    void sendOtpWithText(Context context, int key);

    void registerOtpKeyValidation(SmsWithTextImpl.SendOtpWithTextCallback sendOtpWithTextCallback);

    // потверждение СМС-кода если пользователь сменил номер телефона
    void sendSmsForOtpValidation(Context context, int otpKey, String smsCode);

    void registerSmsSendOtpKeyValidation(SmsWithTextImpl.IsOtpKeyValidCallback callback);
}
