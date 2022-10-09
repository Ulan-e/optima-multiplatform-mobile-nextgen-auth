package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.CardLockImpl;

public interface CardLock {
    void setCardLock(Context context, int code, JSONObject body);
    void registerCallBack(CardLockImpl.Callback callback);
}
