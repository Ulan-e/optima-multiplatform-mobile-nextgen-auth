package kz.optimabank.optima24.model.service;

import android.content.Context;

import org.json.JSONArray;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.AccountsVisibility;
import kz.optimabank.optima24.model.manager.GeneralManager;
import retrofit2.Call;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
 * Created by Максим on 02.08.2017.
 */

public class AccountVisibilityImpl extends GeneralService implements AccountsVisibility {
    private Callback callback;
    private Call accountVis;


    @Override
    public void setVisibilityAccounts(Context context, JSONArray body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        accountVis = NetworkResponse.getInstance().setVisibilityAccounts(context,
                OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                body, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        callback.jsonAccountsVisibilityResponse(code, errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonAccountsVisibilityResponse(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void registerCallBack(Callback callback) { this.callback = callback; }

    @Override
    public void cancelAccountsRequest() {
        if(accountVis != null){
            accountVis.cancel();
        }
    }

    public interface Callback {
        void jsonAccountsVisibilityResponse(int statusCode,String errorMessage);
    }

}
