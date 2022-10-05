package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.CreateTemplate;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 19.05.2017.
 */

public class CreateTemplateImpl extends GeneralService implements CreateTemplate {
    Callback callback;

    @Override
    public void createPaymentTemplate(Context context, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().createPaymentTemplate(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                body, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if (callback != null) {
                            callback.onCreatePaymentTemplateResponse(code, errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callback != null) {
                            callback.onCreatePaymentTemplateResponse(CONNECTION_ERROR_STATUS, null);
                        }
                    }
                });
    }

    @Override
    public void createTransferTemplate(Context context, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().createTransferTemplate(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                body, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if (callback!=null){
                            callback.onCreateTransferTemplateResponse(code, errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callback!=null){
                            callback.onCreateTransferTemplateResponse(CONNECTION_ERROR_STATUS, null);
                        }
                    }
                });
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onCreatePaymentTemplateResponse(int statusCode, String errorMessage);
        void onCreateTransferTemplateResponse(int statusCode, String errorMessage);
    }
}
