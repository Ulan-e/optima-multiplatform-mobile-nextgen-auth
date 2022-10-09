package kz.optimabank.optima24.fragment.confirm;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.model.base.TemplateTransfer;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.interfaces.PaymentTemplateOperation;
import kz.optimabank.optima24.model.interfaces.TransferTemplateOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.PaymentTemplateOperationImpl;
import kz.optimabank.optima24.model.service.TransferTemplateOperationImpl;

import static kz.optimabank.optima24.utility.Constants.CREATE_TEMPLATE_OTP_KEY;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

public class ChangeTemplatesOperationSmsConfirm extends ParentSmsConfirmFragment implements TransferTemplateOperationImpl.CallbackChangeTransfer, PaymentTemplateOperationImpl.CallbackChangePayment {
    //changeTransferTemplate
    private boolean isChangeTransferTemplate, standingInstruction;
    private String amount, startPayDayChTrTemp, nameChangeTrTemp, createDate, standingInstructionDate, currency, contragentAccountNumber, standingInstuctionTime;
    private int productType, standingInstructionType, standingInstructionStatus, templateId;
    //changePaymentTemplate
    private boolean isPaymentTemplateChange, processAfterSaving, isAutoPay;
    private int serviceIdPayment;
    private int sourceAccountId;
    private String startPayDay;
    private String autoPayDate;
    private String autoPayType;
    private String autoPayTime;
    private String templateName;
    private String sourcePaymentAccountId, paymentAmount;
    private TransferTemplateOperation transferTemplateOperation;
    private PaymentTemplateOperation paymentTemplateOperation;

    private int contragentBic;
    private String contragentCardBrandType;
    private String contragentIdn;
    private String contragentName;
    private int userId;
    private int transferType;
    private String operationKnp;
    private int id;
    private int destinationAccountId;
    private int contragentSeco;
    private int contragentResidency;
    private String operationCode;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromBundle();
        initToolbar();
        initImpl();

