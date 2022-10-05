package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;
import org.json.JSONObject;
import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.ProfImageLoader;
import kz.optimabank.optima24.model.manager.GeneralManager;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class ProfImageLoaderImpl extends GeneralService implements ProfImageLoader {
    private Callback callback;
    private SetProfImageCallback setProfImageCallback;

    @Override
    public void setProfImage(Context context, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().setImageProfile(context,
                OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                body, new NetworkResponse.SuccessRequestListener<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, ResponseBody errorBody, int httpStatusCode) {
                        if(httpStatusCode!=200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        setProfImageCallback.jsonSetProfImageResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        setProfImageCallback.jsonSetProfImageResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }
    @Override
    public void getProfImage(Context context) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getProfImage(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListener<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody response, ResponseBody errorBody, int httpStatusCode) {
                        Log.d("getProfImage","onSuccess = " + response);
                        callback.jsonProfImageResponse(httpStatusCode, null, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonProfImageResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void registerCallBack(ProfImageLoaderImpl.Callback callback) {
        this.callback = callback;
    }

    @Override
    public void registerSetProfImageCallBack(SetProfImageCallback callback) {
        this.setProfImageCallback = callback;

    }

    public interface Callback {
        void jsonProfImageResponse(int statusCode, String errorMessage, ResponseBody array);
    }

    public interface SetProfImageCallback {
        void jsonSetProfImageResponse(int statusCode, String errorMessage, BaseResponse<String> response);
    }
}