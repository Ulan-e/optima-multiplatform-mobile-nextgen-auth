package kz.optimabank.optima24.model.service;

import android.content.Context;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.KeepAliveSession;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class KeepAliveSessionImpl implements KeepAliveSession{
    Callback callback;

    @Override
    public void keepAliveRequest(final Context context, boolean isShowProgress) {
        final String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().keepAliveRequest(context, HeaderHelper.getOpenSessionHeader(context, sessionId), new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
            @Override
            public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                if(code == 0) {
                    callback.jsonKeepAliveResponse(code, errorMessage);
                }
            }
        }, new NetworkResponse.ErrorRequestListener() {
            @Override
            public void onError() {
                callback.jsonKeepAliveResponse(CONNECTION_ERROR_STATUS, null);
            }
        }, isShowProgress);
    }

    @Override
    public void registerCallBack(Callback callback){
        this.callback = callback;
    }

    public interface Callback {
        void jsonKeepAliveResponse(int statusCode,String errorMessage);
    }
}
