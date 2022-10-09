package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.AuthorizationUserImpl;
import kz.optimabank.optima24.model.service.KeepAliveSessionImpl;

public interface KeepAliveSession {
    void keepAliveRequest(final Context context, boolean isShowProgress);
    void registerCallBack(KeepAliveSessionImpl.Callback callback);

}
