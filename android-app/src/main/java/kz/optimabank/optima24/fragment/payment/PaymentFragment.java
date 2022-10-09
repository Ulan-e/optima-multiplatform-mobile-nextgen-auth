package kz.optimabank.optima24.fragment.payment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.leanback.widget.HorizontalGridView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.activity.PaymentActivity;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.activity.contacts.SelectContactsActivity;
import kz.optimabank.optima24.activity.contacts.data.Contact;
import kz.optimabank.optima24.controller.adapter.GridViewAdapter;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.db.entry.PredefinedValues;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.ATFAccount;
import kz.optimabank.optima24.model.base.PaymentServiceParameter;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.BankReference;
import kz.optimabank.optima24.model.gson.response.CheckPaymentsResponse;
import kz.optimabank.optima24.model.gson.response.MobileOperatorResponse;
import kz.optimabank.optima24.model.gson.response.TaxDictResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.MobileOperator;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.interfaces.Payments;
import kz.optimabank.optima24.model.interfaces.SmsWithTextSend;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.MobileOperatorImpl;
import kz.optimabank.optima24.model.service.PaymentsImpl;
import kz.optimabank.optima24.model.service.SmsWithTextImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.MaskTextWatcher;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_NAME;
import static kz.optimabank.optima24.activity.PaymentActivity.EXTERNAL_ID;
import static kz.optimabank.optima24.utility.Constants.ACCOUNT_KEY;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.CURRENCY_KGS;
import static kz.optimabank.optima24.utility.Constants.PAYMENT_OTP_KEY;
import static kz.optimabank.optima24.utility.Constants.PAYMENT_TITLE;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_FROM;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_NUMBER;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_FEE;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_NUMBER;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;
import static kz.optimabank.optima24.utility.Utilities.doubleFormatter;
import static kz.optimabank.optima24.utility.Utilities.enableDisableView;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

