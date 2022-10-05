package kz.optimabank.optima24.model.service;

import android.content.Context;
import java.util.ArrayList;

import kg.optima.mobile.R;
import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.base.Terminal;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.ServicePoints;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.getToast;

/**
  Created by Timur on 28.03.2017.
 */

public class ServicePointsImpl extends GeneralService implements ServicePoints {
    private Callback callback;

    @Override
    public void getAllServicePoints(final Context context) {
        if(NetworkResponse.getInstance().getAllServicePoints(context, OptimaBank.getInstance().getOpenSessionHeader(null),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ArrayList<Terminal>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<Terminal>> response, String errorMessage, int code) {
                        if(response != null && response.data != null && !response.data.isEmpty()){
                            ArrayList<Terminal> branchesList = new ArrayList<>();
                            ArrayList<Terminal> terminalList = new ArrayList<>();
                            ArrayList<Terminal> atmList = new ArrayList<>();
                            for(Terminal terminal: response.data){
                                switch (terminal.getPointType()){
                                    case 0:
                                        branchesList.add(terminal);
                                        break;
                                    case 2:
                                        atmList.add(terminal);
                                        break;
                                    case 3:
                                        terminalList.add(terminal);
                                        break;
                                }
                            }
                            GeneralManager.getInstance().setBranches(branchesList);
                            GeneralManager.getInstance().setAtms(atmList);
                            GeneralManager.getInstance().setTerminals(terminalList);
                        }
                        callback.jsonAllServiceResponse(code,errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        callback.jsonAllServiceResponse(CONNECTION_ERROR_STATUS,null);
                    }
                })==null) {
            callback.jsonAllServiceResponse(CONNECTION_ERROR_STATUS,null);
        }
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void jsonAllServiceResponse(int statusCode, String errorMessage);
    }
}
