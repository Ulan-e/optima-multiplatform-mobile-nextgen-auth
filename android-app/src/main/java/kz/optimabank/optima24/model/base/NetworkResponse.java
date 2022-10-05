package kz.optimabank.optima24.model.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.OptimaActivity;
import kz.optimabank.optima24.app.ServiceGenerator;
import kz.optimabank.optima24.db.entry.ForeignBank;
import kz.optimabank.optima24.feature.authorization.authorization.pin.PinEnterActivity;
import kz.optimabank.optima24.model.gson.APIError;
import kz.optimabank.optima24.model.gson.response.AccStatusResponse;
import kz.optimabank.optima24.model.gson.response.AccountsResponse;
import kz.optimabank.optima24.model.gson.response.AuthorizationResponse;
import kz.optimabank.optima24.model.gson.response.BankReference;
import kz.optimabank.optima24.model.gson.response.BankRequisitesResponse;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.gson.response.Bool;
import kz.optimabank.optima24.model.gson.response.CheckPaymentsResponse;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.gson.response.Data;
import kz.optimabank.optima24.model.gson.response.DictionaryResponse;
import kz.optimabank.optima24.model.gson.response.LoanScheduleResponse;
import kz.optimabank.optima24.model.gson.response.MobileOperatorResponse;
import kz.optimabank.optima24.model.gson.response.PaymentContextResponse;
import kz.optimabank.optima24.model.gson.response.PaymentTemplateResponse;
import kz.optimabank.optima24.model.gson.response.TaxDictResponse;
import kz.optimabank.optima24.model.gson.response.TransferConfirmResponse;
import kz.optimabank.optima24.model.interfaces.IApiMethods;
import kz.optimabank.optima24.utility.Utilities;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import static kz.optimabank.optima24.utility.Constants.API_BASE_URL;
import static kz.optimabank.optima24.utility.Constants.TAG;
import static kz.optimabank.optima24.utility.Utilities.isInternetConnectionError;

public class NetworkResponse {
    private static NetworkResponse mInstance;
    private static ServiceGenerator request;
    private ProgressDialog progressDialog;
    public APIError error;
    private AlertDialog alertDialog;
    static boolean isConfirmPaymentsNotEnded;

    public interface SuccessRequestListener<T> {
        void onSuccess(T response, ResponseBody errorBody, int httpStatusCode);
    }

    public interface SuccessRequestListenerAllResponse<T> {
        void onSuccess(T response, String errorMessage, int code);
    }

    public interface ErrorRequestListener {
        void onError();
    }

