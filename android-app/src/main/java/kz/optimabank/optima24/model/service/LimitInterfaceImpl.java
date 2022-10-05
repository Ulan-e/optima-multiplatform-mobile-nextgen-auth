package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.Limit;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.interfaces.LimitInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class LimitInterfaceImpl extends GeneralService implements LimitInterface {

    private Callback callback;

    @Override
    public void getLimit(Context context, int code, boolean isShowProgress) {
        String sessionID = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getLimit(context, OptimaBank.getInstance().getOpenSessionHeader(sessionID),
                code, isShowProgress, (response, errorMessage, code1) -> {
                    ArrayList<Limit> listLimits = new ArrayList<>();

                    if (response != null) {
                        if (response.data != null) {
                                for (Limit li : response.data) {
                                    if(li.Type == 0 && !listLimits.contains(new Limit(li.Name, li.Amount, li.SingleAmount, li.Number, li.Period, li.Status, 0, li.StartDate, li.EndDate, li.SmsCode))){
                                        listLimits.add(new Limit(li.Name, li.Amount, li.SingleAmount, li.Number, li.Period, li.Status, 0, li.StartDate, li.EndDate, li.SmsCode));
                                    }else if(li.Type == 2 && !listLimits.contains(new Limit(li.Name, li.Amount, li.SingleAmount, li.Number, li.Period, li.Status, 2, li.StartDate, li.EndDate, li.SmsCode))){
                                        listLimits.add(new Limit(li.Name, li.Amount, li.SingleAmount, li.Number, li.Period, li.Status, 2, li.StartDate, li.EndDate, li.SmsCode));
                                    }else if(li.Type == 3 && !listLimits.contains(new Limit(li.Name, li.Amount, li.SingleAmount, li.Number, li.Period, li.Status, 3, li.StartDate, li.EndDate, li.SmsCode))){
                                        listLimits.add(new Limit(li.Name, li.Amount, li.SingleAmount, li.Number, li.Period, li.Status, 3, li.StartDate, li.EndDate, li.SmsCode));
                                    }
                                }

                            ArrayList<Limit> sortedList = new ArrayList<>();
                            sortedList.add(listLimits.get(1));
                            sortedList.add(listLimits.get(2));
                            sortedList.add(listLimits.get(0));

                            GeneralManager.getInstance().setLimit(sortedList);
                        }
                        callback.jsonLimitResponse(code1, errorMessage);
                    }
                }, () -> {
                    Log.i("limit", "response = onError");
                    callback.jsonLimitResponse(CONNECTION_ERROR_STATUS, null);
                });
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback{
        void jsonLimitResponse(int statusCode,String errorMessage);
    }
}
