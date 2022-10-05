package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONArray;


import java.util.ArrayList;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.base.PushResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.PushInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 04.06.2017.
 */

public class PushImpl extends GeneralService implements PushInterface {
    private Callback callback;

    @Override
    public void getPushSettings(Context context) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getPushSettings(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ArrayList<PushResponse.PushSettings>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<PushResponse.PushSettings>> response, String errorMessage, int code) {
                        callback.jsonGetPushSettingsResponse(code,errorMessage, response.data);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonGetPushSettingsResponse(CONNECTION_ERROR_STATUS,null,null);
                    }
                });
    }

    @Override
    public void setPushSettings(Context context, JSONArray body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().setPushSettings(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId), body,
                new NetworkResponse.SuccessRequestListener<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode!=200){
                            errorMessage = getErrorMessage(errorBody);
                        }
                        callback.jsonSetPushSettingsResponse(httpStatusCode,errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonSetPushSettingsResponse(CONNECTION_ERROR_STATUS,null);
                    }
                });
    }

    @Override
    public void registerPushSettingsCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonGetPushSettingsResponse(int statusCode, String errorMessage, ArrayList<PushResponse.PushSettings> response);
        void jsonSetPushSettingsResponse(int statusCode, String errorMessage);
    }
}
