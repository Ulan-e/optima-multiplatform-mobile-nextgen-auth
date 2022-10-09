package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import kg.optima.mobile.R;
import kz.optimabank.optima24.app.HeaderHelper;
import kz.optimabank.optima24.model.base.BaseRegistrationResponse;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.base.SecretQuestionResponse;
import kz.optimabank.optima24.model.interfaces.RegistrationClient;
import okhttp3.ResponseBody;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.getToast;

public class RegistrationClientImpl extends GeneralService implements RegistrationClient {
    private static final String TAG = RegistrationClientImpl.class.getSimpleName();

    private CheckClientCallback mCheckClientCallback;
    private SecretQuestionsCallback mSecretQuestionsCallback;
    private RequestSmsClientBankCallback mRequestSmsClientBankCallback;
    private FinishRegCallback mFinishRegCallback;
    private ChangeTempPasswordCallback mChangeTempPasswordCallback;
    private RequestSmsAgainCallback mRequestSmsAgainCallback;
    private RequestSmsNoClientBankCallback mRequestSmsNoClientBankCallback;

    @Override
    public void checkClient(final Context context, JSONObject body) {
        NetworkResponse.getInstance().checkClient(context, HeaderHelper.getOpenSessionHeader(context, null), body,
                new NetworkResponse.SuccessRequestListener<BaseRegistrationResponse>() {
                    @Override
                    public void onSuccess(BaseRegistrationResponse response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        mCheckClientCallback.clientCheckResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        mCheckClientCallback.clientCheckResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void getSecretQuestions(final Context context) {
        NetworkResponse.getInstance().getSecretQuestions(context, HeaderHelper.getOpenSessionHeader(context, null),
                new NetworkResponse.SuccessRequestListener<List<SecretQuestionResponse>>() {
                    @Override
                    public void onSuccess(List<SecretQuestionResponse> response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        mSecretQuestionsCallback.secretQuestionsResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        mSecretQuestionsCallback.secretQuestionsResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void requestSmsClientBank(final Context context, JSONObject body) {
        NetworkResponse.getInstance().requestSmsForClientBank(context, HeaderHelper.getOpenSessionHeader(context, null), body,
                new NetworkResponse.SuccessRequestListener<BaseRegistrationResponse>() {
                    @Override
                    public void onSuccess(BaseRegistrationResponse response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        mRequestSmsClientBankCallback.requestSmsClientBankResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        mRequestSmsClientBankCallback.requestSmsClientBankResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void requestSmsNoClientBank(final Context context, JSONObject clientBody) {
        NetworkResponse.getInstance().requestSmsForNoClientBank(context, HeaderHelper.getOpenSessionHeader(context, null), clientBody,
                new NetworkResponse.SuccessRequestListener<BaseRegistrationResponse>() {
                    @Override
                    public void onSuccess(BaseRegistrationResponse response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        mRequestSmsNoClientBankCallback.requestSmsNoClientBankResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        mRequestSmsNoClientBankCallback.requestSmsNoClientBankResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void requestSmsAgain(final Context context, String phoneNumber) {
        NetworkResponse.getInstance().requestSmsAgain(context, HeaderHelper.getOpenSessionHeader(context, null), phoneNumber,
                new NetworkResponse.SuccessRequestListener<BaseRegistrationResponse>() {
                    @Override
                    public void onSuccess(BaseRegistrationResponse response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        mRequestSmsAgainCallback.requestSmsAgainResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        mRequestSmsAgainCallback.requestSmsAgainResponse(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void finishRegForClientBank(final Context context, JSONObject body) {
        NetworkResponse.getInstance().finishRegClientBank(context, HeaderHelper.getOpenSessionHeader(context, null), body,
                new NetworkResponse.SuccessRequestListener<BaseRegistrationResponse>() {
                    @Override
                    public void onSuccess(BaseRegistrationResponse response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        mFinishRegCallback.finishRegResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        mFinishRegCallback.finishRegResponse(CONNECTION_ERROR_STATUS, null ,null);
                    }
                });
    }

    @Override
    public void finishRegForNoClientBank(final Context context, JSONObject body) {
        NetworkResponse.getInstance().finishRegNoClientBank(context, HeaderHelper.getOpenSessionHeader(context, null), body,
                new NetworkResponse.SuccessRequestListener<BaseRegistrationResponse>() {
                    @Override
                    public void onSuccess(BaseRegistrationResponse response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        mFinishRegCallback.finishRegResponse(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        mFinishRegCallback.finishRegResponse(CONNECTION_ERROR_STATUS, null ,null);
                    }
                });
    }

    @Override
    public void changeTempPassword(final Context context, JSONObject body) {
        NetworkResponse.getInstance().changeTempPassword(context, HeaderHelper.getOpenSessionHeader(context, null), body,
                new NetworkResponse.SuccessRequestListener<BaseRegistrationResponse>() {
                    @Override
                    public void onSuccess(BaseRegistrationResponse response, ResponseBody errorBody, int httpStatusCode) {
                        if (httpStatusCode != 200) {
                            errorMessage = getErrorMessage(errorBody);
                        }
                        mChangeTempPasswordCallback.changeTempPasswordCallback(httpStatusCode, errorMessage, response);
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        mChangeTempPasswordCallback.changeTempPasswordCallback(CONNECTION_ERROR_STATUS, null, null);
                    }
                });
    }

    @Override
    public void checkPhoneNumber(Context context, String phoneNumber) {
        NetworkResponse.getInstance().checkPhoneNumber(phoneNumber, context, HeaderHelper.getOpenSessionHeader(context, null),
                (response, errorBody, httpStatusCode) -> {
                    if (httpStatusCode != 200) {
                        errorMessage = getErrorMessage(errorBody);
                    }
                    try {
                        if (response!=null)
                            mCheckClientCallback.checkPhoneNumberResponse(httpStatusCode, errorMessage, response.string());
                    } catch (IOException e) {
                        Log.e(TAG, "Couldn't get content from response", e);
                    }
                }, () -> {
                    getToast(context,context.getString(R.string.internet_connection_error));
                    mCheckClientCallback.checkPhoneNumberResponse(CONNECTION_ERROR_STATUS, null, null);
                });
    }

    @Override
    public void setCheckClientCallback(CheckClientCallback checkClientCallback) {
        mCheckClientCallback = checkClientCallback;
    }

    @Override
    public void setSecretQuestionsCallback(SecretQuestionsCallback secretQuestionsCallback) {
        mSecretQuestionsCallback = secretQuestionsCallback;
    }

    @Override
    public void setRequestSmsClientBankCallback(RequestSmsClientBankCallback requestSmsClientBankCallback) {
        mRequestSmsClientBankCallback = requestSmsClientBankCallback;
    }

    @Override
    public void setFinishRegCallback(FinishRegCallback finishRegForClientBankCallback) {
        mFinishRegCallback = finishRegForClientBankCallback;
    }

    @Override
    public void setChangeTempPasswordCallback(ChangeTempPasswordCallback changeTempPasswordCallback) {
        mChangeTempPasswordCallback = changeTempPasswordCallback;
    }

    @Override
    public void setRequestSmsAgainCallback(RequestSmsAgainCallback requestSmsAgainCallback) {
        mRequestSmsAgainCallback = requestSmsAgainCallback;
    }

    @Override
    public void setRequestSmsNoClientBankCallback(RequestSmsNoClientBankCallback requestSmsNoClientBankCallback) {
        mRequestSmsNoClientBankCallback = requestSmsNoClientBankCallback;
    }

    public interface CheckClientCallback {
        void clientCheckResponse(int statusCode, String errorMessage, BaseRegistrationResponse response);
        void checkPhoneNumberResponse(int statusCode, String errorMessage, String resultCode);
    }

    public interface SecretQuestionsCallback {
        void secretQuestionsResponse(int statusCode, String errorMessage, List<SecretQuestionResponse> response);
    }

    public interface RequestSmsClientBankCallback {
        void requestSmsClientBankResponse(int statusCode, String errorMessage, BaseRegistrationResponse responseBody);
    }

    public interface RequestSmsNoClientBankCallback {
        void requestSmsNoClientBankResponse(int statusCode, String errorMessage, BaseRegistrationResponse response);
    }

    public interface RequestSmsAgainCallback {
        void requestSmsAgainResponse(int statusCode, String errorMessage, BaseRegistrationResponse responseBody);
    }

    public interface FinishRegCallback {
        void finishRegResponse(int statusCode, String errorMessage, BaseRegistrationResponse responseBody);
    }

    public interface ChangeTempPasswordCallback {
        void changeTempPasswordCallback(int statusCode, String errorMessage, BaseRegistrationResponse responseBody);
    }
}
