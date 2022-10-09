package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import kz.optimabank.optima24.model.service.RemoveOtpKeyImpl;

public interface RemoveOtpKeyInterface {
    void removeOtpKeyRequest(Context context, int otpKey);
    void registerOtpKeyRequest(RemoveOtpKeyImpl.OtpKeyCallBack otpKeyCallBack);
}