        hideAmountFeeFields();
    }

    private void initImpl() {
        if (isChangeTransferTemplate) {
            transferTemplateOperation = new TransferTemplateOperationImpl();
            transferTemplateOperation.registerCallBackChange(this);
        } else if (isPaymentTemplateChange) {
            paymentTemplateOperation = new PaymentTemplateOperationImpl();
            paymentTemplateOperation.registerCallBackChange(this);
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
                    if (isChangeTransferTemplate) {
                        transferTemplateOperation.changeTransferTemplate(requireActivity(), getJsonBody(), templateId);
                    } else if (isPaymentTemplateChange) {
                        paymentTemplateOperation.changePaymentTemplate(requireActivity(), getJsonBody(), templateId, processAfterSaving);
                    }
                }
            }
        });
    }

    // скрыл поля сумма и расчет коммиссии при подтверждении автоплатежа
    private void hideAmountFeeFields(){
        if(isAutoPay){
            layoutAmount.setVisibility(View.GONE);
            layoutFeeWithSum.setVisibility(View.GONE);
        }
    }

    private JSONObject getJsonBody() {
        BodyModel.CreateTemplate createTemplate = new BodyModel.CreateTemplate();
        TemplateTransfer changeTransferTemplate = new TemplateTransfer();
        if (isChangeTransferTemplate) {// TODO: 15.02.2021 changeTransferTemplate
            changeTransferTemplate.setAmount(getDoubleType(amount));
            changeTransferTemplate.setStandingInstruction(standingInstruction);
            changeTransferTemplate.setStartPayDay(startPayDayChTrTemp);
            changeTransferTemplate.setStandingInstructionType(standingInstructionType);
            changeTransferTemplate.setStandingInstructionTime(standingInstuctionTime);
            changeTransferTemplate.setProductType(productType);
            changeTransferTemplate.setConfirmCode(edSms.getText().toString());
            changeTransferTemplate.setName(nameChangeTrTemp);
            changeTransferTemplate.createDate = createDate;
            changeTransferTemplate.setStandingInstructionDate(standingInstructionDate);
            changeTransferTemplate.setCurrency(currency);
            changeTransferTemplate.setStandingInstructionStatus(standingInstructionStatus);
            changeTransferTemplate.setContragentAccountNumber(contragentAccountNumber);
            changeTransferTemplate.setSourceAccountId(sourceAccountId);
            changeTransferTemplate.setContragentBic(String.valueOf(contragentBic));
            changeTransferTemplate.setContragentCardBrandType(contragentCardBrandType);
            changeTransferTemplate.setContragentIdn(contragentIdn);
            changeTransferTemplate.setContragentName(contragentName);
            changeTransferTemplate.setUserId(userId);
            changeTransferTemplate.setTransferType(transferType);
            changeTransferTemplate.setOperationKnp(operationKnp);
            changeTransferTemplate.setId(id);
            changeTransferTemplate.setDestinationAccountId(destinationAccountId);
            changeTransferTemplate.setContragentSeco(contragentSeco);
            changeTransferTemplate.setContragentResidency(contragentResidency);
            changeTransferTemplate.operationCode = operationCode;
            return getFieldNamesAndValues(changeTransferTemplate);
        }
        if (isPaymentTemplateChange) {// TODO: 15.02.2021 changePaymentTemplate
            createTemplate.Amount = getDoubleType(paymentAmount).toString();
            createTemplate.Name = templateName;
            createTemplate.IsAutoPay = isAutoPay;
            createTemplate.AutoPayType = autoPayType;
            createTemplate.AutoPayDate = autoPayDate;
            createTemplate.AutoPayTime = autoPayTime;
            createTemplate.StartPayDay = startPayDay;
            createTemplate.ConfirmCode = edSms.getText().toString();
            createTemplate.ServiceId = String.valueOf(serviceIdPayment);
            createTemplate.SourceAccountId = String.valueOf(sourcePaymentAccountId);
            createTemplate.Parameters = GeneralManager.getInstance().getPaymentParameters();
            createTemplate.operationCode = operationCode;
            return getFieldNamesAndValues(createTemplate);
        }
        return getFieldNamesAndValues(createTemplate);
    }


    private void initToolbar() {
        ((SmsConfirmActivity) (requireActivity())).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> requireActivity().onBackPressed());
    }

    private void getDataFromBundle() {
        if (getArguments() != null) {
            isChangeTransferTemplate = getArguments().getBoolean("isTransferTemplateChange", false);
            isPaymentTemplateChange = getArguments().getBoolean("isPaymentTemplateChange", false);
            if (isChangeTransferTemplate) {
                amount = getArguments().getString("amount");
                standingInstruction = getArguments().getBoolean("standingInstruction");
                startPayDayChTrTemp = getArguments().getString("startPayDay");
                standingInstructionType = getArguments().getInt("standingInstructionType");
                standingInstuctionTime = getArguments().getString("standingInstructionTime");
                productType = getArguments().getInt("productType");
                nameChangeTrTemp = getArguments().getString("name");
                createDate = getArguments().getString("createDate");
                standingInstructionDate = getArguments().getString("standingInstuctionDate");
                currency = getArguments().getString("currency");
                standingInstructionStatus = getArguments().getInt("standingInstructionStatus");
                contragentAccountNumber = getArguments().getString("contragentAccountNumber");
                sourceAccountId = getArguments().getInt("sourceAccountId");
                templateId = getArguments().getInt("templateId");
                contragentBic = getArguments().getInt("contragentBic");
                contragentCardBrandType = getArguments().getString("ContragentCardBrandType");
                contragentIdn = getArguments().getString("ContragentIdn");
                contragentName = getArguments().getString("ContragentName");
                contragentResidency = getArguments().getInt("ContragentResidency");
                contragentSeco = getArguments().getInt("ContragentSeco");
                destinationAccountId = getArguments().getInt("DestinationAccountId");
                id = getArguments().getInt("id");
                operationKnp = getArguments().getString("OperationKnp");
                transferType = getArguments().getInt("TransferType");
                userId = getArguments().getInt("UserId");
                operationCode = getArguments().getString("operationCode");
            } else if (isPaymentTemplateChange) {
                paymentAmount = getArguments().getString("amount");
                startPayDay = getArguments().getString("startPayDay");
                autoPayDate = getArguments().getString("autoPayDate");
                autoPayType = getArguments().getString("autoPayType");
                autoPayTime = getArguments().getString("autoPayTime");
                templateName = getArguments().getString("name");
                isAutoPay = getArguments().getBoolean("isAutoPay");
                sourcePaymentAccountId = getArguments().getString("sourceAccountId");
                serviceIdPayment = getArguments().getInt("serviceId");
                templateId = getArguments().getInt("templateId");
                processAfterSaving = getArguments().getBoolean("processAfterSaving");
                operationCode = getArguments().getString("operationCode");
            }
        }
    }

    @Override
    public void onChangeTransferTemplateResponse(int statusCode, String errorMessage) {
        confirmResponse(statusCode, errorMessage, CREATE_TEMPLATE_OTP_KEY);
    }

    @Override
    public void onChangePaymentTemplateResponse(int statusCode, String errorMessage) {
        confirmResponse(statusCode, errorMessage, CREATE_TEMPLATE_OTP_KEY);
    }
}
