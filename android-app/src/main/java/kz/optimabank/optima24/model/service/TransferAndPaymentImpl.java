package kz.optimabank.optima24.model.service;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import kg.optima.mobile.R;
import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.model.base.NetworkResponse;
import kz.optimabank.optima24.model.base.TemplateTransfer;
import kz.optimabank.optima24.model.gson.response.BaseResponse;
import kz.optimabank.optima24.model.gson.response.PaymentContextResponse;
import kz.optimabank.optima24.model.gson.response.PaymentTemplateResponse;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.getToast;

/**
  Created by Timur on 14.04.2017.
 */

public class TransferAndPaymentImpl extends GeneralService implements TransferAndPayment {
    private Callback callback;
    private UpdateCallback updateCallback;

    @Override
    public void getPaymentContext(final Context context) {
        NetworkResponse.getInstance().getPaymentContext(context, OptimaBank.getInstance().getOpenSessionHeader(GeneralManager.getInstance().getSessionId()),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<PaymentContextResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<PaymentContextResponse> response, String errorMessage, int code) {
                        if(code==0) {
                            PaymentContextController paymentContextController = PaymentContextController.getController();
                            try {
                                paymentContextController.updatePaymentCategory(response.data.paymentContext);
                                paymentContextController.updatePaymentService(response.data.services);
                                paymentContextController.updatePaymentCountry(response.data.countries);
                                paymentContextController.updatePaymentRegions(response.data.regions);
                            }catch (Exception e){

                                //if return null get it again
                                paymentContextController.close();
                                payCont(context);
                            }
                            paymentContextController.close();
                            GeneralManager.setLocaleChanged(false);
                        }
                        if(callback!=null) {
                            callback.jsonPaymentContextResponse(code, errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        if(callback!=null) {
                            callback.jsonPaymentContextResponse(CONNECTION_ERROR_STATUS, null);
                        }
                    }
                });
    }

    private void payCont(final Context context){
        NetworkResponse.getInstance().getPaymentContext(context, OptimaBank.getInstance().getOpenSessionHeader(GeneralManager.getInstance().getSessionId()),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<PaymentContextResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<PaymentContextResponse> response, String errorMessage, int code) {
                        if(code==0) {
                            Log.i("response","PaymentContextResponse = "+response.toString());
                            PaymentContextController paymentContextController = PaymentContextController.getController();
                            try {
                                paymentContextController.updatePaymentCategory(response.data.paymentContext);
                                paymentContextController.updatePaymentService(response.data.services);
                                paymentContextController.updatePaymentCountry(response.data.countries);
                                paymentContextController.updatePaymentRegions(response.data.regions);
                                Log.i("Response", "Regions = " + response.data.regions);
                            }catch (Exception ignored){}
                            paymentContextController.close();
                            GeneralManager.setLocaleChanged(false);
                        }
                        if(callback!=null) {
                            callback.jsonPaymentContextResponse(code, errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        if(callback!=null) {
                            callback.jsonPaymentContextResponse(CONNECTION_ERROR_STATUS, null);
                        }
                    }
                });
    }

    @Override
    public void getPaymentSubscriptions(final Context context) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getPaymentSubscriptions(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<PaymentTemplateResponse>>() {
                    @Override
                    public void onSuccess(BaseResponse<PaymentTemplateResponse> response, String errorMessage, int code) {
                        if(code == 0) {
                            GeneralManager.getInstance().setTemplatesPayment(response.data);
                            GeneralManager.getInstance().setInvoices(response.data.invoices);
                        }
                        if(callback!=null) {
                            callback.jsonPaymentSubscriptionsResponse(code,errorMessage);
                        }
                        if(updateCallback!=null){
                            updateCallback.jsonPaymentSubscriptionsResponse(code,errorMessage);
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        getToast(context,context.getString(R.string.internet_connection_error));
                        if(callback!=null) {
                            callback.jsonPaymentSubscriptionsResponse(CONNECTION_ERROR_STATUS, null);
                        }
                        if(updateCallback!=null){
                            updateCallback.jsonPaymentSubscriptionsResponse(CONNECTION_ERROR_STATUS,null);
                        }
                    }
                });
    }

    @Override
    public void getTransferTemplate(Context context) {
        String sessionId = GeneralManager.getInstance().getSessionId();
        NetworkResponse.getInstance().getTransferTemplate(context, OptimaBank.getInstance().getOpenSessionHeader(sessionId),
                new NetworkResponse.SuccessRequestListenerAllResponse<BaseResponse<ArrayList<TemplateTransfer>>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<TemplateTransfer>> response, String errorMessage, int code) {
                        if(response != null) {
                            if (code == 0) {
                                GeneralManager.getInstance().setTemplatesTransfer(response.data);
                            }
                            if (callback != null) {
                                callback.jsonTransferTemplateResponse(code, errorMessage);
                            }
                            if (updateCallback != null) {
                                updateCallback.jsonTransferSubscriptionsResponse(code, errorMessage);
                            }
                        }
                    }
                }, new NetworkResponse.ErrorRequestListener() {
                    @Override
                    public void onError() {
                        if(callback!=null) {
                            callback.jsonTransferTemplateResponse(CONNECTION_ERROR_STATUS, null);
                        }
                        if(updateCallback!=null){
                            updateCallback.jsonTransferSubscriptionsResponse(CONNECTION_ERROR_STATUS,null);
                        }
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

    public interface Callback {
        void jsonPaymentContextResponse(int statusCode, String errorMessage);
        void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage);
        void jsonTransferTemplateResponse(int statusCode, String errorMessage);
    }

    public interface UpdateCallback {
        void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage);
        void jsonTransferSubscriptionsResponse(int statusCode, String errorMessage);
    }
}
