package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.SetLimitInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class SetLimitInterfaceImpl extends GeneralService implements SetLimitInterface {

    private Callback callback;

    @Override
    public void setLimit(Context context, int code, boolean isShowProgress, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().setLimit(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId), code, isShowProgress,
                body, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if(code != 0) {

                        }else {
//                            errorMessage = getErrorMessage(response.data);
                        }
                        callback.jsonSetLimitResponse(code, errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonSetLimitResponse(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void registerCallBack(SetLimitInterfaceImpl.Callback callback) {
        this.callback = callback;
    }

    public interface Callback{
        void jsonSetLimitResponse(int statusCode, String errorMessage);
//        void jsonGetLimitSmsResponse(int statusCode, String errorMessage, int errorCode);
    }
}
