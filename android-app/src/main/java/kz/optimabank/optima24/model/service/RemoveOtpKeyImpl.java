package kz.optimabank.optima24.model.service;

import android.content.Context;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.RemoveOtpKeyInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

public class RemoveOtpKeyImpl implements RemoveOtpKeyInterface {
    RemoveOtpKeyImpl.OtpKeyCallBack otpKeyCallBack;

    @Override
    public void removeOtpKeyRequest(Context context, int otpKey) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().removeOtpKeyRequest(context,
                HeaderHelper.getOpenSessionHeader(context, sessionId), otpKey, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if (otpKeyCallBack != null) {
                            otpKeyCallBack.onOtpKeyResponse(code, errorMessage, code);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (otpKeyCallBack != null) {
                            otpKeyCallBack.onOtpKeyResponse(CONNECTION_ERROR_STATUS, null, null);
                        }
                    }
                });
    }

    @Override
    public void registerOtpKeyRequest(OtpKeyCallBack otpKeyCallBack) {
        this.otpKeyCallBack= otpKeyCallBack;
    }

    public interface OtpKeyCallBack {
        void onOtpKeyResponse(int statusCode, String errorMessage, Integer errorCode);
    }
}
