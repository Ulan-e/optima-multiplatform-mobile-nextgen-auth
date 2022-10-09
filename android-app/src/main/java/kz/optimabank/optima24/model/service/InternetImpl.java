package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.Limit;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.InternetInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 04.06.2017.
 */

public class InternetImpl extends GeneralService implements InternetInterface {

    private callbackCheck callbackCheck;
    private callbackSet callbackSet;

    @Override
    public void checkInternet(Context context, int code, boolean isShowProgress) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().checkInternet(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<Limit.Internet>>() {
                    @Override
                    public void onSuccess(BaseResponse<Limit.Internet> response, String errorMessage, int code) {
                        if(response != null) {
                            callbackCheck.jsonCheckInternetResponse(code, errorMessage, response.data);
                            Log.i("response", "response = " + response);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callbackCheck.jsonCheckInternetResponse(CONNECTION_ERROR_STATUS,null,null);
                    }
                },code, isShowProgress);
    }

    @Override
    public void setInternet(Context context, int code, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().setInternet(context, HeaderHelper.getOpenSessionHeader(context, sessionId), new NetworkResponse.SuccessRequestListener<BaseResponse<String>>() {
            @Override
            public void onSuccess(BaseResponse response, ResponseBody errorBody, int httpStatusCode) {
                /*if (httpStatusCode!=200){
                    errorMessage = getErrorMessage(errorBody);
                }*/
                callbackSet.jsonSetInternetResponse(httpStatusCode,errorMessage);
            }
        }, new NetworkResponse.ErrorRequestListener() {
            @Override
            public void onError() {
                callbackSet.jsonSetInternetResponse(CONNECTION_ERROR_STATUS,null);
            }
        }, code, body);
    }

    @Override
    public void registerCheckInternetCallBack(callbackCheck callback) {
        this.callbackCheck = callback;
    }

    @Override
    public void registerSetInternetCallBack(callbackSet callback) {
        this.callbackSet = callback;
    }

    public interface callbackCheck{
        void jsonCheckInternetResponse(int statusCode, String errorMessage, Limit.Internet internet);
    }

    public interface callbackSet{
        void jsonSetInternetResponse(int statusCode, String errorMessage);
    }

}
