package kz.optimabank.optima24.fragment.transfer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.activity.TemplateActivity;
import kz.optimabank.optima24.db.controllers.DictionaryController;
import kz.optimabank.optima24.model.base.TemplateTransfer;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.TransferTemplateOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.TransferTemplateOperationImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

public class TransferInterbankCurrencyTemplate extends TransferInterbankCurrency implements TransferTemplateOperationImpl.CallbackChangeTransfer {
    TemplateTransfer templateTransfer;
    int actionTag;
    TransferTemplateOperation transferTemplateOperation;
    DictionaryController dictionaryController;
    String isTTF;
    String isTransferAtTempl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isTemplate = true;
        getBundle();
        transferTemplateOperation = new TransferTemplateOperationImpl();
        transferTemplateOperation.registerCallBackChange(this);
        dictionaryController = DictionaryController.getController();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTemplateParams();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (templateTransfer != null) {
            if (accountFrom != null) {
                setAccountSpinnerFrom(accountFrom);
            }
            if (actionTag == Constants.TAG_CHANGE) {
                setTemplateName();
                btnTransfer.setText(getString(R.string.save));
            }
            btnTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionTag == Constants.TAG_CHANGE) {
                        if (edTemplateName.isShown()) {
                            if (edTemplateName.getText().toString().isEmpty()) {
                                edTemplateName.setError(getString(R.string.error_empty));
                                return;
                            }
                        }
                        transferTemplateOperation.changeTransferTemplate(getActivity(), getTemplateBody(), templateTransfer.getId());
                        GeneralManager.getInstance().setNeedToUpdatePayTempl(true);
                    } else {
                        requestTransfer();
                    }

                }
            });
            setTemplateParams();
        }

    }

    @Override
    public void initToolbar() {
        tvTitle.setText(templateTransfer.getName());
        toolbar.setTitle("");
        ((TemplateActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dictionaryController != null) {
            dictionaryController.close();
        }
    }

    private void getBundle() {
        if (getArguments() != null) {
            isTTF = getArguments().getString("isTTF");
            isTransferAtTempl = getArguments().getString("isTransferAtTempl");
            templateTransfer = (TemplateTransfer) getArguments().getSerializable("template");
            Log.d("TAG", "templateTransfer = " + templateTransfer);
            if (templateTransfer != null) {
                actionTag = getArguments().getInt("actionTag");
                super.accountFrom = GeneralManager.getInstance().getAccountByCode(templateTransfer.getSourceAccountId());
            }
        }
    }

    @Override
    public void onChangeTransferTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            getBundle();
            Intent intent = new Intent(requireContext(), SmsConfirmActivity.class);
            intent.putExtra("isTransfer", true);
            intent.putExtra("isSuccess", true);
            intent.putExtra("isTTF", isTTF);
            intent.putExtra("isSwift", true);
            intent.putExtra("isTTA", 0);
            intent.putExtra("isTransferAtTempl", isTransferAtTempl);
            intent.putExtra("isChange", true);
            intent.putExtra("isTemplate", true);
            requireActivity().startActivity(intent);
            requireActivity().finish();
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void setTemplateName() {
        edTemplateName.setVisibility(View.VISIBLE);
        edTemplateName.setText(templateTransfer.getName());
    }

    private void setTemplateParams() {
        edAmount.setText(templateTransfer.getAmount());
        currency = templateTransfer.getCurrency();
        tvCurrency.setText(templateTransfer.getCurrency());
        etReceiverName.setText(templateTransfer.getContragentName());
        edSpinnerTo.setText(templateTransfer.getContragentAccountNumber());
        edPurpose.setText(templateTransfer.getOperationPurpose());
        tvContragentCountry.setText(templateTransfer.getContragentCountry());
        tvContragentRegisterCountry.setText(templateTransfer.getContragentRegistrationCountry());
        edContragentAddress.setText(templateTransfer.getContragentAddress());
        edTvReceiverIIN.setText(templateTransfer.getContragentIdn());
        if (templateTransfer.getContragentKpp() != null && templateTransfer.getContragentKpp() != "") {
            edKppHint.setVisibility(View.VISIBLE);
            edKpp.setText(templateTransfer.getContragentKpp());
        }
        tvReceiversBankName.setText(templateTransfer.getContragentBicName());
        tvSpinnerBIC.setText(templateTransfer.getContragentBic());
        edReceiversBankCorAccount.setText(templateTransfer.getContragentBankAccountNumber());
        tvMediatorBankName.setText(templateTransfer.getIntermediaryName());
        tvMediatorBIC.setText(templateTransfer.getIntermediaryBic());
        tvSpinnerKNP.setText(templateTransfer.getOperationKnp());
    }
    
    private JSONObject getTemplateBody() {
        templateTransfer.code = -1000;
        templateTransfer.Name = edTemplateName.getText().toString();
        templateTransfer.Amount = getDoubleType(edAmount.getText().toString());
        templateTransfer.Currency = currency;
        SwiftTransfer = new BodyModel.SwiftTransfer();
        if (accountFrom instanceof UserAccounts.Cards) {
            SwiftTransfer.accountCode = ((UserAccounts.Cards) accountFrom).cardAccountCode;
        } else {
            SwiftTransfer.accountCode = accountFrom.code;
            templateTransfer.ProductType = Constants.TransferForeignCurrencyToAnotherBank;
            templateTransfer.PayerName = edPayerName.getText().toString();
            templateTransfer.PayerAddress = edPayerAddress.getText().toString();
            templateTransfer.ContragentAccountNumber = edSpinnerTo.getText().toString();
            templateTransfer.ContragentName = etReceiverName.getText().toString();
            templateTransfer.ContragentCountry = tvContragentCountry.getText().toString();
            templateTransfer.ContragentRegistrationCountry = tvContragentRegisterCountry.getText().toString();
            templateTransfer.ContragentAddress = edContragentAddress.getText().toString();
            templateTransfer.ContragentIdn = edTvReceiverIIN.getText().toString();
            templateTransfer.ContragentKpp = edKpp.getVisibility() == View.VISIBLE ? edKpp.getText().toString() : "";
            templateTransfer.ContragentBicName = tvReceiversBankName.getText().toString();
            templateTransfer.ContragentBic = tvSpinnerBIC.getText().toString();
            templateTransfer.ContragentBankAccountNumber = edReceiversBankCorAccount.getText().toString();
            templateTransfer.IntermediaryName = tvMediatorBankName.getText().toString();
            templateTransfer.IntermediaryBic = tvMediatorBIC.getText().toString();
            templateTransfer.OperationKnp = tvSpinnerKNP.getText().toString();
            templateTransfer.OperationPurpose = edPurpose.getText().toString();
            templateTransfer.setSourceAccountId(accountFrom.code);
            templateTransfer.setTransferType(Constants.TransferForeignCurrencyToAnotherBank);
        }
        return getFieldNamesAndValues(templateTransfer);
    }
}
