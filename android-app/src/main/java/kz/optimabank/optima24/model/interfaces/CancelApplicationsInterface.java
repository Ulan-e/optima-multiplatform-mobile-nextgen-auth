package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.CancelApllicationsImpl;

public interface CancelApplicationsInterface {
    void cancelApplicationFirst(Context context, int id);
    void cancelApplicationSecond(Context context, JSONObject applicationBody);
    void registerCancelCallBack(CancelApllicationsImpl.Callback callback);
}
