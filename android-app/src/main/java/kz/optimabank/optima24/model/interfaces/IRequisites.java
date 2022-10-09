package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.RequisitesImpl;

public interface IRequisites {
    void getBanksRequisites(Context context, String branchCode, String currency, String accountNumber);
    void registerCallBack (RequisitesImpl.Callback callback);
}
