package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import kg.optima.mobile.R;
import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BankRequisitesResponse;
import kz.optimabank.optima24.model.interfaces.IRequisites;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Utilities.getToast;

/**
  Created by Timur on 28.03.2017.
 */

public class RequisitesImpl extends GeneralService implements IRequisites {
    private Callback callback;

    @Override
    public void getBanksRequisites(Context context, String branchCode, String currency, String accountNumber) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getBanksRequisites(context,
                HeaderHelper.getOpenSessionHeader(context, sessionId), branchCode, currency, accountNumber,
                (response, errorBody, httpStatusCode) -> {
                    if(response != null) {
                        Log.d("TAG", "unregisterInfoMe httpStatusCode = " + httpStatusCode);
                        if (httpStatusCode == 200) {
                            GeneralManager.setBankRequisitesResponse(response);
                            callback.successBankRequisites(httpStatusCode, errorMessage, response);
                        } else {
                            Log.e("DeviceUnRegisterPush", "errorCode = " + httpStatusCode);
                        }
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
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void successBankRequisites(int statusCode, String errorMessage, BankRequisitesResponse response);
    }
}
