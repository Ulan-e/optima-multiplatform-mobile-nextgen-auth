package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kz.optimabank.optima24.BuildConfig;
import kz.optimabank.optima24.model.gson.APIError;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.ErrorUtils;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.TAG;

/**
  Created by Timur on 11.04.2017.
 */

abstract class GeneralService {
    String errorMessage = null;
    int errorCode;

    Map<String,String> getOpenSessionBody(String login, String password, String sms) {
        Map<String,String> body = new HashMap<>();
        body.put("Login",login);
        body.put("HashPassword",password);
        body.put("ConfirmationCode", sms);
        return body;
    }

    String getErrorMessage(ResponseBody errorBody) {
        APIError error = ErrorUtils.parseError(errorBody);
        Log.d(TAG,"error = " + error);
        if(error!=null) {
            String errorMessage = error.message();
            Log.d(TAG, "error.message() = " + errorMessage);
            if(error.error!=null) {
                errorCode = error.error.code;
                Log.d(TAG, "error.code = " + errorCode);
                Log.d(TAG, "error.code = " + errorCode);
                Log.d(TAG, "error.code = " + errorCode);
            }
            return errorMessage;
        }
        return null;
    }

    Integer getErrorCode(ResponseBody errorBody) {
        APIError error = ErrorUtils.parseError(errorBody);
        Log.d(TAG,"error = " + error);
        if(error!=null) {
            if(error.error!=null) {
                errorCode = error.error.code;
                Log.d(TAG, "error.code = " + errorCode);
            }
            return errorCode;
        }
        return null;
    }

    JSONObject getPushRegisterBody(String phone) {
        JSONObject json = new JSONObject();
        try {
            json.put("phone", phone);

            JSONObject app = new JSONObject();
            app.put("name", "Optima24");
            app.put("ver", BuildConfig.VERSION_NAME);
            json.put("app", app);

            JSONObject os = new JSONObject();
            os.put("type", "android");
            os.put("ver", Build.VERSION.RELEASE);
            json.put("os", os);
        } catch (JSONException e) {
            Log.d("TAG", "JSONException in LoginFragment = " + e);
        }
        Log.d("TAG", "body = " + json);
        return json;
    }

    JSONObject getPushConfirmBody(String devicePk, String deviceId){
        JSONObject body = new JSONObject();
        try {
            body.put("device_token", deviceId);
            body.put("exchange", devicePk);
        } catch (JSONException e) {
            Log.d("TAG", "JSONException in LoginFragment = " + e);
        }
        return body;
    }

    JSONObject getPushUpdateToken(String token) {
        JSONObject json = new JSONObject();
        try {
            json.put("push_token", token);
        } catch (JSONException e) {
            Log.d("TAG", "JSONException in LoginFragment = " + e);
        }
        Log.d("TAG", "body = " + json);
        return json;
    }

    Map<String,String> getPushUnregisterHeader(Context context) {
        Map<String,String> body = new HashMap<>();
        Log.i("PTKC","----------------------------------");
        Log.i("PTKC","GeneralManager getPushUnregisterHeader first = "+GeneralManager.getInstance().getAuthorizationHeaders(context));
        body.put("XX-InfoMe-AuthToken", GeneralManager.getInstance().getAuthorizationHeaders(context));
        Log.i("PTKC","GeneralManager getPushUnregisterHeader second = "+GeneralManager.getInstance().getAuthorizationHeaders(context));
        return body;
    }
}
