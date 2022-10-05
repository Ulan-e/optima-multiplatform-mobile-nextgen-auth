package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.AccountOperationImpl;

/**
  Created by Timur on 29.05.2017.
 */

public interface AccountOperation {
    void getAccountOperationsAndStats(Context context, int code, String fromDate, String toDate, boolean isMainPage, boolean isShowProgress);
    void cancelAccountOperation();
    void registerCallBack(AccountOperationImpl.Callback callback);
}
