package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.Category;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.Categories;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 28.02.2017.
 */

public class CategoriesImpl extends GeneralService implements Categories {
    private Callback callback;

    @Override
    public void getCategories(final Context context, boolean isShowPB) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getCategories(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ArrayList<Category>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<Category>> response, String errorMessage, int code) {
                        if(code == 0) {
                            Log.i("categoryCI","response = "+response);
                            GeneralManager.getInstance().setCategories(response.data);
                        }
                        if(callback!=null) {
                            callback.jsonCategoriesResponse(code, errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if(callback!=null) {
                            callback.jsonCategoriesResponse(CONNECTION_ERROR_STATUS, null);
                        }
                    }
                }, isShowPB);
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback=callback;
    }

    public interface Callback {
        void jsonCategoriesResponse(int statusCode, String errorMessage);
    }

}