package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.TransferSwiftImpl;

/**
  Created by Timur on 05.05.2017.
 */

public interface TransfersSwift {
    void checkSwift(Context context, JSONObject body);
    void confirmSwift(Context context, JSONObject body);
    void registerCallBack(TransferSwiftImpl.Callback callback);
}
