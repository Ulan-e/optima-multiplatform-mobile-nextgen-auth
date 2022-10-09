package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.PaymentTemplateOperationImpl;

/**
  Created by Timur on 23.05.2017.
 */

public interface PaymentTemplateOperation {
    void deletePaymentTemplate(Context context, int templateId);
    void changePaymentTemplate(Context context, JSONObject body, int templateId, boolean processAfterSaving);
    void changeActivePaymentTemplate(Context context, int templateId, boolean IsActive);
    void quickPayment(Context context, int templateId);
    void registerCallBack(PaymentTemplateOperationImpl.CallbackOperationPayment callback);
    void registerCallBackChange(PaymentTemplateOperationImpl.CallbackChangePayment callback);
}
