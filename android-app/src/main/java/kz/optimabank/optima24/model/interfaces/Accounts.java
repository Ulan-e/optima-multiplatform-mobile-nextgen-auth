package kz.optimabank.optima24.model.interfaces;

import android.content.Context;
import kz.optimabank.optima24.model.service.AccountsImpl;

/**
  Created by Timur on 28.02.2017.
 */

public interface Accounts {
    void getAccounts(Context context, boolean isShowPB, boolean needUpdate);
    void getAccountsOperations(Context context);
    void registerCallBack(AccountsImpl.Callback callback);
    void registerUpdateCallBack(AccountsImpl.UpdateCallback callback);
    void cancelAccountsRequest();
}
