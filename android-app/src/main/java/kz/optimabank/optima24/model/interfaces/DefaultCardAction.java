package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.DefaultCardActionImpl;

public interface DefaultCardAction {
    void setDefaultCardStatus(Context context, long accountId);
    void registerDefaultCardCallBack(DefaultCardActionImpl.DefaultStatusCardCallback callback);
}