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

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.gson.response.TransferConfirmResponse;
import kz.optimabank.optima24.model.interfaces.Transfers;
import kz.optimabank.optima24.model.interfaces.TransfersSwift;
import kz.optimabank.optima24.model.service.TransferImpl;
import kz.optimabank.optima24.model.service.TransferSwiftImpl;

import static kz.optimabank.optima24.utility.Constants.TRANSFER_OTP_KEY;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

public class InterBankAndSwiftOperationsSmsConfirm extends ParentSmsConfirmFragment implements
        TransferSwiftImpl.Callback, TransferImpl.CallbackConfirm {

    private boolean isInterBankInSom, isInterBankSwiftTransfer;
    private String feeCurrency;
    private TransfersSwift confirmSwiftTransfer;
    private Transfers transfer;

    private String interbankAmount, currency, contragentAccountNumber;
    private double swiftAmount;
    private double swiftFeeAmount;
    private int productType;
    //interBank
    private String feeCurrencyInterBank, contragentName, operationKnp, purpose, currencyInterBank, contragentBic, feeAmountInterBank, accountId;
    private int contragentCardBrandType, transferType, accountCode, type;
    //interBankSwift
    private String payerName, intermediaryName, contragentBicName, contragentCountry,
            contragentAddress, intermediaryBic, contragentKpp,
            сontragentRegistrationCountry, contragentIdn, chargesType, payerAddress, mTotalAmount;
    private String contragentBankAccountNumber;
    private String operationCode;
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
        if (isInterBankInSom) {
            transfer = new TransferImpl();
            transfer.registerCallbackConfirm(this);
            tvFee.setText(getDoubleType(feeAmountInterBank) + " " + currencyInterBank);
            tvSumWithFee.setText(interbankAmount + " " + currencyInterBank);
        } else if (isInterBankSwiftTransfer) {
            confirmSwiftTransfer = new TransferSwiftImpl();
            confirmSwiftTransfer.registerCallBack(this);
            tvFee.setText(swiftFeeAmount + " " + feeCurrency);
            tvSumWithFee.setText(mTotalAmount + " " + feeCurrency);
            feeInfo.setText(getString(R.string.transfer_sum));
        } else {
            feeInfo.setText(getString(R.string.sum_with_fee));
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
                    Log.d("TAG", "getBody() = " + getJsonBody());
                    if (isInterBankSwiftTransfer) {
                        confirmSwiftTransfer.confirmSwift(requireActivity(), getJsonBody());
                    } else if (isInterBankInSom) {
                        transfer.confirmMt100Transfer(requireActivity(), getJsonBody());
                    }
                }
            }
        });
    }

    private JSONObject getJsonBody() {
        BodyModel.Mt100Transfer mt100TransferBody = new BodyModel.Mt100Transfer();
        BodyModel.SwiftTransfer swiftTransfer = new BodyModel.SwiftTransfer();
        if (isInterBankSwiftTransfer) {
            swiftTransfer.amount = swiftAmount;
            swiftTransfer.contragentBic = contragentBic;
            swiftTransfer.purpose = purpose;
            swiftTransfer.PayerName = payerName;
            swiftTransfer.accountCode = accountCode;
            swiftTransfer.operationKnp = operationKnp;
            swiftTransfer.productType = productType;
            swiftTransfer.ContragentAccountNumber = contragentAccountNumber;
            swiftTransfer.IntermediaryName = intermediaryName;
            swiftTransfer.ContragentBicName = contragentBicName;
            swiftTransfer.contragentName = contragentName;
            swiftTransfer.ContragentCountry = contragentCountry;
            swiftTransfer.ContragentAccountNumber = contragentAccountNumber;
            swiftTransfer.feeAmount = swiftFeeAmount;
            swiftTransfer.ContragentAddress = contragentAddress;
            swiftTransfer.IntermediaryBic = intermediaryBic;
            swiftTransfer.ContragentKpp = contragentKpp;
            swiftTransfer.feeCurrency = feeCurrency;
            swiftTransfer.ContragentRegistrationCountry = сontragentRegistrationCountry;
            swiftTransfer.contragentIdn = contragentIdn;
            swiftTransfer.ChargesType = chargesType;
            swiftTransfer.PayerAddress = payerAddress;
            swiftTransfer.securityCode = edSms.getText().toString();
            swiftTransfer.ContragentBankAccountNumber = contragentBankAccountNumber;
            swiftTransfer.operationCode = operationCode;
            swiftTransfer.requestId = requestId;
            return getFieldNamesAndValues(swiftTransfer);
        } else if (isInterBankInSom) {
            mt100TransferBody.amount = interbankAmount;
            mt100TransferBody.contragentAccountNumber = contragentAccountNumber;
            mt100TransferBody.productType = productType;
            mt100TransferBody.feeCurrency = currencyInterBank;
            mt100TransferBody.contragentName = contragentName;
            mt100TransferBody.operationKnp = operationKnp;
            mt100TransferBody.purpose = purpose;
            mt100TransferBody.currency = currencyInterBank;
            mt100TransferBody.contragentBic = contragentBic;
            mt100TransferBody.transferType = transferType;
            mt100TransferBody.feeAmount = feeAmountInterBank;
            mt100TransferBody.accountCode = accountCode;
            mt100TransferBody.type = type;
            mt100TransferBody.accountNumber = accountId;
            mt100TransferBody.securityCode = edSms.getText().toString();
            mt100TransferBody.ContragentCardBrandType = contragentCardBrandType;
            mt100TransferBody.operationCode = operationCode;
            mt100TransferBody.requestId = requestId;
            return getFieldNamesAndValues(mt100TransferBody);
        }
        return getFieldNamesAndValues(mt100TransferBody);

    }

    private void initToolbar() {
        toolbar.setTitle("");
        ((SmsConfirmActivity) (requireActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
    }

    private void getDataFromBundle() {
        if (getArguments() != null) {
            isInterBankInSom = getArguments().getBoolean("isTransferInterBank", false);
            isInterBankSwiftTransfer = getArguments().getBoolean("isTransferInterBankSwift", false);
            if (isInterBankInSom) {
                interbankAmount = getArguments().getString("amount");
                contragentAccountNumber = getArguments().getString("ContragentAccountNumber");
                productType = getArguments().getInt("ProductType");
                feeCurrencyInterBank = getArguments().getString("feeCurrencyInterBank");
                contragentCardBrandType = getArguments().getInt("ContragentCardBrandType");
                contragentName = getArguments().getString("ContragentName");
                operationKnp = getArguments().getString("OperationKnp");
                purpose = getArguments().getString("Purpose");
                currencyInterBank = getArguments().getString("Currency");
                contragentBic = getArguments().getString("ContragentBic");
                transferType = getArguments().getInt("TransferType");
                feeAmountInterBank = getArguments().getString("FeeAmount");
                accountCode = getArguments().getInt("AccountCode");
                type = getArguments().getInt("Type");
                accountId = getArguments().getString("AccountId");
                operationCode = getArguments().getString("operationCode");
            } else if (isInterBankSwiftTransfer) {
                swiftAmount = getArguments().getDouble("amount");
                contragentBic = getArguments().getString("ContragentBic");
                purpose = getArguments().getString("Purpose");
                payerName = getArguments().getString("PayerName");
                accountCode = getArguments().getInt("AccountCode");
                operationKnp = getArguments().getString("OperationKnp");
                productType = getArguments().getInt("ProductType");
                intermediaryName = getArguments().getString("IntermediaryName");
                type = getArguments().getInt("Type");
                contragentBicName = getArguments().getString("ContragentBicName");
                contragentCountry = getArguments().getString("ContragentCountry");
                contragentAccountNumber = getArguments().getString("ContragentAccountNumber");
                swiftFeeAmount = getArguments().getDouble("FeeAmount");
                contragentAddress = getArguments().getString("ContragentAddress");
                intermediaryBic = getArguments().getString("IntermediaryBic");
                contragentKpp = getArguments().getString("ContragentKpp");
                accountId = getArguments().getString("AccountId");
                feeCurrency = getArguments().getString("FeeCurrency");
                currency = getArguments().getString("Currency");
                сontragentRegistrationCountry = getArguments().getString("ContragentRegistrationCountry");
                contragentIdn = getArguments().getString("ContragentIdn");
                chargesType = getArguments().getString("ChargesType");
                payerAddress = getArguments().getString("PayerAddress");
                mTotalAmount = getArguments().getString("totalAmount");
                contragentBankAccountNumber = getArguments().getString("ContragentBankAccountNumber");
                operationCode = getArguments().getString("operationCode");
            }
        }
    }

    @Override
    public void onConfirmTransferResponse(int statusCode, String errorMessage) {
        confirmResponse(statusCode, errorMessage, TRANSFER_OTP_KEY);
    }

    @Override
    public void onCheckSwiftResponse(int statusCode, CheckResponse response, String errorMessage) {
    }

    @Override
    public void onConfirmSwiftResponse(int statusCode, TransferConfirmResponse response, String errorMessage) {
        confirmResponse(statusCode, errorMessage, TRANSFER_OTP_KEY);
    }
}
