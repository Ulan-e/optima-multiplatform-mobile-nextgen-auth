package kz.optimabank.optima24.fragment.payment.invoice;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import kg.optima.mobile.R;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.InvoiceContainerItem;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.CheckPaymentsResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_NAME;
import static kz.optimabank.optima24.utility.Utilities.enableDisableView;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

/**
  Created by Timur on 19.07.2017.
 */

public abstract class CommonInvoiceFragment extends ATFFragment implements View.OnClickListener {
    //fee
    @BindView(R.id.fee_linear_wrapper)
    LinearLayout linFee;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.tvSumWithFee) TextView tvSumWithFee;

    @BindView(R.id.linMain) LinearLayout linMain;

    InvoiceContainerItem invoiceItem;
    int position, paymentServiceId = -1000;
    BodyModel.PaymentInvoice paymentInvoiceBody;
    BodyModel.PaymentCheck paymentCheckBody;
    PaymentService paymentService;
    PaymentContextController paymentController;
    public double totalAmount;
    long invoiceId;
    String categoryName;
    boolean isTemplate;

    UserAccounts accountFrom;

    public void getBundle() {
        if(getArguments()!=null) {
            invoiceItem = (InvoiceContainerItem) getArguments().getSerializable("invoice");
            position = getArguments().getInt("itemPosition");
            paymentServiceId = getArguments().getInt("paymentServiceId");
            invoiceId = getArguments().getLong("InvoiceId");
            isTemplate = getArguments().getBoolean("isTemplate");
            categoryName = getArguments().getString(CATEGORY_NAME);
        }
    }

    public void actionBack() {
        GeneralManager.getInstance().setInvoiceCheckedStatus(false);
        linFee.setVisibility(View.GONE);
        enableDisableView(linMain, true);
    }

    public double calculateTotalSum() {
        double sum = 0;
        if(invoiceItem!=null) {
            double occupantsCount = invoiceItem.invoiceBody.OccupantsCount;

            Log.i("OccupantsCount","OccupantsCount = "+occupantsCount);

            for(InvoiceContainerItem.BodyItem item : invoiceItem.invoiceBody.getItems()) {
                double fixedCount = 0;
                Log.i("isPay","calculateTotalSum isPay = "+item.isPay());
                if (item.isPay())
                    if(item.isCounterService()){
                        fixedCount = calculateServiceSum(item,occupantsCount, true);
                        if(fixedCount > 0) {
                            sum += fixedCount;
                        }
                    }else{
                        fixedCount = calculateServiceSumNonCountServ(item);
                        sum += fixedCount;
                    }

                Log.i("fixedCount","fixedCount = "+fixedCount);
                Log.i("fixedCount","getServiceName = "+item.getServiceName());
            }
        }
        Log.i("sum","sum = "+sum);
        return sum;
    }

    public double calculateServiceSumNonCountServ(InvoiceContainerItem.BodyItem item) {
        Log.i("ServiceSumNonCountServ","calculateServiceSumNonCountServ");
        Log.i("ServiceSumNonCountServ","item.getServiceName() = "+item.getServiceName());
        double fixedCount = 0;
        //fixedCount = Double.valueOf( item.getCalc());
        if (!item.isNotUseDebtForFixInvoice()) {
            Log.i("ServiceSumNonCountServ","1111");
            //if (item.getDebtInfo() > 0) {
            //    fixedCount = (Double.valueOf(item.getCalc()) - Math.abs(item.getDebtInfo()));
            //} else {
            //    fixedCount = (Double.valueOf(item.getCalc()) + Math.abs(item.getDebtInfo()));
            //}
            if (Double.valueOf(item.getCalc())-item.getDebtInfo()<0){
                Log.i("ServiceSumNonCountServ","22222");
                fixedCount = 0;
            }else {
                Log.i("ServiceSumNonCountServ","33333");
                if (item.getDebtInfo() > 0 && Double.valueOf(item.getCalc()) == 0) {
                    Log.i("ServiceSumNonCountServ","444444");
                    fixedCount = 0;
                } else {
                    Log.i("ServiceSumNonCountServ","555555");
                    Log.i("ServiceSumNonCountServ","item.getCalc() = "+item.getCalc());
                    Log.i("ServiceSumNonCountServ","item.getDebtInfo() = "+item.getDebtInfo());
                    fixedCount = (Double.valueOf(item.getCalc()) - item.getDebtInfo());
                }
            }
        }else{
            Log.i("ServiceSumNonCountServ","6666666");
            Log.i("ServiceSumNonCountServ","item.getCalc() = "+item.getCalc());
            fixedCount = Double.valueOf(item.getCalc());
        }
        if(item.isUsePenalty()) {
            Log.i("ServiceSumNonCountServ","777777");
            Log.i("ServiceSumNonCountServ","item.getPenalty() = "+item.getPenalty());
            Log.i("PENALTYUSE","calculateServiceSumNonCountServ");
            fixedCount += item.getPenalty();
        }
        Log.i("ServiceSumNonCountServ","fixedCount = " + fixedCount);
        return fixedCount;
    }

    public double calculateServiceSum(InvoiceContainerItem.BodyItem item, double occupantsCount, boolean isTotal) {
        Log.i("calculateServiceSum","getServiceName = "+item.getServiceName());
        double fixedCount = 0;
        if(item.isCounterService() && item.isPay()) {
            Log.i("calculateServiceSum","calculateServiceSum isPay = "+item.isPay());
            if(item.getServiceSum() >= 0) {
                fixedCount = item.getServiceSum();//isCounterService
                Log.i("calculateServiceSum","11111!!!");
                Log.i("sesdsvsdf111111","fixedCount = "+fixedCount);
            } else {
                Log.i("calculateServiceSum","22222");
                double difCount = item.getLastCount() - item.getPrevCount();
                if (difCount < 0) {
                    difCount = 0;
                }

                Log.i("sesdsvsdf22222","fixedCount = "+fixedCount);
                if (difCount <= item.getMinTariffThreshold() * occupantsCount || item.getMinTariffThreshold() == 0) {
                    Log.i("calculateServiceSum","33333");
                    fixedCount += item.getMinTariffValue() * difCount;
                    Log.i("sesdsvsdf3333333","fixedCount = "+fixedCount);

                } else if (difCount <= item.getMiddleTariffThreshold() * occupantsCount || item.getMiddleTariffThreshold() == 0) {
                    Log.i("calculateServiceSum","44444");
                    fixedCount += item.getMinTariffValue() * item.getMinTariffThreshold() * occupantsCount;
                    fixedCount += item.getMiddleTariffValue() * (difCount - item.getMinTariffThreshold() * occupantsCount);

                    Log.i("sesdsvsdf444444","fixedCount = "+fixedCount);
                } else {
                    Log.i("calculateServiceSum","55555");
                    fixedCount += item.getMinTariffValue() * item.getMinTariffThreshold() * occupantsCount;
                    fixedCount += (item.getMiddleTariffThreshold() - item.getMinTariffThreshold()) * item.getMiddleTariffValue()
                            * occupantsCount;
                    fixedCount += (difCount - item.getMiddleTariffThreshold() * occupantsCount) * item.getMaxTariffValue();

                    Log.i("sesdsvsdf5555555","fixedCount = "+fixedCount);
                }
            }

            if(item.isUseDebt() && isTotal) {
                Log.i("calculateServiceSum","666666");
                fixedCount -= item.getDebtInfo();
                Log.i("sesdsvsdf6666666","fixedCount = "+fixedCount);
            }

        } else {
            Log.i("sesdsvsdf7777777","fixedCount = "+fixedCount);
            Log.i("calculateServiceSum","calculateServiceSum else isPay = "+item.isPay());
            if (item.isPay()) {
                if (item.getServiceSum() >= 0) {
                    Log.i("calculateServiceSum","77777");
                    fixedCount = item.getServiceSum();
                    Log.i("sesdsvsdf88888888","fixedCount = "+fixedCount);
                } else {
                    Log.i("calculateServiceSum","88888");
                    //fixedCount += item.getFixSum();
                    fixedCount += Double.valueOf(item.getCalc());
                    Log.i("sesdsvsdf99999999","fixedCount = "+fixedCount);
                    if (item.isNotUseDebtForFixInvoice()) {
                        Log.i("calculateServiceSum","99999");
                        fixedCount += item.getDebtInfo();
                        Log.i("sesdsvsdf10 10 10","fixedCount = "+fixedCount);
                    }
                }
            }
        }
        Log.i("sesdsvsdf","fixedCount = "+fixedCount);

        if (fixedCount==0&&!item.isEditable()){
            fixedCount = item.getFixSum();
        }
        Log.i("sesdsvsdf 11 11 11","fixedCount = "+fixedCount);

        Log.i("isPay","CSS isPay = "+item.isPay());
        if(item.isUsePenalty() && isTotal && item.isPay()) {
            Log.i("PENALTYUSE","calculateServiceSum");
            fixedCount += item.getPenalty();
        }
        Log.i("sesdsvsdf 12 12 12","fixedCount = "+fixedCount);
        Log.i("-----------","---------------------------------");
        Log.i("ServiceName","ServiceName = "+item.getServiceName());
        Log.i("getAvgCount","getAvgCount = "+item.getAvgCount());
        Log.i("getAvgPaySum","getAvgPaySum = "+item.getAvgPaySum());
        Log.i("getCalc","getCalc = "+item.getCalc());
        Log.i("getDebtInfo","getDebtInfo = "+item.getDebtInfo());
        Log.i("getExpireDateInfo","getExpireDateInfo = "+item.getExpireDateInfo());
        Log.i("getFixCount","getFixCount = "+item.getFixCount());
        Log.i("getFixSum","getFixSum = "+item.getFixSum());
        Log.i("getLastCount","getLastCount = "+item.getLastCount());
        Log.i("getLastCountDate","getLastCountDate = "+item.getLastCountDate());
        Log.i("getLosses","getLosses = "+item.getLosses());
        Log.i("getMaxTariffValue","getMaxTariffValue = "+item.getMaxTariffValue());
        Log.i("getMeasure","getMeasure = "+item.getMeasure());
        Log.i("getMiddleTariff","getMiddleTariffThreshold = "+item.getMiddleTariffThreshold());
        Log.i("getMiddleTariffValue","getMiddleTariffValue = "+item.getMiddleTariffValue());
        Log.i("getMinTariffThreshold","getMinTariffThreshold = "+item.getMinTariffThreshold());
        Log.i("getMinTariffValue","getMinTariffValue = "+item.getMinTariffValue());
        Log.i("getPaySum","getPaySum = "+item.getPaySum());
        Log.i("getPenalty","getPenalty = "+item.getPenalty());
        Log.i("getPrevCount","getPrevCount = "+item.getPrevCount());
        Log.i("getPrevCountDate","getPrevCountDate = "+item.getPrevCountDate());
        Log.i("getServiceId","getServiceId = "+item.getServiceId());
        Log.i("getServiceName","getServiceName = "+item.getServiceName());
        Log.i("getTransformation","getTransformation = "+item.getTransformation());
        Log.i("getServiceSum","getServiceSum = "+item.getServiceSum());
        Log.i("isUseDebt","isUseDebt = "+item.isUseDebt());
        Log.i("isCounterService","isCounterService = "+item.isCounterService());
        Log.i("-----------","---------------------------------");
        return fixedCount;
    }

    public void setAccountSpinnerFrom(UserAccounts userAccounts, TextView tvSpinnerFrom, LinearLayout linAccountInfo,
                                      TextView tvAccountName, TextView tvAccountBalance, TextView tvAccountNumber, ImageView imgType, TextView tvMulti) {
        if(userAccounts!=null) {
            tvSpinnerFrom.setVisibility(View.GONE);
            linAccountInfo.setVisibility(View.VISIBLE);
            tvAccountName.setText(userAccounts.name);
            tvAccountBalance.setText(userAccounts.getFormattedBalance(getActivity()));
            tvAccountNumber.setText(userAccounts.number);
            if (userAccounts instanceof UserAccounts.Cards) {
                UserAccounts.Cards card = (UserAccounts.Cards) userAccounts;

                byte[] min = card.getByteArrayMiniatureImg();
                if (min!=null)
                    Utilities.setCardToImageView(card, imgType, tvMulti, BitmapFactory.decodeByteArray(min, 0, min.length));//card.miniatureIm
                else
                    Utilities.setCardToImageView(card, imgType, tvMulti, BitmapFactory.decodeResource(getResources(),R.drawable.arrow_left));
                /*try {
                    Utilities.setCardToImageView(card, imgType,tvMulti, card.readObject(false));//card.miniatureIm
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    Utilities.setCardToImageView(card, imgType, tvMulti, card.miniatureIm);
                }*/
            }
        }
    }

    public JSONObject getCheckBody() {
        paymentController = PaymentContextController.getController();
        paymentCheckBody = new BodyModel.PaymentCheck();
        totalAmount = calculateTotalSum();
        if (Float.valueOf(String.valueOf(totalAmount).replaceAll("\\s", "").replaceAll(" ", "").replaceAll(",", "."))>0){
            paymentCheckBody.Amount = String.valueOf(totalAmount).replaceAll("\\s", "").replaceAll(" ", "").replaceAll(",", ".");
        }
        //paymentCheckBody.amount = String.valueOf(totalAmount).replaceAll("\\s", "").replaceAll(" ", "").replaceAll(",", ".");
        paymentCheckBody.isAutoPay = false;

        if (paymentServiceId != -1000) {
            paymentService = paymentController.getPaymentServiceById(paymentServiceId);
        }

        if(paymentService != null) {
            paymentCheckBody.PaymentServiceId = paymentService.id;
            paymentCheckBody.Parameters = getBodyParameters(paymentService);
        }
        return getFieldNamesAndValues(paymentCheckBody);
    }

    public JSONArray getBodyParameters(PaymentService paymentService) {
        JSONArray bodyList = new JSONArray();
        try {
            int id = 0;
            for (int i = 0; i < paymentService.parameters.size(); i++) {
                id = paymentService.parameters.get(i).id;
            }
            JSONObject bodyObjects = new JSONObject();
            bodyObjects.put("serviceParameterId",id);
            bodyObjects.put("value", invoiceItem.ownerInfo.getId());
            bodyList.put(bodyObjects);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bodyList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(paymentController!=null) {
            paymentController.close();
        }
    }

    public JSONObject getInvoiceBody(CheckPaymentsResponse checkResponse) {
        paymentInvoiceBody = new BodyModel.PaymentInvoice();
        if(invoiceItem!=null) {
            if(paymentService!=null) {
                paymentInvoiceBody.paymentServiceId = paymentService.id;
            } else {
                if (paymentServiceId != -1000) {
                    paymentService = paymentController.getPaymentServiceById(paymentServiceId);
                    paymentInvoiceBody.paymentServiceId = paymentService.id;
                }
            }
            paymentInvoiceBody.invoiceId = invoiceId;
            paymentInvoiceBody.accountId = String.valueOf(accountFrom.code);
            paymentInvoiceBody.fixComm = String.valueOf(checkResponse.fixComm);
            paymentInvoiceBody.minComm = String.valueOf(checkResponse.minComm);
            paymentInvoiceBody.prcntComm = String.valueOf(checkResponse.prcntComm);

            paymentInvoiceBody.parameters = getInvoiceBodyParameters();
            Log.d("paymentInvoiceBody","paymentInvoiceBody.paymentServiceId() = " + paymentInvoiceBody.paymentServiceId);
            Log.d("paymentInvoiceBody","paymentInvoiceBody.invoiceId() = " + paymentInvoiceBody.invoiceId);
            Log.d("paymentInvoiceBody","paymentInvoiceBody.accountId() = " + paymentInvoiceBody.accountId);
            Log.d("paymentInvoiceBody","paymentInvoiceBody.fixComm() = " + paymentInvoiceBody.fixComm);
            Log.d("paymentInvoiceBody","paymentInvoiceBody.minComm() = " + paymentInvoiceBody.minComm);
            Log.d("paymentInvoiceBody","paymentInvoiceBody.prcntComm() = " + paymentInvoiceBody.prcntComm);
            Log.d("paymentInvoiceBody","paymentInvoiceBody.parameters() = " + paymentInvoiceBody.parameters);

        }

        return getFieldNamesAndValues(paymentInvoiceBody);
    }

    public JSONArray getInvoiceBodyParameters() {
        JSONArray bodyList = new JSONArray();
        for(InvoiceContainerItem.BodyItem item : invoiceItem.invoiceBody.getItems()) {
            JSONObject jsonObject = new JSONObject();
            try {
                //double fixCount;
                //if (!item.isNotUseDebtForFixInvoice()) {
                //    if (Double.valueOf(item.getCalc())-item.getDebtInfo()<0){
                //        fixCount = 0;
                //    }else {
                //        if (item.getDebtInfo() > 0 && Double.valueOf(item.getCalc()) == 0) {
                //            fixCount = 0;
                //        } else {
                //            fixCount = (Double.valueOf(item.getCalc()) - item.getDebtInfo());
                //        }
                //    }
                //}else{
                //    fixCount = Double.valueOf(item.getCalc());
                //}

                if (item.isPay()) {
                    Log.i("invoiceCommon","item.getServiceName() = "+item.getServiceName());
                    Log.i("invoiceCommon","item.isCounterService() = "+item.isCounterService());
                    Log.i("invoiceCommon","calculateServiceSumNonCountServ(item) = "+calculateServiceSumNonCountServ(item));
                    Log.i("invoiceCommon","item.getFixSum() = "+item.getFixSum());
                    Log.i("invoiceCommon","PaySum true = "+calculateServiceSum(item, invoiceItem.invoiceBody.OccupantsCount, true));
                    Log.i("invoiceCommon","PaySum false = "+calculateServiceSum(item, invoiceItem.invoiceBody.OccupantsCount, false));
                    //!item.isCounterService() = fixed
                    if ((!item.isCounterService() ? calculateServiceSumNonCountServ(item) : calculateServiceSum(item, invoiceItem.invoiceBody.OccupantsCount, true))>0) {
                        jsonObject.put("FixSum", Double.valueOf(  String.format(Locale.ENGLISH,"%.2f", !item.isCounterService() ? calculateServiceSumNonCountServ(item) :
                                calculateServiceSum(item, invoiceItem.invoiceBody.OccupantsCount, true))  ));
                    } else {
                        jsonObject.put("FixSum",0);
                    }
                        jsonObject.put("LastCount", item.getLastCount());
                        jsonObject.put("FixCount", item.getFixCount());
                        jsonObject.put("IsCounterService", item.isCounterService());
                        jsonObject.put("PrevCountDate", item.getPrevCountDate());
                        jsonObject.put("LastCountDate", item.getLastCountDate());
                        jsonObject.put("ServiceId", item.getServiceId());
                        jsonObject.put("PrevCount", item.getPrevCount());
                        jsonObject.put("ServiceName", item.getServiceName());
                    bodyList.put(jsonObject);
                }
                //Log.i("ServiceName123","ServiceName123 = "+item.getServiceName());
                //Log.i("ServiceId","ServiceId = "+item.getServiceId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return bodyList;
    }
}
