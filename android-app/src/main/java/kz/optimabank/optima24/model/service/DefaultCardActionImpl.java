package kz.optimabank.optima24.model.service;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

import android.content.Context;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.interfaces.DefaultCardAction;
import kz.optimabank.optima24.model.manager.GeneralManager;
import retrofit2.Call;

public class DefaultCardActionImpl implements DefaultCardAction {

    private DefaultStatusCardCallback defaultStatusCardCallback;
    private Call call;

    @Override
    public void setDefaultCardStatus(Context context, long accountId) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        call = NetworkResponse.getInstance().setDefaultCard(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                accountId, (response, errorMessage, code) -> {
                    if (response != null) {
                        defaultStatusCardCallback.jsonDefaultStatusCardResponse(response.code, errorMessage);
                    }
                }, () -> {
                    if (defaultStatusCardCallback != null) {
                        defaultStatusCardCallback.jsonDefaultStatusCardResponse(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void registerDefaultCardCallBack(DefaultStatusCardCallback callback) {
        defaultStatusCardCallback = callback;
    }

    public interface DefaultStatusCardCallback {
        void jsonDefaultStatusCardResponse(int code, String message);
    }
}
