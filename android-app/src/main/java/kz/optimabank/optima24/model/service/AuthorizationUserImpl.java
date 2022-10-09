package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import kg.optima.mobile.R;
import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.AuthorizationResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.AuthorizationUser;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.BANK_ID;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.getPreferences;
import static kz.optimabank.optima24.utility.Constants.REGISTRATION_TAG;
import static kz.optimabank.optima24.utility.Utilities.getToast;

public class AuthorizationUserImpl extends GeneralService implements AuthorizationUser {
    private Callback callback;
    private GetAuthorizationTypeCallback getAuthorizationTypeCallback;

    @Override
    public void loginRequest(final Context context, String login, String password, String sms) {
        NetworkResponse.getInstance().openSession(context,
                getOpenSessionBody(login, password, sms), HeaderHelper.getOpenSessionHeader(context, null),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<AuthorizationResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<AuthorizationResponse> response, String errorMessage, int code) {
                        if (code == 0) {

                            if (response != null) {
                                //Converter<kz.optimabank.optima24.domain.entity.dto.BaseResponse<AuthorizationResponse>, AuthorizationResponse> converter = ServiceGenerator.retrofit().responseBodyConverter(AuthorizationResponse.class, new Annotation[0]);
                                //try {
                                Log.e("loginReQUEST","true");
                                boolean isInitializedMobocardsSdk = GeneralManager.getInstance().isInitializedMobocardsSdk();
                                AuthorizationResponse authResponse = response.data;
                                GeneralManager.dispose();
                                GeneralManager.getInstance().setInitializedMobocardsSdk(isInitializedMobocardsSdk);
                                GeneralManager.getInstance().setUser(mapToUser(context));
                                GeneralManager.getInstance().setAppOpen(true);
                                GeneralManager.getInstance().setAutoEncrypt(authResponse.user.autoEncrypt);
                                GeneralManager.setPhone(authResponse.user.mobilePhoneNumber);
                                GeneralManager.setSessionDuration(authResponse.sessionDuration);
                                GeneralManager.getInstance().setProfImage(authResponse.user.imageHash);

                                if (authResponse.getUser() != null) {
                                    saveBankId(context, authResponse.user.getBankId());
                                }

                                FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                                crashlytics.setUserId(authResponse.user.login);
                                //} catch (IOException e) {
                                //    Log.e(TAG, "IOException when convert authResponse", e);
                                //}
                            }
                        }
                        callback.jsonAuthorizationResponse(code, errorMessage, code);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context, context.getString(R.string.internet_connection_error));
                        callback.jsonAuthorizationResponse(CONNECTION_ERROR_STATUS, null, 0);
                    }
                });
    }

    private AuthorizationResponse.User mapToUser(Context context){
        return null;
        /*UserPreferences userPreferences = new UserPreferencesImpl(context);

        AuthorizationResponse.User user = new AuthorizationResponse.User();
        user.setAddress(userPreferences.getAddress());
        user.setBankId(userPreferences.getBankId());
        user.setFirstName(userPreferences.getFirstName());
        user.setIdn(userPreferences.getIdn());
        user.setFullName(userPreferences.getFullName());
        user.setImageHash(userPreferences.getUserPhoto());
        user.setLogin(userPreferences.getUserLogin());
        user.setLastName(userPreferences.getLastName());
        user.setMiddleName(userPreferences.getMiddleName());
        user.setSex(userPreferences.getSex());
        user.setMobilePhoneNumber(userPreferences.getPhoneNumber());
        user.setAutoEncrypt(userPreferences.getAutoEncrypt());
        return user;*/
    }

    // сохранение банка айди
    private void saveBankId(Context context, String bankId) {
        if (bankId != null) {
            SharedPreferences.Editor editor = getPreferences(context).edit();
            editor.putString(BANK_ID, bankId);
            editor.apply();
        }
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void getAuthorizationType(Context context, String login) {
        NetworkResponse.getInstance().getAuthorizationType(context, HeaderHelper.getOpenSessionHeader(context, null), login,
                (response, errorMessage, code) -> {
                    if (response != null) {
                        getAuthorizationTypeCallback.jsonCheckPasswordResponse(code, errorMessage, response.data);
                        Log.d(REGISTRATION_TAG, "success response data" + response.data);
                    }
                }, () -> {
                    getToast(context, context.getString(R.string.internet_connection_error));
                    getAuthorizationTypeCallback.jsonCheckPasswordResponse(CONNECTION_ERROR_STATUS, null, null);
                    Log.d(REGISTRATION_TAG, "error checkPassword ");
                });
    }

    @Override
    public void registerGetAuthorizationTypeCallback(GetAuthorizationTypeCallback callback) {
        this.getAuthorizationTypeCallback = callback;
    }

    public interface Callback {
        void jsonAuthorizationResponse(int statusCode, String errorMessage, int errorCode);
    }

    public interface GetAuthorizationTypeCallback {
        void jsonCheckPasswordResponse(int statusCode, String errorMessage, String responseType);
    }
}