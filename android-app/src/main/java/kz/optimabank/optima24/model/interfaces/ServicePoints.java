package kz.optimabank.optima24.model.interfaces;

import android.content.Context;
import kz.optimabank.optima24.model.service.ServicePointsImpl;

/**
  Created by Timur on 28.03.2017.
 */

public interface ServicePoints {
    void getAllServicePoints(Context context);
    void registerCallBack(ServicePointsImpl.Callback callback);
}
