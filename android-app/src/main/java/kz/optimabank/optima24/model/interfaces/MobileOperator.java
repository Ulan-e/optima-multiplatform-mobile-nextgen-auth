package kz.optimabank.optima24.model.interfaces;

import android.content.Context;
import kz.optimabank.optima24.model.service.MobileOperatorImpl;

/**
  Created by Timur on 16.05.2017.
 */

public interface MobileOperator {
    void getMobileOperator(Context context,String mobileNumber);
    void registerCallBack(MobileOperatorImpl.Callback callback);
}
