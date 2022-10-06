package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.ApplicationTypeDto;
import kz.optimabank.optima24.model.base.HistoryApplications;
import kz.optimabank.optima24.model.base.HistoryDetailsApplications;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.interfaces.HistoryApplicationsInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

/**
  Created by Timur on 17.07.2017.
 */

public class HistoryApllicationsImpl extends GeneralService implements HistoryApplicationsInterface {
    Callback callback;
    CallbackTypes callbackTypes;
    CallbackCreateApplication mCallbackCreateApplication;

    @Override
    public void getHistoryApplications(Context context) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getHistoryApplications(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                 new NetworkResponse.SuccessRequestListener<ArrayList<HistoryApplications>>() {
                    @Override
                    public void onSuccess(ArrayList<HistoryApplications> response, ResponseBody errorBody, int httpStatusCode) {
                        if(httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        } else {
                            GeneralManager.getInstance().setHistoryApplicationses(response);
                        }
                        Log.i("HistorAppl","response starter= "+response);
                        if (callback != null) {
                            callback.jsonHistoryAppResponse(httpStatusCode, errorMessage, response);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if (callback != null) {
                            callback.jsonHistoryAppResponse(CONNECTION_ERROR_STATUS, null, null);
                        }
                    }
                });
    }

    @Override
    public void getTypesApplications(Context context) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getTypesApplications(context, HeaderHelper.getOpenSessionHeader(context, sessionId),
                new NetworkResponse.SuccessRequestListener<ArrayList<ApplicationTypeDto>>() {
                    @Override
                    public void onSuccess(ArrayList<ApplicationTypeDto> response, ResponseBody errorBody, int httpStatusCode) {
                        if(httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        Log.i("TypesAppl","response starter= "+response);
                        callbackTypes.jsonTypesApplicationsResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callbackTypes.jsonTypesApplicationsResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void getApplicationById(Context context, int id) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getApplicationById(context, id, HeaderHelper.getOpenSessionHeader(context, sessionId),
                new NetworkResponse.SuccessRequestListener<ApplicationTypeDto>() {
                    @Override
                    public void onSuccess(ApplicationTypeDto response, ResponseBody errorBody, int httpStatusCode) {
                        if(httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        Log.i("TypesAppl","response starter= "+response);
                        callbackTypes.jsongetApplicationByIdResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callbackTypes.jsongetApplicationByIdResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void getApplicationDetailsById(Context context, int id) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getApplicationDetailsById(context, id, HeaderHelper.getOpenSessionHeader(context, sessionId),
                new NetworkResponse.SuccessRequestListener<ApplicationTypeDto>() {
                    @Override
                    public void onSuccess(ApplicationTypeDto response, ResponseBody errorBody, int httpStatusCode) {
                        if(httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        Log.i("ApplDetailsById","response = "+response);
                        callback.jsonApplicationDetailsById(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonApplicationDetailsById(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void getHistoryDetailsById(Context context, int id) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getHistoryDetailsById(context, id, HeaderHelper.getOpenSessionHeader(context, sessionId),
                new NetworkResponse.SuccessRequestListener<HistoryDetailsApplications>() {
                    @Override
                    public void onSuccess(HistoryDetailsApplications response, ResponseBody errorBody, int httpStatusCode) {
                        if(httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        Log.i("HistoryApplDetailsById","response = "+response);
                        callback.jsonHistoryDetailsById(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonHistoryDetailsById(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void getHistoryApplicationsByDate(Context context, String dateFrom, String dateTo) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getHistoryApplicationsByDate(context, HeaderHelper.getOpenSessionHeader(context, sessionId), dateFrom, dateTo,
                 new NetworkResponse.SuccessRequestListener<ArrayList<HistoryApplications>>() {
                    @Override
                    public void onSuccess(ArrayList<HistoryApplications> response, ResponseBody errorBody, int httpStatusCode) {
                        if(httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        Log.i("HistorAppl","response starter= "+response);
                        callback.jsonHistoryAppResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        callback.jsonHistoryAppResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void createApplication(Context context, JSONObject applicationBody) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().createApplication(context, HeaderHelper.getOpenSessionHeader(context, sessionId), applicationBody,
                new NetworkResponse.SuccessRequestListener<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        mCallbackCreateApplication.jsonCreateApplication(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        mCallbackCreateApplication.jsonCreateApplication(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }


    @Override
    public void registerCallBack(HistoryApllicationsImpl.Callback callback) {
        this.callback = callback;
    }

    @Override
    public void registerCallBackTypes(HistoryApllicationsImpl.CallbackTypes callbackTypes) {
        this.callbackTypes = callbackTypes;
    }

    @Override
    public void registerCallbackCreateApplication(CallbackCreateApplication callbackCreateApplication) {
        this.mCallbackCreateApplication = callbackCreateApplication;
    }

    public interface Callback {
        void jsonHistoryAppResponse(int statusCode, String errorMessage, ArrayList<HistoryApplications> response);
        void jsonApplicationDetailsById(int statusCode, String errorMessage, ApplicationTypeDto response);
        void jsonHistoryDetailsById(int statusCode, String errorMessage, HistoryDetailsApplications response);
    }

    public interface CallbackTypes {
        void jsonTypesApplicationsResponse(int statusCode, String errorMessage, ArrayList<ApplicationTypeDto> response);
        void jsongetApplicationByIdResponse(int statusCode, String errorMessage, ApplicationTypeDto response);
    }

    public interface CallbackCreateApplication {
        void jsonCreateApplication(int statusCode, String errorMessage, ResponseBody response);
    }
}
