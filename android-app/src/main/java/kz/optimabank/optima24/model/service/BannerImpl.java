package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.app.ServiceGenerator;
import kz.optimabank.optima24.model.base.Banner;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.interfaces.BannerInterface;
import kz.optimabank.optima24.model.interfaces.IApiMethods;
import retrofit2.Response;

import static kz.optimabank.optima24.utility.Constants.API_BASE_URL;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.SUCCESS_STATUS_CODE;

public class BannerImpl extends GeneralService implements BannerInterface {

    private static final String TAG = "banners_tag";

    private Callback bannersCallback;
    private CheckServerAvailabilityCallback checkServerAvailabilityCallback;

    @Override
    public void getBanners(Context context) {
        NetworkResponse.getInstance().getBanners(context, HeaderHelper.getOpenSessionHeader(context, null),
                (response, errorMessage, code) -> {
                    if (response != null)
                        bannersCallback.jsonBannersResponse(code, errorMessage, response.data);
                }, () -> bannersCallback.jsonBannersResponse(CONNECTION_ERROR_STATUS, null, null));
    }

    @Override
    public void registerCallBack(BannerImpl.Callback callback) {
        this.bannersCallback = callback;
    }

    @Override
    public void checkServerAvailability(Context context) {
        getApi(context).checkServer(HeaderHelper.getOpenSessionHeader(context, null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response<BaseResponse<String>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<String>> response) {
                        if (response.code() == SUCCESS_STATUS_CODE && response != null) {
                            checkServerAvailabilityCallback.jsonCheckServerResponse(true);
                        } else {
                            checkServerAvailabilityCallback.jsonCheckServerResponse(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        checkServerAvailabilityCallback.jsonCheckServerResponse(false);
                        Log.d(TAG, "Error check server " + e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void registerCheckServerAvailabilityCallback(CheckServerAvailabilityCallback callback) {
        this.checkServerAvailabilityCallback = callback;
    }

    // получаем ссылку на API запрос
    private IApiMethods getApi(Context context) {
        ServiceGenerator request = new ServiceGenerator();
        return request.request(context, null, API_BASE_URL, false, false);
    }

    public interface Callback {
        void jsonBannersResponse(int statusCode, String errorMessage, ArrayList<Banner> banners);
    }

    public interface CheckServerAvailabilityCallback {
        void jsonCheckServerResponse(Boolean isSuccess);
    }
}