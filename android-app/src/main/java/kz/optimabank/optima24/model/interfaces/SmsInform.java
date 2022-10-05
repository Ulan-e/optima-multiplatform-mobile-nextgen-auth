package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.SmsInformImpl;

/**
  Created by Timur on 17.07.2017.
 */

public interface SmsInform {
    void checkSmsInform(Context context, int code, boolean isShowProgress);
    void setSmsInform(Context context, int code, JSONObject body, boolean isShowProgress);
    void registerCheckSmsCallBack(SmsInformImpl.Callback callback);
    void registerSetSmsInformCallBack(SmsInformImpl.Callback callback);
}
