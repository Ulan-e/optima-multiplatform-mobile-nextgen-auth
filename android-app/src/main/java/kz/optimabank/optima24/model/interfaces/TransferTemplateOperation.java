package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.TransferTemplateOperationImpl;

public interface TransferTemplateOperation {
    void deleteTransferTemplate(Context context, int templateId);
    void changeTransferTemplate(Context context, JSONObject body, int templateId);
    void changeActiveTransferTemplate(Context context, int templateId, boolean IsActive);
//    void quickTransfer(Context context, int templateId);
    void registerTransferCallBack(TransferTemplateOperationImpl.CallbackOperationTransfer callback);
    void registerCallBackChange(TransferTemplateOperationImpl.CallbackChangeTransfer callback);
}
