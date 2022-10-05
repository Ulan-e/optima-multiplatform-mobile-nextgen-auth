package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.NumberOfAttemptsImpl;

public interface NumberOfAttemptsInterface {
    void getMaxNumberOfAttemptsRequest(Context context);
    void registerCallBack(NumberOfAttemptsImpl.Callback callback);
}
