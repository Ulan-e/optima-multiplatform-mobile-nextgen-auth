package kz.optimabank.optima24.model.interfaces;

        import android.content.Context;

        import kz.optimabank.optima24.model.service.SmsSendImpl;

/**
 Created by Timur on 04.06.2017.
 */

public interface SmsSend {
    void sendSms(Context context, int otpKey);
    void registerSmsCallBackForTransfer(SmsSendImpl.TransferCallback callback);
    void registerSmsCallBackForPayment(SmsSendImpl.PaymentCallback callback);
    void registerSmsCallBackForCreateTemplate(SmsSendImpl.CreateTemplateCallback callback);
    void registerSmsCallBackForPaymentTemplate(SmsSendImpl.ChangePaymentTemplateCallback callback);

}
