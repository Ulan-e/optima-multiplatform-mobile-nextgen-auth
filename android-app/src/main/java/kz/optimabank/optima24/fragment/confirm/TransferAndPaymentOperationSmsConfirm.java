package kz.optimabank.optima24.fragment.confirm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.Objects;

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.BankReference;
import kz.optimabank.optima24.model.interfaces.Payments;
import kz.optimabank.optima24.model.interfaces.Transfers;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.PaymentsImpl;
import kz.optimabank.optima24.model.service.TransferImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Utilities.doubleFormatter;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

public class TransferAndPaymentOperationSmsConfirm extends ParentSmsConfirmFragment implements TransferImpl.CallbackConfirm, PaymentsImpl.CallbackConfirmPayment {
    Payments payments;
    Transfers transfer;
    boolean isTransfer, isPayment;
    String mAmount;
    int mAccountId;
    int mServiceId;
    String mFixComm;
    String mMinComm;
    String mPrcntComm;
    String mProvReference;
    String mFeePayment;
    String mSumWithAmountPayment;
    String fee, feeCurrency, feeAmount;
    String operationCode;
    double feeWithAmount;
    BodyModel.Mt100Transfer mt100TransferBody = new BodyModel.Mt100Transfer();
    private long requestId;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromBundle();
        initToolbar();
        initImpl();

        requestId = System.currentTimeMillis();
    }

    @SuppressLint("SetTextI18n")
    private void initImpl() {
        if (isTransfer) {
            transfer = new TransferImpl();
            transfer.registerCallbackConfirm(this);
            tvFee.setText(doubleFormatter(getDoubleType(fee)) + " " + feeCurrency);
            tvSumWithFee.setText(doubleFormatter(feeWithAmount) + " " + feeCurrency);
        } else if (isPayment) {
            payments = new PaymentsImpl();
            payments.registerPaymentConfirmCallBack(this);
            tvFee.setText(mFeePayment);
            tvSumWithFee.setText(mSumWithAmountPayment);
        }

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
                    if (isPayment) {
                        payments.confirmPayments(requireContext(), getJsonBody());
                    } else if (isTransfer) {
                        transfer.confirmMt100Transfer(requireContext(), getJsonBody());
                    }
                }
            }
        });
    }

    private JSONObject getJsonBody() {
        BodyModel.PaymentCheck paymentCheck = new BodyModel.PaymentCheck();
        if (isPayment) {
            paymentCheck.Amount = mAmount;
            paymentCheck.AccountId = String.valueOf(mAccountId);
            paymentCheck.PaymentServiceId = mServiceId;
            paymentCheck.FixComm = mFixComm;
            paymentCheck.MinComm = mMinComm;
            paymentCheck.PrcntComm = mPrcntComm;
            paymentCheck.ProvReference = mProvReference;
            paymentCheck.ConfirmCode = edSms.getText().toString();
            paymentCheck.operationCode = operationCode;
            paymentCheck.Parameters = GeneralManager.getInstance().getPaymentParameters();
            paymentCheck.requestId = requestId;
            return getFieldNamesAndValues(paymentCheck);
        } else if (isTransfer) {
            mt100TransferBody.feeAmount = fee;
            mt100TransferBody.feeCurrency = feeCurrency;
            mt100TransferBody.operationCode = operationCode;
            mt100TransferBody.securityCode = edSms.getText().toString();
            mt100TransferBody.requestId = requestId;
            return getFieldNamesAndValues(mt100TransferBody);
        }
        return getFieldNamesAndValues(mt100TransferBody);
    }

    private void getDataFromBundle() {
        if (getArguments() != null) {
            isPayment = getArguments().getBoolean("isPayment", false);
            isTransfer = getArguments().getBoolean("isTransfer", false);
            if (isTransfer) {
                fee = getArguments().getString("fee");
                feeWithAmount = getArguments().getDouble("feeWithAmount", 0);
                feeCurrency = getArguments().getString("feeCurrency");
                feeAmount = getArguments().getString("feeAmount");
                mt100TransferBody = (BodyModel.Mt100Transfer) getArguments().getSerializable("mt100TransferBody");
                operationCode = getArguments().getString("operationCode");
            } else if (isPayment) {
                mAmount = getArguments().getString("amount");
                mAccountId = getArguments().getInt("sourceAccountId");
                mServiceId = getArguments().getInt("serviceId");
                mFixComm = getArguments().getString("fixComm");
                mMinComm = getArguments().getString("minComm");
                mPrcntComm = getArguments().getString("prcntComm");
                mProvReference = getArguments().getString("ProvReference");
                mFeePayment = getArguments().getString("feeSum");
                mSumWithAmountPayment = getArguments().getString("sumWithAmount");
                operationCode = getArguments().getString("operationCode");
            }
        }
    }

    private void initToolbar() {
        if (isTransfer) {
            tvTitle.setText(getString(R.string.transfer_confirmation));
        }
        toolbar.setTitle("");
        ((SmsConfirmActivity) Objects.requireNonNull(requireActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(requireActivity()).onBackPressed();
            }
        });
    }

    @Override
    public void onConfirmTransferResponse(int statusCode, String errorMessage) {
        confirmResponse(statusCode, errorMessage, Constants.TRANSFER_OTP_KEY);
    }

    @Override
    public void onConfirmPaymentResponse(int statusCode, BankReference response, String errorMessage) {
        confirmResponse(statusCode, errorMessage, Constants.PAYMENT_OTP_KEY);
    }
}
