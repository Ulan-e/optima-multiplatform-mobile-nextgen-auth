package kz.optimabank.optima24.fragment.confirm;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.model.base.TemplateTransfer;
import kz.optimabank.optima24.model.interfaces.TransferTemplateOperation;
import kz.optimabank.optima24.model.service.TransferTemplateOperationImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

public class InterbankChangeTemplateOperationSmsConfirm extends ParentSmsConfirmFragment implements TransferTemplateOperationImpl.CallbackChangeTransfer {
    boolean isInterbankTemplate, standingInstruction;
    String templateName, currency, contragentAccountNumber, contragentName, operationPurpose, knp, contragentBic, standingInstuctionDate, startPayDay, standingInstructionTime, createDate;
    double amount;
    int productType, standingInstructionType, standingInstructionStatus, templateId, userId, destinationAccountId, transferType, sourceAccountId, contragentResidency, contragentSeco;
    private TransferTemplateOperation transferTemplateOperation;
    private String operationCode;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromBundle();
        initToolbar();
        initImpl();
    }

    private void initImpl() {
        transferTemplateOperation = new TransferTemplateOperationImpl();
        transferTemplateOperation.registerCallBackChange(this);
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
                    transferTemplateOperation.changeTransferTemplate(requireContext(), getJsonBody(), templateId);
                }
            }
        });
    }

    private JSONObject getJsonBody() {
        TemplateTransfer changeTransferTemplate = new TemplateTransfer();
        changeTransferTemplate.setAmount(amount);
        changeTransferTemplate.setStandingInstruction(standingInstruction);
        changeTransferTemplate.setStartPayDay(startPayDay);
        changeTransferTemplate.setStandingInstructionType(standingInstructionType);
        changeTransferTemplate.setStandingInstructionTime(standingInstructionTime);
        changeTransferTemplate.setProductType(productType);
        changeTransferTemplate.setConfirmCode(edSms.getText().toString());
        changeTransferTemplate.setName(templateName);
        changeTransferTemplate.createDate = createDate;
        changeTransferTemplate.setStandingInstructionDate(standingInstuctionDate);
        changeTransferTemplate.setCurrency(currency);
        changeTransferTemplate.setStandingInstructionStatus(standingInstructionStatus);
        changeTransferTemplate.setContragentAccountNumber(contragentAccountNumber);
        changeTransferTemplate.setContragentBic(contragentBic);
        changeTransferTemplate.setContragentName(contragentName);
        changeTransferTemplate.setOperationKnp(knp);
        changeTransferTemplate.setUserId(userId);
        changeTransferTemplate.setDestinationAccountId(destinationAccountId);
        changeTransferTemplate.setTransferType(transferType);
        changeTransferTemplate.setSourceAccountId(sourceAccountId);
        changeTransferTemplate.setContragentResidency(contragentResidency);
        changeTransferTemplate.setContragentSeco(contragentSeco);
        changeTransferTemplate.setOperationPurpose(operationPurpose);
        changeTransferTemplate.operationCode = operationCode;
        return getFieldNamesAndValues(changeTransferTemplate);
    }

    private void initToolbar() {
        ((SmsConfirmActivity) (requireActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> requireActivity().onBackPressed());
    }

    private void getDataFromBundle() {
        if (getArguments() != null) {
            isInterbankTemplate = getArguments().getBoolean("isInterbankTemplate");
            templateName = getArguments().getString("templateName");
            amount = getArguments().getDouble("amount");
            currency = getArguments().getString("currency");
            productType = getArguments().getInt("productType");
            contragentAccountNumber = getArguments().getString("contragentAccountNumber");
            contragentName = getArguments().getString("contragentName");
            operationPurpose = getArguments().getString("operationPurpose");
            knp = getArguments().getString("knp");
            contragentBic = getArguments().getString("contragentBic");
            standingInstruction = getArguments().getBoolean("standingInstruction"); //check
            startPayDay = getArguments().getString("startPayDay");
            standingInstructionTime = getArguments().getString("standingInstructionTime");
            createDate = getArguments().getString("createDate");
            standingInstructionType = getArguments().getInt("standingInstructionType");
            standingInstuctionDate = getArguments().getString("standingInstuctionDate");
            standingInstructionStatus = getArguments().getInt("standingInstructionStatus");
            templateId = getArguments().getInt("templateId");
            userId = getArguments().getInt("UserId");
            destinationAccountId = getArguments().getInt("DestinationAccountId");
            transferType = getArguments().getInt("TransferType");
            sourceAccountId = getArguments().getInt("sourceAccountId");
            contragentResidency = getArguments().getInt("ContragentResidency");
            contragentSeco = getArguments().getInt("ContragentSeco");
            operationCode = getArguments().getString("operationCode");
        }
    }

    @Override
    public void onChangeTransferTemplateResponse(int statusCode, String errorMessage) {
        confirmResponse(statusCode, errorMessage, Constants.CREATE_TEMPLATE_OTP_KEY);
    }
}
