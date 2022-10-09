package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.TransferImpl;

/**
  Created by Timur on 05.05.2017.
 */

public interface Transfers {
    void getAccountData(Context context, String cardNumber);
    void mastercardRegister(Context context, JSONObject body);
    void checkMt100Transfer(Context context, JSONObject body);
    void confirmMt100Transfer(Context context, JSONObject body);
    void registerCallBack(TransferImpl.Callback callback);
    void registerCallbackInterbank(TransferImpl.CallbackTransfer callback);
    void registerCallbackConfirm(TransferImpl.CallbackConfirm callback);
}
