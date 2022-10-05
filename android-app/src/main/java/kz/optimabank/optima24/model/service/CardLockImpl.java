package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.CardLock;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.manager.GeneralManager;

/**
 * Created by Max on 31.08.2017.
 */

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class CardLockImpl extends GeneralService implements CardLock {
    private Callback callback;

    @Override
    public void setCardLock(Context context, int code, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().setLockCard(context,
                OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                code, body, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int httpStatusCode) {
                        if (callback != null) {
                            callback.jsonCardLockResponse(httpStatusCode, errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callback != null) {
                            callback.jsonCardLockResponse(CONNECTION_ERROR_STATUS, null);
                        }
                    }
                });
    }

    @Override
    public void registerCallBack(CardLockImpl.Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonCardLockResponse(int statusCode, String errorMessage);
    }
}
