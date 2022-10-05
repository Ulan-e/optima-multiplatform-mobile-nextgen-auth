package kz.optimabank.optima24.fragment.transfer.phoneNumberRequest;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

import android.content.Context;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.manager.GeneralManager;

public class DefaultCardStatusActionImpl implements DefaultCardStatusAction {

    private DefaultCardStatusCallback defaultCardStatusCallback;

    @Override
    public void setDefaultCard(Context context, long accountId) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().setDefaultCard(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId), accountId,
                (response, message, code) -> {
                    if (response != null) {
                        if (defaultCardStatusCallback != null) {
                            defaultCardStatusCallback.getDefaultCardStatus(code, message);
                        }
                    }
                }, () -> {
                    if (defaultCardStatusCallback != null) {
                        defaultCardStatusCallback.getDefaultCardStatus(CONNECTION_ERROR_STATUS, null);
                    }
                }
        );
    }

    @Override
    public void registerCardStatusCallBack(DefaultCardStatusCallback callback) {
        this.defaultCardStatusCallback = callback;
    }
}
