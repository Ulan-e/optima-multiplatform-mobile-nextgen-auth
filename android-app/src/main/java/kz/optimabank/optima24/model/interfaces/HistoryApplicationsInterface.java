package kz.optimabank.optima24.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

import kz.optimabank.optima24.model.service.HistoryApllicationsImpl;

public interface HistoryApplicationsInterface {
    void getHistoryApplications(Context context);
    void getTypesApplications(Context context);
    void getApplicationById(Context context, int id);
    void getApplicationDetailsById(Context context, int id);
    void getHistoryDetailsById(Context context, int id);
    void getHistoryApplicationsByDate(Context context, String dateFrom, String dateTo);
    void createApplication(Context context, JSONObject applicationBody);
    void registerCallBack(HistoryApllicationsImpl.Callback callback);
    void registerCallBackTypes(HistoryApllicationsImpl.CallbackTypes callback);
    void registerCallbackCreateApplication(HistoryApllicationsImpl.CallbackCreateApplication callbackCreateApplication);
}
