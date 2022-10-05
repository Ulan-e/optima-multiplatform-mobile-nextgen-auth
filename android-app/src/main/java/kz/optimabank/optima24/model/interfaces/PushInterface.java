package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONArray;

import kz.optimabank.optima24.model.service.PushImpl;

public interface PushInterface {
    void getPushSettings(Context context);
    void setPushSettings(Context context, JSONArray body);
    void registerPushSettingsCallback(PushImpl.Callback callback);
}