public class PaymentFragment extends ATFFragment implements View.OnClickListener, TextWatcher,
        MobileOperatorImpl.Callback, PaymentsImpl.CallbackCheck, PaymentsImpl.CallbackConfirmPayment, SmsWithTextImpl.SmsSendWithTextCallback {

    @BindView(R.id.templateGridView)
    HorizontalGridView templateGridView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvAccountName)
    TextView tvAccountName;
    @BindView(R.id.tvAccountBalance)
    TextView tvAccountBalance;
    @BindView(R.id.tvAccountNumber)
    TextView tvAccountNumber;
    @BindView(R.id.mobile_num)
    EditText edMobileNum;
    @BindView(R.id.hint7)
    EditText hint7;
    @BindView(R.id.edAmount)
    EditText edAmount;
    @BindView(R.id.tvSpinnerFrom)
    TextView tvSpinnerFrom;
    @BindView(R.id.btnPlus_500)
    Button btnPlus_500;
    @BindView(R.id.btnPlus_1000)
    Button btnPlus_1000;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imgType)
    ImageView imgType;
    @BindView(R.id.ivOperator)
    ImageView ivOperator;
    @BindView(R.id.linSpinnerFrom)
    LinearLayout linSpinnerFrom;
    @BindView(R.id.linAccountInfo)
    LinearLayout linAccountInfo;
    @BindView(R.id.linPhoneNumber)
    LinearLayout linPhoneNumber;
    @BindView(R.id.linTemplate)
    LinearLayout linTemplate;
    @BindView(R.id.linMain)
    LinearLayout linMain;
    @BindView(R.id.btnPayment)
    Button btnPayment;
    @BindView(R.id.btnGetPenalty)
    Button btnGetPenalty;
    @BindView(R.id.separator)
    View separator;
    @BindView(R.id.linAddFields)
    LinearLayout linAddFields;
    @BindView(R.id.tv_multi)
    TextView tvMulti;
    @BindView(R.id.timeBegin_linear)
    LinearLayout linTimeBegin;
    @BindView(R.id.regular_pay_time_linear)
    LinearLayout linRePayTime;
    @BindView(R.id.repeat_pay_linear)
    LinearLayout linRepeat;
    @BindView(R.id.auto_layout_wrapper)
    LinearLayout autoLayoutWrapper;
    @BindView(R.id.switchRegular)
    Switch switchRegular;
    @BindView(R.id.tvTimeBegin)
    TextView tvTimeBegin;
    @BindView(R.id.tvRePayTime)
    TextView tvRePayTime;
    @BindView(R.id.tvRepeat)
    TextView tvRepeat;
    @BindView(R.id.buttons_auto_layout_wrapper)
    LinearLayout buttonsAutoLayoutWrapper;
    @BindView(R.id.text_view_contact_name)
    TextView textViewContactName;
    //fee
    @BindView(R.id.general_fee_layout)
    LinearLayout subGeneralFeeLayout;
    @BindView(R.id.inside_fee_layout)
    LinearLayout insideFeeLayout;
    @BindView(R.id.sum_with_fee_linear)
    LinearLayout sumWithFeeLinear;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.tvSumWithFee)
    TextView tvSumWithFee;

    //clientInfo
    @BindView(R.id.linFullName)
    LinearLayout linFullName;
    @BindView(R.id.tvFullName)
    TextView tvFullName;
    @BindView(R.id.linAddress)
    LinearLayout linAddress;
    @BindView(R.id.tvAddress)
    TextView tvAddress;

    //template
    @BindView(R.id.linTemplateName)
    LinearLayout linTemplateName;
    @BindView(R.id.edTemplateName)
    EditText edTemplateName;
    @BindView(R.id.separatorTemplateName)
    View separatorTemplateName;
    @BindView(R.id.tv_overpayment)
    TextView tvOverpayment;
    @BindView(R.id.switch_auto_layout_wrapper)
    LinearLayout linSwitch;

    ImageButton btnContacts;
    private TextView textViewNameOfContact;

    private static final String TAG = "PaymentFragment";
    private static final String SETTLEMENT_CODE = "SettlementCode";
    private static final String REGION_CODE = "RegionCode";
    private static final String AREA_CODE = "AreaCode";
    private static final int SELECT_CONTACT_REQUEST = 77;
    private static final String TYPE_MOBILE = "TYPE_MOBILE";

    BodyModel.PaymentCheck paymentCheckBody;
    GridViewAdapter templateGridAdapter;
    ArrayList<Object> template;
    MobileOperator mobileOperator;
    UserAccounts accountFrom;
    Payments payments;
    PaymentService paymentService;
    PaymentContextController paymentController;
    EditTextParam[] paramEditTexts;
    EditText edParameter;
    String categoryName, fixComm, prcntComm, minComm, listItem, provReference, extIdSelectedEditText, fee, isMobileOperator;
    int serviceExternalId = -1000, paymentServiceId = -1000;
    private long requestId;

    //isTemplate for PaymentTemplateFragment
    boolean isMobile = false, isCheck = true, isTemplate = false, isTemplateExist = false, isAdd = true, isPhoneAdd = true, isConfirmOnProcess = false,
            ifChange = true, mIsNeedSmsConfirmation;
    double sumWithAmount;
    PaymentCategory paymentCategory;
    private boolean isClickToTemplateGridView;
    int paramLength = 0;
    int lengthForCheck = 999;
    private EditText edittext;
    int[] ids;
    private String[] params;
    private PaymentInfo paymentInfo;
    private TaxDictResponse taxDictResponse;
    private int idSelectEditText;
    public SmsWithTextSend smsWithTextSend;
    private String operationCode = String.valueOf(System.currentTimeMillis());
    private int selectedRayonId;
    private String mobileNumberForReceipt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_payment, container, false);
        ButterKnife.bind(this, view);

        requestId = System.currentTimeMillis();

        if (GeneralManager.getInstance().getSessionId() != null) {
            getBundle();
            linSpinnerFrom.setOnClickListener(this);
            btnPayment.setOnClickListener(this);
            edAmount.addTextChangedListener(this);
            edAmount.setFilters(new InputFilter[]{new Utilities.DecimalDigitsInputFilter(2)});
            paymentController = PaymentContextController.getController();
            smsWithTextSend = new SmsWithTextImpl();
            smsWithTextSend.registerSmsWithTextCallBack(this);
            final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                    "([000])[00]-[00]-[00]",
                    true,
                    edMobileNum,
                    null,
                    new MaskedTextChangedListener.ValueListener() {
                        @Override
                        public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
                            isClickToTemplateGridView = false;
                            if (edMobileNum.getText().toString().replaceAll(Constants.REG_EX_FOR_PHONE, "").length() == 10) {
                                //mobileOperator.getMobileOperator(getActivity(), extractedValue);
                            } else {
                                ivOperator.setVisibility(View.GONE);
                            }
                            if (edMobileNum.getText().length() > 0) {
                                hint7.setText(getString(R.string._7));
                            }

                        }
                    }
            );
            edMobileNum.addTextChangedListener(listener);
            initToolbar();
        }
        return view;
    }

    private void checkPayment() {
        try {
            if (isAdded()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(R.string.template_error);
                builder.setMessage(R.string.template_errorMessage);
                builder.setPositiveButton(R.string.status_ok, (dialog, which) -> {
                    dialog.cancel();
                    requireActivity().onBackPressed();
                });
                builder.create();
                builder.show();
            }
        } catch (Exception e) {
            requireActivity().onBackPressed();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (GeneralManager.getInstance().getSessionId() != null) {
            payments = new PaymentsImpl();
            payments.registerCheckCallBack(this);
            payments.registerPaymentConfirmCallBack(this);

            if (!isMobile) {
                if (paymentServiceId != -1000) {
                    try {
                        paymentService = paymentController.getPaymentServiceById(paymentServiceId);
                        if (paymentService.ExternalId.equalsIgnoreCase("prov_taxes")) {
                            payments.getTaxDict(getActivity());
                        }
                        if (paymentService.isFixedAmount || paymentService.isAllowedGetBalance) {
                            btnGetPenalty.setVisibility(View.VISIBLE);
                        }
                        if (paymentService.isFixedAmount) {
                            edAmount.setFocusable(false);
                            edAmount.setClickable(false);
                            btnPlus_500.setOnClickListener(null);
                            btnPlus_1000.setOnClickListener(null);
                        } else {
                            btnPlus_500.setOnClickListener(this);
                            btnPlus_1000.setOnClickListener(this);
                        }
                        paymentCategory = paymentController.getPaymentCategoryByServiceId(paymentService.paymentCategoryId);
                    } catch (Exception e) {
                        checkPayment();
                    }

                    if (paymentService != null) {
                        initView(linAddFields);
                    }
                }
            } else {
                mobileOperator = new MobileOperatorImpl();
                mobileOperator.registerCallBack(this);
                linPhoneNumber.setVisibility(View.VISIBLE);
                separator.setVisibility(View.VISIBLE);
                hint7.setFocusable(false);
                hint7.setVisibility(View.VISIBLE);
            }

            if (getView() != null) {
                getView().setFocusableInTouchMode(true);
                getView().requestFocus();
                getView().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                            if (isCheck) {
                                getActivity().onBackPressed();
                            } else {
                                actionBack();
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
            if (!isTemplate) {
                setTemplateGridAdapter();
            } else {
                linTemplate.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_ACCOUNT_FROM_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    if (data.getSerializableExtra(ACCOUNT_KEY) != null) {
                        accountFrom = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                        setAccountSpinnerFrom(accountFrom);

                        if (edittext != null)
                            if (!edittext.getText().toString().isEmpty() && edittext.getText().length() > 3)
                                payments.checkPayments(getActivity(), true, getBody(true));

                        try {
                            if (paymentService.alias.contains("BatysEnergo")) {
                                if (paramEditTexts[0].edittext.getText().length() > 3)
                                    payments.checkPayments(getActivity(), true, getBody(true));
                            }
                        } catch (Exception ignored) {
                        }
                    } else if (data.getSerializableExtra(STRING_KEY) != null) {
                        listItem = (String) data.getSerializableExtra(STRING_KEY);
                        paramEditTexts[idSelectEditText].edittext.setText(listItem);
                        paramEditTexts[idSelectEditText].edittext.setError(null);
                        if (extIdSelectedEditText != null && extIdSelectedEditText.equalsIgnoreCase("TaxeCode")) {
                            String[] array = listItem.split("\\s+");
                            String externalId = array[0];
                            Log.i("onActivityResult", "externalId of TaxeCode = " + externalId);
                            if (externalId.equals("1520")) {
                                for (int i = 0; i < paramEditTexts.length; i++) {
                                    if (paramEditTexts[i].externalId.equalsIgnoreCase("VehicleNum")) {
                                        paramEditTexts[i].edittext.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                for (int i = 0; i < paramEditTexts.length; i++) {
                                    if (paramEditTexts[i].externalId.equalsIgnoreCase("VehicleNum")) {
                                        paramEditTexts[i].edittext.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                        if (extIdSelectedEditText != null && extIdSelectedEditText.equalsIgnoreCase(REGION_CODE)) {
                            String[] array = listItem.split("\\s+");
                            String externalId = array[0];
                            int code = Integer.parseInt(externalId);
                            Log.i("onActivityResult", "externalId of RegionCode = " + externalId);
                            if (isCity(code)) {
                                for (EditTextParam paramEditText : paramEditTexts) {
                                    if (paramEditText.externalId.equalsIgnoreCase(SETTLEMENT_CODE)) {
                                        setEditTextAvailability(paramEditText, false);
                                    }
                                }
                            } else {
                                for (EditTextParam paramEditText : paramEditTexts) {
                                    if (paramEditText.externalId.equalsIgnoreCase(SETTLEMENT_CODE)) {
                                        setEditTextAvailability(paramEditText, true);
                                    }
                                }
                            }
                        }

                        // очищаем поля район и айылный аймак после повторного выбора региона
                        if (extIdSelectedEditText != null && extIdSelectedEditText.equalsIgnoreCase(REGION_CODE)) {
                            for (EditTextParam paramEditText : paramEditTexts) {
                                if (paramEditText.externalId.equalsIgnoreCase(AREA_CODE)) {
                                    paramEditText.edittext.getText().clear();
                                }
                                if (paramEditText.externalId.equalsIgnoreCase(SETTLEMENT_CODE)) {
                                    paramEditText.edittext.getText().clear();
                                }
                            }
                        }

                        if (extIdSelectedEditText != null && extIdSelectedEditText.equalsIgnoreCase(AREA_CODE)) {
                            for (EditTextParam paramEditText : paramEditTexts) {

                                // очищаем поле айылный аймак после повторного выбора района
                                if (paramEditText.externalId.equalsIgnoreCase(SETTLEMENT_CODE)) {
                                    paramEditText.edittext.getText().clear();
                                }

                                // делаем не кликабельным поле айылный аймак если это город
                                String[] array = listItem.split("\\s+");
                                String externalId = array[0];
                                int code = Integer.parseInt(externalId);
                                if (isAreaCity(code)) {
                                    for (EditTextParam areaParamsEditText : paramEditTexts) {
                                        if (areaParamsEditText.externalId.equalsIgnoreCase(SETTLEMENT_CODE)) {
                                            setEditTextAvailability(areaParamsEditText, false);
                                        }
                                    }
                                } else {
                                    for (EditTextParam areaParamsEditText : paramEditTexts) {
                                        if (areaParamsEditText.externalId.equalsIgnoreCase(SETTLEMENT_CODE)) {
                                            setEditTextAvailability(areaParamsEditText, true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (requestCode == SELECT_CONTACT_REQUEST) {
            Log.d(TAG, "select contact");
            if (data != null) {
                Contact contact = data.getParcelableExtra("contact");
                setContactRecipientData(contact);
            }
        }
    }

    private void setContactRecipientData(Contact contact) {
        if (contact != null) {
            if (edParameter != null) {
                edParameter.setText(contact.getFormattedPhoneNumber());
            }
            if (contact.getContactName() != null) {
                textViewContactName.setVisibility(View.VISIBLE);
                textViewContactName.setText(contact.getContactName());
            }
        }
    }

    private boolean isCity(int code) {
        return (code == 1 || code == 9 || code == 13 || code == 19 || code == 20
                || code == 32 || code == 33 || code == 42 || code == 43 || code == 44
                || code == 45 || code == 50 || code == 54 || code == 55
                || code == 56 || code == 57);
    }

    private boolean isAreaCity(int code) {
        return (code == 1 || code == 2 || code == 3 || code == 4 || code == 19
                || code == 20 || code == 31 || code == 32 || code == 33 || code == 48
                || code == 49 || code == 50 || code == 52 || code == 57 || code == 58
                || code == 59 || code == 60);
    }

    private void setEditTextAvailability(EditTextParam paramEditText, boolean isAvailable) {
        if (isAvailable) {
            paramEditText.edittext.setEnabled(true);
            paramEditText.edittext.setError(null);
            paramEditText.edittext.setFocusable(true);
        } else {
            paramEditText.edittext.getText().clear();
            paramEditText.edittext.setEnabled(false);
            paramEditText.edittext.setError(null);
            paramEditText.edittext.setFocusable(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (paymentController != null) {
            paymentController.close();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlus_500:
                increaseAmount(100);
                break;
            case R.id.btnPlus_1000:
                increaseAmount(200);
                break;
            case R.id.linSpinnerFrom:
                Intent intent = new Intent(requireContext(), SelectAccountActivity.class);
                intent.putExtra("isCards", true);
                startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
                break;
            case R.id.btnPayment:
                if (isCheck && checkField()) {
                    payments.checkPayments(requireContext(), false, getBody(false));
                } else if (!isCheck && !isConfirmOnProcess) {
                    btnPayment.setEnabled(false);
                    if (mIsNeedSmsConfirmation) {
                        GeneralManager.getInstance().setPaymentParameters(getBodyParameters(paymentService));
                        smsWithTextSend.sendSmsWithText(requireContext(), PAYMENT_OTP_KEY, getDoubleType(edAmount.getText().toString()) + " " + CURRENCY_KGS, operationCode);
                    } else {
                        payments.registerPaymentConfirmCallBack(this);
                        payments.confirmPayments(requireContext(), getBody(false));
                    }
                    isConfirmOnProcess = true;
                }
                break;
        }
    }

    @Override
    public void jsonMobileOperatorResponse(int statusCode, MobileOperatorResponse response, String errorMessage) {
        if (isAttached()) {
            if (statusCode == Constants.SUCCESS_STATUS_CODE && response != null) {
                serviceExternalId = response.serviceId;
                ivOperator.setVisibility(View.VISIBLE);
                switch (response.operator) {
                    case "Beeline":
                        ivOperator.setImageDrawable(getResources().getDrawable(R.drawable.beeline));
                        break;
                    case "Tele2":
                        ivOperator.setImageDrawable(getResources().getDrawable(R.drawable.tele_2));
                        break;
                    case "Activ":
                        ivOperator.setImageDrawable(getResources().getDrawable(R.drawable.activ));
                        break;
                    case "Kcell":
                        ivOperator.setImageDrawable(getResources().getDrawable(R.drawable.kcell));
                        break;
                    case "Altel":
                        ivOperator.setImageDrawable(getResources().getDrawable(R.drawable.altel));
                        break;
                }
                Log.d(TAG, "response.operator = " + response.operator);
            } else {
                onError(errorMessage);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void checkPaymentResponse(int statusCode, CheckPaymentsResponse response, String errorMessage, boolean isBalance) {
        if (statusCode == Constants.SUCCESS) {
            isTemplateExist = response.isTemplateExist;
            mIsNeedSmsConfirmation = response.isNeedSmsConfirmation;
            if (response.balance >= 0 && btnGetPenalty.getVisibility() == View.VISIBLE) {
                edAmount.setText(String.valueOf(response.balance).replace(".", ","));
                edAmount.setError(null);
            }
            if (isBalance) {
                if (response.clientInfo != null && !response.clientInfo.isEmpty()) {
                    subGeneralFeeLayout.setVisibility(View.VISIBLE);
                    insideFeeLayout.setVisibility(View.GONE);
                    sumWithFeeLinear.setVisibility(View.GONE);
                    linAddress.setVisibility(View.GONE);
                    linFullName.setVisibility(View.VISIBLE);
                    tvFullName.setText(response.clientInfo);
                }
            } else {
                try {
                    if (paymentCategory.alias.equalsIgnoreCase("telecom")) {
                        if (response.balance >= 0) {
                            tvOverpayment.setText(getString(R.string.debt_) + " " + String.valueOf(response.balance).replace(".", ",") + " " + getString(R.string.tenge_icon));
                            tvOverpayment.setVisibility(View.VISIBLE);
                        } else {
                            tvOverpayment.setText(getString(R.string.overpayment) + ": " + String.valueOf(Math.abs(response.balance)).replace(".", ",") + " " + getString(R.string.tenge_icon));
                            tvOverpayment.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception ignored) {
                }
                fee = String.valueOf(response.fee);
                sumWithAmount = response.fee + getDoubleType(edAmount.getText().toString());
                isCheck = false;
                subGeneralFeeLayout.setVisibility(View.VISIBLE);
                insideFeeLayout.setVisibility(View.VISIBLE);
                sumWithFeeLinear.setVisibility(View.VISIBLE);
                tvFee.setText(getFormattedBalance(/*requireContext(), */response.fee, CURRENCY_KGS));
                tvSumWithFee.setText(getFormattedBalance(/*requireContext(),*/ sumWithAmount, CURRENCY_KGS));
                enableDisableView(linMain, false);
                btnGetPenalty.setEnabled(false);
                provReference = response.provReference;
                fixComm = String.valueOf(response.fixComm);
                prcntComm = String.valueOf(response.prcntComm);
                minComm = String.valueOf(response.minComm);
                if (response.clientInfo != null && !response.clientInfo.isEmpty()) {
                    linFullName.setVisibility(View.VISIBLE);
                    tvFullName.setText(response.clientInfo);
                }
                if (response.clientAddress != null && !response.clientAddress.isEmpty()) {
                    linAddress.setVisibility(View.VISIBLE);
                    tvAddress.setText(response.clientAddress);
                }
            }
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void onConfirmPaymentResponse(int statusCode, BankReference response, String errorMessage) {
        Log.i("statusCode", "confirmPaymentResponse code = " + statusCode);
        if (statusCode == Constants.SUCCESS && response != null) {
            GeneralManager.getInstance().setPaymentParameters(getBodyParameters(paymentService));
            Intent intent = new Intent(getActivity(), SmsConfirmActivity.class);
            intent.putExtra("isPayment", true);
            intent.putExtra("isTemplateExist", isTemplateExist);
            intent.putExtra("isSuccess", true);
            intent.putExtra("isTTA", 1);
            intent.putExtra("isTemplate", isTemplate);
            intent.putExtra("isClickToTemplateGridView", isClickToTemplateGridView);
            intent.putExtra("feeWithAmount", sumWithAmount);
            intent.putExtra("paymentTitle", categoryName);
            intent.putExtra("operationCurrency", accountFrom.currency);
            intent.putExtra("paymentFee", fee);
            intent.putExtra("sumWithAmount", tvSumWithFee.getText().toString());//Сумма с коммисией

            //for template
            if (!isTemplate) {
                intent.putExtra("amount", edAmount.getText().toString());
                intent.putExtra("sourceAccountId", accountFrom.code);
                intent.putExtra("serviceId", paymentService.id);
            }

            intent.putExtra(RECEIPT_ACCOUNT_FROM, accountFrom.number);
            intent.putExtra(RECEIPT_NUMBER, response.getReference());
            intent.putExtra(RECEIPT_FEE, getFormattedBalance(getDoubleType(edAmount.getText().toString()),
                    accountFrom.currency));
            intent.putExtra(RECEIPT_ACCOUNT_NUMBER, mobileNumberForReceipt);

            startActivity(intent);
            requireActivity().finish();
        } else {
            btnPayment.setEnabled(true);
            isConfirmOnProcess = false;
            onError(errorMessage);
        }
    }

    @Override
    public void taxDictResponse(int statusCode, TaxDictResponse response, String errorMessage) {
        if (statusCode == Constants.SUCCESS && response != null)
            taxDictResponse = response;
    }

    private void phoneFormat(Editable string, EditText ed) {
        if (string.toString().replaceAll("-", "").length() > 3 && string.toString().replaceAll("-", "").length() <= 5) {
            ed.setText(string.insert(3, "-"));
            ed.setSelection(ed.getText().length());
            isAdd = false;
            Log.i("> 3", "< 5");
        } else if (string.toString().replaceAll("-", "").length() > 5 && string.toString().replaceAll("-", "").length() <= 7) {
            ed.setText(string.insert(3, "-").insert(7, "-"));
            ed.setSelection(ed.getText().length());
            //edittextLogin.setText(string.insert(7, "-"));
            isAdd = false;
            Log.i("> 5", "< 7");
        } else if (string.toString().replaceAll("-", "").length() > 7 && string.toString().replaceAll("-", "").length() <= 10) {
            ed.setText(string.insert(3, "-").insert(7, "-").insert(10, "-"));
            ed.setSelection(ed.getText().length());
            //edittextLogin.setText(string.insert(7, "-"));
            //edittextLogin.setText(string.insert(10, "-"));
            isAdd = false;
            Log.i("> 7", "< 9");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        paymentInfo = new PaymentInfo();
        if (!edMobileNum.getText().toString().equals(String.valueOf(editable))) {

            textViewContactName.setText("");
            textViewContactName.setVisibility(View.GONE);
        }
        if (!hint7.getText().toString().equals(String.valueOf(editable))) {

            textViewContactName.setText("");
            textViewContactName.setVisibility(View.GONE);
        }

        if (!edAmount.getText().toString().equals(String.valueOf(editable))) {
            if (paymentService != null) {
                try {
                    int length = 0;
                    if (lengthForCheck == 999)
                        length = paymentService.parameters.get(0).getLength();
                    else length = lengthForCheck;
                    if (length > 0) {
                        if (editable.length() == length) {
                            Log.d(TAG, "getBody() = " + getBody(true));
                            payments.checkPayments(getActivity(), true, getBody(true));
                        }
                    }
                } catch (Exception e) {
                    checkPayment();
                }
            }

            ids = new int[paramEditTexts.length];
            params = new String[paramEditTexts.length];

            paymentInfo = new PaymentInfo();

            paymentInfo.paramValues = new ParamValue[paramEditTexts.length];

            for (int i = 1; i < paramEditTexts.length; i++) {
                EditText paramEdit = paramEditTexts[i].edittext;
                String paramEditValue = "";
                if (!isTemplate) {
                    Log.i("length", "paramEditTexts[i].paramLength = " + i + ";    " + paramEditTexts[i].paramLength);
                    paramEditValue = paramEdit.getText().toString().replaceAll("[^0-9/]", "");
                    if (paramEditTexts[i].paramLength != 0) {
                        if (paramEditTexts[i].paramLength != paramEditValue.length()) {
                            paramEdit.setError(getString(R.string.error_wrong_format));
                            return;
                        }
                    }
                }

                ids[i] = paramEditTexts[i].id;
                params[i] = paramEditValue;
                paymentInfo.paramValues[i] = new ParamValue(paymentService.getParameters()
                        .get(i).getName(), params[i]);
            }
        }

        if (edAmount.getText().toString().equals(String.valueOf(editable))) {
            boolean isReverce = false;
            StringBuffer stringBuffer = new StringBuffer();
            String string = edAmount.getText().toString().replaceAll(" ", "");
            string = Utilities.dotAndComma(string);
            if (ifChange) {
                ifChange = false;
                if (edAmount.getText().length() > 3) {
                    int pointPosition = string.indexOf(".");
                    if (pointPosition == 0 || pointPosition == -1) {
                        pointPosition = string.indexOf(",");
                    }
                    Log.i("pointPosition", "pointPosition = " + pointPosition);
                    if (pointPosition == -1) {
                        pointPosition = string.length();
                        if (edAmount.getText().length() == 4) {
                            isReverce = true;
                            stringBuffer.delete(0, stringBuffer.length());
                            stringBuffer.append(string);
                            stringBuffer.reverse();
                            string = String.valueOf(stringBuffer);
                            string = string.substring(0, pointPosition - 1) + " " + string.substring(pointPosition - 1);
                        } else {
                            isReverce = false;
                            string = string.substring(0, pointPosition - 3) + " " + string.substring(pointPosition - 3);
                        }
                    } else {
                        if (pointPosition > 3)
                            string = string.substring(0, pointPosition - 3) + " " + string.substring(pointPosition - 3);
                    }
                    for (int i = 0; i < pointPosition / 3; i++) {
                        int lastSpacePosition = string.indexOf(" ");
                        if ((lastSpacePosition - 3) > 0) {
                            string = string.substring(0, lastSpacePosition - 3) + " " + string.substring(lastSpacePosition - 3);
                        } else {
                            break;
                        }
                    }
                }
            } else {
                ifChange = true;
                return;
            }
            if (string.length() != 0) {
                if (isReverce) {
                    stringBuffer.delete(0, stringBuffer.length());
                    stringBuffer.append(string);
                    stringBuffer.reverse();
                    string = String.valueOf(stringBuffer);
                }
                int selection = edAmount.getSelectionStart();
                if ((edAmount.getText().toString().length() + 2) == string.length()) {
                    selection = selection + 2;
                } else if ((edAmount.getText().toString().length() + 1) == string.length()) {
                    selection = selection + 1;
                } else if ((edAmount.getText().toString().length() - 1) == string.length()) {
                    selection = selection - 1;
                } else if ((edAmount.getText().toString().length() - 2) == string.length()) {
                    selection = selection - 2;
                } else {
                    selection = edAmount.getSelectionStart();
                }

                edAmount.setText(string);

                try {
                    edAmount.setSelection(selection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getBundle() {
        if (getArguments() != null) {
            if(getArguments().getString(CATEGORY_NAME) != null){
                categoryName = getArguments().getString(CATEGORY_NAME).trim();
            }
            isMobile = getArguments().getBoolean("isMobile");
            isMobileOperator = getArguments().getString(EXTERNAL_ID);
            paymentServiceId = getArguments().getInt("paymentServiceId");
            Log.d(TAG, "paymentServiceId =  " + paymentServiceId);
            accountFrom = (UserAccounts) getArguments().getSerializable(ACCOUNT);
            if (accountFrom != null) {
                setAccountSpinnerFrom(accountFrom);
            }
        }
    }

    public boolean checkField() {
        boolean success = true;
        if (accountFrom != null) {
            if (isMobile && edMobileNum.getText().toString().replaceAll(Constants.REG_EX_FOR_PHONE, "").isEmpty()) {
                edMobileNum.setError(getResources().getString(R.string.error_empty));
                edMobileNum.requestFocus();
                success = false;
            } else if (!isMobile) {
                for (EditTextParam paramEditText : paramEditTexts) {
                    EditText paramEdit = paramEditText.edittext;
                    if (paramEdit.getVisibility() == View.VISIBLE && TextUtils.isEmpty(paramEdit.getText())) {
                        if (paramEditText.externalId.equalsIgnoreCase(SETTLEMENT_CODE)) {
                            for (EditTextParam paramEditTextInside : paramEditTexts) {
                                if (paramEditTextInside.externalId.equalsIgnoreCase(AREA_CODE) && !paramEditTextInside.edittext.getText().toString().isEmpty()) {
                                    String selectedRegion = paramEditTextInside.edittext.getText().toString();
                                    String[] array = selectedRegion.split("\\s+");
                                    String externalId = array[0];
                                    int code = Integer.parseInt(externalId);
                                    if (isAreaCity(code)) {
                                        paramEdit.setError(null);
                                    } else {
                                        paramEdit.setError(getString(R.string.error_empty));
                                        success = false;
                                    }
                                }
                            }
                        } else {
                            paramEdit.setError(getString(R.string.error_empty));
                            success = false;
                        }
                    }
                }
            }
            if (edAmount.getText().toString().isEmpty()) {
                edAmount.setError(getResources().getString(R.string.error_empty));
                edAmount.requestFocus();
                success = false;
            } else if (getDoubleType(edAmount.getText().toString()) == 0) {
                edAmount.setError(getString(R.string.input_sum_greater_than_0));
                edAmount.requestFocus();
                success = false;
            }

            if (edAmount.getText().toString().startsWith(".") || edAmount.getText().toString().startsWith(",")) {
                edAmount.setError(getResources().getString(R.string.error_wrong_format));
                edAmount.requestFocus();
                return false;
            }

            if (isMobile && success && edMobileNum.getText().toString().replaceAll(Constants.REG_EX_FOR_PHONE, "").length() != 10) {
                edMobileNum.setError(getResources().getString(R.string.error_wrong_format));
                edMobileNum.requestFocus();
                success = false;
            }

            if (autoLayoutWrapper.isShown()) {
                if (btnPayment.getText().equals(getString(R.string.pay_action))) {
                    if (switchRegular.isChecked()) {
                        if (tvTimeBegin.getText().toString().isEmpty() ||
                                tvRePayTime.getText().toString().isEmpty() ||
                                tvRepeat.getText().toString().isEmpty()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(R.string.template_error);
                            builder.setMessage(R.string.template_errorMessage2);
                            builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    requireActivity().onBackPressed();
                                }
                            });
                            builder.create();
                            builder.show();
                        }
                    }
                } else {
                    if (switchRegular.isChecked()) {
                        if (tvTimeBegin.getText().toString().isEmpty()) {
                            tvTimeBegin.setError(getString(R.string.error_empty));
                            tvTimeBegin.requestFocus();
                            success = false;
                        } else if (tvRePayTime.getText().toString().isEmpty()) {
                            tvRePayTime.setError(getString(R.string.error_empty));
                            tvRePayTime.requestFocus();
                            success = false;
                        } else if (tvRepeat.getText().toString().isEmpty()) {
                            tvRepeat.setError(getString(R.string.error_empty));
                            tvRepeat.requestFocus();
                            success = false;
                        }
                    }
                }
            }

            if (edTemplateName.isShown() && edTemplateName.getText().toString().isEmpty()) {
                edTemplateName.setError(getString(R.string.error_empty));
                edTemplateName.requestFocus();
            }
            if (accountFrom.currency.equals(CURRENCY_KGS))
                if (!edAmount.getText().toString().isEmpty() && accountFrom.balance < getDoubleType(edAmount.getText().toString())) {
                    onError(getResources().getString(R.string.error_large_sum));
                    return false;
                }
        } else {
            tvSpinnerFrom.setError(getResources().getString(R.string.error_empty));
            tvSpinnerFrom.requestFocus();
            success = false;
        }
        return success;
    }

    public boolean checkFieldForChangeTemplate() {
        boolean success = true;
        if (accountFrom != null) {
            if (isMobile && edMobileNum.getText().toString().replaceAll(
                    Constants.REG_EX_FOR_PHONE,
                    ""
            ).isEmpty()) {
                edMobileNum.setError(getResources().getString(R.string.error_empty));
                edMobileNum.requestFocus();
                success = false;
            } else if (!isMobile) {
                for (EditTextParam paramEditText : paramEditTexts) {
                    EditText paramEdit = paramEditText.edittext;
                    if (paramEdit.getVisibility() ==
                            View.VISIBLE && TextUtils.isEmpty(paramEdit.getText())) {
                        if (paramEditText.externalId.equalsIgnoreCase(SETTLEMENT_CODE)) {
                            for (EditTextParam paramEditTextInside : paramEditTexts) {
                                if (paramEditTextInside.externalId.equalsIgnoreCase(AREA_CODE)
                                        && !paramEditTextInside.edittext.getText().toString()
                                        .isEmpty()) {
                                    String selectedRegion =
                                            paramEditTextInside.edittext.getText().toString();
                                    String[] array = selectedRegion.split("\\s+");
                                    String externalId = array[0];
                                    int code = Integer.parseInt(externalId);
                                    if (isAreaCity(code)) {
                                        paramEdit.setError(null);
                                    } else {
                                        paramEdit.setError(getString(R.string.error_empty));
                                        success = false;
                                    }
                                }
                            }
                        } else {
                            paramEdit.setError(getString(R.string.error_empty));
                            success = false;
                        }
                    }
                }
            }
            if (edAmount.getText().toString().isEmpty()) {
                edAmount.setError(getResources().getString(R.string.error_empty));
                edAmount.requestFocus();
                success = false;
            } else if (getDoubleType(edAmount.getText().toString()) == 0) {
                edAmount.setError(getString(R.string.input_sum_greater_than_0));
                edAmount.requestFocus();
                success = false;
            }

            if (edAmount.getText().toString().startsWith(".")
                    || edAmount.getText().toString().startsWith(",")) {
                edAmount.setError(getResources().getString(R.string.error_wrong_format));
                edAmount.requestFocus();
                return false;
            }

            if (isMobile && success && edMobileNum.getText().toString().replaceAll(
                    Constants.REG_EX_FOR_PHONE,
                    ""
            ).length() != 10) {
                edMobileNum.setError(getResources().getString(R.string.error_wrong_format));
                edMobileNum.requestFocus();
                success = false;
            }

            if (autoLayoutWrapper.isShown()) {
                if (btnPayment.getText().equals(getString(R.string.pay_action))) {
                    if (switchRegular.isChecked()) {
                        if (tvTimeBegin.getText().toString().isEmpty() ||
                                tvRePayTime.getText().toString().isEmpty() ||
                                tvRepeat.getText().toString().isEmpty()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(R.string.template_error);
                            builder.setMessage(R.string.template_errorMessage2);
                            builder.setPositiveButton(R.string.status_ok, (dialog, which) -> {
                                dialog.cancel();
                                requireActivity().onBackPressed();
                            });
                            builder.create();
                            builder.show();
                        }
                    }
                } else {
                    if (switchRegular.isChecked()) {
                        if (tvTimeBegin.getText().toString().isEmpty()) {
                            tvTimeBegin.setError(getString(R.string.error_empty));
                            tvTimeBegin.requestFocus();
                            success = false;
                        } else if (tvRePayTime.getText().toString().isEmpty()) {
                            tvRePayTime.setError(getString(R.string.error_empty));
                            tvRePayTime.requestFocus();
                            success = false;
                        } else if (tvRepeat.getText().toString().isEmpty()) {
                            tvRepeat.setError(getString(R.string.error_empty));
                            tvRepeat.requestFocus();
                            success = false;
                        }
                    }
                }
            }

            if (edTemplateName.isShown() && edTemplateName.getText().toString().isEmpty()) {
                edTemplateName.setError(getString(R.string.error_empty));
                edTemplateName.requestFocus();
            }
        } else {
            tvSpinnerFrom.setError(getResources().getString(R.string.error_empty));
            tvSpinnerFrom.requestFocus();
            success = false;
        }
        return success;
    }


    void increaseAmount(int amount) {
        edAmount.setError(null);
        if (!edAmount.getText().toString().startsWith(".") && !edAmount.getText().toString().startsWith(",")) {
            if (!edAmount.getText().toString().isEmpty()) {
                float sumInField = Float.parseFloat(edAmount.getText().toString().replaceAll(" ", "").replaceAll(",", "."));
                float sum = sumInField + amount;
                edAmount.setText(doubleFormatter(String.valueOf(sum), getActivity()));
            } else {
                edAmount.setText(doubleFormatter(String.valueOf(amount), getActivity()));
            }
        } else {
            edAmount.setError(getResources().getString(R.string.error_wrong_format));
            edAmount.requestFocus();
        }
    }

    public void initToolbar() {
        if (categoryName != null) {
            tvTitle.setText(categoryName);
        }
        toolbar.setTitle("");
        try {
            ((PaymentActivity) requireActivity()).setSupportActionBar(toolbar);
        } catch (Exception e) {
            ((NavigationActivity) requireActivity()).setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheck) {
                    requireActivity().onBackPressed();
                } else {
                    actionBack();
                }
            }
        });
    }

    public void setTemplateGridAdapter() {
        templateGridAdapter = new GridViewAdapter(requireActivity(),
                getMobileTemplate(paymentService != null ? paymentService.alias : null), setOnClick());

        templateGridView.setAdapter(templateGridAdapter);
    }

    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        if (template.get(position) instanceof TemplatesPayment && isCheck) {
                            TemplatesPayment tempPayment = (TemplatesPayment) template.get(position);
                            Editable editable = new SpannableStringBuilder(tempPayment.getAmount());
                            edAmount.setText(String.valueOf(editable));
                            UserAccounts account = GeneralManager.getInstance().getCardByCode(tempPayment.sourceAccountId);
                            Log.d(TAG, "account = " + account);
                            if (account != null) {
                                setAccountSpinnerFrom(account);
                                accountFrom = account;
                            }
                            try {
                                if (isMobile && !tempPayment.parameters.isEmpty()) {
                                    Editable editable2 = new SpannableStringBuilder(tempPayment.parameters.get(0).getValue());
                                    edMobileNum.setText(editable2);
                                } else if (!tempPayment.parameters.isEmpty() && paramEditTexts.length != 0) {
                                    for (int i = 0; i < paymentService.parameters.size(); i++) {
                                        GeneralManager.setTemplateClicked(true);
                                        paramEditTexts[i].edittext.setText(tempPayment.parameters.get(i).getValue());
                                    }
                                }
                                isClickToTemplateGridView = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                    }
                };
                clickAnimation(view, animatorListener);
            }
        };
    }

    public ArrayList<Object> getMobileTemplate(String alias) {
        template = new ArrayList<>();
        for (TemplatesPayment tempPayment : GeneralManager.getInstance().getTemplatesPayment()) {
            PaymentService paymentService = paymentController.getPaymentServiceById(tempPayment.serviceId);
            if (isMobile) {
                if (paymentService != null) {
                    PaymentCategory paymentCategory = paymentController.getPaymentCategoryByServiceId(paymentService.paymentCategoryId);
                    if (paymentCategory != null) {
                        if (paymentCategory.getAlias().equals("mobile")) {
                            template.add(tempPayment);
                        }
                    }
                }
            } else {
                if (paymentService != null && paymentService.alias.equals(alias)) {
                    template.add(tempPayment);
                }
            }
        }
        if (template.isEmpty()) {
            linTemplate.setVisibility(View.GONE);
        }
        return template;
    }

    public void setAccountSpinnerFrom(UserAccounts userAccounts) {
        if (userAccounts != null) {
            tvSpinnerFrom.setVisibility(View.GONE);
            linAccountInfo.setVisibility(View.VISIBLE);
            tvAccountName.setText(userAccounts.name);
            tvAccountBalance.setText(userAccounts.getFormattedBalance(getActivity()));
            tvAccountNumber.setText(userAccounts.number);
            if (userAccounts instanceof UserAccounts.Cards) {
                UserAccounts.Cards card = (UserAccounts.Cards) userAccounts;
                byte[] min = card.getByteArrayMiniatureImg();
                if (min != null)
                    Utilities.setCardToImageView(card, imgType, tvMulti, BitmapFactory.decodeByteArray(min, 0, min.length));//card.miniatureIm
                else
                    Utilities.setCardToImageView(card, imgType, tvMulti, BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left));
            }
        }
    }

    public JSONObject getBody(boolean isCheckBalance) {
        paymentCheckBody = new BodyModel.PaymentCheck();
        if (edAmount.getText().toString().isEmpty() || isCheckBalance || edAmount.getText().toString().equals("0")) {
            paymentCheckBody.Amount = "0";
        } else {
            paymentCheckBody.Amount = getDoubleType(edAmount.getText().toString()).toString();
        }
        if (!isTemplate) {
            if (serviceExternalId != -1000) {
                paymentService = paymentController.getPaymentServiceByExternalId(serviceExternalId);
            } else {
                if (paymentServiceId != -1000) {
                    paymentService = paymentController.getPaymentServiceById(paymentServiceId);
                }
            }
        }
        if (paymentService != null) {
            paymentCheckBody.PaymentServiceId = paymentService.id;
            paymentCheckBody.Parameters = getBodyParameters(paymentService);
            JSONArray jsonArr = getBodyParameters(paymentService);
            try {
                JSONObject jsonObject = jsonArr.getJSONObject(0);
                mobileNumberForReceipt = jsonObject.get("value").toString();
            } catch (JSONException e) {
                Log.d("mobileNumberForReceipt", "JSONException " + e.getLocalizedMessage());
            }
        }

        if (accountFrom != null)
            paymentCheckBody.AccountId = String.valueOf(accountFrom.code);

        if (!isCheck) {
            paymentCheckBody.FixComm = fixComm;
            paymentCheckBody.PrcntComm = prcntComm;
            paymentCheckBody.MinComm = minComm;
            paymentCheckBody.ProvReference = provReference;
        }
        paymentCheckBody.requestId = requestId;
        return getFieldNamesAndValues(paymentCheckBody);
    }

    public JSONArray getBodyParameters(PaymentService paymentService) {
        JSONArray bodyList = new JSONArray();
        try {
            if (isMobile) {
                JSONObject bodyObjects = new JSONObject();
                bodyObjects.put("serviceParameterId", paymentService.parameters.get(0).id);
                bodyObjects.put("value", edMobileNum.getText().toString());
                bodyList.put(bodyObjects);
            } else {
                for (EditTextParam paramEditText : paramEditTexts) {
                    JSONObject bodyObjects = new JSONObject();
                    bodyObjects.put("serviceParameterId", paramEditText.id);
                    if (paramEditText.identitylist.size() > 0) {
                        String str = paramEditText.edittext.getText().toString();
                        String externalId;
                        if (str.contains(" ")) {
                            externalId = str.substring(0, str.indexOf(" "));
                        } else {
                            externalId = str;
                        }
                        String id = "99991111";
                        Log.i("externalId", "externalId = " + externalId);
                        for (identity identity : paramEditText.identitylist) {
                            if (identity.externalId.equals(externalId)) {
                                id = identity.externalId;
                                Log.i("externalId", "id = " + id);
                            }
                        }
                        if (!id.contains("99991111")) {
                            bodyObjects.put("value", id);
                        }
                    } else {

                        {
                            bodyObjects.put("value", paramEditText.edittext.getText().toString());
                        }
                    }
                    bodyList.put(bodyObjects);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bodyList;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void initView(LinearLayout layout) {
        int count = 0;
        paramEditTexts = new EditTextParam[paymentService.parameters.size()];
        for (int i = 0; i < paymentService.parameters.size(); i++) {
            final PaymentServiceParameter parameter = paymentService.parameters.get(i);
            if (parameter != null) {
                LinearLayout parent = new LinearLayout(getActivity());
                parent.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                parent.setOrientation(LinearLayout.HORIZONTAL);
                edParameter = new EditText(requireContext());
                edParameter.setMaxLines(1);
                edParameter.setHint(parameter.getName());
                if (parameter.getExternalId() != null && parameter.getExternalId().equalsIgnoreCase("VehicleNum")) {
                    edParameter.setVisibility(View.GONE);
                }
                int paddingStartEndInDP = 15;
                int paddingTopBottomInDP = 10;
                final float scale = getResources().getDisplayMetrics().density;
                int paddingStartEndInPX = (int) (paddingStartEndInDP * scale + 0.5f);
                int paddingTopBottomInPX = (int) (paddingTopBottomInDP * scale + 0.5f);
                edParameter.setPadding(paddingStartEndInPX, paddingTopBottomInPX, paddingStartEndInPX, paddingTopBottomInPX);
                edParameter.setTextSize(18);
                edParameter.setBackground(getResources().getDrawable(R.drawable.list_item_selector));
                edParameter.setImeOptions(EditorInfo.IME_ACTION_DONE);
                ArrayList<identity> identity = new ArrayList<>();
                if (parameter.predefinedValues.size() > 0) {
                    Log.i("idSelectEditText", "idSelectEditText = " + idSelectEditText);
                    edParameter.setHint(getString(R.string.choose) + " " + parameter.name.toLowerCase());
                    final String name = parameter.name;
                    final ArrayList<String> stringList = new ArrayList<>();
                    for (PredefinedValues values : parameter.predefinedValues) {
                        identity.add(new identity(values.id, values.externalId));
                        stringList.add(values.externalId + "   " + values.value);
                    }
                    edParameter.setClickable(true);
                    edParameter.setTextIsSelectable(true);
                    edParameter.setCursorVisible(false);
                    edParameter.setLongClickable(false);
                    edParameter.setFocusable(false);
                    edParameter.setSelected(false);
                    edParameter.setKeyListener(null);
                    int finalI = i;
                    edParameter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
                            intent.putExtra("customListBool", true);
                            intent.putExtra("header", name);
                            boolean haveError = false;
                            idSelectEditText = finalI;
                            extIdSelectedEditText = parameter.getExternalId();
                            if (extIdSelectedEditText.equalsIgnoreCase(AREA_CODE)) {
                                for (EditTextParam edParam : paramEditTexts) {
                                    if (edParam.externalId.equalsIgnoreCase(REGION_CODE) && !edParam.edittext.getText().toString().isEmpty()) {
                                        String[] array = edParam.edittext.getText().toString().split("\\s+");
                                        String externalId = array[0];
                                        int selectedRegionCode = Integer.parseInt(externalId.replaceFirst("^0+(?!$)", ""));
                                        if (taxDictResponse != null) {
                                            stringList.clear();
                                            for (TaxDictResponse.Identity rayon : taxDictResponse.rayons) {
                                                if (rayon.regionCode == selectedRegionCode) {
                                                    stringList.add(rayon.code + "   " + rayon.name);
                                                }
                                            }
                                        }
                                    } else if (edParam.externalId.equalsIgnoreCase(REGION_CODE) && edParam.edittext.getText().toString().isEmpty()) {
                                        paramEditTexts[idSelectEditText - 1].edittext.setError(getResources().getString(R.string.error_empty));
                                        haveError = true;
                                    }
                                }
                            }

                            if (extIdSelectedEditText.equalsIgnoreCase(SETTLEMENT_CODE)) {
                                for (EditTextParam edParam : paramEditTexts) {
                                    if (edParam.externalId.equalsIgnoreCase(AREA_CODE) && !edParam.edittext.getText().toString().isEmpty()) {
                                        if (taxDictResponse != null) {
                                            String[] array = edParam.edittext.getText().toString().split("\\s+");
                                            String externalId = array[0];
                                            for (TaxDictResponse.Identity rayon : taxDictResponse.rayons) {
                                                if (rayon.code.equals(externalId)) {
                                                    selectedRayonId = rayon.id;
                                                }
                                            }
                                            Log.i(TAG, "Selected rayon.id " + selectedRayonId);

                                            stringList.clear();
                                            for (TaxDictResponse.Identity settlement : taxDictResponse.settlements) {
                                                if (settlement.rayonCode == selectedRayonId) {
                                                    stringList.add(settlement.code + "   " + settlement.name);
                                                }
                                            }
                                        }
                                    } else if (edParam.externalId.equalsIgnoreCase(AREA_CODE) && edParam.edittext.getText().toString().isEmpty()) {
                                        paramEditTexts[idSelectEditText - 1].edittext.setError(getResources().getString(R.string.error_empty));
                                        haveError = true;
                                    }
                                }
                            }
                            if (!haveError) {
                                intent.putStringArrayListExtra("customList", stringList);
                                if (extIdSelectedEditText.equals(SETTLEMENT_CODE))
                                    intent.putExtra("searchBool", true);
                                startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
                            }
                        }
                    });


                }
                edParameter.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                if (parameter.getMask() != null && !parameter.getMask().isEmpty()) {
                    try {
                        String sMask = parameter.getMask().replaceAll("\\W", "");//.replaceAll("\\s", "").replaceAll("\\D", "");
                        //paramLength = parameter.getMask().replaceAll("[^0-9/]", "").length();
                        paramLength = parameter.getMask().replaceAll("\\W", "").length();
                        edParameter.setHint(parameter.getMask());
                        edParameter.setPadding(paddingStartEndInPX, paddingTopBottomInPX, paddingStartEndInPX, paddingTopBottomInPX);
                        int maxLength = parameter.getMask().length();
                        Log.i("maxLength", " = " + maxLength);
                        if (paymentCategory != null) {
                            if (maxLength > 0) {
                                InputFilter[] FilterArray = new InputFilter[1];
                                if (isTypeMobile(paymentCategory))
                                    FilterArray[0] = new InputFilter.LengthFilter(maxLength + 2);   //TODO +2 - это лишние пробелы при вставке такого номера +996 555 555 555
                                else
                                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                                edParameter.setFilters(FilterArray);
                            }
                        }
                        Log.i("parameter.getMask(", " = " + parameter.getMask());
                        if (isTypeMobile(paymentCategory) || tvTitle.getText().toString().equals("Элсом")) {

                            /** При вводе номера цифры дублировались из за макета 312*55 32 32 где "*"  ‎u200E решило эту проблему‎‎*/
                            final MaskedTextChangedListener listener = new MaskedTextChangedListener("([000])\u200E[00]-[00]-[00]",
                                    true, edParameter, new TextWatcher() {

                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    textViewContactName.setText("");
                                    textViewContactName.setVisibility(View.GONE);
                                }
                            }, (b, s) -> {

                                try {
                                    if (paymentService != null && btnGetPenalty.getVisibility() == View.VISIBLE) {
                                        if (paymentService.ExternalId.equals("PROV_KPAY") || b) {
                                            btnGetPenalty.setEnabled(true);
                                            btnGetPenalty.setFocusable(true);
                                            btnGetPenalty.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (accountFrom == null) {
                                                        tvSpinnerFrom.setError(getResources().getString(R.string.error_empty));
                                                        tvSpinnerFrom.requestFocus();
                                                    } else if (!edParameter.getText().toString().isEmpty() && edParameter.getText().length() > 3) {
                                                        payments.checkPayments(getActivity(), true, getBody(true));
                                                    }
                                                }
                                            });
                                        } else if (b) {
                                            btnGetPenalty.setEnabled(false);
                                            btnGetPenalty.setFocusable(false);
                                        }
                                    }
                                } catch (Exception e) {
                                    checkPayment();
                                }
                            });
                            edParameter.addTextChangedListener(listener);
                        } else {
                            Log.d("PaymentFragment", "is not a Mobile payment");
                            edParameter.addTextChangedListener(new MaskTextWatcher(parameter.getMask(), false, edParameter,
                                    new MaskTextWatcher.ValueListener() {
                                        @Override
                                        public void onTextChanged(boolean maskFilled) {
                                            textViewContactName.setText("");
                                            textViewContactName.setVisibility(View.GONE);
                                            try {
                                                if (paymentService != null && btnGetPenalty.getVisibility() == View.VISIBLE) {
                                                    if (paymentService.ExternalId.equals("PROV_KPAY") || maskFilled) {
                                                        btnGetPenalty.setEnabled(true);
                                                        btnGetPenalty.setFocusable(true);
                                                        btnGetPenalty.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                if (accountFrom == null) {
                                                                    tvSpinnerFrom.setError(getResources().getString(R.string.error_empty));
                                                                    tvSpinnerFrom.requestFocus();
                                                                } else if (!edParameter.getText().toString().isEmpty() && edParameter.getText().length() > 3) {
                                                                    payments.checkPayments(getActivity(), true, getBody(true));
                                                                }
                                                            }
                                                        });
                                                    } else if (!maskFilled) {
                                                        btnGetPenalty.setEnabled(false);
                                                        btnGetPenalty.setFocusable(false);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                checkPayment();
                                            }
                                        }
                                    }));
                        }


                    } catch (Exception e) {
                        paramLength = 0;
                    }
                } else {
                    if (btnGetPenalty.getVisibility() == View.VISIBLE) {
                        edParameter.setHint(getString(R.string.text_payment_code));
                        btnGetPenalty.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (accountFrom == null) {
                                    tvSpinnerFrom.setError(getResources().getString(R.string.error_empty));
                                    tvSpinnerFrom.requestFocus();
                                } else if (!edParameter.getText().toString().isEmpty() && edParameter.getText().length() > 3) {
                                    payments.checkPayments(getActivity(), true, getBody(true));
                                }
                            }
                        });
                    }
                }
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp1.topMargin = 25;
                lp1.leftMargin = 5;
                lp1.rightMargin = 21;
                lp1.bottomMargin = 25;
                lp1.weight = 1;
                edParameter.setLayoutParams(lp1);
                LinearLayout.LayoutParams tv7Params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tv7Params.topMargin = 25;
                tv7Params.leftMargin = paddingStartEndInPX;
                tv7Params.rightMargin = 0;
                tv7Params.bottomMargin = 25;
                TextView tv7 = new TextView(getActivity());
                tv7.setText("+7");
                tv7.setTextSize(18);
                tv7.setTextColor(Color.BLACK);
                tv7.setLayoutParams(tv7Params);
                parent.addView(edParameter);

                LinearLayout.LayoutParams tvBalanceParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tvBalanceParams.gravity = Gravity.CENTER_VERTICAL;
                tvBalanceParams.leftMargin = 10;
                tvBalanceParams.rightMargin = 20;
                tvBalanceParams.topMargin = 25;
                tvBalanceParams.bottomMargin = 25;
                TextView tvBalance = new TextView(getActivity());
                tvBalance.setPadding(15, 20, 15, 8);
                tvBalance.setLayoutParams(tvBalanceParams);
                tvBalance.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                tvBalance.setTextSize(15);
                tvBalance.setVisibility(View.GONE);
                parent.addView(tvBalance);
                layout.addView(parent, count);

//                View separator = new View(getActivity());
//                separator.setPadding(0, 0, 0, 15);
//                separator.setBackgroundColor(getResources().getColor(R.color.gray_231_231_231));
//                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, 4);
//                separator.setLayoutParams(lp2);

                if (GeneralManager.getInstance().getMobilePayment()) {
                    LinearLayout.LayoutParams imageViewContacts = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    imageViewContacts.gravity = Gravity.CENTER_VERTICAL;
                    imageViewContacts.leftMargin = 10;
                    imageViewContacts.rightMargin = 15;
                    imageViewContacts.topMargin = 10;
                    imageViewContacts.bottomMargin = 5;
                    btnContacts = new ImageButton(requireContext());
                    btnContacts.setImageResource(R.drawable.ripple_contacts);
                    btnContacts.setClickable(true);
                    btnContacts.setFocusable(true);
                    btnContacts.setLayoutParams(imageViewContacts);
                    btnContacts.setBackground(ContextCompat.getDrawable(requireContext(), R.color.transparent));
                    parent.addView(btnContacts);
                    btnContacts.setOnClickListener(currencyTextView -> {
                        Intent intent = new Intent(requireActivity(), SelectContactsActivity.class);
                        startActivityForResult(intent, SELECT_CONTACT_REQUEST);
                    });
                }

//                layout.addView(separator, count + 1);
                paramEditTexts[count / 2] = new EditTextParam(parameter.id, parameter.getExternalId(), paramLength, edParameter, tvBalance, identity, tv7);
                count += 2;
            }
        }
    }

    private boolean isTypeMobile(PaymentCategory category) {
        return category.getExternalId().equalsIgnoreCase("TYPE_MOBILE");
    }

    public void actionBack() {
        btnPayment.setText(getResources().getString(R.string.pay_action));
        isCheck = true;
        enableDisableView(linMain, true);
        btnGetPenalty.setEnabled(true);
        templateGridView.setClickable(true);
        subGeneralFeeLayout.setVisibility(View.GONE);
        tvFee.setText("");
        tvSumWithFee.setText("");
        if (paymentService.alias != null)
            if (paymentCategory != null)
                if (paymentService.alias.toUpperCase().equals("CHCOTRAVEL_OPLATA") || paymentService.alias.toUpperCase().equals("AVIATA.KZ") || paymentCategory.alias.equalsIgnoreCase("telecom")) {
                    edAmount.getText().clear();
                }
    }

    @Override
    public void onSmsTextResponse(int statusCode, String errorMessage, Integer errorCode) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.getInstance().setPaymentParameters(getBodyParameters(paymentService));
            String amount = edAmount.getText().toString().replaceAll(" ", "");
            Intent intent = new Intent(requireContext(), SmsConfirmActivity.class);
            intent.putExtra("isPayment", true);
            intent.putExtra("amount", amount);  //amount Чисто вводимая сумма
            intent.putExtra("sourceAccountId", accountFrom.code);
            intent.putExtra("serviceId", paymentService.id);
            intent.putExtra("fixComm", fixComm.replaceAll(" ", ""));
            intent.putExtra("minComm", minComm.replaceAll(" ", ""));
            intent.putExtra("prcntComm", prcntComm);
            intent.putExtra("ProvReference", provReference);
            intent.putExtra(PAYMENT_TITLE, paymentService.name);
            intent.putExtra(RECEIPT_ACCOUNT_NUMBER, mobileNumberForReceipt);
            intent.putExtra(RECEIPT_ACCOUNT_FROM, accountFrom.number);
            intent.putExtra(RECEIPT_FEE, getFormattedBalance(getDoubleType(edAmount.getText().toString()), CURRENCY_KGS));
            intent.putExtra(RECEIPT_NUMBER, provReference);
            intent.putExtra("feeSum", tvFee.getText().toString().replaceAll(" ", ""));  // Коммиссия
            intent.putExtra("paymentFee", fee);
            intent.putExtra("sumWithAmount", tvSumWithFee.getText().toString());//Сумма с коммисией
            intent.putExtra("operationCode", operationCode);
            startActivity(intent);
            requireActivity().getFragmentManager().popBackStack();
            requireActivity().finish();
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        } else {
            btnPayment.setEnabled(true);
            isConfirmOnProcess = false;
            onError(errorMessage);
        }
    }

    public static class EditTextParam {
        public int id;
        public String externalId;
        public int paramLength;
        public EditText edittext;
        public TextView tvBalance;
        public TextView tv7;
        public ArrayList<identity> identitylist;

        EditTextParam(int id, String externalId, int paramLength, EditText editText, TextView tvBalance, ArrayList<identity> identitylist, TextView tv7) {
            this.id = id;
            this.paramLength = paramLength;
            this.externalId = externalId;
            this.edittext = editText;
            this.tvBalance = tvBalance;
            this.identitylist = identitylist;
            this.tv7 = tv7;
        }
    }

    public static class identity {
        public int id;
        public String externalId;

        identity(int id, String externalId) {
            this.id = id;
            this.externalId = externalId;
        }
    }

    static class PaymentInfo {
        public ParamValue[] paramValues;
        public String amount;
        public ATFAccount account;
        public int paymentId;
    }

    static class ParamValue {
        public String name;
        public String value;

        public ParamValue(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
