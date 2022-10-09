package kz.optimabank.optima24.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.confirm.ChangeTemplatesOperationSmsConfirm;
import kz.optimabank.optima24.fragment.confirm.CreateTemplatesOperationSmsConfirm;
import kz.optimabank.optima24.fragment.confirm.InterBankAndSwiftOperationsSmsConfirm;
import kz.optimabank.optima24.fragment.confirm.InterbankChangeTemplateOperationSmsConfirm;
import kz.optimabank.optima24.fragment.confirm.SuccessOperation;
import kz.optimabank.optima24.fragment.confirm.TransferAndPaymentOperationSmsConfirm;
import kz.optimabank.optima24.model.ReceiptType;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.receipt.model.ReceiptModel;
import kz.optimabank.optima24.receipt.ui.ReceiptActivity;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.IS_HIDE_CREATE_TEMPLATE;
import static kz.optimabank.optima24.utility.Constants.IS_RECEIPT_TRANSFER;
import static kz.optimabank.optima24.utility.Constants.PAYMENT_TITLE;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_FEE;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_FROM;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_NUMBER;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_TO;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_TO_FULL_NAME;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_AMOUNT;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_AMOUNT_CURRENCY;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_FEE;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_MODEL;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_NUMBER;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_OWN_ACCOUNT_TO;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_PHONE_NUMBER;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_TYPE;
import static kz.optimabank.optima24.utility.Constants.SUM_WITH_AMOUNT;

/**
 * Created by Timur on 12.05.2017.
 */

public class SmsConfirmActivity extends OptimaActivity {

    private static final String NUMBER_SIGN = "№";
    private static final String DATE_FORMAT = "dd MMMM yyyy HH:mm";

    boolean isTransfer, isPayment, isSuccess, isTemplate, isTemplateExist, isSwift,
            isCreateTemplates, isChangeTransferTemplate, isPaymentTemplateChange,
            isInterBankInSom, isInterBankSwiftTransfer, isInterBankInSomTemplateChange, isChange;
    Intent intent = new Intent();
    String isTTF;
    String isTransferAtTempl;
    private int transferType;
    BodyModel.PaymentCheck mPaymentCheck;
    private Intent receiptIntent;

