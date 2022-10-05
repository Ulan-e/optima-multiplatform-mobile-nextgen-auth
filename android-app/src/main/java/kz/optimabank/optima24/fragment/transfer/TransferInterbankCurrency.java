package kz.optimabank.optima24.fragment.transfer;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SelectParameterActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.db.controllers.DictionaryController;
import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.db.entry.ForeignBank;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.gson.response.TransferConfirmResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.DictionaryContext;
import kz.optimabank.optima24.model.interfaces.TransfersSwift;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.DictionaryImpl;
import kz.optimabank.optima24.model.service.SmsWithTextImpl;
import kz.optimabank.optima24.model.service.TransferSwiftImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.activity.TransfersActivity.TRANSFER_NAME;
import static kz.optimabank.optima24.utility.Constants.ACCOUNT_KEY;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.DICTIONARY_KEY;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_BIC_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;
import static kz.optimabank.optima24.utility.Utilities.dotAndComma;
import static kz.optimabank.optima24.utility.Utilities.enableDisableView;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;
import static kz.optimabank.optima24.utility.Utilities.getLayoutParamsForImageSize;
import static kz.optimabank.optima24.utility.Utilities.transliterate;

/**
 * Created by Timur on 11.05.2017.
 */

public class TransferInterbankCurrency extends ATFFragment implements View.OnClickListener, TransferSwiftImpl.Callback, SmsWithTextImpl.SmsSendWithTextCallback {
    private static final String TAG = TransferInterbankCurrency.class.getSimpleName();

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public @BindView(R.id.tvCurrency)
    TextView tvCurrency;
    public String currency;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.tvSumWithFee)
    TextView tvSumWithFee;
    @BindView(R.id.btnTransfer)
    Button btnTransfer;
    @BindView(R.id.edAmount)
    EditText edAmount;
    @BindView(R.id.etReceiverName)
    EditText etReceiverName;
    @BindView(R.id.edSpinnerTo)
    EditText edSpinnerTo;
    @BindView(R.id.fee_linear_wrapper)
    LinearLayout linFee;
    @BindView(R.id.linMain)
    LinearLayout linMain;

    //account spinner from
    @BindView(R.id.linSpinnerFrom)
    LinearLayout linSpinnerFrom;
    @BindView(R.id.linFromAccountInfo)
    LinearLayout linFromAccountInfo;
    @BindView(R.id.tvSpinnerFrom)
    TextView tvSpinnerFrom;
    @BindView(R.id.tvFromAccountName)
    TextView tvFromAccountName;
    @BindView(R.id.tvFromAccountBalance)
    TextView tvFromAccountBalance;
    @BindView(R.id.tvFromAccountNumber)
    TextView tvFromAccountNumber;
    @BindView(R.id.tvStatusFrom)
    TextView tvStatusFrom;
    @BindView(R.id.imgTypeFrom)
    ImageView imgTypeFrom;
    @BindView(R.id.tv_multi_from)
    TextView tvMultiFrom;
    @BindView(R.id.linSpinnerBIC)
    LinearLayout linSpinnerBIC;
    @BindView(R.id.tvSpinnerKNP)
    TextView tvSpinnerKNP;
    @BindView(R.id.tvSpinnerBIC)
    TextView tvSpinnerBIC;
    @BindView(R.id.edPurpose)
    EditText edPurpose;
    @BindView(R.id.tvReceiversBankName)
    TextView tvReceiversBankName;
    @BindView(R.id.tvMediatorBankName)
    TextView tvMediatorBankName;
    @BindView(R.id.tvMediatorBIC)
    TextView tvMediatorBIC;
    @BindView(R.id.tvContragentCountry)
    TextView tvContragentCountry;
    @BindView(R.id.tvContragentRegisterCountry)
    TextView tvContragentRegisterCountry;
    @BindView(R.id.edKpp)
    EditText edKpp;
    @BindView(R.id.edKppHint)
    TextInputLayout edKppHint;
    @BindView(R.id.edPayerName)
    EditText edPayerName;
    @BindView(R.id.edPayerAddress)
    EditText edPayerAddress;
    @BindView(R.id.edContragentAddress)
    EditText edContragentAddress;
    @BindView(R.id.edTvReceiverIIN)
    EditText edTvReceiverIIN;
    @BindView(R.id.edReceiversBankCorAccount)
    EditText edReceiversBankCorAccount;
    @BindView(R.id.linContragentCountry)
    LinearLayout linContragentCountry;
    @BindView(R.id.linContragentRegisterCountry)
    LinearLayout linContragentRegisterCountry;
    @BindView(R.id.linReceiversBankName)
    LinearLayout linReceiversBankName;
    @BindView(R.id.linMediatorBankName)
    LinearLayout linMediatorBankName;
    @BindView(R.id.linMediatorBIC)
    LinearLayout linMediatorBIC;
    @BindView(R.id.feeType1)
    RadioButton feeType1;
    @BindView(R.id.edTemplateName)
    EditText edTemplateName;

    private static final String RUB = "RUB";
    private static final String KZT = "KZT";
    private static final String USD = "USD";

    private static final String ATF_BANK_BIC = "ALMNKZKA";
    private static final String ATF_BANK_BIC_JSC = "ALMNKZKATRE";

    int SELECT_COUNTRY = 15, SELECT_REGISTR_COUNTRY = 18, FEE = 16;
    private String SENDER_COUNTRY = "KZ";
    public UserAccounts accountFrom;
    public UserAccounts accountFee = new UserAccounts();
    TransfersSwift transfer;
    DictionaryContext dictionaryContext;
    ArrayList<Country> countries;
    Country country;
    Country countryRegistration;
    ForeignBank foreignBank;
    DictionaryController dictionaryController;
    BodyModel.SwiftTransfer SwiftTransfer;
    boolean isTemplate = false, isBackPressed = true;
    boolean isCreate, isError = true, mediatorBank = false, isChange = true;
    int feeType;
    double mTotalAmount;
    CheckResponse response;
    SmsWithTextImpl smsWithText;
    private String mChargeType;
    private String amountWithDot;
    String operationCode = String.valueOf(System.currentTimeMillis());
    private long requestId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_interbank_in_currency, container, false);
        ButterKnife.bind(this, view);

        requestId = System.currentTimeMillis();

        smsWithText = new SmsWithTextImpl();
        smsWithText.registerSmsWithTextCallBack(this);
        feeType = 1;
        feeType1.setChecked(true);
        feeType1.setClickable(true);
        dictionaryContext = new DictionaryImpl();
        dictionaryContext.getAllDictionary(getActivity());
        dictionaryController = DictionaryController.getController();
        countries = dictionaryController.getCountries();
        Utilities.setRobotoTypeFaceToTextView(requireContext(), tvCurrency);
        Utilities.setRobotoTypeFaceToTextView(requireContext(), tvFee);
        Utilities.setRobotoTypeFaceToTextView(requireContext(), tvSumWithFee);
        linSpinnerFrom.setOnClickListener(this);
        btnTransfer.setOnClickListener(this);
        tvSpinnerKNP.setOnClickListener(this);
        linSpinnerBIC.setOnClickListener(this);
        tvSpinnerBIC.setOnClickListener(this);
        linReceiversBankName.setOnClickListener(this);
        linMediatorBankName.setOnClickListener(this);
        linContragentCountry.setOnClickListener(this);
        linContragentRegisterCountry.setOnClickListener(this);
        feeType1.setOnClickListener(this);
        edAmount.addTextChangedListener(new CustomTextWatcher(edAmount));
        edAmount.setFilters(new InputFilter[]{new Utilities.DecimalDigitsInputFilter(2)});
        etReceiverName.addTextChangedListener(new CustomTextWatcher(etReceiverName));
        edContragentAddress.addTextChangedListener(new CustomTextWatcher(edContragentAddress));
        edTvReceiverIIN.addTextChangedListener(new CustomTextWatcher(edTvReceiverIIN));
        edSpinnerTo.addTextChangedListener(new CustomTextWatcher(edSpinnerTo));
        tvReceiversBankName.addTextChangedListener(new CustomTextWatcher(tvReceiversBankName));
        edReceiversBankCorAccount.addTextChangedListener(new CustomTextWatcher(edReceiversBankCorAccount));
        tvContragentCountry.addTextChangedListener(new CustomTextWatcher(tvContragentCountry));
        tvContragentRegisterCountry.addTextChangedListener(new CustomTextWatcher(tvContragentRegisterCountry));
        edPurpose.addTextChangedListener(new CustomTextWatcher(edPurpose));
        edKpp.addTextChangedListener(new CustomTextWatcher(edKpp));
        edPayerAddress.addTextChangedListener(new CustomTextWatcher(edPayerAddress));
        if(GeneralManager.getInstance().getUser().fullName != null) {
            edPayerName.setText(transliterate(GeneralManager.getInstance().getUser().fullName).toUpperCase());
        }
        if (GeneralManager.getInstance().getUser().address != null) {
            edPayerAddress.setText(transliterate(GeneralManager.getInstance().getUser().address).toUpperCase());
        }
        linMediatorBankName.setOnClickListener(this);
        linMediatorBIC.setOnClickListener(this);
        isCreate = true;
        setFilters();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initToolbar();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        transfer = new TransferSwiftImpl();
        transfer.registerCallBack(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dictionaryController != null) {
            dictionaryController.close();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linSpinnerFrom:
                if (GeneralManager.getInstance().getVisibleCheckingAccountsNotKZT(requireContext()).isEmpty()) {
                    onError(getString(R.string.no_accounts));
                } else {
                    intent = new Intent(requireActivity(), SelectAccountActivity.class);
                    intent.putExtra("checkChOnlyNotKZT", true);
                    startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
                }
                break;
            case R.id.linSpinnerBIC:
            case R.id.tvSpinnerBIC:
                intent = new Intent(requireActivity(), SelectParameterActivity.class);
                intent.putExtra("parameterName", getResources().getString(R.string.text_bic));
                intent.putExtra(SelectParameterActivity.IS_FOREIGN_BANK_EXTRA, true);
                startActivityForResult(intent, SELECT_BIC_REQUEST_CODE);
                break;
            case R.id.linReceiversBankName:
            case R.id.tvReceiversBankName:
                intent = new Intent(requireActivity(), SelectParameterActivity.class);
                intent.putExtra("parameterName", getResources().getString(R.string.text_bic));
                intent.putExtra(SelectParameterActivity.IS_FOREIGN_BANK_EXTRA, true);
                startActivityForResult(intent, SELECT_BIC_REQUEST_CODE);
                break;
            case R.id.linMediatorBankName:
            case R.id.tvMediatorBankName:
                intent = new Intent(getActivity(), SelectParameterActivity.class);
                intent.putExtra("parameterName", getResources().getString(R.string.text_bic_mediator));
                intent.putExtra(SelectParameterActivity.IS_FOREIGN_BANK_EXTRA, true);
                startActivityForResult(intent, 11);
                break;
            case R.id.linMediatorBIC:
            case R.id.tvMediatorBIC:
                intent = new Intent(requireActivity(), SelectParameterActivity.class);
                intent.putExtra("parameterName", getResources().getString(R.string.text_bic_mediator));
                intent.putExtra(SelectParameterActivity.IS_FOREIGN_BANK_EXTRA, true);
                startActivityForResult(intent, 11);
                break;
            case R.id.btnTransfer:
                requestTransfer();
                break;
            case R.id.linContragentCountry:
            case R.id.tvContragentCountry:
                setCountry(SELECT_COUNTRY);
                break;
            case R.id.linContragentRegisterCountry:
            case R.id.tvContragentRegisterCountry:
                setCountry(SELECT_REGISTR_COUNTRY);
                break;
            case R.id.feeType1:
                feeType = 1;
                break;
        }
    }

    void setFilters() {

        String tedPayerAddress = transliterate(edPayerAddress.getText().toString());
        String tetReceiverName = transliterate(etReceiverName.getText().toString());

        edReceiversBankCorAccount.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[а-яА-ЯёЁa-zA-Z0-9-/()., ]+")) {
                            return src;
                        }
                        return "";
                    }
                }, new InputFilter.AllCaps(), new InputFilter.LengthFilter(32)
        });

        etReceiverName.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[а-яА-ЯёЁa-zA-Z \\-/() ]+")) {
                            return src;
                        }
                        return "";
                    }
                }, new InputFilter.AllCaps()
        });
        edContragentAddress.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[а-яА-ЯёЁa-zA-Z0-9-/()., ]+")) {
                            return src;
                        }
                        return "";
                    }
                }
                , new InputFilter.AllCaps()
        });
        edPurpose.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[а-яА-ЯёЁa-zA-Z0-9 \\-/() \\., ]+")) {
                            return src;
                        }
                        return "";
                    }
                }, new InputFilter.LengthFilter(500)
        });
        edPayerAddress.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[а-яА-ЯёЁa-zA-Z0-9-/()., ]+")) {
                            return src;
                        }
                        return "";
                    }
                }, new InputFilter.AllCaps()
        });
        edSpinnerTo.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("^[а-яА-ЯёЁa-zA-Z0-9]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });
        SENDER_COUNTRY = "KZ";
        edPayerAddress.setText(tedPayerAddress);
        etReceiverName.setText(tetReceiverName);
        edPayerName.setText(transliterate(GeneralManager.getInstance().getUser().fullName).toUpperCase());
    }

    void setCountry(int requestCode) {
        countries = dictionaryController.getCountries();
        ArrayList<String> customList = new ArrayList<>();
        for (Country country : countries) {
            customList.add(country.getAlphaCode() + "   " + country.getName());
        }
        Intent intent = new Intent(requireActivity(), SelectParameterActivity.class);
        intent.putStringArrayListExtra("customList", customList);
        intent.putExtra("parameterName", getString(R.string.receivers_country));
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CommonStatusCodes.SUCCESS) {
            if (data != null) {
                if (requestCode == SELECT_ACCOUNT_FROM_REQUEST_CODE) {
//                    clearPlaza();
                    tvSpinnerFrom.setError(null);
                    accountFrom = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                    setAccountSpinnerFrom(accountFrom);
                    if (accountFrom != null) {
                        currency = accountFrom.currency;
                        tvCurrency.setText(accountFrom.currency);
                        if (accountFrom.currency.equals("RUB")) {
                            edKppHint.setVisibility(View.VISIBLE);
                        } else edKppHint.setVisibility(View.GONE);
                    }

                } else if (requestCode == FEE) {
                    accountFee = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                } else if (requestCode == SELECT_BIC_REQUEST_CODE) {
                    foreignBank = data.getExtras().getParcelable(DICTIONARY_KEY);
                    tvSpinnerBIC.setError(null);
                    tvSpinnerBIC.setText(this.foreignBank.bic);
                    tvReceiversBankName.setText(this.foreignBank.name);
                } else if (requestCode == 11) {
                    foreignBank = data.getExtras().getParcelable(DICTIONARY_KEY);
                    tvSpinnerBIC.setError(null);
                    tvMediatorBIC.setText(foreignBank.bic);
                    tvMediatorBankName.setText(foreignBank.name);
                    mediatorBank = true;
                } else if (requestCode == SELECT_COUNTRY) {
                    for (Country cou : countries) {
                        if (data.getStringExtra(STRING_KEY).toLowerCase().contains(cou.getName().toLowerCase())) { //equalsIgnoreCase
                            country = cou;
                            countryRegistration = cou;
                            tvContragentCountry.setText(cou.getName());
                            tvContragentRegisterCountry.setText(cou.getName());
                            break;
                        }
                    }
                } else if (requestCode == SELECT_REGISTR_COUNTRY) {
                    for (Country cou : countries) {
                        Log.i(TAG, "onActivityResult(), SELECT_COUNTRY, cou.getName() = " + cou.getName());
                        if (data.getStringExtra(STRING_KEY).toLowerCase().contains(cou.getName().toLowerCase())) { //equalsIgnoreCase
                            countryRegistration = cou;
                            tvContragentRegisterCountry.setText(cou.getName());
                            break;
                        }
                    }
                }

            }
        }
    }

    private void clearPlaza() {
        edPurpose.getText().clear();
        edContragentAddress.getText().clear();
    }

    public void initToolbar() {
        if (getArguments() != null) {
            String categoryName = getArguments().getString(TRANSFER_NAME);
            tvTitle.setText(categoryName);
        }
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

    public void setAccountSpinnerFrom(UserAccounts userAccounts) {
        if (userAccounts != null) {
            tvSpinnerFrom.setVisibility(View.GONE);
            linFromAccountInfo.setVisibility(View.VISIBLE);
            tvStatusFrom.setVisibility(View.GONE);
            tvFromAccountName.setText(userAccounts.name);
            tvFromAccountBalance.setText(userAccounts.getFormattedBalance(getContext()));
            tvFromAccountNumber.setText(userAccounts.number);
            if (userAccounts instanceof UserAccounts.Cards) {
                UserAccounts.Cards userCard = (UserAccounts.Cards) userAccounts;
                tvStatusFrom.setVisibility(View.GONE);
                byte[] min = userCard.getByteArrayMiniatureImg();
                if (min != null)
                    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, BitmapFactory.decodeByteArray(min, 0, min.length));
                else
                    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left));
            }
            if (userAccounts instanceof UserAccounts.DepositAccounts) {
                imgTypeFrom.setLayoutParams(getLayoutParamsForImageSize(requireContext(), 40, 45));
                imgTypeFrom.setImageResource(R.drawable.ic_image_dark_account_depo);//ic_ico_dep
            } else {
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

    void requestTransfer() {
        isError = true;
        if (accountFrom != null) {
            if (!edAmount.getText().toString().isEmpty() && accountFrom.balance < getDoubleType(edAmount.getText().toString()) &&
                    accountFrom.currency.equalsIgnoreCase(currency)) {
                onError(getResources().getString(R.string.error_large_sum));
                isError = false;
            }
        }
        if (accountFrom == null) {
            isError = false;
        }
        if (edPurpose.getText().toString().isEmpty()) {
            edPurpose.setError(getString(R.string._t_fill_) + " " + getString(R.string.text_knp));
            edPurpose.requestFocus();
            isError = false;
        }

        if (tvSpinnerBIC.getText().toString().isEmpty()) {
            tvSpinnerBIC.setError(getString(R.string.choose) + " " + getString(R.string.text_bic));
            tvSpinnerBIC.setFocusable(true);
            tvSpinnerBIC.setFocusableInTouchMode(true);
            tvSpinnerBIC.requestFocus();
            isError = false;
        }

        if (tvReceiversBankName.getText().toString().isEmpty()) {
            tvReceiversBankName.setError(getString(R.string.choose) + " " + getString(R.string.receivers_bank_name));
            tvReceiversBankName.setFocusable(true);
            tvReceiversBankName.setFocusableInTouchMode(true);
            tvReceiversBankName.requestFocus();
            isError = false;
        }

//        if (edKpp.isShown() && edKpp.getText().toString().isEmpty()) {
//            edKpp.setError(getString(R.string._t_fill_) + " " + getString(R.string.kpp));
//            edKpp.requestFocus();
//            isError = false;
//        }

        if (edContragentAddress.getText().toString().isEmpty()) {
            edContragentAddress.setError(getString(R.string._t_fill_) + " " + getString(R.string.receivers_address));
            edContragentAddress.requestFocus();
            isError = false;
        }

        checkFieldsKnpAndInn();

        if (tvContragentCountry.getText().toString().isEmpty()) {
            tvContragentCountry.setError(getString(R.string.choose) + " " + getString(R.string.contragent_country));
            tvContragentCountry.setFocusable(true);
            tvContragentCountry.setFocusableInTouchMode(true);
            tvContragentCountry.requestFocus();
            isError = false;
        }

        if (tvContragentRegisterCountry.getText().toString().isEmpty()) {
            tvContragentRegisterCountry.setError(getString(R.string.choose) + " " + getString(R.string.contragent_register_country));
            tvContragentRegisterCountry.setFocusable(true);
            tvContragentRegisterCountry.setFocusableInTouchMode(true);
            tvContragentRegisterCountry.requestFocus();
            isError = false;
        }

        if (etReceiverName.getText().toString().isEmpty()) {
            etReceiverName.setError(getString(R.string._t_fill_) + " " + getString(R.string.receiver_name));
            etReceiverName.requestFocus();
            isError = false;
        }

        if (edSpinnerTo.getText().toString().isEmpty()) {
            edSpinnerTo.setError(getString(R.string._t_fill_) + " " + getString(R.string.text_receiver_acc_number));
            edSpinnerTo.requestFocus();
            isError = false;
        }

        if (edPayerAddress.getText().toString().length() <= 0) {
            edPayerAddress.setError(getString(R.string._t_fill_) + " " + getString(R.string.sender_address));
            edPayerAddress.requestFocus();
            isError = false;
        }

        if (accountFrom != null)
            if (accountFrom.balance <= 0) {
                edAmount.setError(getString(R.string._t_fill_) + " " + getString(R.string.text_sum));
                edAmount.requestFocus();
                isError = false;
            }

        if (edAmount.getText().toString().isEmpty()) {
            edAmount.setError(getString(R.string._t_fill_) + " " + getString(R.string.text_sum));
            edAmount.requestFocus();
            isError = false;
        }

        if (accountFrom != null)
            if (accountFrom.accountType == 0) {
                tvSpinnerFrom.setError(getString(R.string.choose_account_toast));
                tvSpinnerFrom.setFocusable(true);
                tvSpinnerFrom.setFocusableInTouchMode(true);
                tvSpinnerFrom.requestFocus();
                isError = false;
            }

        if (accountFrom == null) {
            tvSpinnerFrom.setError(getString(R.string.choose_account_toast));
            tvSpinnerFrom.setFocusable(true);
            tvSpinnerFrom.setFocusableInTouchMode(true);
            tvSpinnerFrom.requestFocus();
            isError = false;
        }

        if (isError) {
            SwiftTransfer = new BodyModel.SwiftTransfer();

            if (feeType == 1) {
                mChargeType = "OUR";
            } else if (feeType == 2) {
                mChargeType = "BEN";
            } else {
                mChargeType = "SHA";
            }
            mChargeType = mChargeType;

            SwiftTransfer.productType = 110;
            SwiftTransfer.accountCode = accountFrom.getCode();
            if (feeType == 2)
                SwiftTransfer.FeeAccountCode = null;
            else
                SwiftTransfer.FeeAccountCode = accountFee.getCode();
            SwiftTransfer.contragentName = etReceiverName.getText().toString();
            SwiftTransfer.ContragentKpp = edKpp.getVisibility() == View.VISIBLE ? edKpp.getText().toString() : "";
            if (isTemplate) {
                SwiftTransfer.ContragentCountry = tvContragentCountry.getText().toString();
                SwiftTransfer.ContragentRegistrationCountry = tvContragentRegisterCountry.getText().toString();
            } else {
                SwiftTransfer.ContragentCountry = country.AlphaCode;
                SwiftTransfer.ContragentRegistrationCountry = countryRegistration.AlphaCode;
                Log.i("Swift = ", "ContragentCountry " + SwiftTransfer.ContragentCountry);
                Log.i("Swift = ", "ContragentRegistrationCountry " + SwiftTransfer.ContragentRegistrationCountry);
            }
            SwiftTransfer.ContragentAddress = edContragentAddress.getText().toString();
            SwiftTransfer.contragentIdn = edTvReceiverIIN.getText().toString();
            SwiftTransfer.ContragentAccountNumber = edSpinnerTo.getText().toString();
            SwiftTransfer.contragentBic = tvSpinnerBIC.getText().toString();
            SwiftTransfer.ContragentBicName = tvReceiversBankName.getText().toString();
            SwiftTransfer.ContragentBankAccountNumber = edReceiversBankCorAccount.getText().toString();
            SwiftTransfer.operationKnp = tvSpinnerKNP.getText().toString();
            Log.i("Swift = ", "operationKnp " + SwiftTransfer.operationKnp);
            SwiftTransfer.purpose = edPurpose.getText().toString();
            String sum = edAmount.getText().toString();
            sum = sum.replaceAll(",", ".");
            sum = sum.replaceAll("\\s", "");
            SwiftTransfer.amount = Double.parseDouble(sum);
            SwiftTransfer.currency = accountFrom.currency;
            SwiftTransfer.InnerOperation = null;
            SwiftTransfer.ChargesType = mChargeType;
            SwiftTransfer.PayerName = edPayerName.getText().toString();
            SwiftTransfer.PayerCountry = SENDER_COUNTRY;
            SwiftTransfer.PayerAddress = edPayerAddress.getText().toString();
            if (mediatorBank) {
                SwiftTransfer.IntermediaryName = tvMediatorBankName.getText().toString();
                SwiftTransfer.IntermediaryBic = tvMediatorBIC.getText().toString();
            }
            SwiftTransfer.requestId = requestId;
            transfer.checkSwift(requireContext(), getFieldNamesAndValues(SwiftTransfer));
        }
    }

    private void confirmRequest() {
        SwiftTransfer.feeAmount = Double.parseDouble(response.feeAmount);
        SwiftTransfer.feeCurrency = response.feeCurrency;
        transfer.confirmSwift(requireContext(), getFieldNamesAndValues(SwiftTransfer));
    }

    private void checkFieldsKnpAndInn(){
        if (accountFrom != null && !tvSpinnerBIC.getText().toString().isEmpty()) {
            // для рублей и тенге поля КНП и ИНН обязательны
            if (accountFrom.currency.equalsIgnoreCase(RUB) || accountFrom.currency.equalsIgnoreCase(KZT)) {
                requireKnpAndInnFields();
            }

            //  валюте доллар если банк посредник АТФ банк, то поля КНП и ИНН обязательны
            if (accountFrom.currency.equalsIgnoreCase(USD)) {
                if (tvMediatorBIC.getText().toString().equalsIgnoreCase(ATF_BANK_BIC) || tvMediatorBIC.getText().toString().equalsIgnoreCase(ATF_BANK_BIC_JSC)) {
                    requireKnpAndInnFields();
                }
            }
        }
    }

    private void requireKnpAndInnFields(){
        // поле КНП
        if (tvSpinnerKNP.getText().toString().isEmpty()) {
            tvSpinnerKNP.setError(getString(R.string._t_fill_) + " " + getString(R.string.text_knp_code));
            tvSpinnerKNP.setFocusable(true);
            tvSpinnerKNP.setFocusableInTouchMode(true);
            tvSpinnerKNP.requestFocus();
            isError = false;
        } else {
            tvSpinnerKNP.setError(null);
        }

        // поле ИНН получателя
        if (edTvReceiverIIN.getText().toString().isEmpty()) {
            edTvReceiverIIN.setError(getString(R.string._t_fill_) + " " + getString(R.string.text_receiver_iin));
            edTvReceiverIIN.requestFocus();
            isError = false;
        } else {
            edTvReceiverIIN.setError(null);
        }
    }

    public void actionBack() {
        btnTransfer.setText(getResources().getString(R.string.send_transfer));
        btnTransfer.setOnClickListener(this);
        isBackPressed = true;
        enableDisableView(linMain, true);
        linFee.setVisibility(View.GONE);
        tvFee.setText("");
        tvSumWithFee.setText("");
        SwiftTransfer = null;
        response = null;
    }

    public boolean isAmountWithFeeExceedsBalance(double amount, double feeAmount) {
        mTotalAmount = amount + feeAmount;
        return accountFrom.balance < mTotalAmount;
    }

    @Override
    public void onCheckSwiftResponse(int statusCode, CheckResponse response, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            amountWithDot = edAmount.getText().toString().replace(",", ".").replaceAll(" ", "");
            double fee = Double.parseDouble(response.feeAmount.replaceAll(" ", ""));
            double amount = Double.parseDouble(amountWithDot);
            this.response = response;
            isBackPressed = false;
            enableDisableView(linMain, false);
            linFee.setVisibility(View.VISIBLE);
            tvFee.setText(getFormattedBalance(getDoubleType(response.feeAmount), response.feeCurrency));
            tvSumWithFee.setText(getFormattedBalance(getDoubleType(edAmount.getText().toString()), accountFrom.currency));
            btnTransfer.setText(getResources().getString(R.string.transfer_confirm));
            btnTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {// TODO: 26.02.2021
                    if (isAmountWithFeeExceedsBalance(amount, fee)) {
                        onError(getString(R.string.error_large_sum));
                    } else {
                        if (response.isNeedSmsConfirmation) {
                            smsWithText.sendSmsWithText(requireContext(), Constants.TRANSFER_OTP_KEY, amountWithDot + " " + currency, operationCode);
                        } else {
                            confirmRequest();
                        }
                    }
                }
            });
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void onConfirmSwiftResponse(int statusCode, TransferConfirmResponse response, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            double sumWithAmount = getDoubleType(edAmount.getText().toString());
            Intent intent = new Intent(requireContext(), SmsConfirmActivity.class);
            intent.putExtra("isSuccess", true);
            intent.putExtra("isTemplate", isTemplate);
            intent.putExtra("isTransfer", true);
            intent.putExtra("feeWithAmount", sumWithAmount);
            intent.putExtra("amount", getDoubleType(dotAndComma(edAmount.getText().toString())));
            intent.putExtra("operationCurrency", currency);
            intent.putExtra("isSwift", true);
            startActivity(intent);
            requireActivity().finish();
        } else {
            onError(errorMessage);
        }
    }

    @Override
    public void onSmsTextResponse(int statusCode, String errorMessage, Integer errorCode) {
        if (statusCode == Constants.SUCCESS) {
            String alphaCode = "";
            String contragentCountry = "";
            Intent intent = new Intent(requireActivity(), SmsConfirmActivity.class);
            intent.putExtra("isTransferInterBankSwift", true);
            intent.putExtra("amount", getDoubleType(amountWithDot));   //withDot
            intent.putExtra("ContragentBic", tvSpinnerBIC.getText().toString()); //ok
            intent.putExtra("Purpose", edPurpose.getText().toString());//ok
            intent.putExtra("PayerName", edPayerName.getText().toString());//ok
            intent.putExtra("AccountCode", accountFrom.getCode());//ok
            intent.putExtra("OperationKnp", tvSpinnerKNP.getText().toString());//ok
            intent.putExtra("ProductType", 120); //ok
            intent.putExtra("ContragentBankAccountNumber", edReceiversBankCorAccount.getText().toString()); //need check
            intent.putExtra("IntermediaryName", tvMediatorBankName.getText().toString()); //ok
            intent.putExtra("Type", 2); //nope
            intent.putExtra("ContragentBicName", tvReceiversBankName.getText().toString());//ok
            intent.putExtra("ContragentName", etReceiverName.getText().toString());//ok
            intent.putExtra("ContragentCountry", contragentCountry = tvTitle.getText().toString().equals(getString(R.string.transfer_swift)) ? country.AlphaCode : tvContragentCountry.getText().toString());//ok
            intent.putExtra("ContragentAccountNumber", edSpinnerTo.getText().toString()); //ok
            intent.putExtra("FeeAmount", Double.parseDouble(response.feeAmount));  //nope
            intent.putExtra("ContragentAddress", edContragentAddress.getText().toString()); //ok
            intent.putExtra("IntermediaryBic", tvMediatorBIC.getText().toString()); //ok
            intent.putExtra("ContragentKpp", edKpp.getVisibility() == View.VISIBLE ? edKpp.getText().toString() : "");//ok
            intent.putExtra("AccountId", ((UserAccounts.CheckingAccounts) accountFrom).id);
            intent.putExtra("FeeCurrency", response.feeCurrency);
            intent.putExtra("Currency", response.feeCurrency);
            intent.putExtra("ContragentRegistrationCountry", alphaCode = tvTitle.getText().toString().equals(getString(R.string.transfer_swift)) ? countryRegistration.AlphaCode : tvContragentRegisterCountry.getText().toString());
            intent.putExtra("ContragentIdn", edTvReceiverIIN.getText().toString());
            intent.putExtra("ChargesType", mChargeType);
            intent.putExtra("PayerAddress", edPayerAddress.getText().toString());
            intent.putExtra("totalAmount", String.valueOf(amountWithDot));
            intent.putExtra("operationCode", operationCode);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    private class CustomTextWatcher implements TextWatcher {
        private TextView mEditText;

        public CustomTextWatcher(TextView e) {
            mEditText = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEditText.setError(null);
        }

        public void afterTextChanged(Editable s) {
            if (mEditText == edAmount) {
                if (isChange) {
                    isChange = false;
                    String dotAndCommaFormattedString = Utilities.dotAndComma(s.toString());
                    Utilities.amountFormat(dotAndCommaFormattedString, edAmount);
                } else {
                    isChange = true;
                }
            }
        }
    }
}
