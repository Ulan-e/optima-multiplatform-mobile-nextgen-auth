package kz.optimabank.optima24.fragment.transfer;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.activity.TransfersActivity.TRANSFER_NAME;
import static kz.optimabank.optima24.utility.Constants.ACCOUNT_KEY;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.DICTIONARY_KEY;
import static kz.optimabank.optima24.utility.Constants.IS_RECEIPT_TRANSFER;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_FEE;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_FROM;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_TO;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_TO_FULL_NAME;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_OWN_ACCOUNT_TO;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_TO_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_CITIZENSHIP_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_CURRENCY_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_KNP_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;
import static kz.optimabank.optima24.utility.Utilities.enableDisableView;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;
import static kz.optimabank.optima24.utility.Utilities.getLayoutParamsForImageSize;
import static kz.optimabank.optima24.utility.Utilities.hasInternetConnection;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.snackbar.Snackbar;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cards.pay.paycardsrecognizer.sdk.Card;
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SelectParameterActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.AccStatusResponse;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.SmsWithTextSend;
import kz.optimabank.optima24.model.interfaces.Transfers;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.SmsWithTextImpl;
import kz.optimabank.optima24.model.service.TransferImpl;
import kz.optimabank.optima24.scan.RPSScanCardIntent;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

public class TransferAccountsFragment extends ATFFragment implements View.OnClickListener,
        TextWatcher, TransferImpl.Callback, TransferImpl.CallbackConfirm, View.OnFocusChangeListener, SmsWithTextImpl.SmsSendWithTextCallback {
    private static final String TAG = TransferAccountsFragment.class.getSimpleName();
    protected static final int SELECT_COUNTRY_FOR_REG_MASTERCARD_REQUEST_CODE = 11;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnTransfer)
    public Button btnTransfer;
    @BindView(R.id.edAmount)
    EditText edAmount;
    @BindView(R.id.linMain)
    LinearLayout linMain;

    //account spinner from
    @BindView(R.id.linSpinnerFrom)
    LinearLayout linSpinnerFrom;
    @BindView(R.id.linFromAccountInfo)
    LinearLayout linFromAccountInfo;
    public @BindView(R.id.tvSpinnerFrom)
    TextView tvSpinnerFrom;
    @BindView(R.id.tvFromAccountName)
    TextView tvFromAccountName;
    @BindView(R.id.tvFromAccountBalance)
    public TextView tvFromAccountBalance;
    @BindView(R.id.tvFromAccountNumber)
    TextView tvFromAccountNumber;
    @BindView(R.id.tvStatusFrom)
    TextView tvStatusFrom;
    @BindView(R.id.imgTypeFrom)
    ImageView imgTypeFrom;
    @BindView(R.id.tv_multi_from)
    TextView tvMultiFrom;

    //account spinner to
    @BindView(R.id.linSpinnerTo)
    LinearLayout linSpinnerTo;
    @BindView(R.id.linToAccountInfo)
    LinearLayout linToAccountInfo;
    @BindView(R.id.imgSpinnerTo)
    protected LinearLayout imgSpinnerTo;
    public @BindView(R.id.edSpinnerTo)
    EditText edSpinnerTo;
    @BindView(R.id.tvToAccountName)
    TextView tvToAccountName;
    @BindView(R.id.tvToAccountBalance)
    TextView tvToAccountBalance;
    @BindView(R.id.tvToAccountNumber)
    TextView tvToAccountNumber;
    @BindView(R.id.tvStatusTo)
    TextView tvStatusTo;
    @BindView(R.id.imgTypeTo)
    ImageView imgTypeTo;
    @BindView(R.id.tv_multi_to)
    TextView tvMultiTo;

    //Currency
    @BindView(R.id.linCurrency)
    LinearLayout linCurrency;
    @BindView(R.id.imgCurrency)
    ImageView imgCurrency;
    public @BindView(R.id.tvCurrency)
    TextView tvCurrency;
    public String currency;

    //fields
    @BindView(R.id.receiver_form)
    View receiverForm;
    @BindView(R.id.etReceiverName)
    EditText etReceiverName;
    @BindView(R.id.linField2BA)
    LinearLayout linField2BA;

    @BindView(R.id.separatorEdCitizenship)
    View separatorEdCitizenship;
    @BindView(R.id.edReceiverIIN)
    EditText edReceiverIIN;
    @BindView(R.id.separatorEdReceiverIIN)
    View separatorEdReceiverIIN;
    @BindView(R.id.tvPurpose)
    EditText edPurpose;

    @BindView(R.id.mastercard_register_form)
    View mastercardRegisterForm;
    protected @BindView(R.id.etCountry)
    EditText etCountry;
    @BindView(R.id.etState)
    EditText etState;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etPostalCode)
    EditText etPostalCode;
    @BindView(R.id.linSelectCountry)
    View selectCountryView;

    @BindView(R.id.linSpinnerCitizenship)
    LinearLayout linSpinnerCitizenship;
    public @BindView(R.id.tvSpinnerCitizenship)
    TextView tvSpinnerCitizenship;

    @BindView(R.id.linSpinnerKNP)
    LinearLayout linSpinnerKNP;
    public @BindView(R.id.tvSpinnerKNP)
    TextView tvSpinnerKNP;

    @BindView(R.id.switch_autolayout_wrapper)
    LinearLayout switchLinearWrapper;
    @BindView(R.id.fee_linear_wrapper)
    LinearLayout feeLinearWrapper;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.tvSumWithFee)
    TextView tvSumWithFee;
    @BindView(R.id.tvConvertInfo)
    TextView tvConvertInfo;

    @BindView(R.id.edTemplateName)
    EditText edTemplateName;

    @BindView(R.id.auto_layout_wrapper)
    LinearLayout autoLayoutWrapper;

    @BindView(R.id.timeBegin_linear)
    LinearLayout timeBeginLinear;
    @BindView(R.id.regular_pay_time_linear)
    LinearLayout regularPayTimeLinear;
    @BindView(R.id.repeat_pay_linear)
    LinearLayout repeatPayLinear;

    @BindView(R.id.switchRegular)
    Switch switchRegular;
    @BindView(R.id.tvTimeBegin)
    TextView tvTimeBegin;
    @BindView(R.id.tvRePayTime)
    TextView tvRePayTime;
    @BindView(R.id.tvRepeat)
    TextView tvRepeat;
    @BindView(R.id.buttons_auto_layout_wrapper)
    LinearLayout buttons_auto_layout_wrapper;

    @BindView(R.id.scan)
    protected ImageView scan;
    @BindView(R.id.tvAttention)
    TextView tvAttention;

    @BindView(R.id.warning_text_content)
    LinearLayout warningTextContent;
    @BindView(R.id.text_warning_multi)
    TextView textViewWarningTextMulti;
    @BindView(R.id.btn_description)
    ImageButton btnDescription;

    int NO_PRODUCT_TYPE = -10;
    private static final int LENGTH_EXTRACTED_VALUE = 20;
    private static final int LENGTH_EDIT_TEXT_CARD = 16;
    private static final int DEFAULT_VALUE_FIRST_FOUR_SYMBOLS = 0000;
    private static final int ZERO_VALUE = 0;
    public Transfers transfer;
    public UserAccounts accountFrom, accountTo;
    public ArrayList<String> stringList = new ArrayList<>();
    public ArrayList<String> citizenshipList = new ArrayList<>();
    BodyModel.Mt100Transfer mt100TransferBody;
    public Dictionary knp;
    String fee, feeCurrency, categoryName, EncryCardNumber = "";
    double sumWithAmount;
    public boolean isResident, isBackPressed = true, isTemplate, Replenish, isAccountToEncrypted = true;
    int selection, selectionSpinnTo;
    protected boolean ifChange = true, successCardBrandOrAccountCheck;
    protected int MY_SCAN_REQUEST_CODE = 5555;
    protected CardTransferType cardTransferType;
    protected String isoCodeCountry;
    String templateAccToNumb;
    protected boolean isCard, needSendFeeRespAfterGetAccData, isGetAccountDataRequested, isForeignCardIsElkart, isCheking;
    public boolean clear;
    private SmsWithTextSend smsWithTextSend;
    private String operationCode = String.valueOf(System.currentTimeMillis());
    public boolean isShowFeeInfo;

    private long requestId;
    private String recipientCardNumber;
    private String recipientName;
    private boolean isMultiCard = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_accounts, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        initToolbar();
        linSpinnerFrom.setOnClickListener(this);
        imgSpinnerTo.setOnClickListener(this);
        linSpinnerCitizenship.setOnClickListener(this);
        linSpinnerKNP.setOnClickListener(this);
        btnTransfer.setOnClickListener(this);
        edSpinnerTo.setOnClickListener(this);
        selectCountryView.setOnClickListener(this);
        edSpinnerTo.addTextChangedListener(this);
        smsWithTextSend = new SmsWithTextImpl();
        smsWithTextSend.registerSmsWithTextCallBack(this);
        setEdSpinnerToParams(false);
        edSpinnerTo.setOnFocusChangeListener(this);
        edAmount.addTextChangedListener(this);
        edAmount.setFilters(new InputFilter[]{new Utilities.DecimalDigitsInputFilter(2)});
        InputFilter[] editFilters = etReceiverName.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        etReceiverName.setFilters(newFilters);
        etReceiverName.setTag(etReceiverName.getKeyListener());
        etReceiverName.setKeyListener(null);
        scan.setOnClickListener(this);
        Utilities.setRobotoTypeFaceToTextView(requireContext(), tvCurrency);
        Utilities.setRobotoTypeFaceToTextView(requireContext(), tvFee);
        Utilities.setRobotoTypeFaceToTextView(requireContext(), tvSumWithFee);
        tvAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAttention.getMaxLines() == 1) {
                    ObjectAnimator animation = ObjectAnimator.ofInt(
                            tvAttention,
                            "maxLines",
                            30);
                    animation.setDuration(300);
                    animation.start();
                    tvAttention.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
                } else {
                    ObjectAnimator animation = ObjectAnimator.ofInt(
                            tvAttention,
                            "maxLines",
                            1);
                    animation.setDuration(300);
                    animation.start();
                    tvAttention.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                }
            }
        });
        citizenshipList.add(getResources().getString(R.string.text_resident));
        citizenshipList.add(getResources().getString(R.string.text_not_resident));

        requestId = System.currentTimeMillis();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        numberOfCardChangeListener();
    }

    public void numberOfCardChangeListener() {
        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "[0000] [0000] [0000] [0000]",
                true,
                edSpinnerTo,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        if (edSpinnerTo.getText().toString().replace(" ", "").length() == LENGTH_EDIT_TEXT_CARD) {
                            if (!GeneralManager.getIsFromTemplate()) {
                                fetchCardDetails();
                            }
                            GeneralManager.setIsFromTemplate(false);
                        }

                        char ch = DEFAULT_VALUE_FIRST_FOUR_SYMBOLS;
                        if (edSpinnerTo.getText().toString().length() > ZERO_VALUE) {
                            ch = edSpinnerTo.getText().toString().toCharArray()[0];
                        }

                        if (Character.isDigit(ch)) {
                            if (edSpinnerTo.getText().toString().replace(" ", "").length() == LENGTH_EDIT_TEXT_CARD)
                                isCard = true;
                            else
                                isCard = false;
                            isGetAccountDataRequested = false;
                            successCardBrandOrAccountCheck = false;
                            needSendFeeRespAfterGetAccData = false;
                            isForeignCardIsElkart = false;
                            isCheking = false;
                            setEdSpinnerToParams(true);
                            receiverForm.setVisibility(View.GONE);
                            etReceiverName.getText().clear();
                            getCurrencyAccountFrom();

                        } else if (Character.isLetter(ch)) {
                            isCard = false;
                            setEdSpinnerToParams(false);
                            if (extractedValue.toString().replace(" ", "").length() == LENGTH_EXTRACTED_VALUE) {
                                linField2BA.setVisibility(View.VISIBLE);
                                receiverForm.setVisibility(View.VISIBLE);
                            } else {
                                linField2BA.setVisibility(View.GONE);
                                receiverForm.setVisibility(View.GONE);
                            }
                        }

                        if (!edSpinnerTo.getText().toString().replaceAll(" ", "").equals(templateAccToNumb)) {
                            etReceiverName.getText().clear();
                            edReceiverIIN.getText().clear();
                            tvSpinnerCitizenship.setText("");
                            tvSpinnerKNP.setText("");
                            edPurpose.getText().clear();
                        }

                    }
                }
        );
        edSpinnerTo.addTextChangedListener(listener);
    }

    // получаем данные получаетеля по номеру карты
    public void fetchCardDetails() {
        if (edSpinnerTo.getText().toString().replace(" ", "").length() == 16) {
            if (edSpinnerTo.getText().toString().replaceAll(" ", "").replaceAll("[0-9]", "").length() <= 0) {
                transfer.getAccountData(parentActivity, edSpinnerTo.getText().toString().replace(" ", ""));
                isGetAccountDataRequested = true;
                cardTransferType = null;
                hideSoftKeyBoard(requireContext());
            }
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(edSpinnerTo, message, Snackbar.LENGTH_SHORT).show();
    }

    // метод вызывается при скрытии клавиатуры или при нажатии кнопки "отправить" не скрыв предварительно клавиатуру
    public void fetchAccountData() {
        if ((accountFrom instanceof UserAccounts.Cards || accountFrom instanceof UserAccounts.CheckingAccounts) && accountTo == null) {
            if (edSpinnerTo.getText().toString().replace(" ", "").length() == 11 || edSpinnerTo.getText().toString().replace(" ", "").length() == 16) {
                if (edSpinnerTo.getText().toString().replaceAll(" ", "").replaceAll("[0-9]", "").length() <= 0) {
                    transfer.getAccountData(parentActivity, edSpinnerTo.getText().toString().replace(" ", ""));
                    isGetAccountDataRequested = true;
                    cardTransferType = null;
                }
            }
        } else if (accountTo != null && btnTransfer.getText().toString().equals(getString(R.string.send_transfer)) && checkAmountField()) {
            if (checkField()) {
                transfer.checkMt100Transfer(parentActivity, getBody(NO_PRODUCT_TYPE)); /* Стягивание данных по карты ФИО И.Т.Д*/
            }
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        transfer = new TransferImpl();
        transfer.registerCallBack(this);
        transfer.registerCallbackConfirm(this);
        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        if (isBackPressed) {
                            requireActivity().onBackPressed();
                            isShowFeeInfo = false;
                        } else {
                            actionBack();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        edSpinnerTo.setOnClickListener(this);
        if (resultCode == CommonStatusCodes.SUCCESS && data != null) {
            if (requestCode == SELECT_ACCOUNT_FROM_REQUEST_CODE) {
                tvSpinnerFrom.setError(null);
                accountFrom = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                if (accountTo != null) {
                    if (accountFrom.code == accountTo.code) {
                        resetSpinnerTo();
                    }
                }
                setAccountSpinnerFrom(accountFrom);
                setEdSpinnerToParams();
                getCurrencyAccountFrom();
                getCurrencyAccountTo();
                setCurrency();
                needSendFeeRespAfterGetAccData = false;
//                fetchAccountData();
            } else if (requestCode == SELECT_ACCOUNT_TO_REQUEST_CODE) {
                accountTo = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                isForeignCardIsElkart = false;
                isCheking = false;
                if (accountTo != null && accountTo.code == Constants.NEW_CARD_ID) {
                    resetSpinnerTo();
                    if (accountFrom instanceof UserAccounts.Cards) {
                        scan.setVisibility(View.VISIBLE);
                    }
                } else {
                    scan.setVisibility(View.GONE);
                    setAccountSpinnerTo(accountTo);
                    getCurrencyAccountTo();
                    setCurrency();
                }
            } else if (requestCode == SELECT_CURRENCY_REQUEST_CODE) {
                this.currency = (String) data.getSerializableExtra(STRING_KEY);

                tvCurrency.setText(currency);
                if (accountFrom != null && accountFrom instanceof UserAccounts.Cards && accountFrom.isMultiBalance) {
                    this.isMultiCard = true;
                    for (UserAccounts.Cards.MultiBalanceList multi : ((UserAccounts.Cards) accountFrom).multiBalanceList) {
                        if (this.currency.equals(multi.currency)) {
                            String balance = getFormattedBalance(multi.amount, currency);
                            tvFromAccountBalance.setText(balance);
                        }
                    }
                }
            } else if (requestCode == SELECT_CITIZENSHIP_REQUEST_CODE) {
                String resident = (String) data.getSerializableExtra(STRING_KEY);
                tvSpinnerCitizenship.setError(null);
                tvSpinnerCitizenship.setText(resident);
                isResident = resident.equals(getResources().getString(R.string.text_resident));
            } else if (requestCode == SELECT_KNP_REQUEST_CODE) {
                knp = (Dictionary) data.getSerializableExtra(DICTIONARY_KEY);
                tvSpinnerKNP.setError(null);
                tvSpinnerKNP.setText(knp.getDescription());
            } else if (requestCode == SELECT_COUNTRY_FOR_REG_MASTERCARD_REQUEST_CODE) {
                Dictionary countryForRegMaster = (Dictionary) data.getSerializableExtra(DICTIONARY_KEY);
                etCountry.setText(Objects.requireNonNull(countryForRegMaster).getCode() + " - " + countryForRegMaster.getDescription());
                tvCurrency.setText(getString(R.string.tenge_icon));
                isoCodeCountry = countryForRegMaster.getCode();
            }
        }

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            Log.i(TAG, "Joined to Scan");
            ifChange = true;
            if (resultCode == Activity.RESULT_OK) {
                Card card = data.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD);
                String cardData = "Card number: " + card.getCardNumber() + "\n"
                        + "Card holder: " + card.getCardHolderName() + "\n"
                        + "Card expiration date: " + card.getExpirationDate();
                etReceiverName.getText().clear();
                edSpinnerTo.setText(card.getCardNumber());
                edSpinnerTo.setSelection(edSpinnerTo.getText().length());
                Log.i(TAG, "Card info: " + cardData);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                edSpinnerTo.getText().clear();
                edSpinnerTo.requestFocus();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "Scan canceled");
            } else {
                Log.i(TAG, "Scan failed");
            }
        }
    }

    public void checkAndSetToElcart() {
        if ((accountFrom != null && accountFrom instanceof UserAccounts.Cards && ((UserAccounts.Cards) accountFrom).brandType == 1)
                || (accountTo != null && accountTo instanceof UserAccounts.Cards && ((UserAccounts.Cards) accountTo).brandType == 1)
                || isForeignCardIsElkart) {
            setCurrencyToElcart();
        }
    }

    public boolean currentCardIsElcart() {
        return ((accountFrom != null && accountFrom instanceof UserAccounts.Cards && ((UserAccounts.Cards) accountFrom).brandType == 1)
                || (accountTo != null && accountTo instanceof UserAccounts.Cards && ((UserAccounts.Cards) accountTo).brandType == 1)
                || isForeignCardIsElkart);
    }

    private void setCurrencyToElcart() {
        tvCurrency.setText(Constants.CURRENCY_KGS);
        currency = Constants.CURRENCY_KGS;
        isClickableSpinnerCurrency(false);
    }

    public void setCurrency() {
        if ((isCheking || (accountTo != null && accountTo instanceof UserAccounts.CheckingAccounts)) &&
                ((accountFrom instanceof UserAccounts.CardAccounts || accountFrom instanceof UserAccounts.Cards))) {
            if (accountTo != null) {
                tvCurrency.setText(accountTo.currency);
                currency = accountTo.currency;
            }
            isClickableSpinnerCurrency(false);
        } else if (accountFrom != null && (accountTo != null && accountTo instanceof UserAccounts.DepositAccounts)) {
            tvCurrency.setText(accountTo.currency);
            currency = accountTo.currency;
        } else {
            if (accountFrom != null) {
                tvCurrency.setText(accountFrom.currency);
                currency = accountFrom.currency;
            }
        }
        if ((accountFrom != null && accountFrom instanceof UserAccounts.CheckingAccounts) || (accountTo != null && accountTo instanceof UserAccounts.DepositAccounts))
            isClickableSpinnerCurrency(false);
        checkAndSetToElcart();
    }

    public void scanCard() {
        Intent intent = new RPSScanCardIntent.Builder(getContext()).build();
        startActivityForResult(intent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linSpinnerFrom:
                intent = new Intent(getActivity(), SelectAccountActivity.class);
                if (accountTo != null && accountTo instanceof UserAccounts.CheckingAccounts) {
                    intent.putExtra("exceptElcart", true);
                }
                intent.putExtra("transferAccFrom", true);
                startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
                edSpinnerTo.setText(""); // TODO: 03.02.2021 Очистить поле ввода куда отправлять
                break;
            case R.id.imgSpinnerTo:
                actionForSpinnerTo();
                edSpinnerTo.setText("");// TODO: 03.02.2021 Очистить поле ввода куда отправлять
                break;
            case R.id.scan:
                if (accountFrom != null) {
                    if (accountFrom instanceof UserAccounts.Cards) {
                        scanCard();
                    }
                }
                break;
            case R.id.edSpinnerTo:
                tvSpinnerFrom.setFocusable(true);
                tvSpinnerFrom.setFocusableInTouchMode(true);
                tvSpinnerFrom.requestFocus();
                tvSpinnerFrom.setError(getString(R.string.error_empty));
                break;
            case R.id.linCurrency:
                intent = new Intent(getActivity(), SelectAccountActivity.class);
                intent.putExtra("Currency", true);
                intent.putExtra("listCurrency", stringList);
                startActivityForResult(intent, SELECT_CURRENCY_REQUEST_CODE);
                break;
            case R.id.linSpinnerCitizenship:
                intent = new Intent(getActivity(), SelectAccountActivity.class);
                intent.putExtra("citizenship", true);
                intent.putExtra("citizenshipList", citizenshipList);
                startActivityForResult(intent, SELECT_CITIZENSHIP_REQUEST_CODE);
                break;
            case R.id.linSpinnerKNP:
                intent = new Intent(getActivity(), SelectParameterActivity.class);
                intent.putExtra("parameterName", getResources().getString(R.string.penalty_knp));
                startActivityForResult(intent, SELECT_KNP_REQUEST_CODE);
                break;
            case R.id.btnTransfer:
                if (hasInternetConnection(requireContext())) {
                    if (btnTransfer.getText().toString().equals(getString(R.string.send_transfer)) && !edAmount.getText().toString().isEmpty()) {
                        fetchAccountData();
                    } else {

                        if (edAmount.getText().toString().isEmpty()) {
                            showSnackBar(getString(R.string.input_sum));
                            edAmount.requestFocus();
                        }

                        if ((accountFrom instanceof UserAccounts.Cards || accountFrom instanceof UserAccounts.CheckingAccounts) && accountTo == null) {
                            if (edSpinnerTo.getText().toString().replaceAll(" ", "").replaceAll("[0-9]", "").length() <= 0) {
                                if (checkField() && !edAmount.getText().toString().isEmpty() && btnTransfer.getText().toString().equals(getString(R.string.transfer_confirm))) {
                                    btnTransfer.setClickable(false);
                                    transfer.registerCallbackConfirm(this);
                                    transfer.confirmMt100Transfer(requireContext(), getBody(NO_PRODUCT_TYPE));
                                    btnTransfer.setEnabled(false);
//                            transfer.checkMt100Transfer(requireContext(), getBody(NO_PRODUCT_TYPE));
//                        } else if (!isBackPressed) {
//                            btnTransfer.setClickable(false);
//                            transfer.registerCallbackConfirm(this);
//                            transfer.confirmMt100Transfer(requireContext(), getBody(NO_PRODUCT_TYPE));
                                    // TODO: 20.05.2021  закомитил из за того что выдавал коммисию даже если требуется ввести ФИО
                                }
                            }
                        } else {
                            if (checkField() && !edAmount.getText().toString().isEmpty() && isBackPressed) {
                                transfer.checkMt100Transfer(requireContext(), getBody(NO_PRODUCT_TYPE));
                            } else if (!isBackPressed) {
                                btnTransfer.setClickable(false);
                                transfer.registerCallbackConfirm(this);
                                transfer.confirmMt100Transfer(requireContext(), getBody(NO_PRODUCT_TYPE));
                                btnTransfer.setEnabled(false);
                            }
                        }
                    }
                } else {
                    showToast(getString(R.string.internet_switched_off));
                }
                break;
            case R.id.linSelectCountry:
                intent = new Intent(parentActivity, SelectParameterActivity.class);
                intent.putExtra("parameterName", getString(R.string.country));
                startActivityForResult(intent, SELECT_COUNTRY_FOR_REG_MASTERCARD_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void checkCardBrandResponse(int statusCode, String errorMessage, String cardValue) {
    }

    @Override
    public void getAccDataResponse(int statusCode, String errorMessage, AccStatusResponse response) {
        if (statusCode == Constants.SUCCESS) {
            if (response != null) {
                String numberTo = edSpinnerTo.getText().toString();
                //Карта АТФ не выводить поле для ввода и тд
                if (!numberTo.startsWith("4052 55") && !numberTo.startsWith("405255")
                        && !numberTo.startsWith("4052 56") && !numberTo.startsWith("405256")
                        && !numberTo.startsWith("4052 57") && !numberTo.startsWith("405257")
                        && !numberTo.startsWith("4052 58") && !numberTo.startsWith("405258")
                        && !numberTo.startsWith("4052 63") && !numberTo.startsWith("405263")
                        && !numberTo.startsWith("4312 53") && !numberTo.startsWith("431253")
                        && !numberTo.startsWith("4318 79") && !numberTo.startsWith("431879")
                        && !numberTo.startsWith("4841 97") && !numberTo.startsWith("484197")
                        && !numberTo.startsWith("4266 68") && !numberTo.startsWith("426668")
                        && !numberTo.startsWith("4232 08") && !numberTo.startsWith("423208")
                        && !numberTo.startsWith("4971 49") && !numberTo.startsWith("497149")
                        && !numberTo.startsWith("4620 00") && !numberTo.startsWith("462000")) {
                    if (numberTo.startsWith("9417")) {
                        //Если чужая карта элкарт не выводить поле для ввода и тд
                        if (!numberTo.startsWith("941749") && !numberTo.startsWith("941750") && !numberTo.startsWith("9417 49") && !numberTo.startsWith("9417 50")) {
                            receiverForm.setVisibility(View.VISIBLE);
                            etReceiverName.setKeyListener((KeyListener) etReceiverName.getTag());
                        } else {
                            //наш элкарт вывести фИО
                            if (response.ownerName != null && !response.ownerName.isEmpty()) {
                                receiverForm.setVisibility(View.VISIBLE);
                                etReceiverName.setText(response.ownerName);
                            }
                        }
                    } else {
                        //Наша виза
                        if (response.ownerName != null && !response.ownerName.isEmpty()) {
                            receiverForm.setVisibility(View.VISIBLE);
                            etReceiverName.setText(response.ownerName);
                        } else if (numberTo.startsWith("4") && response.ownerName == null) {
                            //Чужая виза
                            if (currentCardIsElcart()) {
                                onError(getString(R.string.cant_transfer_tovisa));
                                btnTransfer.setClickable(false);
                            } else {
                                receiverForm.setVisibility(View.VISIBLE);
                                etReceiverName.setKeyListener((KeyListener) etReceiverName.getTag());
                            }
                        }
                    }
                } else {
                    //с элкарт на АТФ
                    if (currentCardIsElcart()) {
                        onError(getString(R.string.cant_transfer_tovisa));
                        btnTransfer.setClickable(false);
                    } else {
                        // На АТФ
                        receiverForm.setVisibility(View.GONE);
                    }
                }
                //Наша карта
                successCardBrandOrAccountCheck = true;
                if (response.accountType == 1) {
                    if (!(accountFrom instanceof UserAccounts.CheckingAccounts)) {
                        tvCurrency.setText(response.currency);
                        currency = response.currency;
                    }
                    isClickableSpinnerCurrency(false);
                    isCheking = true;
                } else {
                    isCheking = false;
                }
                if (response.brandType == 1) {  //Elcart
                    setCurrencyToElcart();
                    isForeignCardIsElkart = true;
                } else
                    isForeignCardIsElkart = false;
                edSpinnerTo.setError(null);
                if (checkField() && !edAmount.getText().toString().isEmpty() && isBackPressed)
                    transfer.checkMt100Transfer(requireContext(), getBody(NO_PRODUCT_TYPE));
            } else {
                edSpinnerTo.requestFocus();
                if (edSpinnerTo.getText().toString().replace(" ", "").length() == 11) {
                    edSpinnerTo.setError(getActivity().getString(R.string.incorrect_beneficiary_number));
                } else if (edSpinnerTo.getText().toString().replace(" ", "").length() == 16)
                    edSpinnerTo.setError(getActivity().getString(R.string.incorrect_card_number));
                successCardBrandOrAccountCheck = false;
            }
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            successCardBrandOrAccountCheck = false;
            onError(errorMessage);
        }
    }

    protected void prepareLayout(CardTransferType cardTransferType) {
        if (cardTransferType == null) {
            receiverForm.setVisibility(View.GONE);
            tvAttention.setVisibility(View.GONE);
            mastercardRegisterForm.setVisibility(View.GONE);
            etReceiverName.getText().clear();
            etCountry.getText().clear();
            etState.getText().clear();
            etCity.getText().clear();
            etAddress.getText().clear();
            etPostalCode.getText().clear();
            edSpinnerTo.setError(null);
            etReceiverName.setError(null);
            etCountry.setError(null);
            etState.setError(null);
            etCity.setError(null);
            etAddress.setError(null);
            etPostalCode.setError(null);
            btnTransfer.setText(getString(R.string.send_transfer));
            return;
        }
        switch (cardTransferType) {
            case INSIDE_ATF:
                receiverForm.setVisibility(View.GONE);
                tvAttention.setVisibility(View.GONE);
                mastercardRegisterForm.setVisibility(View.GONE);
                break;
            case VISA:
                receiverForm.setVisibility(View.VISIBLE);
                tvAttention.setVisibility(View.VISIBLE);
                mastercardRegisterForm.setVisibility(View.GONE);
                tvCurrency.setText(getString(R.string.KGS));
                currency = "KGS";
                break;
        }
        btnTransfer.setText(getString(R.string.send_transfer));

    }

    @Override
    public void mastercardRegisterResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            cardTransferType = CardTransferType.MASTERCARD;
            prepareLayout(cardTransferType);
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void checkMt100TransferResponse(int statusCode, final CheckResponse response, String errorMessage) {
        if (statusCode == 0 && response != null) {
            sumWithAmount = getDoubleType(edAmount.getText().toString());
            if (response.isNeedSmsConfirmation) {
                isBackPressed = false;
                enableDisableView(linMain, false);
                feeLinearWrapper.setVisibility(View.VISIBLE);
                fee = response.feeAmount;
                feeCurrency = response.feeCurrency;
                tvFee.setText(getFormattedBalance(getDoubleType(fee), feeCurrency));
                if (Double.parseDouble(fee) == 0) {
                    feeCurrency = accountFrom.currency;
                }
                tvSumWithFee.setText(getFormattedBalance(sumWithAmount, feeCurrency));
                if (getContext() != null) {
                    btnTransfer.setText(getContext().getResources().getString(R.string.transfer_confirm));
                    isShowFeeInfo = true;
                }
                btnTransfer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        smsWithTextSend.sendSmsWithText(requireContext(), Constants.TRANSFER_OTP_KEY, getDoubleType(edAmount.getText().toString()) + " " + currency, operationCode);
                    }
                });
            } else {
                isBackPressed = false;
                enableDisableView(linMain, false);
                feeLinearWrapper.setVisibility(View.VISIBLE);
                fee = response.feeAmount;
                feeCurrency = response.feeCurrency;

                if (!(response.confirmText.equals(""))) {
                    warningTextContent.setVisibility(View.VISIBLE);
                    btnDescription.setVisibility(View.VISIBLE);
                    textViewWarningTextMulti.setText(response.confirmText);
                    btnDescription.setOnClickListener(view -> showMultiplicatorDescription());
                } else {
                    warningTextContent.setVisibility(View.GONE);
                }

                tvFee.setText(getFormattedBalance(getDoubleType(fee), feeCurrency));
                btnTransfer.setOnClickListener(this);
                tvSumWithFee.setText(getFormattedBalance(sumWithAmount, feeCurrency));
                btnTransfer.setText(getResources().getString(R.string.transfer_confirm));
                isShowFeeInfo = true;
            }
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void showMultiplicatorDescription() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.text_multiplicator_description));
        builder.setPositiveButton(getString(R.string.status_ok), (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onConfirmTransferResponse(int statusCode, String errorMessage) {
        if (isAttached()) {
            if (statusCode == Constants.SUCCESS) {
                Intent intent = new Intent(getActivity(), SmsConfirmActivity.class);
                intent.putExtra("isTransfer", true);
                intent.putExtra("isSuccess", true);
                intent.putExtra("isTemplate", isTemplate);
                intent.putExtra("feeWithAmount", sumWithAmount);
                intent.putExtra("amount", getDoubleType(edAmount.getText().toString()));
                intent.putExtra("operationCurrency", currency);
                intent.putExtra("sourceAccountId", accountFrom.code);

                // данные для чека по операции
                intent.putExtra(RECEIPT_ACCOUNT_FROM, accountFrom.number);
                intent.putExtra(RECEIPT_ACCOUNT_FEE, tvFee.getText().toString());
                intent.putExtra(RECEIPT_OWN_ACCOUNT_TO, tvToAccountNumber.getText().toString());
                intent.putExtra(RECEIPT_ACCOUNT_TO, edSpinnerTo.getText().toString());
                intent.putExtra(RECEIPT_ACCOUNT_TO_FULL_NAME, etReceiverName.getText().toString());
                intent.putExtra(IS_RECEIPT_TRANSFER, true);
                startActivity(intent);
                requireActivity().finish();
            } else {
                errorDialog(errorMessage);
            }
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
        Log.i("TAFafterTextChanged", "editable = " + editable);

    }

    public void initToolbar() {
        tvTitle.setText(categoryName);
        toolbar.setTitle("");
        ((TransfersActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBackPressed) {
                    requireActivity().onBackPressed();
                } else {
                    actionBack();
                }
            }
        });
    }

    private void errorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(errorMessage);
        builder.setTitle(R.string.alert_error);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (errorMessage == null) {
                    dialog.cancel();
                    Intent intent = new Intent(requireContext(), MenuActivity.class);
                    startActivity(intent);
                } else {
                    dialog.cancel();
                    btnTransfer.setEnabled(true);
                    btnTransfer.setClickable(true);
                }
            }
        });
        builder.create();
        builder.show();
    }

    private void getBundle() {
        if (getArguments() != null && !isTemplate) {
            categoryName = getArguments().getString(TRANSFER_NAME);
            Replenish = getArguments().getBoolean("Replenish", false);
            UserAccounts account = (UserAccounts) getArguments().getSerializable(ACCOUNT);
            if (account instanceof UserAccounts.DepositAccounts || Replenish) {
                if (account instanceof UserAccounts.WishAccounts) {
                    if (getArguments().getBoolean("acTo")) {
                        accountTo = account;
                    } else {
                        accountFrom = account;
                    }
                } else if (getArguments().getBoolean("acToO")) {
                    accountFrom = account;
                } else {
                    accountTo = account;
                }
            } else {
                accountFrom = account;
            }
        }
    }

    public void actionForSpinnerTo() {
        Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
        if (accountFrom != null) {
            if (accountFrom instanceof UserAccounts.CheckingAccounts) {
                intent.putExtra("isCardsAndChekingAndDepositAccounts", true);
                intent.putExtra("exceptElcart", true);
            } else if (accountFrom instanceof UserAccounts.CardAccounts) {
                intent.putExtra("isChekingAndDepositAccounts", true);
            } else if (accountFrom instanceof UserAccounts.Cards && ((UserAccounts.Cards) accountFrom).brandType == 1) {
                intent.putExtra("isElcart", true);
            } else {
                intent.putExtra("isAccountsForDebitWithoutCardAcc", true);
                intent.putExtra("tvCurrency", currency);
                if (accountFrom instanceof UserAccounts.DepositAccounts)
                    intent.putExtra("exceptElcart", true);
            }
            intent.putExtra("accountFrom", accountFrom);
            startActivityForResult(intent, SELECT_ACCOUNT_TO_REQUEST_CODE);
        } else {
            tvSpinnerFrom.setFocusable(true);
            tvSpinnerFrom.setFocusableInTouchMode(true);
            tvSpinnerFrom.requestFocus();
            tvSpinnerFrom.setError(getString(R.string.error_empty));
        }
    }

    public void setAccountSpinnerFrom(UserAccounts userAccounts) {
        if (userAccounts != null) {
            if (userAccounts instanceof UserAccounts.WishAccounts) {
                UserAccounts.WishAccounts wishAccounts = (UserAccounts.WishAccounts) userAccounts;
                currency = wishAccounts.currency;
                tvCurrency.setText(wishAccounts.currency);
                tvSpinnerFrom.setVisibility(View.GONE);
                linFromAccountInfo.setVisibility(View.VISIBLE);
                tvStatusFrom.setVisibility(View.GONE);
                tvFromAccountName.setText(wishAccounts.wishName);

                tvFromAccountBalance.setText(userAccounts.getFormattedBalance(getContext()));
                tvFromAccountNumber.setText(wishAccounts.number);
            } else {
                tvSpinnerFrom.setVisibility(View.GONE);
                linFromAccountInfo.setVisibility(View.VISIBLE);
                tvStatusFrom.setVisibility(View.GONE);
                tvFromAccountName.setText(userAccounts.name);
                tvFromAccountBalance.setText(userAccounts.getFormattedBalance(getContext()));
                tvFromAccountNumber.setText(userAccounts.number);
            }
            if (userAccounts instanceof UserAccounts.Cards) {
                if (accountTo == null || accountTo.code == Constants.NEW_CARD_ID) {
                    cardTransferType = null;
                    prepareLayout(null);
                    scan.setVisibility(View.VISIBLE);
                } else {
                    if (scan.isShown())
                        scan.setVisibility(View.GONE);
                }
                linField2BA.setVisibility(View.GONE);
                receiverForm.setVisibility(View.GONE);
                UserAccounts.Cards userCard = (UserAccounts.Cards) userAccounts;
                imgTypeFrom.setLayoutParams(getLayoutParamsForImageSize(requireContext(), 50, 55));
                byte[] min = userCard.getByteArrayMiniatureImg();
                if (min != null)
                    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, BitmapFactory.decodeByteArray(min, 0, min.length));//card.miniatureIm
                else
                    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left));
            } else if (userAccounts instanceof UserAccounts.DepositAccounts) {
                if (scan.getVisibility() == View.VISIBLE)
                    scan.setVisibility(View.GONE);
                imgTypeFrom.setLayoutParams(getLayoutParamsForImageSize(requireContext(), 40, 45));
                imgTypeFrom.setImageResource(R.drawable.ic_image_dark_account_depo);//ic_ico_dep
            } else {
                if (scan.getVisibility() == View.VISIBLE)
                    scan.setVisibility(View.GONE);
                tvStatusFrom.setVisibility(View.VISIBLE);
                if (userAccounts.accountType == 2) {
                    tvStatusFrom.setText(R.string.current_card_accounts);
                } else if (userAccounts.accountType == 1) {
                    tvStatusFrom.setText(R.string.current_account_type);
                }
                imgTypeFrom.setLayoutParams(getLayoutParamsForImageSize(requireContext(), 40, 45));

                imgTypeFrom.setImageResource(R.drawable.ic_image_dark_account_current);//bank_account
            }
        }
    }

    public void setAccountSpinnerTo(UserAccounts userAccounts) {
        if (userAccounts != null) {
            if (userAccounts instanceof UserAccounts.WishAccounts) {
                tvToAccountName.setText(((UserAccounts.WishAccounts) userAccounts).wishName);
            } else {
                tvToAccountName.setText(userAccounts.name);
            }
            linField2BA.setVisibility(View.GONE);
            receiverForm.setVisibility(View.GONE);
            edSpinnerTo.setVisibility(View.GONE);
            linToAccountInfo.setVisibility(View.VISIBLE);
            tvStatusTo.setVisibility(View.GONE);
            tvToAccountBalance.setText(userAccounts.getFormattedBalance(getContext()));
            tvToAccountNumber.setText(userAccounts.number);

            if (userAccounts instanceof UserAccounts.Cards) {
                UserAccounts.Cards userCard = (UserAccounts.Cards) userAccounts;
                imgTypeTo.setLayoutParams(getLayoutParamsForImageSize(requireContext(), 50, 55));
                byte[] min = userCard.getByteArrayMiniatureImg();
                if (min != null)
                    Utilities.setCardToImageView(userCard, imgTypeTo, tvMultiTo, BitmapFactory.decodeByteArray(min, 0, min.length));//card.miniatureIm
                else
                    Utilities.setCardToImageView(userCard, imgTypeTo, tvMultiTo, BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left));
            } else if (userAccounts instanceof UserAccounts.DepositAccounts) {
                Log.i("userAcc", "ACC");
                imgTypeTo.setLayoutParams(getLayoutParamsForImageSize(requireContext(), 40, 45));
                imgTypeTo.setImageResource(R.drawable.ic_image_dark_account_depo);//ic_ico_dep
                //}
            } else {
                Log.i("userAcc", "ELSE");
                Log.i("userAcc", "userAccounts.currency = " + userAccounts.currency);
                tvStatusTo.setVisibility(View.VISIBLE);
                if (userAccounts.accountType == 2) {
                    tvStatusTo.setText(R.string.current_card_accounts);
                } else if (userAccounts.accountType == 1) {
                    tvStatusTo.setText(R.string.current_account_type);
                }
                imgTypeTo.setLayoutParams(getLayoutParamsForImageSize(requireContext(), 40, 45));
                imgTypeTo.setImageResource(R.drawable.ic_image_dark_account_current);//bank_account
            }
        }
    }

    public void setEncryptedCardTo(UserAccounts userAccounts) {
        if (userAccounts != null) {
            StringBuffer stringBuffer = new StringBuffer();
            String string = String.valueOf(userAccounts.number.replace("-", ""));
            stringBuffer.append(string);
            for (int l = 0; l < stringBuffer.length() / 4; l++) {
                int pos = 0;
                pos = stringBuffer.lastIndexOf(" ");
                if (pos == -1) {
                    if (string.length() > 4)
                        stringBuffer.insert(4, " ");
                } else {
                    if (pos > 0) {
                        if (string.length() > pos + 5) {
                            stringBuffer.insert(pos + 5, " ");
                        }
                    }
                }
            }
            EncryCardNumber = String.valueOf(stringBuffer);
            edSpinnerTo.setText(userAccounts.number.replaceAll("-", ""));
        }
    }

    public void resetSpinnerTo() {
        linField2BA.setVisibility(View.GONE);
        receiverForm.setVisibility(View.GONE);
        edSpinnerTo.setVisibility(View.VISIBLE);
        linToAccountInfo.setVisibility(View.GONE);
        accountTo = null;
        currency = accountFrom.currency;
        tvCurrency.setText(accountFrom.currency);
        edSpinnerTo.getText().clear();
        etReceiverName.getText().clear();
        edReceiverIIN.getText().clear();
        tvSpinnerCitizenship.setText("");
        tvSpinnerKNP.setText("");
        edPurpose.getText().clear();
    }

    public void setEdSpinnerToParams() {
        int maxLength;
        if (accountFrom instanceof UserAccounts.WishAccounts) {
            return;
        }
        if (accountFrom instanceof UserAccounts.Cards) {
            maxLength = 19;
        } else {
            edSpinnerTo.setFocusable(true);
            edSpinnerTo.setFocusableInTouchMode(true);
            maxLength = 20;
        }
        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        fArray[1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
                if (src.equals("")) { // for backspace
                    return src;
                }
                if (src.toString().matches("[a-zA-Z0-9 ]+")) {
                    return src;
                }
                return "";
            }
        };
        edSpinnerTo.setFilters(fArray);
    }

    public void setEdSpinnerToParams(boolean isNumberCard) {
        int maxLength;
        InputFilter[] fArray = new InputFilter[2];
        maxLength = 19;                                     //В полях, где необходимо вводить номер карты/счета, отображается клавиатура со всеми символами (скрин 1). Необходимо что бы клавиатура использовалась как при вводе ID клиента
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        fArray[1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
                if (src.equals("")) { // for backspace
                    return src;
                }
                if (src.toString().matches("[0-9 ]+")) {
                    return src;
                }
                return "";
            }
        };
        edSpinnerTo.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edSpinnerTo.setFilters(fArray);
        edSpinnerTo.setFocusableInTouchMode(true);
    }

    public void setEdSpinnerToParamsToNull() {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
                if (src.equals("")) { // for backspace
                    return src;
                }
                if (src.toString().matches("[a-zA-Z0-9 ]+")) {
                    return src;
                }
                return "";
            }
        };
        edSpinnerTo.setFilters(fArray);
    }

    public void getCurrencyAccountFrom() {
        stringList.clear();
        if (accountFrom != null) {
            if (accountFrom instanceof UserAccounts.Cards) {
                UserAccounts.Cards card = (UserAccounts.Cards) accountFrom;
                if (((UserAccounts.Cards) accountFrom).isMultiBalance) {
                    isClickableSpinnerCurrency(true);
                    for (UserAccounts.Cards.MultiBalanceList item : card.multiBalanceList) {
                        addItemInListCurrency(item.currency);
                    }
                } else {
                    isClickableSpinnerCurrency(false);
                    addItemInListCurrency(accountFrom.currency);
                }
            } else if (accountFrom instanceof UserAccounts.CardAccounts) {
                UserAccounts.Cards card = GeneralManager.getInstance().getCardByAccountCode(accountFrom.code);
                if (card != null) {
                    if (card.isMultiBalance) {
                        isClickableSpinnerCurrency(true);
                        for (UserAccounts.Cards.MultiBalanceList item : card.multiBalanceList) {
                            addItemInListCurrency(item.currency);
                        }
                    } else {
                        isClickableSpinnerCurrency(false);
                        addItemInListCurrency(card.currency);
                    }
                }
            } else {
                isClickableSpinnerCurrency(false);
            }
        }
    }

    public void getCurrencyAccountTo() {
        if (accountTo != null) {
            getCurrencyAccountFrom();
            if (accountTo instanceof UserAccounts.Cards) {
                UserAccounts.Cards card = (UserAccounts.Cards) accountTo;
                if (card.isMultiBalance) {
                    for (UserAccounts.Cards.MultiBalanceList item : card.multiBalanceList) {
                        addItemInListCurrency(item.currency);
                    }
                } else {
                    addItemInListCurrency(accountTo.currency);
                }
            } else if (accountTo instanceof UserAccounts.CardAccounts) {
                UserAccounts.Cards card = GeneralManager.getInstance().getCardByAccountCode(accountTo.code);
                if (card != null) {
                    if (card.isMultiBalance) {
                        for (UserAccounts.Cards.MultiBalanceList item : card.multiBalanceList) {
                            addItemInListCurrency(item.currency);
                        }
                    } else {
                        addItemInListCurrency(card.currency);
                    }
                }
            }
        }
        if (stringList.size() > 1) {
            isClickableSpinnerCurrency(true);
        } else {
            isClickableSpinnerCurrency(false);
        }
    }

    private void addItemInListCurrency(String item) {
        if (!stringList.contains(item)) {
            stringList.add(item);
        }
    }

    public void isClickableSpinnerCurrency(boolean isClickable) {
        if (isClickable) {
            imgCurrency.setVisibility(View.VISIBLE);
            linCurrency.setOnClickListener(this);
        } else {
            imgCurrency.setVisibility(View.GONE);
            linCurrency.setOnClickListener(null);
        }
    }

    public void clickSpinnerTo() {
        if (accountFrom != null && accountFrom instanceof UserAccounts.WishAccounts) {
            edSpinnerTo.setFocusableInTouchMode(false);
            edSpinnerTo.setOnClickListener(this);
        } else {
            edSpinnerTo.setFocusableInTouchMode(true);
            edSpinnerTo.setOnClickListener(null);
            if (linToAccountInfo.isShown()) {
                linSpinnerTo.setOnClickListener(this);
            } else {
                imgSpinnerTo.setOnClickListener(this);
                linSpinnerTo.setOnClickListener(null);
            }
        }
    }

    public boolean checkField() {

        if (etReceiverName.isShown()) {
            if (!etReceiverName.getText().toString().isEmpty()) {
                needSendFeeRespAfterGetAccData = false;
            } else {
                needSendFeeRespAfterGetAccData = true;
            }
        }

        boolean success = true;
        if (accountFrom != null) {
            ArrayList<TextView> fields = new ArrayList<>();
            if (!edAmount.getText().toString().isEmpty()) {
                if (edAmount.getText().toString().startsWith(".") || edAmount.getText().toString().startsWith(",")) {
                    edAmount.setError(getResources().getString(R.string.error_wrong_format));
                    edAmount.requestFocus();
                    return false;
                }
                if (!edAmount.getText().toString().isEmpty() && accountFrom.currency.equalsIgnoreCase(currency) &&
                        accountFrom.balance < getDoubleType(edAmount.getText().toString())) {
                    onError(getResources().getString(R.string.error_large_sum));
                    return false;
                }
                if (etReceiverName.isShown()) {
                    fields.add(etReceiverName);
                    if (etReceiverName.getText().toString().split(" ").length < 2 && etReceiverName.getText().toString().isEmpty()) {
                        etReceiverName.setError(getString(R.string.input_first_last_name_error));
                        etReceiverName.requestFocus();
                        success = false;
                    }
                }
                if (edSpinnerTo.isShown()) {
                    fields.add(edSpinnerTo);
                }
                fields.add(edAmount);
            }
            if (autoLayoutWrapper.getVisibility() == View.VISIBLE) {
                if (!btnTransfer.getText().equals(getString(R.string.save))) {
                    if (switchRegular.isChecked()) {
                        if (tvTimeBegin.getText().toString().isEmpty() ||
                                tvRePayTime.getText().toString().isEmpty() ||
                                tvRepeat.getText().toString().isEmpty()) {
                            success = false;
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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
            for (TextView view : fields) {
                if (view.getText().toString().isEmpty()) {
                    view.setError(getResources().getString(R.string.error_empty));
                    view.requestFocus();
                    success = false;
                }
            }
            if (success && edSpinnerTo.isShown()) {
                if (edSpinnerTo.getText().toString().replaceAll(" ", "").length() != 16 && edSpinnerTo.getText().toString().replaceAll(" ", "").length() != 11) {
                    edSpinnerTo.setError(getResources().getString(R.string.error_wrong_format));
                    edSpinnerTo.requestFocus();
                    success = false;
                }
                if (!successCardBrandOrAccountCheck)
                    if (edSpinnerTo.getText().toString().replaceAll(" ", "").length() == 16) {
                        success = true;
                    } else if (edSpinnerTo.getText().toString().replaceAll(" ", "").length() == 11) {
                        success = true;
                    }

            }
            if (tvSpinnerFrom.isShown()) {
                tvSpinnerFrom.setError(getResources().getString(R.string.error_empty));
                tvSpinnerFrom.requestFocus();
                success = false;
            }
        } else {
            tvSpinnerFrom.setError(getResources().getString(R.string.error_empty));
            tvSpinnerFrom.requestFocus();
            success = false;
        }
        return success;
    }

    private boolean checkAmountField() {
        return !edAmount.getText().toString().isEmpty();
    }

    public String getNextPaymentAmount() {
        String amount = edAmount.getText().toString();
        if (!amount.isEmpty()) {
            BigDecimal bd = new BigDecimal(getDoubleType(amount.replaceAll(" ", "")));
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("##0.00", symbols);
            return formatter.format(bd.doubleValue()) + "";
        }
        return "";
    }

    public JSONObject getBody(int productType) {
        mt100TransferBody = new BodyModel.Mt100Transfer();
        if (accountTo != null) {
            if ((accountFrom instanceof UserAccounts.Cards && accountTo instanceof UserAccounts.Cards) ||
                    (!(accountFrom instanceof UserAccounts.Cards) && !(accountTo instanceof UserAccounts.Cards)) ||
                    !(accountFrom instanceof UserAccounts.Cards) && accountTo instanceof UserAccounts.Cards) {
                mt100TransferBody.accountCode = accountFrom.code;
            } else {
                mt100TransferBody.accountCode = ((UserAccounts.Cards) accountFrom).cardAccountCode;
            }
        } else {
            if ((isCard && accountFrom instanceof UserAccounts.Cards) || (!isCard && !(accountFrom instanceof UserAccounts.Cards)) ||
                    !(accountFrom instanceof UserAccounts.Cards) && isCard) {
                mt100TransferBody.accountCode = accountFrom.code;
            } else {
                mt100TransferBody.accountCode = ((UserAccounts.Cards) accountFrom).cardAccountCode;
            }
        }
        mt100TransferBody.accountNumber = accountFrom.number;
        mt100TransferBody.amount = getNextPaymentAmount().replaceAll(",", ".");
        mt100TransferBody.currency = currency;
        mt100TransferBody.type = 1;

        if (accountTo != null && isAccountToEncrypted) {

            if (!(accountFrom instanceof UserAccounts.Cards) && accountTo instanceof UserAccounts.Cards) {
                mt100TransferBody.contragentAccountCode = String.valueOf(((UserAccounts.Cards) accountTo).cardAccountCode);
            } else {
                mt100TransferBody.contragentAccountCode = String.valueOf(accountTo.code);
            }
        } else {
            mt100TransferBody.contragentAccountNumber = edSpinnerTo.getText().toString().replaceAll(" ", "");
            if (etReceiverName.isShown())
                if (!etReceiverName.getText().toString().isEmpty())
                    mt100TransferBody.contragentName = etReceiverName.getText().toString();
        }
        if (!isBackPressed) {
            mt100TransferBody.feeAmount = fee;
            mt100TransferBody.feeCurrency = feeCurrency;
        }
        mt100TransferBody.requestId = requestId;
        return getFieldNamesAndValues(mt100TransferBody);
    }

    public void actionBack() {
        btnTransfer.setText(getResources().getString(R.string.send_transfer));
        btnTransfer.setOnClickListener(this);
        isBackPressed = true;
        enableDisableView(linMain, true);
        feeLinearWrapper.setVisibility(View.GONE);
        tvFee.setText("");
        tvSumWithFee.setText("");
        fee = null;
        feeCurrency = null;
        isShowFeeInfo = false;
    }

    @Override // TODO: 08.02.2021 Focus
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            // code to execute when EditText loses focus
//            try {  // TODO: 03.02.2021 Ловля исключения,если выбрана бизнес карта нельзя вводить по следующим параметрам и клик отправки будет отключён
//                try {// TODO: 04.02.2021 Ловля исключения на приведение типов
//                    if (accountFrom != null) {
//                        if (((UserAccounts.Cards) accountFrom).brand.equals("visabusiness")) {
//                            if (edSpinnerTo.getText().toString().length() < 16 || edSpinnerTo.getText().toString().substring(0, 3).equals("109")) {
//                                if (edSpinnerTo.getText().toString().equals("")) { // TODO: 03.02.2021 Если карта выбрана из списка
//                                    btnTransfer.setClickable(true);
//                                } else {
//                                    btnTransfer.setClickable(false);
//                                    tvSpinnerFrom.setFocusable(true);
//                                    tvSpinnerFrom.setFocusableInTouchMode(true);
//                                    tvSpinnerFrom.requestFocus();
//                                    edSpinnerTo.setError(getString(R.string.forbidden_transfer_to_account));
//                                }
//                            } else {
//                                btnTransfer.setClickable(true);
//                            }
//                        } else {
//                            btnTransfer.setClickable(true);
//                        }
//                    }
//                } catch (ClassCastException c) {
//                    c.printStackTrace();
//                }
//            } catch (StringIndexOutOfBoundsException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public void onSmsTextResponse(int statusCode, String errorMessage, Integer errorCode) {
        if (statusCode == Constants.SUCCESS) {
            Intent intent = new Intent(getActivity(), SmsConfirmActivity.class);
            intent.putExtra("isTransfer", true);
            intent.putExtra("operationCurrency", currency);
            intent.putExtra("fee", fee);
            intent.putExtra("isSwift", false);
            intent.putExtra("feeCurrency", feeCurrency);
            intent.putExtra("feeWithAmount", sumWithAmount);
            intent.putExtra("amount", getDoubleType(edAmount.getText().toString()));
            intent.putExtra("mt100TransferBody", mt100TransferBody);
            intent.putExtra("isTemplate", isTemplate);
            intent.putExtra("operationCode", operationCode);

            // данные для чека по операции
            intent.putExtra(RECEIPT_ACCOUNT_FROM, accountFrom.number);
            intent.putExtra(RECEIPT_ACCOUNT_FEE, tvFee.getText().toString());
            intent.putExtra(RECEIPT_OWN_ACCOUNT_TO, tvToAccountNumber.getText().toString());
            intent.putExtra(RECEIPT_ACCOUNT_TO_FULL_NAME, etReceiverName.getText().toString());
            intent.putExtra(RECEIPT_ACCOUNT_TO, edSpinnerTo.getText().toString());
            intent.putExtra(IS_RECEIPT_TRANSFER, true);

            startActivity(intent);
            requireActivity().finish();
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private enum CardBrands {
        ATF_CARD(1),
        NOT_DEFINED_CARD(2),
        VISA(3),
        MASTERCARD(4),
        NOT_REGISTERED_MASTERCARD(5),
        UNION_PAYMENT(6),
        HALYK(7);

        private final int cardValue;

        CardBrands(int cardValue) {
            this.cardValue = cardValue;
        }
    }

    public enum CardTransferType {
        INSIDE_ATF,
        VISA,
        MASTERCARD,
    }
}