package kz.optimabank.optima24.fragment.payment.invoice;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.leanback.widget.HorizontalGridView;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.InvoiceAblePaymentActivity;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.activity.PaymentActivity;
import kz.optimabank.optima24.controller.adapter.GridViewAdapter;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.CheckPaymentsResponse;
import kz.optimabank.optima24.model.gson.response.Invoices;
import kz.optimabank.optima24.model.gson.response.TaxDictResponse;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.interfaces.Payments;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.PaymentsImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_NAME;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

/**
  Created by Timur on 12.07.2017.
 */

public class FindInvoiceAccountFragment extends ATFFragment implements View.OnClickListener,
        PaymentsImpl.CallbackCheck {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;

    @BindView(R.id.templateGridView) HorizontalGridView templateGridView;
    @BindView(R.id.edAccountNumber) EditText edAccountNumber;
    @BindView(R.id.btnContinue) Button btnContinue;
    @BindView(R.id.linTemplate)
    LinearLayout linTemplate;

    int paymentServiceId = - 1000;
    Payments payments;
    String categoryName;
    BodyModel.PaymentCheck paymentCheckBody;
    PaymentService paymentService;
    PaymentContextController paymentController;
    GridViewAdapter templateGridAdapter;
    ArrayList<Object> template;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_invoice, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        initToolbar();
        btnContinue.setOnClickListener(this);
        setTemplateGridAdapter();
        if (paymentController==null){
            paymentController = PaymentContextController.getController();
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnContinue) {
            payments = new PaymentsImpl();
            payments.registerCheckCallBack(this);
            payments.checkPayments(getActivity(),false,getBody());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(paymentController!=null) {
            paymentController.close();
        }
    }

    public void setTemplateGridAdapter() {
        if (paymentController==null){
            paymentController = PaymentContextController.getController();
        }
        paymentService = paymentController.getPaymentServiceById(paymentServiceId);
        templateGridAdapter = new GridViewAdapter(getActivity(),
                getTemplate(paymentService!=null ? paymentService.alias : null), setOnClick());

        templateGridView.setAdapter(templateGridAdapter);
    }

    public ArrayList<Object> getTemplate(String alias) {
        template = new ArrayList<>();
        //Log.i("FIAFgrid","GeneralManager.getInstance().getTemplatesPayment() = "+GeneralManager.getInstance().getTemplatesPayment());
        //Log.i("FIAFgrid","alias = "+alias);
        for(TemplatesPayment tempPayment : GeneralManager.getInstance().getTemplatesPayment()) {
            //try {
            //Log.i("FIAFgrid", "tempPayment.serviceId ="+tempPayment.serviceId);
            if (paymentController==null){
                paymentController = PaymentContextController.getController();
            }
                PaymentService paymentService = paymentController.getPaymentServiceById(tempPayment.serviceId);
                //Log.i("FIAFgrid", "tempPayment = " + tempPayment);
                //Log.i("FIAFgrid", "getAllPaymentServiceById = " + paymentController.getAllPaymentServiceById());
                if (paymentService != null && paymentService.alias.equals(alias)) {
                    //Log.i("FIAFgrid", "tempPayment add");
                    template.add(tempPayment);
                }
            //}catch (Exception ignored){}
        }

        if(template.isEmpty()) {
            linTemplate.setVisibility(View.GONE);
        }
        return template;
    }

    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {}
                    @Override
                    public void onAnimationEnd(View view) {
                        if(template.get(position) instanceof TemplatesPayment) {// && isCheck
                            TemplatesPayment tempPayment = (TemplatesPayment) template.get(position);
                            //Log.i("FIAFtempPayment","tempPayment = "+tempPayment);
                            try {
                            edAccountNumber.setText(tempPayment.parameters.get(0).getValue());
                            }catch (Exception ignored){}
                        }
                    }
                    @Override
                    public void onAnimationCancel(View view) {}
                };
                clickAnimation(view,animatorListener);
            }
        };
    }

    @Override
    public void checkPaymentResponse(int statusCode, CheckPaymentsResponse response, String errorMessage, boolean isBalance) {
        try {
            if (statusCode == Constants.SUCCESS) {
                if (response != null && !response.invoices.isEmpty()) {
                    Invoices invoice = response.invoices.get(0);
                    if (invoice.getStatus() == 0) {
                        Log.d(TAG, "getInvoiceId = " + invoice.getInvoiceId());
                        Intent intent = new Intent(getActivity(), InvoiceAblePaymentActivity.class);
                        if (getArguments() != null) {
                            intent.putExtra(CATEGORY_NAME, getArguments().getString(CATEGORY_NAME));
                        }
                        intent.putExtra("InvoiceId", invoice.getInvoiceId());
                        intent.putExtra("paymentServiceId", paymentServiceId);
                        startActivity(intent);
                    } else {
                        onError(getString(R.string.not_invoices));
                    }
                } else {
                    onError(getString(R.string.not_invoices));
                }
            } else if(statusCode != Constants.CONNECTION_ERROR_STATUS) {
                onError(errorMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void taxDictResponse(int statusCode, TaxDictResponse response, String errorMessage) {}

    private void initToolbar() {
        toolbar.setTitle("");
        tvTitle.setText(categoryName);
        try {
            ((PaymentActivity) getActivity()).setSupportActionBar(toolbar);
        }catch (Exception e){
            ((NavigationActivity) getActivity()).setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getBundle() {
        if(getArguments() != null) {
            categoryName = getArguments().getString(CATEGORY_NAME);
            paymentServiceId = getArguments().getInt("paymentServiceId");
            Log.i("payServIdFIAF","paymentServiceId = "+paymentServiceId);
        }
    }

    public JSONObject getBody() {
        paymentCheckBody = new BodyModel.PaymentCheck();
        paymentCheckBody.Amount = "0";
        paymentCheckBody.isAutoPay = false;

        if (paymentServiceId != -1000) {
            paymentService = paymentController.getPaymentServiceById(paymentServiceId);
            Log.i("paymentService","paymentService = "+paymentService);
        }

        Log.d(TAG,"paymentService.id = " + paymentService.id);
        paymentCheckBody.PaymentServiceId = paymentService.id;
        paymentCheckBody.Parameters = getBodyParameters(paymentService);

        return getFieldNamesAndValues(paymentCheckBody);
    }

    public JSONArray getBodyParameters(PaymentService paymentService) {
        JSONArray bodyList = new JSONArray();
        try {
            JSONObject bodyObjects = new JSONObject();
            bodyObjects.put("serviceParameterId",paymentService.parameters.get(0).id);
            bodyObjects.put("value",edAccountNumber.getText().toString());
            bodyList.put(bodyObjects);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bodyList;
    }
}
