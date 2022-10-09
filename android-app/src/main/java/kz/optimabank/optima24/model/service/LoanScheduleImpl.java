package kz.optimabank.optima24.model.service;

import android.content.Context;

import java.util.ArrayList;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.gson.response.LoanScheduleResponse;
import kz.optimabank.optima24.model.interfaces.LoanSchedule;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 27.06.2017.
 */

public class LoanScheduleImpl extends GeneralService implements LoanSchedule {
    Callback callback;

    @Override
    public void getLoanSchedule(Context context, int code) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getLoanSchedule(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                code, new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ArrayList<LoanScheduleResponse>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<LoanScheduleResponse>> response, String errorMessage, int code) {
                        if(code == 0) {
                            GeneralManager.getInstance().setLoanScheduleResponses(response.data);
                        }
                        if(callback!=null) {
                            callback.jsonLoanScheduleResponse(code,errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if(callback!=null) {
                            callback.jsonLoanScheduleResponse(CONNECTION_ERROR_STATUS, null);
                        }
                    }
                });
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonLoanScheduleResponse(int statusCode, String errorMessage);
    }
}
