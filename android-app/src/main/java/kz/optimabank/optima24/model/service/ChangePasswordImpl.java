package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.ChangePassword;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
 * Created by Максим on 04.08.2017.
 */

public class ChangePasswordImpl extends GeneralService implements ChangePassword {
    private Callback callback;

    @Override
    public void ChangePassword(Context context, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().changePassword(context,
                HeaderHelper.getOpenSessionHeader(context, sessionId),
                body, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        callback.jsonChangePasswordResponse(code, errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonChangePasswordResponse(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void registerCallBack(Callback callback) { this.callback = callback; }

    public interface Callback {
        void jsonChangePasswordResponse(int statusCode,String errorMessage);
    }
}
