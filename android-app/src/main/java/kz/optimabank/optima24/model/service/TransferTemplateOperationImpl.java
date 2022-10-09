package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.TransferTemplateOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 26.05.2017.
 */

public class TransferTemplateOperationImpl extends GeneralService implements TransferTemplateOperation {
    private CallbackOperationTransfer callbackOperationTransfer;
    private CallbackChangeTransfer callbackChangeTransfer;

    @Override
    public void deleteTransferTemplate(Context context, int templateId) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().deleteTransferTemplate(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                templateId, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        callbackOperationTransfer.deleteTransferTemplateResponse(code, errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callbackOperationTransfer.deleteTransferTemplateResponse(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void changeTransferTemplate(Context context, JSONObject body, int templateId) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().changeTransferTemplate(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                body, templateId, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if (callbackChangeTransfer!=null){
                            callbackChangeTransfer.onChangeTransferTemplateResponse(code,errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callbackChangeTransfer!=null){
                            callbackChangeTransfer.onChangeTransferTemplateResponse(CONNECTION_ERROR_STATUS,null);
                        }
                    }
                });
    }

    @Override
    public void changeActiveTransferTemplate(Context context, int templateId, boolean IsActive) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().changeActiveTransfersTemplate(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                templateId, IsActive, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if (callbackOperationTransfer!=null){
                            callbackOperationTransfer.changeActiveTransferTemplate(code,errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callbackOperationTransfer!=null){
                            callbackOperationTransfer.changeActiveTransferTemplate(CONNECTION_ERROR_STATUS,null);
                        }
                    }
                });
    }

    @Override
    public void registerTransferCallBack(CallbackOperationTransfer callback) {
        this.callbackOperationTransfer = callback;
    }

    @Override
    public void registerCallBackChange(CallbackChangeTransfer callback) {
        this.callbackChangeTransfer = callback;
    }

    public interface CallbackOperationTransfer {
        void deleteTransferTemplateResponse(int statusCode, String errorMessage);
        void changeActiveTransferTemplate(int statusCode, String errorMessage);
    }

    public interface CallbackChangeTransfer {
        void onChangeTransferTemplateResponse(int statusCode, String errorMessage);
    }
}
