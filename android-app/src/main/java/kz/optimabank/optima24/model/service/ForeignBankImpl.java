package kz.optimabank.optima24.model.service;

import android.content.Context;

import java.util.ArrayList;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.db.entry.ForeignBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.ForeignBankContext;

/**
  Created by Timur on 13.05.2017.
 */

public class ForeignBankImpl extends GeneralService implements ForeignBankContext {
    private Callback callback;

    @Override
    public void filterForeignBanks(Context context, String param) {
        NetworkResponse.getInstance().filterForeignBanks(context, HeaderHelper.getOpenSessionHeader(context, null), param,
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ArrayList<ForeignBank>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<ForeignBank>> response, String errorMessage, int code) {
                        if(response != null) {
                            if (callback != null)
                                callback.jsonFilterForeignBankResponse(code, errorMessage, response.data);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void registerCallBack(ForeignBankImpl.Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonFilterForeignBankResponse(int statusCode, String errorMessage, ArrayList<ForeignBank> response);
    }
}
