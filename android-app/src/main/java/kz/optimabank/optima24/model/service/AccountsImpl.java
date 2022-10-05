package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.ATFStatement;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.AccountsResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.Accounts;
import kz.optimabank.optima24.model.manager.GeneralManager;
import retrofit2.Call;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 28.02.2017.
 */

public class AccountsImpl extends GeneralService implements Accounts {
    private Callback callback;
    private UpdateCallback updateCallback;
    private Call accountsCall;

    @Override
    public void getAccounts(final Context context, boolean isShowPB, boolean needUpdate) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        accountsCall = NetworkResponse.getInstance().getAccounts(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<AccountsResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<AccountsResponse> response, String errorMessage, int code) {
                        if(code == 0) {
                            GeneralManager.getInstance().setCards(response.data);
                            GeneralManager.getInstance().setCardsAccounts(response.data);
                            GeneralManager.getInstance().setCheckingAccounts(response.data);
                            GeneralManager.getInstance().setCredit(response.data);
                            GeneralManager.getInstance().setDeposits(response.data);
                            GeneralManager.getInstance().setWishDeposits(response.data);
                            GeneralManager.getInstance().setEncryptedCards(response.data);
                        }
                        if(callback!=null) {
                            callback.jsonAccountsResponse(code, errorMessage);
                        }
                        if(updateCallback!=null){
                            updateCallback.jsonAccountsResponse(code, errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if(callback!=null) {
                            callback.jsonAccountsResponse(CONNECTION_ERROR_STATUS, null);
                        }
                        if(updateCallback!=null) {
                            updateCallback.jsonAccountsResponse(CONNECTION_ERROR_STATUS, null);
                        }
                    }
                }, isShowPB, needUpdate);
    }

    @Override
    public void getAccountsOperations(final Context context) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getAccountsOperations(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ArrayList<ATFStatement>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<ATFStatement>> response, String errorMessage, int code) {
                        if(code == 0) {
                            GeneralManager.getInstance().setStatements(response.data);
                            Log.i("responseStatments","response = "+response);
                        }
                        callback.jsonAccountsOperationsResponse(code, errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonAccountsResponse(CONNECTION_ERROR_STATUS,null);
                    }
                });
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void registerUpdateCallBack(UpdateCallback callback) {
        this.updateCallback = callback;
    }

    @Override
    public void cancelAccountsRequest() {
        if(accountsCall != null){
            accountsCall.cancel();
        }
    }

    public interface Callback {
        void jsonAccountsResponse(int statusCode,String errorMessage);
        void jsonAccountsOperationsResponse(int statusCode,String errorMessage);
    }

    public interface UpdateCallback {
        void jsonAccountsResponse(int statusCode,String errorMessage);
    }
}