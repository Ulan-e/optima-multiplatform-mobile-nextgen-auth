package kz.optimabank.optima24.model.interfaces;

import android.content.Context;
import android.graphics.drawable.LevelListDrawable;

import kz.optimabank.optima24.model.service.NewsImpl;

/**
  Created by Timur on 30.03.2017.
 */

public interface News {
    void getNews(Context context);
    void getNewsImage(Context context, String name, String category);
    void registerCallBack(NewsImpl.Callback callback);
    void registerImageCallBack(NewsImpl.CallbackImage callback);
}
