package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.widget.Toast;

import kg.optima.mobile.R;
import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.interfaces.NumberOfAttemptsInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Utilities.getToast;

public class NumberOfAttemptsImpl implements NumberOfAttemptsInterface {
    private NumberOfAttemptsImpl.Callback callback;

    @Override
    public void getMaxNumberOfAttemptsRequest(Context context) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getMaxNumberOfAttemptsRequest(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                (response, errorBody, httpStatusCode) -> {
                    if (httpStatusCode == 0) {
                        callback.onNumberOfAttemptsResponse(httpStatusCode, response.message,response.data);
                    } else {
                        Toast.makeText(context,context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                    }
                });
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback= callback;
    }

    public interface Callback {
        void onNumberOfAttemptsResponse(int statusCode, String errorMessage, String count);
    }
}
