package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.CategoriesImpl;

/**
  Created by Timur on 28.02.2017.
 */

public interface Categories {
    void getCategories(Context context, boolean isShowPB);
    void registerCallBack(CategoriesImpl.Callback callback);
}
