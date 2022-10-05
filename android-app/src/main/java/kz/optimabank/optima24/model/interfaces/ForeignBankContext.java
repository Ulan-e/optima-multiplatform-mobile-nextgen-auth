package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.ForeignBankImpl;

public interface ForeignBankContext {
    void filterForeignBanks(Context context, String param);
    void registerCallBack(ForeignBankImpl.Callback callback);
}
