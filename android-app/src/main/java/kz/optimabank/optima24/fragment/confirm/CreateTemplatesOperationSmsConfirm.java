package kz.optimabank.optima24.fragment.confirm;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.interfaces.CreateTemplate;
import kz.optimabank.optima24.model.service.CreateTemplateImpl;

import static kz.optimabank.optima24.utility.Constants.CREATE_TEMPLATE_OTP_KEY;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

public class CreateTemplatesOperationSmsConfirm extends ParentSmsConfirmFragment implements CreateTemplateImpl.Callback {

    String startPayDay;
    String autoPayDate;
    int sourceAccountId;
    String autoPayTime;
    String name;
    String autoPayType;
    String serviceId;
    String documentId;
    boolean isAutoPay,isPaymentTemplate,isTransferTemplate;
    String parameters;
    CreateTemplate createTemplateInterface;
    String mSumWithAmountPayment;
    String mAmount;
    String mFeePayment;
    private String operationCode;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromBundle();
        initToolbar();
        initImpl();
    }

    private void getDataFromBundle() {
        if (getArguments() != null) {
            mAmount = getArguments().getString("amount");
            startPayDay = getArguments().getString("startPayDay");
            autoPayDate = getArguments().getString("autoPayDate");
            autoPayType = getArguments().getString("autoPayType");
            autoPayTime = getArguments().getString("autoPayTime");
            name = getArguments().getString("name");
            isAutoPay = getArguments().getBoolean("isAutoPay");
            sourceAccountId = getArguments().getInt("sourceAccountId");
            serviceId = getArguments().getString("serviceId");
            documentId = getArguments().getString("documentId");
            parameters = getArguments().getString("parameters");
            operationCode = getArguments().getString("operationCode");
        }
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(view -> Objects.requireNonNull(requireActivity()).onBackPressed());
    }

    private void initImpl() {
        createTemplateInterface = new CreateTemplateImpl();
        createTemplateInterface.registerCallBack(this);
        tvFee.setText(mFeePayment);
        tvSumWithFee.setText(mSumWithAmountPayment);
        edSms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    Log.e(TAG, "afterTextChanged: " + getJsonBody());
                    if (isPaymentTemplate) {
                        createTemplateInterface.createPaymentTemplate(getActivity(), getJsonBody());
                    } else if (isTransferTemplate) {
                        createTemplateInterface.createTransferTemplate(getActivity(), getJsonBody());
                    }
                }
            }
        });
    }
    private JSONObject getJsonBody() {
        BodyModel.CreateTemplate createTemplate = new BodyModel.CreateTemplate();
        if (serviceId.equals("-1000")) { // CreateTransfer
            isTransferTemplate = true;
            createTemplate.documentId = documentId;
            createTemplate.Name = name;
            createTemplate.IsAutoPay = isAutoPay;
            createTemplate.AutoPayType = autoPayType;
            createTemplate.AutoPayDate = autoPayDate;
            createTemplate.AutoPayTime = autoPayTime;
            createTemplate.StartPayDay = startPayDay;
            createTemplate.ConfirmCode = edSms.getText().toString();
            createTemplate.operationCode = operationCode;
        } else {// CreatePayment
            isPaymentTemplate = true;
            createTemplate.ServiceId = serviceId;
            createTemplate.Amount = getDoubleType(mAmount).toString();
            createTemplate.AutoPayDate = autoPayDate;
            createTemplate.IsAutoPay = isAutoPay;
            createTemplate.ConfirmCode = edSms.getText().toString();
            createTemplate.AutoPayTime = autoPayTime;
            createTemplate.Name = name;
            createTemplate.AutoPayType = autoPayType;
            createTemplate.SourceAccountId = String.valueOf(sourceAccountId);
            createTemplate.operationCode = operationCode;
            try {
                JSONArray array = new JSONArray(parameters);
                createTemplate.Parameters = array;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return getFieldNamesAndValues(createTemplate);
    }

    @Override
    public void onCreatePaymentTemplateResponse(int statusCode, String errorMessage) {
        confirmResponse(statusCode,errorMessage,CREATE_TEMPLATE_OTP_KEY);
    }

    @Override
    public void onCreateTransferTemplateResponse(int statusCode, String errorMessage) {
        confirmResponse(statusCode,errorMessage,CREATE_TEMPLATE_OTP_KEY);
    }
}
