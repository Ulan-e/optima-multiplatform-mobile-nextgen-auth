package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.CreateTemplateImpl;

/**
  Created by Timur on 19.05.2017.
 */

public interface CreateTemplate {
    void createPaymentTemplate(Context context, JSONObject body);
    void createTransferTemplate(Context context, JSONObject body);
    void registerCallBack(CreateTemplateImpl.Callback callback);
}
