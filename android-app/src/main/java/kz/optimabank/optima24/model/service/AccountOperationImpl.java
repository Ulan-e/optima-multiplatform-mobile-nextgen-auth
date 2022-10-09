package kz.optimabank.optima24.model.service;

import android.content.Context;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.base.StatementsWithStats;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.AccountOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;
import retrofit2.Call;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
 Created by Timur on 29.05.2017.
 */

public class AccountOperationImpl extends GeneralService implements AccountOperation {
    Callback callback;
    private Call call;

    @Override
    public void getAccountOperationsAndStats(Context context, int code, String fromDate, String toDate,
                                             final boolean isMainPage,boolean isShowProgress) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        call = NetworkResponse.getInstance().getAccountOperationsAndStats(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                code, fromDate, toDate, isShowProgress, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<StatementsWithStats>>() {
                    @Override
                    public void onSuccess(BaseResponse<StatementsWithStats> response, String errorMessage, int code) {
                        if(response != null) {
                            if (code == 0) {
                                if (isMainPage) {
                                    GeneralManager.getInstance().setStatementsTwoWeeksAccount(response.data.statements);
                                } else {
                                    GeneralManager.getInstance().setStatementsAccount(response.data.statements);
                                }
                            } else {
                                GeneralManager.getInstance().setStatementsTwoWeeksAccount(null);
                                GeneralManager.getInstance().setStatementsAccount(null);
                            }
                            if (callback != null) {
                                callback.jsonAccountOperationsAndStatsResponse(code, errorMessage, response.data);
                            }
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callback != null) {
                            callback.jsonAccountOperationsAndStatsResponse(CONNECTION_ERROR_STATUS, null, null);
                        }
                    }
                });
    }

    @Override
    public void cancelAccountOperation() {
        if(call!=null) {
            call.cancel();
        }
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonAccountOperationsAndStatsResponse(int code, String errorMessage, StatementsWithStats response);
    }
}