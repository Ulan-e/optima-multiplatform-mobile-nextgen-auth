package kz.optimabank.optima24.model.service;

import android.content.Context;
import java.util.ArrayList;

import kg.optima.mobile.R;
import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.base.Rate;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.Rates;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.getToast;

/**
  Created by Timur on 03.04.2017.
 */

public class RatesImpl extends GeneralService implements Rates {
    private Callback callback;

    @Override
    public void getRates(final Context context, boolean visibleProgressBar) {
        NetworkResponse.getInstance().getRates(context, OptimaBank.getInstance().getOpenSessionHeader(null), visibleProgressBar,
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ArrayList<Rate>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<Rate>> response, String errorMessage, int code) {
                        if(code == 0){
//                            ArrayList<Rate> listRate = new ArrayList<>();
//                            for(Rate rate: response.data){
//                                if(rate.type == 0){
//                                   listRate.add(rate);
//                                }
//                            }
                            GeneralManager.getInstance().setRate(response.data);
                        }
                        callback.jsonRatesResponse(code,errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        callback.jsonRatesResponse(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonRatesResponse(int statusCode,String errorMessage);
    }
}
