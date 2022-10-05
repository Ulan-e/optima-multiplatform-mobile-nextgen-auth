package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONArray;

import kz.optimabank.optima24.model.service.AccountVisibilityImpl;


public interface AccountsVisibility {
    void setVisibilityAccounts(Context context, JSONArray body);
    void registerCallBack(AccountVisibilityImpl.Callback callback);
    void cancelAccountsRequest();
}
