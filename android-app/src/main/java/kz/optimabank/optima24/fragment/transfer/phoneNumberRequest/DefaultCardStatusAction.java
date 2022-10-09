package kz.optimabank.optima24.fragment.transfer.phoneNumberRequest;

import android.content.Context;

public interface DefaultCardStatusAction {
    void setDefaultCard(Context context, long accountId);
    void registerCardStatusCallBack(DefaultCardStatusCallback callback);

    interface DefaultCardStatusCallback {
        void getDefaultCardStatus(int statusCode, String errorMessage);
    }
}