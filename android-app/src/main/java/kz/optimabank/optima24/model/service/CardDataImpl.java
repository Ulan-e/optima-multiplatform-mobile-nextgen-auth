package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import kg.optima.mobile.R;
import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.Data;
import kz.optimabank.optima24.model.interfaces.CardsInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Utilities.getToast;

public class CardDataImpl extends GeneralService implements CardsInterface {
    private Callback callback;

    @Override
    public void getInterfaceViewData(Context context) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getInterfaceViewData(context, HeaderHelper.getOpenSessionHeader(context, sessionId),  // null чтоб получить только user agent для получения токена нужно указать ключ
                (response, errorBody, httpStatusCode) -> {
                    if (httpStatusCode == 200) {
                        callback.onSuccessfulResponse(httpStatusCode, errorMessage, response.data);
                    } else {
                        Toast.makeText(context,context.getString(R.string.internet_connection_error),Toast.LENGTH_SHORT).show();
                        Log.e("DeviceUnRegisterPush", "errorCode = " + httpStatusCode);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        Log.e("DeviceUnRegisterPush", "onError");
                    }
                });
    }

    @Override
    public void registerCallBack(CardDataImpl.Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onSuccessfulResponse(int statusCode, String errorMessage, List<Data> dataList);
    }
}
