package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.InternetImpl;

/**
  Created by Timur on 04.06.2017.
 */

public interface InternetInterface {
    void checkInternet(Context context, int code, boolean isShowProgress);
    void registerCheckInternetCallBack(InternetImpl.callbackCheck callback);

    void setInternet(Context context, int code, JSONObject body);
    void registerSetInternetCallBack(InternetImpl.callbackSet callback);
}
