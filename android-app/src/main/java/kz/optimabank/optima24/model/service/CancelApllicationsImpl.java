package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.CancelApplicationModel;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.interfaces.CancelApplicationsInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class CancelApllicationsImpl extends GeneralService implements CancelApplicationsInterface {
    Callback callback;

    @Override
    public void cancelApplicationFirst(Context context, int id) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().cancelApplicationFirst(context, HeaderHelper.getOpenSessionHeader(context, sessionId), id,
                new NetworkResponse.SuccessRequestListener<CancelApplicationModel>() {
                    @Override
                    public void onSuccess(CancelApplicationModel response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        if (callback != null) {
                            callback.jsonCancelAppFirst(httpStatusCode, errorMessage, response);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callback != null) {
                            callback.jsonCancelAppFirst(CONNECTION_ERROR_STATUS, null, null);
                        }
                    }
                });
    }

    @Override
    public void cancelApplicationSecond(Context context, JSONObject applicationBody) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().cancelApplicationSecond(context, HeaderHelper.getOpenSessionHeader(context, sessionId), applicationBody,
                new NetworkResponse.SuccessRequestListener<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        if (callback != null) {
                            callback.jsonCancelAppSecond(httpStatusCode, errorMessage, response);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callback != null) {
                            callback.jsonCancelAppSecond(CONNECTION_ERROR_STATUS, null, null);
                        }
                    }
                });
    }

    @Override
    public void registerCancelCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonCancelAppFirst(int statusCode, String errorMessage, CancelApplicationModel response);

        void jsonCancelAppSecond(int statusCode, String errorMessage, ResponseBody response);
    }

}