    public static NetworkResponse getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkResponse();
        }
        if (request == null) {
            request = new ServiceGenerator();
        }
        return mInstance;
    }

    private <T> void httpResponseSuccess(Context context, SuccessRequestListener<T> success, Response<T> response) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (response.code() == 401) {
            Retrofit retrofit = ServiceGenerator.retrofit();
            if (retrofit != null) {
                Converter<ResponseBody, APIError> converter =
                        ServiceGenerator.retrofit().responseBodyConverter(APIError.class, new Annotation[0]);
                try {
                    error = converter.convert(response.errorBody());
                } catch (Exception e) {
                    Log.e("ErrorUtils", "error when convert " + e);
                }
            }

            if (error != null) {
                Log.i("NetworkResponse", "error.message = " + error.message);
                Log.i("NetworkResponse", "error.code = " + error.error.code);
            /*try {
                if (response.errorBody().string().contains("-100")){
                    Log.i("NetworkResponse", "contains - 100");
                    Toast.makeText(context,R.string.exception_pasword,Toast.LENGTH_LONG).show();
                }else {
                    if (response.errorBody().string().contains("-105")) {
                        Log.i("NetworkResponse", "contains - 105");
                        context.startActivity(new Intent(context, UnauthorizedTabActivity.class));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
                if (error.error.code == -100) {
                    Log.i("NetworkResponse", "contains - 100");
                    Toast.makeText(context, R.string.exception_password_wrong, Toast.LENGTH_LONG).show();
                } else {
                    if (error.error.code == -105) {
                        Log.i("NetworkResponse", "contains - 105");
//                        context.startActivity(new Intent(context, UnauthorizedTabActivity.class));
                    }
                }

                //return;
            }
        }
        if (response.body() == null) {
            success.onSuccess(null, response.errorBody(), response.code());
            Log.i("ResponseNull", "connection error" + response.code());
        } else if (response.code() != 200) {
            Log.i("ERRORHTTP", "error = " + ((BaseResponse) response.body()).message);
            Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        } else {
            if (response.body() instanceof BaseResponse && ((BaseResponse) response.body()).code == -105) {
                Log.i("Response.body.code", " = -105 session end");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setMessage(context.getString(R.string.session_ended));
                builder.setPositiveButton(context.getString(R.string._yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, PinEnterActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        ((OptimaActivity) context).finish();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            } else {
                Log.i("response.code()", " = " + response.code());
                success.onSuccess(response.body(), response.errorBody(), response.code());
            }
        }
    }

    private <T> void httpResponseSuccessWithAllResponseInSuccesListener(Context context, SuccessRequestListenerAllResponse<BaseResponse<T>> successRequestListenerAllResponse, Response<BaseResponse<T>> response) {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }

        if (response.code() == 401) {
            Retrofit retrofit = ServiceGenerator.retrofit();
            if (retrofit != null) {
                Converter<ResponseBody, APIError> converter =
                        ServiceGenerator.retrofit().responseBodyConverter(APIError.class, new Annotation[0]);
                try {
                    error = converter.convert(response.errorBody());
                } catch (Exception e) {
                    Log.e("ErrorUtils", "error when convert " + e);
                }
            }
            if (!(this.error == null || this.error.error == null)) {
                Log.i("NetworkResponse", "error.message = " + this.error.message);
                Log.i("NetworkResponse", "error.code = " + this.error.error.code);
                if (this.error.error.code == -100) {
                    Log.i("NetworkResponse", "contains - 100");
                    Toast.makeText(context, R.string.exception_password_wrong, Toast.LENGTH_LONG).show();
                } else if (this.error.error.code == -105) {
                    Log.i("NetworkResponse", "contains - 105");
//                    context.startActivity(new Intent(context, UnauthorizedTabActivity.class));
                }
            }
        }
        if (response.body() == null) {
            successRequestListenerAllResponse.onSuccess(null, context.getString(R.string.internet_connection_error), response.code());
            Log.i("ResponseNull", "(WithAll)connection error" + response.code());
        } else if (response.code() != 200) {
            Log.i("ERRORHTTP", "(WithAll)error = " + ((BaseResponse) response.body()).message);
            Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        } else {
            if (response.body().code == -105) {
                Log.i("Response.body.code", " = -105 session end");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setMessage(context.getString(R.string.session_ended));
                builder.setPositiveButton(context.getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, PinEnterActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        ((OptimaActivity) context).finish();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            } else {
                Log.i("SuccesResponse1111", "(WithAll) = " + ((BaseResponse) response.body()).message);
                successRequestListenerAllResponse.onSuccess(response.body(), ((BaseResponse) response.body()).message, ((BaseResponse) response.body()).code);
            }
        }
    }

    private void httpResponseFailure(ErrorRequestListener error, Throwable t) {
        Log.d(TAG, "onFailure = " + t.getMessage());
        error.onError();
//        GeneralManager.getInstance().refreshSessionTime();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public Call openSession(final Context context, Map<String, String> body, Map<String, String> header,
                            final SuccessRequestListenerAllResponse<BaseResponse<AuthorizationResponse>> success,
                            final ErrorRequestListener error) {
        if (!isInternetConnectionError()) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<AuthorizationResponse>> call = api.openSession(body, header);
            call.enqueue(new Callback<BaseResponse<AuthorizationResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<AuthorizationResponse>> call, Response<BaseResponse<AuthorizationResponse>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                    Log.d(TAG, "onResponse MLoginFragment = ");
                }

                @Override
                public void onFailure(Call<BaseResponse<AuthorizationResponse>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure MLoginFragment = ");
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call keepAliveRequest(final Context context, Map<String, String> header,
                                 final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                                 final ErrorRequestListener error, boolean isShowPB) {
        if (!isInternetConnectionError()) {
            if (isShowPB) {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            } else {
                progressDialog = null;
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.keepAliveRequest(header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getAccounts(final Context context, Map<String, String> header, final SuccessRequestListenerAllResponse<BaseResponse<AccountsResponse>> success,
                            final ErrorRequestListener error, boolean isShowPB, boolean needUpdate) {
        if (!isInternetConnectionError()) {
            if (isShowPB) {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            } else {
                progressDialog = null;
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, false);
            Call<BaseResponse<AccountsResponse>> call = api.getAccounts(header, needUpdate);
            call.enqueue(new Callback<BaseResponse<AccountsResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<AccountsResponse>> call, Response<BaseResponse<AccountsResponse>> response) {
                    if (isConfirmPaymentsNotEnded)
                        progressDialog = null;
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<AccountsResponse>> call, Throwable t) {
                    if (isConfirmPaymentsNotEnded)
                        progressDialog = null;
                    if (!call.isCanceled()) {
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    }
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getCategories(final Context context, Map<String, String> header, final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<Category>>> success,
                              final ErrorRequestListener error, boolean isShowPB) {
        if (!isInternetConnectionError()) {
            if (isShowPB) {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            } else {
                progressDialog = null;
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, false);
            Call<BaseResponse<ArrayList<Category>>> call = api.getCategories(header);
            call.enqueue(new Callback<BaseResponse<ArrayList<Category>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<Category>>> call, Response<BaseResponse<ArrayList<Category>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                    Log.i("categoryNR", "response = " + response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<Category>>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    }
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getAccountsOperations(final Context context, Map<String, String> header, final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<ATFStatement>>> success,
                                      final ErrorRequestListener error) {

        if (!isInternetConnectionError() && context != null) {
            IApiMethods api = request.request(context, null, API_BASE_URL, false, true);
            Call<BaseResponse<ArrayList<ATFStatement>>> call = api.getAccountsOperations(
                    Utilities.getDateMonthAgo("yyyy-MM-dd") + "T00:00:00.000",
                    //Utilities.getDateThirtyDayAgo("yyyy-MM-dd'T'HH:mm:ss"),
                    Utilities.getCurrentDate("yyyy-MM-dd") + "T23:59:59.000",
                    header);
            call.enqueue(new Callback<BaseResponse<ArrayList<ATFStatement>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<ATFStatement>>> call, Response<BaseResponse<ArrayList<ATFStatement>>> response) {
                    if (isConfirmPaymentsNotEnded)
                        progressDialog = null;
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<ATFStatement>>> call, Throwable t) {
                    if (isConfirmPaymentsNotEnded)
                        progressDialog = null;
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getAccountOperationsAndStats(final Context context, Map<String, String> header, int accountCode, String fromDate,
                                             String toDate, boolean isShowProgress, final SuccessRequestListenerAllResponse<BaseResponse<StatementsWithStats>> success,
                                             final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (isShowProgress) {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            } else {
                progressDialog = null;
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<StatementsWithStats>> call = api.getAccountOperationsAndStats(accountCode, fromDate, toDate, header);
            call.enqueue(new Callback<BaseResponse<StatementsWithStats>>() {
                @Override
                public void onResponse(Call<BaseResponse<StatementsWithStats>> call, Response<BaseResponse<StatementsWithStats>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<StatementsWithStats>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        httpResponseFailure(error, t);
                    }
                }
            });
            return call;
        }
        return null;
    }

    public Call getLimit(final Context context, Map<String, String> header, int accountCode,
                         boolean isShowProgress, final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<Limit>>> success,
                         final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (isShowProgress) {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            } else {
                progressDialog = null;
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, false);
            Call<BaseResponse<ArrayList<Limit>>> call = api.getLimit(header, accountCode);
            call.enqueue(new Callback<BaseResponse<ArrayList<Limit>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<Limit>>> call, Response<BaseResponse<ArrayList<Limit>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<Limit>>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        httpResponseFailure(error, t);
                    }
                }
            });
            return call;
        }
        return null;
    }

    public Call setLimit(final Context context, Map<String, String> header, int accountCode,
                         boolean isShowProgress, JSONObject body, final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                         final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (isShowProgress) {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            } else {
                progressDialog = null;
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<String>> call = api.setLimit(header, accountCode, requestBody);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    Log.i("onResponse - setLimit", "error = ");
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Log.i("Onfail - setLimit", "error = ");
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        httpResponseFailure(error, t);
                    }
                }
            });
            return call;
        }
        return null;
    }

    public Call setLockCard(final Context context, Map<String, String> header, int accountCode, JSONObject body,
                            final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                            final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<String>> call = api.setLockCard(accountCode, header, requestBody);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        httpResponseFailure(error, t);
                    }
                }
            });
            return call;
        }
        return null;
    }

    public Call sendSMS(final Context context, Map<String, String> header, int otpKey,
                        final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                        final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.sendSMS(otpKey, header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        httpResponseFailure(error, t);
                    }
                }
            });
            return call;
        }
        return null;
    }


    public Call getAllServicePoints(final Context context, Map<String, String> header, final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<Terminal>>> success,
                                    final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            IApiMethods api = request.request(context, null, API_BASE_URL, false, false);
            Call<BaseResponse<ArrayList<Terminal>>> call = api.getAllServicePoints(header);
            call.enqueue(new Callback<BaseResponse<ArrayList<Terminal>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<Terminal>>> call, Response<BaseResponse<ArrayList<Terminal>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<Terminal>>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call checkSmsInform(final Context context, Map<String, String> header, final SuccessRequestListenerAllResponse<BaseResponse<Bool>> success,
                               final ErrorRequestListener error, int code, boolean isShowProgress) {
        if (!isInternetConnectionError() && context != null) {
            if (isShowProgress) {
                if (progressDialog != null)
                    if (!progressDialog.isShowing())
                        progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            } else {
                progressDialog = null;
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Log.d("DOOOO", "verticalOffset = " + "doooo");
            Call<BaseResponse<Bool>> call = api.checkSmsInform(header, code);
            call.enqueue(new Callback<BaseResponse<Bool>>() {
                @Override
                public void onResponse(Call<BaseResponse<Bool>> call, Response<BaseResponse<Bool>> response) {
                    if (response.body() == null)
                        Log.d("POOOOSLESUCCES", "verticalOffset = " + "null");
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<Bool>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call setSmsInform(final Context context, Map<String, String> header, final SuccessRequestListener<BaseResponse<String>> success,
                             final ErrorRequestListener error, int code, JSONObject body, boolean isShowProgress) {
        if (!isInternetConnectionError() && context != null) {
            if (isShowProgress) {
                if (progressDialog != null)
                    if (!progressDialog.isShowing())
                        progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            } else {
                progressDialog = null;
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Log.i("setSmsInform", "setSmsInform = " + code);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<String>> call = api.setSmsInform(header, code, requestBody);
            Log.i("onResponse", "response = " + "000000");
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    Log.i("onResponse", "response = " + response.code());
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call checkInternet(final Context context, Map<String, String> header, final SuccessRequestListenerAllResponse<BaseResponse<Limit.Internet>> success,
                              final ErrorRequestListener error, int code, boolean isShowProgress) {
        if (!isInternetConnectionError() && context != null) {
            if (isShowProgress) {
                if (progressDialog != null)
                    if (!progressDialog.isShowing())
                        progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            } else {
                progressDialog = null;
            }
            IApiMethods iApiMethods = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<Limit.Internet>> call = iApiMethods.checkInternet(header, code);
            call.enqueue(new Callback<BaseResponse<Limit.Internet>>() {
                @Override
                public void onResponse(Call<BaseResponse<Limit.Internet>> call, Response<BaseResponse<Limit.Internet>> response) {
                    Log.i("success", "success = " + success.toString());
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<Limit.Internet>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call setInternet(final Context context, Map<String, String> header, final SuccessRequestListener<BaseResponse<String>> success,
                            final ErrorRequestListener error, int code, JSONObject body) {
        if (!isInternetConnectionError() && context != null) {
            IApiMethods iApiMethods = request.request(context, null, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<String>> call = iApiMethods.setInternet(header, code, requestBody);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getBanners(final Context context, final Map<String, String> header, final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<Banner>>> success,
                           final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            IApiMethods iApiMethods = request.request(context, null, API_BASE_URL, false, false);
            Call<BaseResponse<ArrayList<Banner>>> call = iApiMethods.getBanner(header);
            call.enqueue(new Callback<BaseResponse<ArrayList<Banner>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<Banner>>> call, Response<BaseResponse<ArrayList<Banner>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<Banner>>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getNews(final Context context, Map<String, String> header, final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<NewsItem>>> success,
                        final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
//            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods api = request.request(context, null, API_BASE_URL, false, false);
            Call<BaseResponse<ArrayList<NewsItem>>> call = api.getNews(header);
            call.enqueue(new Callback<BaseResponse<ArrayList<NewsItem>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<NewsItem>>> call, Response<BaseResponse<ArrayList<NewsItem>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<NewsItem>>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getNewsImage(final Context context, Map<String, String> header, String name, String category, final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                             final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            IApiMethods api = request.request(context, null, API_BASE_URL, false, false);
            Call<BaseResponse<String>> call = api.getNewsImage(header, name, category);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call checkVersion(final Context context, Map<String, String> header, final SuccessRequestListener<BaseResponse<String>> success,
                             final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            IApiMethods api = request.request(context, null, API_BASE_URL, false, false);
            Call<BaseResponse<String>> call = api.checkVersion(header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getRates(final Context context, Map<String, String> header, boolean visibleProgressBar,
                         final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<Rate>>> success,
                         final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (visibleProgressBar) {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            } else {
                progressDialog = null;
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, false);
            Call<BaseResponse<ArrayList<Rate>>> call = api.getRates(header);
            call.enqueue(new Callback<BaseResponse<ArrayList<Rate>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<Rate>>> call, Response<BaseResponse<ArrayList<Rate>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<Rate>>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getPaymentContext(final Context context, Map<String, String> header, final SuccessRequestListenerAllResponse<BaseResponse<PaymentContextResponse>> success,
                                  final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
//            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods api = request.request(context, null, API_BASE_URL, false, true);
            Call<BaseResponse<PaymentContextResponse>> call = api.getPaymentContext(header);
            call.enqueue(new Callback<BaseResponse<PaymentContextResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<PaymentContextResponse>> call, Response<BaseResponse<PaymentContextResponse>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<PaymentContextResponse>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getPaymentSubscriptions(final Context context, Map<String, String> header, final SuccessRequestListenerAllResponse<BaseResponse<PaymentTemplateResponse>> success,
                                        final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
//            if(progressDialog!=null){
//            if(progressDialog!=null){
//                if(!progressDialog.isShowing()) {
//                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
//                }
//            } else {
//                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
//            }
            IApiMethods api = request.request(context, null, API_BASE_URL, false, false);
            Call<BaseResponse<PaymentTemplateResponse>> call = api.getPaymentSubscriptions(header);
            call.enqueue(new Callback<BaseResponse<PaymentTemplateResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<PaymentTemplateResponse>> call, Response<BaseResponse<PaymentTemplateResponse>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<PaymentTemplateResponse>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getAccData(final Context context, Map<String, String> header, String cardNumber,
                           final SuccessRequestListenerAllResponse<BaseResponse<AccStatusResponse>> success,
                           final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<AccStatusResponse>> call = api.getAccData(header, cardNumber);
            call.enqueue(new Callback<BaseResponse<AccStatusResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<AccStatusResponse>> call, Response<BaseResponse<AccStatusResponse>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<AccStatusResponse>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call setDefaultCard(final Context context, Map<String, String> header, long accountId,
                               final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                               final ErrorRequestListener error
    ) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.setDefaultStatusForCard(header, accountId);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(
                        @NonNull Call<BaseResponse<String>> call,
                        @NonNull Response<BaseResponse<String>> response
                ) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(
                        @NonNull Call<BaseResponse<String>> call,
                        @NonNull Throwable t
                ) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getAccDataByPhoneNumber(final Context context, Map<String, String> header, String phoneNumber,
                                        final SuccessRequestListenerAllResponse<BaseResponse<AccountPhoneNumberResponse>> success,
                                        final ErrorRequestListener error
    ) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<AccountPhoneNumberResponse>> call = api.getAccountDataByPhoneNumber(header, phoneNumber);
            call.enqueue(new Callback<BaseResponse<AccountPhoneNumberResponse>>() {
                @Override
                public void onResponse(
                        @NonNull Call<BaseResponse<AccountPhoneNumberResponse>> call,
                        @NonNull Response<BaseResponse<AccountPhoneNumberResponse>> response
                ) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(
                        @NonNull Call<BaseResponse<AccountPhoneNumberResponse>> call,
                        @NonNull Throwable t
                ) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call registerMasterCard(final Context context, Map<String, String> header, JSONObject body, final SuccessRequestListenerAllResponse<BaseResponse<ResponseBody>> success,
                                   final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<ResponseBody>> call = api.registerMastercard(header, requestBody);
            call.enqueue(new Callback<BaseResponse<ResponseBody>>() {
                @Override
                public void onResponse(Call<BaseResponse<ResponseBody>> call, Response<BaseResponse<ResponseBody>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ResponseBody>> call, Throwable t) {
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call checkMt100Transfer(final Context context, JSONObject body, Map<String, String> header,
                                   final SuccessRequestListenerAllResponse<BaseResponse<CheckResponse>> success,
                                   final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<CheckResponse>> call = api.checkMt100Transfer(requestBody, header);
            call.enqueue(new Callback<BaseResponse<CheckResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<CheckResponse>> call, Response<BaseResponse<CheckResponse>> response) {
                    Log.i("checkMt100Transfer", "onResponse");
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<CheckResponse>> call, Throwable t) {
                    Log.i("checkMt100Transfer", "onFailure");
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call checkSwift(final Context context, JSONObject body, Map<String, String> header,
                           final SuccessRequestListenerAllResponse<BaseResponse<CheckResponse>> success,
                           final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<CheckResponse>> call = api.checkSwift(requestBody, header);
            call.enqueue(new Callback<BaseResponse<CheckResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<CheckResponse>> call, Response<BaseResponse<CheckResponse>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<CheckResponse>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call confirmSwift(final Context context, JSONObject body, Map<String, String> header,
                             final SuccessRequestListenerAllResponse<BaseResponse<TransferConfirmResponse>> success,
                             final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            isConfirmPaymentsNotEnded = true;
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<TransferConfirmResponse>> call = api.confirmSwift(requestBody, header);
            call.enqueue(new Callback<BaseResponse<TransferConfirmResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<TransferConfirmResponse>> call, Response<BaseResponse<TransferConfirmResponse>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                    isConfirmPaymentsNotEnded = false;
                }

                @Override
                public void onFailure(Call<BaseResponse<TransferConfirmResponse>> call, Throwable t) {
                    isConfirmPaymentsNotEnded = false;
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call confirmMt100Transfer(final Context context, JSONObject body, Map<String, String> header,
                                     final SuccessRequestListenerAllResponse<BaseResponse<TransferConfirmResponse>> success,
                                     final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            isConfirmPaymentsNotEnded = true;
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<TransferConfirmResponse>> call = api.confirmMt100Transfer(requestBody, header);
            call.enqueue(new Callback<BaseResponse<TransferConfirmResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<TransferConfirmResponse>> call, Response<BaseResponse<TransferConfirmResponse>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                    isConfirmPaymentsNotEnded = false;
                }

                @Override
                public void onFailure(Call<BaseResponse<TransferConfirmResponse>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    isConfirmPaymentsNotEnded = false;
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getAllDictionary(final Context context, Map<String, String> header,
                                 final SuccessRequestListenerAllResponse<BaseResponse<DictionaryResponse>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, false);
            Call<BaseResponse<DictionaryResponse>> call = api.getAllDictionary(header);
            call.enqueue(new Callback<BaseResponse<DictionaryResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<DictionaryResponse>> call, Response<BaseResponse<DictionaryResponse>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<DictionaryResponse>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call filterForeignBanks(final Context context, Map<String, String> header, String param,
                                   final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<ForeignBank>>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<ArrayList<ForeignBank>>> call = api.filterForeignBanks(header, param);
            call.enqueue(new Callback<BaseResponse<ArrayList<ForeignBank>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<ForeignBank>>> call, Response<BaseResponse<ArrayList<ForeignBank>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<ForeignBank>>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getPushSettings(final Context context, Map<String, String> header,
                                final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<PushResponse.PushSettings>>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<ArrayList<PushResponse.PushSettings>>> call = api.getPushSettings(header);
            call.enqueue(new Callback<BaseResponse<ArrayList<PushResponse.PushSettings>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<PushResponse.PushSettings>>> call, Response<BaseResponse<ArrayList<PushResponse.PushSettings>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<PushResponse.PushSettings>>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call setPushSettings(final Context context, Map<String, String> header, JSONArray body,
                                final SuccessRequestListener<ResponseBody> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<ResponseBody> call = api.setPushSettings(header, requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getMobileOperator(final Context context, Map<String, String> header, String mobileNumber,
                                  final SuccessRequestListener<MobileOperatorResponse> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<MobileOperatorResponse> call = api.getMobileOperator(header, mobileNumber);
            call.enqueue(new Callback<MobileOperatorResponse>() {
                @Override
                public void onResponse(Call<MobileOperatorResponse> call, Response<MobileOperatorResponse> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<MobileOperatorResponse> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call checkPayments(final Context context, Map<String, String> header, JSONObject body,
                              final SuccessRequestListenerAllResponse<BaseResponse<CheckPaymentsResponse>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<CheckPaymentsResponse>> call = api.checkPayments(requestBody, header);
            call.enqueue(new Callback<BaseResponse<CheckPaymentsResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<CheckPaymentsResponse>> call, Response<BaseResponse<CheckPaymentsResponse>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<CheckPaymentsResponse>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getTaxDict(final Context context, Map<String, String> header,
                           final SuccessRequestListenerAllResponse<BaseResponse<TaxDictResponse>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<TaxDictResponse>> call = api.getTaxDict(header);
            call.enqueue(new Callback<BaseResponse<TaxDictResponse>>() {
                @Override
                public void onResponse(Call<BaseResponse<TaxDictResponse>> call, Response<BaseResponse<TaxDictResponse>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<TaxDictResponse>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call confirmPayments(final Context context, Map<String, String> header, JSONObject body,
                                final SuccessRequestListenerAllResponse<BaseResponse<BankReference>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            isConfirmPaymentsNotEnded = true;
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<BankReference>> call = api.confirmPayments(requestBody, header);
            call.enqueue(new Callback<BaseResponse<BankReference>>() {
                @Override
                public void onResponse(Call<BaseResponse<BankReference>> call, Response<BaseResponse<BankReference>> response) {
                    isConfirmPaymentsNotEnded = false;
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<BankReference>> call, Throwable t) {
                    isConfirmPaymentsNotEnded = false;
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call createPaymentTemplate(final Context context, Map<String, String> header, JSONObject body,
                                      final SuccessRequestListenerAllResponse<BaseResponse<String>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<String>> call = api.createPaymentTemplate(requestBody, header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call createTransferTemplate(final Context context, Map<String, String> header, JSONObject body,
                                       final SuccessRequestListenerAllResponse<BaseResponse<String>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<String>> call = api.createTransfersTemplate(requestBody, header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call setVisibilityAccounts(final Context context, Map<String, String> header, JSONArray body,
                                      final SuccessRequestListenerAllResponse<BaseResponse<String>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));

            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<String>> call = api.setVisibilityAccounts(requestBody, header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call changePassword(final Context context, Map<String, String> header, JSONObject body,
                               final NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<String>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));

            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<String>> call = api.changePassword(requestBody, header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call setImageProfile(final Context context, Map<String, String> header, JSONObject body,
                                final SuccessRequestListener<BaseResponse<String>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<String>> call = api.setProfImage(requestBody, header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getProfImage(final Context context, Map<String, String> header,
                             final SuccessRequestListener<ResponseBody> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, false);
            Call<ResponseBody> call = api.getProfImage(header);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("onResponse", "Call<String> call = " + response.body().byteStream());
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getTransferTemplate(final Context context, Map<String, String> header,
                                    final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<TemplateTransfer>>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
//            if(progressDialog!=null){
//                if(!progressDialog.isShowing()) {
//                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
//                }
//            } else {
//                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
//            }
            IApiMethods api = request.request(context, null, API_BASE_URL, false, false);
            Call<BaseResponse<ArrayList<TemplateTransfer>>> call = api.getTransfersTemplate(header);
            call.enqueue(new Callback<BaseResponse<ArrayList<TemplateTransfer>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<TemplateTransfer>>> call, Response<BaseResponse<ArrayList<TemplateTransfer>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<TemplateTransfer>>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call deleteTransferTemplate(final Context context, Map<String, String> header, int templateId,
                                       final SuccessRequestListenerAllResponse<BaseResponse<String>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.deleteTransfersTemplate(header, templateId);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call changeTransferTemplate(final Context context, Map<String, String> header, JSONObject body, int templateId,
                                       final SuccessRequestListenerAllResponse<BaseResponse<String>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<String>> call = api.changeTransfersTemplate(header, requestBody, templateId);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call deletePaymentTemplate(final Context context, Map<String, String> header, int templateId,
                                      final SuccessRequestListenerAllResponse<BaseResponse<String>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.deletePaymentTemplate(header, templateId);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call changePaymentTemplate(final Context context, Map<String, String> header, JSONObject body, int templateId,
                                      boolean processAfterSaving, final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                                      final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseResponse<String>> call = api.changePaymentTemplate(header, requestBody, templateId, processAfterSaving);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call changeActivePaymentTemplate(final Context context, Map<String, String> header, int templateId,
                                            boolean IsActive, final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                                            final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.changeActivePaymentTemplate(header, templateId, IsActive);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call changeActiveTransfersTemplate(final Context context, Map<String, String> header, int templateId,
                                              boolean IsActive, final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                                              final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.changeActiveTransfersTemplate(header, templateId, IsActive);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call quickPayment(final Context context, Map<String, String> header, int templateId,
                             final SuccessRequestListenerAllResponse<BaseResponse<String>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.quickPayment(header, templateId);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getPaymentHistory(final Context context, Map<String, String> header, String fromDate,
                                  String toDate, final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<HistoryItem.PaymentHistoryItem>>> success,
                                  final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            IApiMethods api = request.request(context, null, API_BASE_URL, false, true);
            Call<BaseResponse<ArrayList<HistoryItem.PaymentHistoryItem>>> call = api.getPaymentHistory(fromDate, toDate, header);
            call.enqueue(new Callback<BaseResponse<ArrayList<HistoryItem.PaymentHistoryItem>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<HistoryItem.PaymentHistoryItem>>> call,
                                       Response<BaseResponse<ArrayList<HistoryItem.PaymentHistoryItem>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<HistoryItem.PaymentHistoryItem>>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call getTransferHistory(final Context context, Map<String, String> header, String fromDate,
                                   String toDate, final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<HistoryItem.TransferHistoryItem>>> success,
                                   final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            IApiMethods api = request.request(context, null, API_BASE_URL, false, true);
            Call<BaseResponse<ArrayList<HistoryItem.TransferHistoryItem>>> call = api.getTransferHistory(fromDate, toDate, header);
            call.enqueue(new Callback<BaseResponse<ArrayList<HistoryItem.TransferHistoryItem>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<HistoryItem.TransferHistoryItem>>> call,
                                       Response<BaseResponse<ArrayList<HistoryItem.TransferHistoryItem>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<HistoryItem.TransferHistoryItem>>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call getLoanSchedule(final Context context, Map<String, String> header, int code,
                                final SuccessRequestListenerAllResponse<BaseResponse<ArrayList<LoanScheduleResponse>>> success,
                                final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            IApiMethods api = request.request(context, null, API_BASE_URL, false, true);
            Call<BaseResponse<ArrayList<LoanScheduleResponse>>> call = api.getLoanSchedule(header, code);
            call.enqueue(new Callback<BaseResponse<ArrayList<LoanScheduleResponse>>>() {
                @Override
                public void onResponse(Call<BaseResponse<ArrayList<LoanScheduleResponse>>> call, Response<BaseResponse<ArrayList<LoanScheduleResponse>>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<ArrayList<LoanScheduleResponse>>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call getInvoice(final Context context, Map<String, String> header, long invoiceId,
                           final SuccessRequestListener<InvoiceContainerItem> success,
                           final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            IApiMethods api = request.request(context, null, API_BASE_URL, false, true);
            Call<InvoiceContainerItem> call = api.getInvoice(header, invoiceId);
            call.enqueue(new Callback<InvoiceContainerItem>() {
                @Override
                public void onResponse(Call<InvoiceContainerItem> call, Response<InvoiceContainerItem> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<InvoiceContainerItem> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call getTypesApplications(final Context context, Map<String, String> header,
                                     final SuccessRequestListener<ArrayList<ApplicationTypeDto>> success,
                                     final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, null, API_BASE_URL, false, true);
            Call<ArrayList<ApplicationTypeDto>> call = api.getTypesApplications(header);
            call.enqueue(new Callback<ArrayList<ApplicationTypeDto>>() {
                @Override
                public void onResponse(Call<ArrayList<ApplicationTypeDto>> call, Response<ArrayList<ApplicationTypeDto>> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ArrayList<ApplicationTypeDto>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call getApplicationById(final Context context, int id, Map<String, String> header,
                                   final SuccessRequestListener<ApplicationTypeDto> success,
                                   final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<ApplicationTypeDto> call = api.getApplicationById(header, id);
            call.enqueue(new Callback<ApplicationTypeDto>() {
                @Override
                public void onResponse(Call<ApplicationTypeDto> call, Response<ApplicationTypeDto> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ApplicationTypeDto> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call getApplicationDetailsById(final Context context, int id, Map<String, String> header,
                                          final SuccessRequestListener<ApplicationTypeDto> success,
                                          final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            IApiMethods api = request.request(context, null, API_BASE_URL, false, true);
            Call<ApplicationTypeDto> call = api.getApplicationDetailsById(header, id);
            call.enqueue(new Callback<ApplicationTypeDto>() {
                @Override
                public void onResponse(Call<ApplicationTypeDto> call, Response<ApplicationTypeDto> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ApplicationTypeDto> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call getHistoryDetailsById(final Context context, int id, Map<String, String> header,
                                      final SuccessRequestListener<HistoryDetailsApplications> success,
                                      final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<HistoryDetailsApplications> call = api.getHistoryDetailsById(header, id);
            call.enqueue(new Callback<HistoryDetailsApplications>() {
                @Override
                public void onResponse(Call<HistoryDetailsApplications> call, Response<HistoryDetailsApplications> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<HistoryDetailsApplications> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call getHistoryApplications(final Context context, Map<String, String> header,
                                       final SuccessRequestListener<ArrayList<HistoryApplications>> success,
                                       final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<ArrayList<HistoryApplications>> call = api.getHistoryApplications(header);
            call.enqueue(new Callback<ArrayList<HistoryApplications>>() {
                @Override
                public void onResponse(Call<ArrayList<HistoryApplications>> call, Response<ArrayList<HistoryApplications>> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ArrayList<HistoryApplications>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call getHistoryApplicationsByDate(final Context context, Map<String, String> header, String dateFrom, String dateTo,
                                             final SuccessRequestListener<ArrayList<HistoryApplications>> success,
                                             final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<ArrayList<HistoryApplications>> call = api.getHistoryApplicationsByDate(dateFrom, dateTo, header);
            call.enqueue(new Callback<ArrayList<HistoryApplications>>() {
                @Override
                public void onResponse(Call<ArrayList<HistoryApplications>> call, Response<ArrayList<HistoryApplications>> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ArrayList<HistoryApplications>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call createApplication(final Context context, Map<String, String> header, JSONObject applicationBody,
                                  final SuccessRequestListener<ResponseBody> success,
                                  final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), applicationBody.toString());
            Call<ResponseBody> call = api.createApplication(header, requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call cancelApplicationFirst(final Context context, Map<String, String> header, int id,
                                       final SuccessRequestListener<CancelApplicationModel> success,
                                       final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<CancelApplicationModel> call = api.cancelApplicationFirst(id, header);
            call.enqueue(new Callback<CancelApplicationModel>() {
                @Override
                public void onResponse(Call<CancelApplicationModel> call, Response<CancelApplicationModel> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<CancelApplicationModel> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call cancelApplicationSecond(final Context context, Map<String, String> header, JSONObject applicationBody,
                                        final SuccessRequestListener<ResponseBody> success,
                                        final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
                }
            } else {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), applicationBody.toString());
            Call<ResponseBody> call = api.cancelApplicationSecond(header, requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call invoicePayment(final Context context, Map<String, String> header, JSONObject body,
                               final SuccessRequestListener<ResponseBody> success,
                               final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<ResponseBody> call = api.invoicePayment(header, requestBody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call checkClient(final Context context, Map<String, String> header, JSONObject body,
                            final SuccessRequestListener<BaseRegistrationResponse> success,
                            final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods iApiMethods = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseRegistrationResponse> call = iApiMethods.checkClient(header, requestBody);
            call.enqueue(new Callback<BaseRegistrationResponse>() {
                @Override
                public void onResponse(Call<BaseRegistrationResponse> call, Response<BaseRegistrationResponse> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseRegistrationResponse> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call requestSmsForClientBank(final Context context, Map<String, String> header, JSONObject body,
                                        final SuccessRequestListener<BaseRegistrationResponse> success,
                                        final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods iApiMethods = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseRegistrationResponse> call = iApiMethods.requestSmsForClientOfBank(header, requestBody);
            call.enqueue(new Callback<BaseRegistrationResponse>() {
                @Override
                public void onResponse(Call<BaseRegistrationResponse> call, Response<BaseRegistrationResponse> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseRegistrationResponse> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call requestSmsForNoClientBank(final Context context, Map<String, String> header, JSONObject clientBody,
                                          final SuccessRequestListener<BaseRegistrationResponse> success,
                                          final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods iApiMethods = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (clientBody).toString());
            Call<BaseRegistrationResponse> call = iApiMethods.requestSmsForNoClientOfBank(header, requestBody);
            call.enqueue(new Callback<BaseRegistrationResponse>() {
                @Override
                public void onResponse(Call<BaseRegistrationResponse> call, Response<BaseRegistrationResponse> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseRegistrationResponse> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call requestSmsAgain(final Context context, Map<String, String> header, String phone,
                                final SuccessRequestListener<BaseRegistrationResponse> success,
                                final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods iApiMethods = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseRegistrationResponse> call = iApiMethods.requestSmsAgain(header, phone);
            call.enqueue(new Callback<BaseRegistrationResponse>() {
                @Override
                public void onResponse(Call<BaseRegistrationResponse> call, Response<BaseRegistrationResponse> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseRegistrationResponse> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getInterfaceViewData(final Context context, Map<String, String> map, //todo : request by Data
                                     final SuccessRequestListener<BaseResponse<List<Data>>> success,
                                     final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
//            progressDialog= Utilities.progressDialog(context,context.getResources().getString(R.string.t_loading));
            IApiMethods iApiMethods = request.request(context, null, API_BASE_URL, false, true); //без progress диалога
            Call<BaseResponse<List<Data>>> call = iApiMethods.getInterfaceViewData(map);
            call.enqueue(new Callback<BaseResponse<List<Data>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<Data>>> call, Response<BaseResponse<List<Data>>> response) {
                    httpResponseSuccess(context, success, response);

                }

                @Override
                public void onFailure(Call<BaseResponse<List<Data>>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call finishRegClientBank(final Context context, Map<String, String> header, JSONObject body,
                                    final SuccessRequestListener<BaseRegistrationResponse> success,
                                    final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods iApiMethods = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseRegistrationResponse> call = iApiMethods.finishRegForClientOfBank(header, requestBody);
            call.enqueue(new Callback<BaseRegistrationResponse>() {
                @Override
                public void onResponse(Call<BaseRegistrationResponse> call, Response<BaseRegistrationResponse> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseRegistrationResponse> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call finishRegNoClientBank(final Context context, Map<String, String> header, JSONObject body,
                                      final SuccessRequestListener<BaseRegistrationResponse> success,
                                      final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods iApiMethods = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseRegistrationResponse> call = iApiMethods.finishRegForNoClientOfBank(header, requestBody);
            call.enqueue(new Callback<BaseRegistrationResponse>() {
                @Override
                public void onResponse(Call<BaseRegistrationResponse> call, Response<BaseRegistrationResponse> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseRegistrationResponse> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call changeTempPassword(final Context context, Map<String, String> header, JSONObject body,
                                   final SuccessRequestListener<BaseRegistrationResponse> success,
                                   final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods iApiMethods = request.request(context, progressDialog, API_BASE_URL, false, true);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (body).toString());
            Call<BaseRegistrationResponse> call = iApiMethods.changeTempPassword(header, requestBody);
            call.enqueue(new Callback<BaseRegistrationResponse>() {
                @Override
                public void onResponse(Call<BaseRegistrationResponse> call, Response<BaseRegistrationResponse> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseRegistrationResponse> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call getSecretQuestions(final Context context, Map<String, String> header,
                                   final SuccessRequestListener<List<SecretQuestionResponse>> success,
                                   final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods iApiMethods = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<List<SecretQuestionResponse>> call = iApiMethods.getSecretQuestions(header);
            call.enqueue(new Callback<List<SecretQuestionResponse>>() {
                @Override
                public void onResponse(Call<List<SecretQuestionResponse>> call, Response<List<SecretQuestionResponse>> response) {
                    httpResponseSuccess(context, success, response);

                }

                @Override
                public void onFailure(Call<List<SecretQuestionResponse>> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }

    public Call checkPhoneNumber(String phoneNumber, final Context context, Map<String, String> header,
                                 final SuccessRequestListener<ResponseBody> success,
                                 final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods iApiMethods = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<ResponseBody> call = iApiMethods.checkPhoneNumber(phoneNumber, header);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
            return call;
        }
        return null;
    }


    public Call calculateSignature(final Context context, String signatureInput, boolean showProgress,
                                   Map<String, String> header, final SuccessRequestListener<ResponseBody> success,
                                   final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            if (showProgress) {
                progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            } else {
                progressDialog = null;
            }
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Log.d("TAG", "str1234 = " + signatureInput);
            Call<ResponseBody> call = api.calculateSignature(header, signatureInput.trim());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    httpResponseFailure(error, t);
                }
            });

            return call;
        }
        return null;
    }

    public Call getBanksRequisites(final Context context, Map<String, String> header, final String branchCode,
                                   final String currency,
                                   final String accountNumber, final SuccessRequestListener<BankRequisitesResponse> success,
                                   final ErrorRequestListener error) {
        if (!isInternetConnectionError()) {
            IApiMethods api = request.request(context, null, API_BASE_URL, false, false);
            Call<BankRequisitesResponse> call = api.getBanksRequisites(header, branchCode, currency, accountNumber);
            call.enqueue(new Callback<BankRequisitesResponse>() {
                @Override
                public void onResponse(Call<BankRequisitesResponse> call, Response<BankRequisitesResponse> response) {
                    httpResponseSuccess(context, success, response);
                }

                @Override
                public void onFailure(Call<BankRequisitesResponse> call, Throwable t) {
                    Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    httpResponseFailure(error, t);
                }
            });
        }
        return null;
    }
    public Call confirmationCodeWithKeyAmountAndCurrencyRequest(final Context context, Map<String, String> header, int otpKey, String amount, String operationCode,
                                                                final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                                                                final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.sendAmountAndCurrency(otpKey, amount, operationCode, header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        httpResponseFailure(error, t);
                    }
                }
            });
            return call;
        }
        return null;
    }

    public Call getMaxNumberOfAttemptsRequest(final Context context, Map<String, String> header,
                                              final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                                              final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.getAttemptCount(header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        httpResponseFailure(error, t);
                    }
                }
            });
            return call;
        }
        return null;
    }

    public Call removeOtpKeyRequest(final Context context, Map<String, String> header, int otpKey,
                                    final SuccessRequestListenerAllResponse<BaseResponse<String>> success,
                                    final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods api = request.request(context, null, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.removeOtpKey(otpKey, header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        httpResponseFailure(error, t);
                    }
                }
            });
            return call;
        }
        return null;
    }

    public void checkIsOtpKeyValid(final Context context, Map<String, String> header,
            int otpKey, String smsCode,
            final SuccessRequestListenerAllResponse<BaseResponse<String>> success, final ErrorRequestListener error) {
        if (!isInternetConnectionError() && context != null) {
            IApiMethods api = request.request(context, null, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.isOtpKeyValid(otpKey, smsCode, header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        httpResponseFailure(error, t);
                    }
                }
            });
        }
    }

    public void getAuthorizationType(final Context context, Map<String, String> header, String login,
                              final SuccessRequestListenerAllResponse<BaseResponse<String>> success, final ErrorRequestListener error){
        if(!isInternetConnectionError() && context != null){
            progressDialog = Utilities.progressDialog(context, context.getResources().getString(R.string.t_loading));
            IApiMethods api = request.request(context, progressDialog, API_BASE_URL, false, true);
            Call<BaseResponse<String>> call = api.getAuthorizationType(login, header);
            call.enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    httpResponseSuccessWithAllResponseInSuccesListener(context, success, response);
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    if(!call.isCanceled()){
                        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                        httpResponseFailure(error, t);
                    }
                }
            });
        }
    }
}