package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.BannerImpl;

/**
  Created by Timur on 28.02.2017.
 */

public interface BannerInterface {
    void getBanners(Context context);
    void registerCallBack(BannerImpl.Callback callback);

    void checkServerAvailability(Context context);
    void registerCheckServerAvailabilityCallback(BannerImpl.CheckServerAvailabilityCallback callback);
}