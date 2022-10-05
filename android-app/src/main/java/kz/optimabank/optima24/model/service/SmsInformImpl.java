package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.gson.response.Bool;
import kz.optimabank.optima24.model.interfaces.SmsInform;
import kz.optimabank.optima24.model.manager.GeneralManager;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 17.07.2017.
 */

public class SmsInformImpl extends GeneralService implements SmsInform {
    Callback callbackCheck;
    Callback callbackSet;

    @Override
    public void checkSmsInform(Context context, int code, boolean isShowProgress) {
        final String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().checkSmsInform(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId), new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<Bool>>() {
            @Override
            public void onSuccess(BaseResponse<Bool> response, String errorMessage, int code) {
                if(response.data != null)
                    callbackCheck.jsonCheckSmsInformResponse(code, errorMessage, String.valueOf(response.data.getIsActive()));
                else
                    callbackCheck.jsonCheckSmsInformResponse(code, errorMessage, null);
            }
        }, new NetworkResponse.ErrorRequestListener() {
            @Override
            public void onError() {
                callbackCheck.jsonCheckSmsInformResponse(CONNECTION_ERROR_STATUS, null, "");
            }
        },code, isShowProgress);
    }

    @Override
    public void setSmsInform(Context context, int code, JSONObject body, boolean isShowProgress) {
        final String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().setSmsInform(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListener<BaseResponse<String>>() {
            @Override
            public void onSuccess(BaseResponse response, ResponseBody errorBody, int httpStatusCode) {
                /*if (httpStatusCode!=200){
                    errorMessage = getErrorMessage(errorBody);
                }*/
                callbackSet.jsonSetSmsInformResponse(httpStatusCode,errorMessage);
            }
        }, new NetworkResponse.ErrorRequestListener() {
            @Override
            public void onError() {
                callbackSet.jsonSetSmsInformResponse(CONNECTION_ERROR_STATUS, null);
            }
        },code, body, isShowProgress);
    }

    @Override
    public void registerCheckSmsCallBack(SmsInformImpl.Callback callback) {
        this.callbackCheck = callback;
    }

    @Override
    public void registerSetSmsInformCallBack(SmsInformImpl.Callback callback) {
        this.callbackSet = callback;
    }


    public interface Callback {
        void jsonSetSmsInformResponse(int statusCode, String errorMessage);
        void jsonCheckSmsInformResponse(int statusCode, String errorMessage, String response);
    }
}
