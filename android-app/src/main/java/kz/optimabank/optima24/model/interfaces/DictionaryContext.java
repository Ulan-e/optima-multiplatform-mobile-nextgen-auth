package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.DictionaryImpl;

/**
  Created by Timur on 13.05.2017.
 */

public interface DictionaryContext {
    void getAllDictionary(Context context);
    void registerCallBack(DictionaryImpl.Callback callback);
}
