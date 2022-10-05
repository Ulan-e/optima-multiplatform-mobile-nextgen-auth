package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.ChangePasswordImpl;

public interface ChangePassword {
    void ChangePassword(Context context, JSONObject body);
    void registerCallBack(ChangePasswordImpl.Callback callback);
}
