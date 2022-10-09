package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.LimitInterfaceImpl;

/**
  Created by Timur on 29.05.2017.
 */

public interface LimitInterface {
    void getLimit(Context context, int code, boolean isShowProgress);
    void registerCallBack(LimitInterfaceImpl.Callback callback);
}
