package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.graphics.drawable.LevelListDrawable;

import java.util.ArrayList;

import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.base.NewsItem;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.News;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 30.03.2017.
 */

public class NewsImpl extends GeneralService implements News {
    private Callback callback;
    private CallbackImage callbackImage;

    @Override
    public void getNews(Context context) {
        NetworkResponse.getInstance().getNews(context, OptimaBank.getInstance().getOpenSessionHeader(null),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ArrayList<NewsItem>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<NewsItem>> response, String errorMessage, int code) {
                        if(code == 0) {
                            if (response.data!=null)
                                GeneralManager.getInstance().setNews(response.data);
                        }
                        callback.jsonNewsResponse(code,errorMessage);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonNewsResponse(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void getNewsImage(Context context, String name, String category) {
        NetworkResponse.getInstance().getNewsImage(context, OptimaBank.getInstance().getOpenSessionHeader(null), name, category,
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, String errorMessage, int code) {
                        if(code == 0) {
                            if (response.data!=null)
                                callbackImage.jsonNewsImageResponse(code, response.data);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callbackImage.jsonNewsImageResponse(CONNECTION_ERROR_STATUS, null);
                    }
                });
    }

    @Override
    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void registerImageCallBack(CallbackImage callbackImage) {
        this.callbackImage = callbackImage;
    }

    public interface Callback {
        void jsonNewsResponse(int statusCode,String errorMessage);
    }

    public interface CallbackImage {
        void jsonNewsImageResponse(int statusCode,String imageBytes);
    }
}
