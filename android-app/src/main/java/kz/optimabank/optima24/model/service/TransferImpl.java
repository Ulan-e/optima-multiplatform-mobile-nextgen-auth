package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.AccStatusResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.gson.response.TransferConfirmResponse;
import kz.optimabank.optima24.model.interfaces.Transfers;
import kz.optimabank.optima24.model.manager.GeneralManager;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
 Created by Timur on 05.05.2017.
 */

public class TransferImpl extends GeneralService implements Transfers {
    private static final String TAG = TransferImpl.class.getSimpleName();

    private Callback callback;
    private CallbackTransfer callbackInterbank;
    private CallbackConfirm callbackConfirm;

    @Override
    public void getAccountData(final Context context, String cardNumber) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getAccData(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                cardNumber, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<AccStatusResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<AccStatusResponse> response, String errorMessage, int code) {
                        if(response != null) {
                            callback.getAccDataResponse(code, errorMessage, response.data);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.checkCardBrandResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void mastercardRegister(Context context, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().registerMasterCard(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId), body,
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ResponseBody>>() {
                    @Override
                    public void onSuccess(BaseResponse<ResponseBody> response, String errorMessage, int code) {
                        if(response != null) {
                            if (code != 0) {

                            }
                            if (callback != null) {
                                callback.mastercardRegisterResponse(code, errorMessage);
                            }
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.mastercardRegisterResponse(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void checkMt100Transfer(Context context,JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().checkMt100Transfer(context, body, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<CheckResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<CheckResponse> response, String errorMessage, int code) {
                        if(response != null) {
                            Log.i("checkMt100Transfer", "onSuccess");
                            if (callback != null) {
                                callback.checkMt100TransferResponse(code, response.data, errorMessage);
                            }
                            if (callbackInterbank != null) {
                                callbackInterbank.checkTransferResponse(code, response.data, errorMessage);
                            }
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        Log.i("checkMt100Transfer","onError");
                        callback.checkMt100TransferResponse(CONNECTION_ERROR_STATUS,null,null);
                    }
                });
    }

    @Override
    public void confirmMt100Transfer(Context context, JSONObject body) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().confirmMt100Transfer(context, body, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<TransferConfirmResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<TransferConfirmResponse> response, String errorMessage, int code) {
                        if(response != null) {
                            if (code != 0) {
                                GeneralManager.getInstance().setTransferDocumentId(null);
                            }
                            if (code == 0) {
                                Log.d(TAG, "documentId = " + response.data.documentId);
                                GeneralManager.getInstance().setTransferDocumentId(String.valueOf(response.data.documentId));
                            }
                            if (callbackConfirm != null) {
                                callbackConfirm.onConfirmTransferResponse(code, errorMessage);
                            }
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callbackConfirm.onConfirmTransferResponse(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void registerCallbackInterbank(CallbackTransfer callback) {
        this.callbackInterbank = callback;
    }

    @Override
    public void registerCallbackConfirm(CallbackConfirm callback) {
        this.callbackConfirm = callback;
    }

    public interface Callback {
        void checkMt100TransferResponse(int statusCode, CheckResponse response, String errorMessage);
        void checkCardBrandResponse(int statusCode, String errorMessage, String cardValue);
        void getAccDataResponse(int statusCode, String errorMessage, AccStatusResponse response);
        void mastercardRegisterResponse(int statusCode, String errorMessage);
    }

    public interface CallbackTransfer {
        void checkTransferResponse(int statusCode, CheckResponse response, String errorMessage);
    }

    public interface CallbackConfirm {
        void onConfirmTransferResponse(int statusCode, String errorMessage);
    }
}