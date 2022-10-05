package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.AuthorizationUserImpl;

/**
 Created by Timur on 06.02.2017.
 */

public interface AuthorizationUser {
    void loginRequest(Context context, String login, String password, String sms);
    void registerCallBack(AuthorizationUserImpl.Callback callback);

    void getAuthorizationType(Context context, String login);
    void registerGetAuthorizationTypeCallback(AuthorizationUserImpl.GetAuthorizationTypeCallback callback);
}

