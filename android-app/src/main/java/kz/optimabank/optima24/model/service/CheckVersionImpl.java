package kz.optimabank.optima24.model.service;

import android.content.Context;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import okhttp3.ResponseBody;

public class CheckVersionImpl extends GeneralService {
    private Callback callback;
    private final Context contextT;

    public CheckVersionImpl(Context context){
        contextT = context;
    }



    public void checkVersion() {
        NetworkResponse.getInstance().checkVersion(contextT, OptimaBank.getInstance().getOpenSessionHeader(null),
                new NetworkResponse.SuccessRequestListener<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, ResponseBody errorBody, int httpStatusCode) {
                        if(response != null) {
                            if (httpStatusCode == 200) {
                                callback.jsonCheckVersionResponse(response.code, response.message, true);
                            } else {
                                errorMessage = getErrorMessage(errorBody);
                                callback.jsonCheckVersionResponse(response.code, errorMessage, false);
                            }
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonCheckVersionResponse(100 ,null,false);
                    }
                });
    }

    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonCheckVersionResponse(int resultCode, String message, boolean isSuccess);
    }
}
