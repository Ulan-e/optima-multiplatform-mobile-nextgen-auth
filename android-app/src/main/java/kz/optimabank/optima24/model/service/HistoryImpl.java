package kz.optimabank.optima24.model.service;

import android.content.Context;

import java.util.ArrayList;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.HistoryItem;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.History;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;

/**
  Created by Timur on 04.06.2017.
 */

public class HistoryImpl extends GeneralService implements History {
    private CallbackPaymentHistory callbackPaymentHistory;
    private CallbackTransferHistory callbackTransferHistory;

    @Override
    public void getPaymentHistory(Context context, String fromDate, String toDate) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getPaymentHistory(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId), fromDate,
                toDate, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ArrayList<HistoryItem.PaymentHistoryItem>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<HistoryItem.PaymentHistoryItem>> response, String errorMessage, int code){
                        if(code == 0) {
                            //Log.d("TAG","response getPaymentHistory = " + response.get(0).AccountNumber);
                            GeneralManager.getInstance().setPaymentHistory(response.data);
                        }
                        callbackPaymentHistory.jsonPaymentHistoryResponse(code, errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callbackPaymentHistory.jsonPaymentHistoryResponse(Constants.CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void getTransferHistory(Context context, String fromDate, String toDate) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getTransferHistory(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId), fromDate,
                toDate, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ArrayList<HistoryItem.TransferHistoryItem>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<HistoryItem.TransferHistoryItem>> response, String errorMessage, int code){
                        if(code == 0) {
                            //Log.d("TAG","response getTransferHistory = " + response);
                            GeneralManager.getInstance().setTransferHistory(response.data);
                        }
                        callbackTransferHistory.jsonTransferHistoryResponse(code, errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callbackTransferHistory.jsonTransferHistoryResponse(Constants.CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void registerPaymentCallBack(CallbackPaymentHistory callback) {
        this.callbackPaymentHistory = callback;
    }

    @Override
    public void registerTransferCallBack(CallbackTransferHistory callback) {
        this.callbackTransferHistory = callback;
    }

    public interface CallbackPaymentHistory {
        void jsonPaymentHistoryResponse(int statusCode,String errorMessage);
    }

    public interface CallbackTransferHistory {
        void jsonTransferHistoryResponse(int statusCode,String errorMessage);
    }
}
