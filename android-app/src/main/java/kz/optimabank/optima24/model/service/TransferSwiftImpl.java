
package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.gson.response.TransferConfirmResponse;
import kz.optimabank.optima24.model.interfaces.TransfersSwift;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.TAG;

/**
 * Created by Timur on 05.05.2017.
 */

public class TransferSwiftImpl extends GeneralService implements TransfersSwift {
    private Callback callback;

    @Override
    public void checkSwift(Context context, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().checkSwift(context, body, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<CheckResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<CheckResponse> response, String errorMessage, int code) {
                        Log.i("CSR", "httpStatusCode = " + code);
                        if (callback != null && response != null) {
                            callback.onCheckSwiftResponse(code, response.data, errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callback != null) {
                            callback.onCheckSwiftResponse(CONNECTION_ERROR_STATUS, null, null);
                        }
                    }
                });
    }

    @Override
    public void confirmSwift(Context context, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().confirmSwift(context, body, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<TransferConfirmResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<TransferConfirmResponse> response, String errorMessage, int code) {
                        if (code != 0) {
                            GeneralManager.getInstance().setTransferDocumentId(null);
                        }
                        if (code == 0) {
                            Log.d(TAG, "documentId = " + response.data.documentId);
                            GeneralManager.getInstance().setTransferDocumentId(String.valueOf(response.data.documentId));
                        }
                        if (callback != null) {
                            callback.onConfirmSwiftResponse(code, response.data, errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callback != null)
                            callback.onConfirmSwiftResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void registerCallBack(TransferSwiftImpl.Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onCheckSwiftResponse(int statusCode, CheckResponse response, String errorMessage);
        void onConfirmSwiftResponse(int statusCode, TransferConfirmResponse response, String errorMessage);
    }
}
