package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.SetLimitInterfaceImpl;

/**
  Created by Timur on 29.05.2017.
 */

public interface SetLimitInterface {
    void setLimit(Context context, int code, boolean isShowProgress, JSONObject body);
    void registerCallBack(SetLimitInterfaceImpl.Callback callback);
}
