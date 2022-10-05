package kz.optimabank.optima24.fragment.transfer.phoneNumberRequest;

import android.content.Context;

import kz.optimabank.optima24.model.base.AccountPhoneNumberResponse;

public interface TransferByPhoneNumberAction {

    void getAccountDataByPhoneNumber(Context context, String phoneNumber);

    void registerAccDataPhoneNumberCallback(AccDataByPhoneNumberCallback callback);

    interface AccDataByPhoneNumberCallback {
        void getAccDataByPhoneNumberResponse(int statusCode, String errorMessage, AccountPhoneNumberResponse response);
    }
}

