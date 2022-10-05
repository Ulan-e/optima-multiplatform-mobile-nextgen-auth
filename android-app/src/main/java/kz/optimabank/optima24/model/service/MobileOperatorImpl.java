package kz.optimabank.optima24.model.service;

import android.content.Context;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.MobileOperatorResponse;
import kz.optimabank.optima24.model.interfaces.MobileOperator;
import kz.optimabank.optima24.model.manager.GeneralManager;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 16.05.2017.
 */

public class MobileOperatorImpl extends GeneralService implements MobileOperator {
    Callback callback;

    @Override
    public void getMobileOperator(Context context, String mobileNumber) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getMobileOperator(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId), mobileNumber,
                new NetworkResponse.SuccessRequestListener<MobileOperatorResponse>() {
                    @Override
                    public void onSuccess(MobileOperatorResponse response, ResponseBody errorBody, int httpStatusCode) {
                        if(httpStatusCode!=200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        callback.jsonMobileOperatorResponse(httpStatusCode,response,errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonMobileOperatorResponse(CONNECTION_ERROR_STATUS,null,null);
                    }
                });
    }

    @Override
    public void registerCallBack(MobileOperatorImpl.Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonMobileOperatorResponse(int statusCode, MobileOperatorResponse response, String errorMessage);
    }
}