    private Button btnShowReceipt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_confirm);

        setupShowReceiptButton();
        receiptIntent = new Intent(SmsConfirmActivity.this, ReceiptActivity.class);
        getBooleans();
        checkWhichFragmentOpen();
    }

    private void checkWhichFragmentOpen() {
        if (isSuccess) {
            if (isChange || isTransfer || isPayment || isCreateTemplates || isChangeTransferTemplate || isPaymentTemplateChange || isInterBankInSom || isInterBankSwiftTransfer || isInterBankInSomTemplateChange) {
                openFragment(new SuccessOperation());
            }
        } else {
            if (isCreateTemplates) {
                openFragment(new CreateTemplatesOperationSmsConfirm());
            } else if (isPayment || isTransfer) {
                openFragment(new TransferAndPaymentOperationSmsConfirm());
            } else if (isChangeTransferTemplate || isPaymentTemplateChange) {
                openFragment(new ChangeTemplatesOperationSmsConfirm());
            } else if (isInterBankInSom || isInterBankSwiftTransfer) {
                openFragment(new InterBankAndSwiftOperationsSmsConfirm());
            } else if (isInterBankInSomTemplateChange) {
                openFragment(new InterbankChangeTemplateOperationSmsConfirm());
            }
        }
    }

    private void setupShowReceiptButton(){
        btnShowReceipt = findViewById(R.id.btn_show_receipt);
        btnShowReceipt.setOnClickListener(v -> {
            if (receiptIntent != null) {
                getBundle();
                startActivity(receiptIntent);
            }
        });
    }

    private void getBooleans() {
        intent = getIntent();
        isTransfer = intent.getBooleanExtra("isTransfer", false);
        isPayment = intent.getBooleanExtra("isPayment", false);
        isCreateTemplates = intent.getBooleanExtra("isCreateTemplate", false);
        isChangeTransferTemplate = intent.getBooleanExtra("isTransferTemplateChange", false);
        isPaymentTemplateChange = intent.getBooleanExtra("isPaymentTemplateChange", false);
        isInterBankInSom = intent.getBooleanExtra("isTransferInterBank", false);
        isInterBankSwiftTransfer = intent.getBooleanExtra("isTransferInterBankSwift", false);
        isSuccess = intent.getBooleanExtra("isSuccess", false);
        isSwift = intent.getBooleanExtra("isSwift", false);
        isTemplateExist = intent.getBooleanExtra("isTemplateExist", false);
        isTemplate = intent.getBooleanExtra("isTemplate", false);
        isTTF = intent.getStringExtra("isTTF");
        mPaymentCheck = new BodyModel.PaymentCheck();
        isTransferAtTempl = intent.getStringExtra("isTransferAtTempl");
        isInterBankInSomTemplateChange = intent.getBooleanExtra("isInterbankTemplate", false);
        transferType = intent.getIntExtra(IS_HIDE_CREATE_TEMPLATE, 0);
        isChange = intent.getBooleanExtra("isChange", false);
    }

    public void openFragment(Fragment fragment) {
        btnShowReceipt.setVisibility(View.GONE);
        fragment.setArguments(getBundle());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_content, fragment);
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GeneralManager.getInstance().setPaymentParameters(null);
        GeneralManager.getInstance().setTransferDocumentId(null);
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isTransfer", isTransfer);
        bundle.putBoolean("isTemplateExist", isTemplateExist);
        bundle.putBoolean("isSwift", isSwift);
        bundle.putBoolean("isSwift", isSwift);
        Log.i("isTemplateExist", "isTemplateExist = " + isTemplateExist);
        Log.i("isSwift", "isSwift = " + isSwift);
        bundle.putBoolean("isPayment", isPayment);
        bundle.putBoolean("isTemplate", isTemplate);
        bundle.putBoolean("isClickToTemplateGridView", intent.getBooleanExtra("isClickToTemplateGridView", false));
        bundle.putString("isTTF", isTTF);
        bundle.putInt("isTTA", intent.getIntExtra("isTTA", 9));
        bundle.putString("isTransferAtTempl", isTransferAtTempl);
        Log.i("isTTF 9", "" + intent.getStringExtra("isTTF"));
        Log.i("isTTA SCA", "" + intent.getIntExtra("isTTA", 9));
        Log.i("isTTF 7", "" + isTTF);
        Log.i("isTransferAtTempl", "" + isTransferAtTempl);
        bundle.putString("operationCurrency", intent.getStringExtra("operationCurrency"));
        if (isInterBankInSomTemplateChange) {
            bundle.putBoolean("isInterbankTemplate", getIntent().getBooleanExtra("isInterbankTemplate", false));
            bundle.putString("templateName", getIntent().getStringExtra("templateName"));
            bundle.putDouble("amount", getIntent().getDoubleExtra("amount", Constants.DEFAULT_VALUE_1000));
            bundle.putString("currency", getIntent().getStringExtra("currency"));
            bundle.putInt("productType", getIntent().getIntExtra("productType", Constants.DEFAULT_VALUE_1000));
            bundle.putString("contragentAccountNumber", getIntent().getStringExtra("contragentAccountNumber"));
            bundle.putString("contragentName", getIntent().getStringExtra("contragentName"));
            bundle.putString("operationPurpose", getIntent().getStringExtra("operationPurpose"));
            bundle.putString("knp", getIntent().getStringExtra("knp"));
            bundle.putString("contragentBic", getIntent().getStringExtra("contragentBic"));
            bundle.putBoolean("standingInstruction", getIntent().getBooleanExtra("standingInstruction", false));
            bundle.putString("startPayDay", getIntent().getStringExtra("startPayDay"));
            bundle.putString("standingInstructionTime", getIntent().getStringExtra("standingInstructionTime"));
            bundle.putString("createDate", getIntent().getStringExtra("createDate"));
            bundle.putInt("standingInstructionType", getIntent().getIntExtra("standingInstructionType", Constants.DEFAULT_VALUE_1000));
            bundle.putString("standingInstuctionDate", getIntent().getStringExtra("standingInstuctionDate"));
            bundle.putInt("standingInstructionStatus", getIntent().getIntExtra("standingInstructionStatus", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("templateId", getIntent().getIntExtra("templateId", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("UserId", getIntent().getIntExtra("UserId", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("DestinationAccountId", getIntent().getIntExtra("DestinationAccountId", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("TransferType", getIntent().getIntExtra("TransferType", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("sourceAccountId", getIntent().getIntExtra("sourceAccountId", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("ContragentResidency", getIntent().getIntExtra("ContragentResidency", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("ContragentSeco", getIntent().getIntExtra("ContragentSeco", Constants.DEFAULT_VALUE_1000));
            bundle.putString("operationCode", getIntent().getStringExtra("operationCode"));
        }
        if (isInterBankSwiftTransfer) {
            bundle.putBoolean("isTransferInterBankSwift", isInterBankSwiftTransfer);
            bundle.putDouble("amount", intent.getDoubleExtra("amount", Constants.DEFAULT_VALUE_1000));
            bundle.putString("ContragentBic", getIntent().getStringExtra("ContragentBic"));
            bundle.putString("Purpose", getIntent().getStringExtra("Purpose"));
            bundle.putString("PayerName", getIntent().getStringExtra("PayerName"));
            bundle.putInt("AccountCode", getIntent().getIntExtra("AccountCode", Constants.DEFAULT_VALUE_1000));
            bundle.putString("OperationKnp", getIntent().getStringExtra("OperationKnp"));
            bundle.putInt("ProductType", getIntent().getIntExtra("ProductType", Constants.DEFAULT_VALUE_1000));
            bundle.putString("ContragentBankAccountNumber", getIntent().getStringExtra("ContragentBankAccountNumber"));
            bundle.putString("IntermediaryName", getIntent().getStringExtra("IntermediaryName"));
            bundle.putInt("Type", getIntent().getIntExtra("Type", Constants.DEFAULT_VALUE_1000));
            bundle.putString("ContragentBicName", getIntent().getStringExtra("ContragentBicName"));
            bundle.putString("ContragentName", getIntent().getStringExtra("ContragentName"));
            bundle.putString("ContragentCountry", getIntent().getStringExtra("ContragentCountry"));
            bundle.putString("ContragentAccountNumber", getIntent().getStringExtra("ContragentAccountNumber"));
            bundle.putDouble("FeeAmount", getIntent().getDoubleExtra("FeeAmount", Constants.DEFAULT_VALUE_1000));
            bundle.putString("ContragentAddress", getIntent().getStringExtra("ContragentAddress"));
            bundle.putString("IntermediaryBic", getIntent().getStringExtra("IntermediaryBic"));
            bundle.putString("ContragentKpp", getIntent().getStringExtra("ContragentKpp"));
            bundle.putString("AccountId", getIntent().getStringExtra("AccountId"));
            bundle.putString("FeeCurrency", getIntent().getStringExtra("FeeCurrency"));
            bundle.putString("Currency", getIntent().getStringExtra("Currency"));
            bundle.putString("ContragentRegistrationCountry", getIntent().getStringExtra("ContragentRegistrationCountry"));
            bundle.putString("ContragentIdn", getIntent().getStringExtra("ContragentIdn"));
            bundle.putString("ChargesType", getIntent().getStringExtra("ChargesType"));
            bundle.putString("PayerAddress", getIntent().getStringExtra("PayerAddress"));
            bundle.putString("totalAmount", getIntent().getStringExtra("totalAmount"));
            bundle.putString("operationCode", intent.getStringExtra("operationCode"));
        }
        if (isInterBankInSom) {
            bundle.putBoolean("isTransferInterBank", true);
            bundle.putString("amount", getIntent().getStringExtra("amount"));
            bundle.putString("ContragentAccountNumber", getIntent().getStringExtra("ContragentAccountNumber"));
            bundle.putInt("ProductType", getIntent().getIntExtra("ProductType", Constants.DEFAULT_VALUE_1000));
            bundle.putString("FeeCurrency", getIntent().getStringExtra("FeeCurrency"));
            bundle.putInt("ContragentCardBrandType", getIntent().getIntExtra("ContragentCardBrandType", Constants.DEFAULT_VALUE_1000));
            bundle.putString("ContragentName", getIntent().getStringExtra("ContragentName"));
            bundle.putString("OperationKnp", getIntent().getStringExtra("OperationKnp"));
            bundle.putString("Purpose", getIntent().getStringExtra("Purpose"));
            bundle.putString("Currency", getIntent().getStringExtra("Currency"));
            bundle.putString("ContragentBic", getIntent().getStringExtra("ContragentBic"));
            bundle.putInt("TransferType", getIntent().getIntExtra("TransferType", Constants.DEFAULT_VALUE_1000));
            bundle.putString("FeeAmount", getIntent().getStringExtra("FeeAmount"));
            bundle.putInt("AccountCode", getIntent().getIntExtra("AccountCode", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("Type", getIntent().getIntExtra("Type", Constants.DEFAULT_VALUE_1000));
            bundle.putString("AccountId", getIntent().getStringExtra("AccountId"));
            bundle.putString("operationCode", intent.getStringExtra("operationCode"));
        }
        if (isPaymentTemplateChange) {
            bundle.putBoolean("isPaymentTemplateChange", getIntent().getBooleanExtra("isPaymentTemplateChange", false));
            bundle.putBoolean("isAutoPay", getIntent().getBooleanExtra("isAutoPay", false));
            bundle.putString("amount", getIntent().getStringExtra("amount"));
            bundle.putInt("serviceId", getIntent().getIntExtra("serviceId", Constants.DEFAULT_VALUE_1000));
            bundle.putString("startPayDay", getIntent().getStringExtra("startPayDay"));
            bundle.putString("autoPayType", getIntent().getStringExtra("autoPayType"));
            bundle.putString("autoPayTime", getIntent().getStringExtra("autoPayTime"));
            bundle.putString("name", getIntent().getStringExtra("name"));
            bundle.putString("sourceAccountId", getIntent().getStringExtra("sourceAccountId"));
            bundle.putString("autoPayDate", getIntent().getStringExtra("autoPayDate"));
            bundle.putInt("templateId", getIntent().getIntExtra("templateId", Constants.DEFAULT_VALUE_1000));
            bundle.putBoolean("processAfterSaving", getIntent().getBooleanExtra("processAfterSaving", false));
            bundle.putString("operationCode", getIntent().getStringExtra("operationCode"));
        }
        if (isChangeTransferTemplate) {
            bundle.putBoolean("isTransferTemplateChange", getIntent().getBooleanExtra("isTransferTemplateChange", false));
            bundle.putString("amount", getIntent().getStringExtra("amount"));
            bundle.putBoolean("standingInstruction", getIntent().getBooleanExtra("standingInstruction", false));
            bundle.putString("startPayDay", getIntent().getStringExtra("startPayDay"));
            bundle.putInt("standingInstructionType", getIntent().getIntExtra("standingInstructionType", Constants.DEFAULT_VALUE_1000));
            bundle.putString("standingInstructionTime", getIntent().getStringExtra("standingInstructionTime"));
            bundle.putInt("productType", getIntent().getIntExtra("productType", Constants.DEFAULT_VALUE_1000));
            bundle.putString("name", getIntent().getStringExtra("name"));
            bundle.putString("createDate", getIntent().getStringExtra("createDate"));
            bundle.putString("standingInstuctionDate", getIntent().getStringExtra("standingInstuctionDate"));
            bundle.putString("currency", getIntent().getStringExtra("currency"));
            bundle.putInt("standingInstructionStatus", getIntent().getIntExtra("standingInstructionStatus", Constants.DEFAULT_VALUE_1000));
            bundle.putString("contragentAccountNumber", getIntent().getStringExtra("contragentAccountNumber"));
            bundle.putInt("sourceAccountId", getIntent().getIntExtra("sourceAccountId", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("templateId", getIntent().getIntExtra("templateId", Constants.DEFAULT_VALUE_1000));
            bundle.putString("contragentBic", getIntent().getStringExtra("contragentBic"));
            bundle.putString("ContragentCardBrandType", getIntent().getStringExtra("ContragentCardBrandType"));
            bundle.putString("ContragentIdn", getIntent().getStringExtra("ContragentIdn"));
            bundle.putString("ContragentName", getIntent().getStringExtra("ContragentName"));
            bundle.putInt("ContragentResidency", getIntent().getIntExtra("ContragentResidency", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("ContragentSeco", getIntent().getIntExtra("ContragentSeco", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("DestinationAccountId", getIntent().getIntExtra("DestinationAccountId", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("id", getIntent().getIntExtra("id", Constants.DEFAULT_VALUE_1000));
            bundle.putString("OperationKnp", getIntent().getStringExtra("OperationKnp"));
            bundle.putInt("TransferType", getIntent().getIntExtra("TransferType", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("UserId", getIntent().getIntExtra("UserId", Constants.DEFAULT_VALUE_1000));
            bundle.putString("operationCode", getIntent().getStringExtra("operationCode"));
        }
        if (isCreateTemplates) {
            bundle.putBoolean("isCreateTemplate", intent.getBooleanExtra("isCreateTemplate", false));
            bundle.putBoolean("isAutoPay", intent.getBooleanExtra("isAutoPay", false));
            bundle.putString("amount", intent.getStringExtra("amount"));
            bundle.putString("serviceId", intent.getStringExtra("serviceId"));
            bundle.putString("startPayDay", intent.getStringExtra("startPayDay"));
            bundle.putString("autoPayType", intent.getStringExtra("autoPayType"));
            bundle.putString("autoPayTime", intent.getStringExtra("autoPayTime"));
            bundle.putString("name", intent.getStringExtra("name"));
            bundle.putString("documentId", intent.getStringExtra("documentId"));
            bundle.putString("autoPayDate", intent.getStringExtra("autoPayDate"));
            bundle.putInt("sourceAccountId", intent.getIntExtra("sourceAccountId", Constants.DEFAULT_VALUE_1000));
            bundle.putString("parameters", intent.getStringExtra("parameters"));
            bundle.putString("operationCode", getIntent().getStringExtra("operationCode"));
        }

        bundle.putBoolean("isChange", intent.getBooleanExtra("isChange", false));
        bundle.putDouble("feeWithAmount", intent.getDoubleExtra("feeWithAmount", Constants.DEFAULT_VALUE_1000));
        if (isTransfer) {
            bundle.putInt("isNeedSmsConfirmation", intent.getIntExtra("isNeedSmsConfirmation", Constants.DEFAULT_VALUE_1000));
            bundle.putString("fee", intent.getStringExtra("fee"));
            bundle.putString("feeCurrency", intent.getStringExtra("feeCurrency"));
            bundle.putString("feeAmount", intent.getStringExtra("feeAmount"));
            bundle.putDouble("amount", intent.getDoubleExtra("amount", Constants.DEFAULT_VALUE_1000));
            bundle.putSerializable("mt100TransferBody", intent.getSerializableExtra("mt100TransferBody"));
            bundle.putSerializable("SwiftTransfer", intent.getSerializableExtra("SwiftTransfer"));
            bundle.putInt("sourceAccountId", intent.getIntExtra("sourceAccountId", Constants.DEFAULT_VALUE_1000));
            bundle.putString("operationCode", intent.getStringExtra("operationCode"));

            bundle.putInt(IS_HIDE_CREATE_TEMPLATE, intent.getIntExtra(RECEIPT_PHONE_NUMBER, 0));

            // чек показывается только в обычных переводах, в межбанках чека нет
            boolean isTransferWithReceipt = intent.getBooleanExtra(IS_RECEIPT_TRANSFER, false);
            if (isTransferWithReceipt ) {
                makeTransferReceiptData();
            }
        }
        if (isPayment) {
            bundle.putBoolean("isPayment", intent.getBooleanExtra("isPayment", false));
            bundle.putBoolean("isCreateTemplate", intent.getBooleanExtra("isCreateTemplate", false));
            bundle.putString("amount", intent.getStringExtra("amount"));
            bundle.putString("paymentTitle", intent.getStringExtra("paymentTitle"));
            bundle.putInt("sourceAccountId", intent.getIntExtra("sourceAccountId", Constants.DEFAULT_VALUE_1000));
            bundle.putInt("serviceId", intent.getIntExtra("serviceId", Constants.DEFAULT_VALUE_1000));
            bundle.putString("fixComm", intent.getStringExtra("fixComm"));
            bundle.putString("minComm", intent.getStringExtra("minComm"));
            bundle.putString("prcntComm", intent.getStringExtra("prcntComm"));
            bundle.putString("ProvReference", intent.getStringExtra("ProvReference"));
            bundle.putString("feeSum", intent.getStringExtra("feeSum"));
            bundle.putString("sumWithAmount", intent.getStringExtra("sumWithAmount"));
            bundle.putBoolean("isFromPayment", true);
            bundle.putString("operationCode", intent.getStringExtra("operationCode"));
            bundle.putBoolean("isTemplate", intent.getBooleanExtra("isTemplate", false));

            btnShowReceipt.setVisibility(View.VISIBLE);
            makePaymentReceiptData();
        }
        return bundle;
    }

    private void makeTransferReceiptData() {
        if(GeneralManager.getInstance().getTransferDocumentId() != null) {
            btnShowReceipt.setVisibility(View.VISIBLE);
            ReceiptModel receiptModel = new ReceiptModel();
            receiptModel.setOperationTime(getOperationTime());
            receiptModel.setReceiptNumber(NUMBER_SIGN + GeneralManager.getInstance().getTransferDocumentId());
            receiptModel.setSendCardNumber(intent.getStringExtra(RECEIPT_ACCOUNT_FROM));
            receiptModel.setFee(intent.getStringExtra(RECEIPT_ACCOUNT_FEE));
            receiptModel.setAmount(
                    intent.getDoubleExtra(RECEIPT_AMOUNT, 0) + " " +
                            intent.getStringExtra(RECEIPT_AMOUNT_CURRENCY)
            );
            receiptModel.setType(intent.getIntExtra(RECEIPT_PHONE_NUMBER, 0));
            receiptModel.setOperationStatus(getResources().getString(R.string.text_transfer_done));

            String fullName = intent.getStringExtra(RECEIPT_ACCOUNT_TO_FULL_NAME);
            if (!fullName.equals("")) {
                receiptModel.setRecipientName(fullName);
            } else {
                if (GeneralManager.getInstance().getUser().fullName != null) {
                    String ownFullName = GeneralManager.getInstance().getUser().fullName;
                    if (ownFullName != null) receiptModel.setRecipientName(ownFullName);
                }
            }

            String cardNumber = intent.getStringExtra(RECEIPT_ACCOUNT_TO);
            String ownCardNumber = intent.getStringExtra(RECEIPT_OWN_ACCOUNT_TO);
            if (!cardNumber.equals("")) {
                receiptModel.setRecipientCardNumber(cardNumber);
            } else {
                receiptModel.setRecipientCardNumber(ownCardNumber);
            }
            receiptIntent.putExtra(RECEIPT_MODEL, receiptModel);
            receiptIntent.putExtra(RECEIPT_TYPE, ReceiptType.TRANSFER);
        }
    }

    private void makePaymentReceiptData() {
        btnShowReceipt.setVisibility(View.VISIBLE);
        ReceiptModel receiptModel = new ReceiptModel();
        receiptModel.setOperationTime(getOperationTime());
        receiptModel.setReceiptNumber(NUMBER_SIGN + intent.getStringExtra(RECEIPT_NUMBER));
        receiptModel.setSendCardNumber(intent.getStringExtra(RECEIPT_ACCOUNT_FROM));
        receiptModel.setServiceName(intent.getStringExtra(PAYMENT_TITLE));
        receiptModel.setFee(intent.getStringExtra(RECEIPT_FEE));
        receiptModel.setAmount(intent.getStringExtra(SUM_WITH_AMOUNT));
        receiptModel.setOperationStatus(getResources().getString(R.string.text_payment_in_process));
        receiptModel.setAccountNumber(intent.getStringExtra(RECEIPT_ACCOUNT_NUMBER));

        receiptIntent.putExtra(RECEIPT_MODEL, receiptModel);
        receiptIntent.putExtra(RECEIPT_TYPE, ReceiptType.PAYMENT);
    }

    private String getOperationTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}