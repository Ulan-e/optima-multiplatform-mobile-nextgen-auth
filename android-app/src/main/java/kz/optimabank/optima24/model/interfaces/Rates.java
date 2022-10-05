package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.RatesImpl;

/**
  Created by Timur on 03.04.2017.
 */

public interface Rates {
    void getRates(Context context,boolean visibleProgressBar);
    void registerCallBack(RatesImpl.Callback callback);
}
