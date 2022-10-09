package kz.optimabank.optima24.fragment.transfer.phoneNumberRequest;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

import android.content.Context;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.manager.GeneralManager;

public class TransferByPhoneNumberActionImpl implements TransferByPhoneNumberAction {

    private AccDataByPhoneNumberCallback accDataByPhoneNumberCallback;

    @Override
    public void getAccountDataByPhoneNumber(Context context, String phoneNumber) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getAccDataByPhoneNumber(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                phoneNumber, (response, errorMessage, code) -> {
                    if (response != null) {
                        accDataByPhoneNumberCallback.getAccDataByPhoneNumberResponse(code, errorMessage, response.data);
                    }
                },
                () -> accDataByPhoneNumberCallback.getAccDataByPhoneNumberResponse(
                        CONNECTION_ERROR_STATUS,
                        null,
                        null
                )
        );
    }

    @Override
    public void registerAccDataPhoneNumberCallback(AccDataByPhoneNumberCallback callback) {
        this.accDataByPhoneNumberCallback = callback;
    }
}