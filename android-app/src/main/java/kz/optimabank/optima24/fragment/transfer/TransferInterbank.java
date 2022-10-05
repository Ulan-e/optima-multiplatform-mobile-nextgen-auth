package kz.optimabank.optima24.fragment.transfer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SelectParameterActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.Transfers;
import kz.optimabank.optima24.model.service.SmsWithTextImpl;
import kz.optimabank.optima24.model.service.TransferImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.activity.TransfersActivity.TRANSFER_NAME;
import static kz.optimabank.optima24.utility.Constants.ACCOUNT_KEY;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.DICTIONARY_KEY;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_BIC_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_KNP_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;
import static kz.optimabank.optima24.utility.Constants.TRANSFER_INTERBANK_TYPE_CODE;
import static kz.optimabank.optima24.utility.Constants.TRANSFER_OTP_KEY;
import static kz.optimabank.optima24.utility.Utilities.enableDisableView;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;
import static kz.optimabank.optima24.utility.Utilities.getLayoutParamsForImageSize;

/**
 * Created by Timur on 11.05.2017.
 */

public class TransferInterbank extends ATFFragment implements View.OnClickListener, TransferImpl.CallbackTransfer, TextWatcher, TransferImpl.CallbackConfirm, SmsWithTextImpl.SmsSendWithTextCallback {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public @BindView(R.id.tvCurrency)
    TextView tvCurrency;
    public String currency;
    //    @BindView(R.id.tvSpinnerCitizenship) TextView tvSpinnerCitizenship;
    @BindView(R.id.btnTransfer)
    Button btnTransfer;
    @BindView(R.id.tvTransferType)
    TextView tvTransferType;
    @BindView(R.id.edAmount)
    EditText edAmount;
    @BindView(R.id.etReceiverName)
    EditText etReceiverName;
    @BindView(R.id.edSpinnerTo)
    EditText edSpinnerTo;
    @BindView(R.id.linMain)
    LinearLayout linMain;
    @BindView(R.id.tv_multi_from)
    TextView tvMultiFrom;

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

    //field
    @BindView(R.id.linTransferType)
    LinearLayout linTransferType;
    @BindView(R.id.linSpinnerBIC)
    LinearLayout linSpinnerBIC;
    @BindView(R.id.linSpinnerKNP)
    LinearLayout linSpinnerKNP;
    @BindView(R.id.tvSpinnerKNP)
    TextView tvSpinnerKNP;
    @BindView(R.id.tvSpinnerBIC)
    TextView tvSpinnerBIC;
    @BindView(R.id.edPurpose)
    EditText edPurpose;

    @BindView(R.id.edTemplateName)
    EditText edTemplateName;
    //lin fee
    @BindView(R.id.fee_linear_wrapper)
    LinearLayout feeLinear;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.tvSumWithFee)
    TextView tvSumWithFee;

    @BindView(R.id.auto_layout_wrapper)
    LinearLayout autoLayoutWrapper;

    @BindView(R.id.timeBegin_linear)
    LinearLayout linTimeBegin;
    @BindView(R.id.regular_pay_time_linear)
    LinearLayout linRePayTime;
    @BindView(R.id.repeat_pay_linear)
    LinearLayout linRepeat;

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

    public UserAccounts accountFrom;
    Transfers transfer;
    Dictionary knp;
    Dictionary bic;
    CheckResponse response;
    ArrayList<String> stringList = new ArrayList<>();
    BodyModel.Mt100Transfer mt100TransferBody;
    boolean isResident, isTemplate;
    boolean isCreate, isChange = true, isBackPressed = true;
    int transferTypeCode = 0;
    SmsWithTextImpl smsWithText;
    private Double mTotalAmount;
    String operationCode = String.valueOf(System.currentTimeMillis());
    private long requestId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestId = System.currentTimeMillis();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_interbank, container, false);
        ButterKnife.bind(this, view);
        smsWithText = new SmsWithTextImpl();
        smsWithText.registerSmsWithTextCallBack(this);
        Utilities.setRobotoTypeFaceToTextView(requireContext(), tvCurrency);
        linSpinnerFrom.setOnClickListener(this);
        btnTransfer.setOnClickListener(this);
        linSpinnerKNP.setOnClickListener(this);
        tvSpinnerKNP.setOnClickListener(this);
        linSpinnerBIC.setOnClickListener(this);
        tvSpinnerBIC.setOnClickListener(this);
        edAmount.addTextChangedListener(this);
        edAmount.setFilters(new InputFilter[]{new Utilities.DecimalDigitsInputFilter(2)});
        linTransferType.setOnClickListener(this);
        tvTransferType.setOnClickListener(this);
        isCreate = true;
        autoLayoutWrapper.setVisibility(View.GONE);
        InputFilter[] editFilters = edSpinnerTo.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        edSpinnerTo.setFilters(newFilters);
        edSpinnerTo.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[a-zA-Z0-9]+")) {
                            return src;
                        }
                        return "";
                    }
                }, new InputFilter.AllCaps(), new InputFilter.LengthFilter(22)
        });

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
        transfer = new TransferImpl();
        transfer.registerCallbackInterbank(this);
        transfer.registerCallbackConfirm(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linSpinnerFrom:
                intent = new Intent(requireContext(), SelectAccountActivity.class);
                intent.putExtra("checkOnlyKZT", true);
                startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
                break;
            case R.id.linSpinnerKNP:
            case R.id.tvSpinnerKNP:
                intent = new Intent(requireContext(), SelectParameterActivity.class);
                intent.putExtra("parameterName", getResources().getString(R.string.penalty_knp));
                startActivityForResult(intent, SELECT_KNP_REQUEST_CODE);
                break;
            case R.id.linSpinnerBIC:
            case R.id.tvSpinnerBIC:
                intent = new Intent(requireContext(), SelectParameterActivity.class);
                intent.putExtra("parameterName", getResources().getString(R.string.text_bic));
                startActivityForResult(intent, SELECT_BIC_REQUEST_CODE);
                break;
            case R.id.btnTransfer:
                if (checkField() && isBackPressed) {
                    transfer.checkMt100Transfer(requireContext(), getBody());
                }
                break;
            case R.id.linTransferType:
            case R.id.tvTransferType:
                stringList.clear();
                stringList.add(getResources().getString(R.string.transfer_type_cleering));
                stringList.add(getResources().getString(R.string.transfer_type_gross));
                intent = new Intent(requireContext(), SelectAccountActivity.class);
                intent.putExtra("isTransferType", true);
                intent.putExtra("transferTypeList", stringList);
                startActivityForResult(intent, TRANSFER_INTERBANK_TYPE_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CommonStatusCodes.SUCCESS) {
            if (data != null) {
                if (requestCode == SELECT_ACCOUNT_FROM_REQUEST_CODE) {
                    tvSpinnerFrom.setError(null);
                    accountFrom = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                    setAccountSpinnerFrom(accountFrom);
                    currency = accountFrom.currency;
                    tvCurrency.setText(accountFrom.currency);
                } else if (requestCode == SELECT_KNP_REQUEST_CODE) {
                    knp = (Dictionary) data.getSerializableExtra(DICTIONARY_KEY);
                    tvSpinnerKNP.setError(null);
                    try {
                        tvSpinnerKNP.setText(knp.getCode() + " - " + knp.getDescription());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == SELECT_BIC_REQUEST_CODE) {
                    bic = (Dictionary) data.getSerializableExtra(DICTIONARY_KEY);
                    tvSpinnerBIC.setError(null);
                    tvSpinnerBIC.setText(bic.getDescription());
                } else if (requestCode == TRANSFER_INTERBANK_TYPE_CODE) {
                    String transferType = (String) data.getSerializableExtra(STRING_KEY);
                    tvTransferType.setText(transferType);
                    tvTransferType.setError(null);
                    if (transferType.equals(getResources().getString(R.string.transfer_type_cleering))) {
                        transferTypeCode = 1;
                    } else if (transferType.equals(getResources().getString(R.string.transfer_type_gross))) {
                        transferTypeCode = 2;
                    }
                }
            }
        }
    }

    @Override
    public void checkTransferResponse(int statusCode, CheckResponse response, String errorMessage) {
        if (statusCode == 0 && response != null) {
            double fee = getDoubleType(response.feeAmount);
            double amount = getDoubleType(edAmount.getText().toString());
            this.response = response;
            isBackPressed = false;
            enableDisableView(linMain, false);
            feeLinear.setVisibility(View.VISIBLE);
            tvFee.setText(getFormattedBalance(getDoubleType(response.feeAmount), response.feeCurrency));
            tvSumWithFee.setText(getFormattedBalance(getDoubleType(response.feeAmount) + getDoubleType(edAmount.getText().toString()), response.feeCurrency));
            btnTransfer.setText(getResources().getString(R.string.transfer_confirm));

            btnTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAmountWithFeeExceedsBalance(amount, fee)) {
                        onError(getString(R.string.error_large_sum));
                    } else {
                        if (response.isNeedSmsConfirmation) {
                            smsWithText.sendSmsWithText(requireContext(), TRANSFER_OTP_KEY, amount + " " + currency, operationCode);
                        } else {
                            transfer.confirmMt100Transfer(requireContext(), getBody());
                        }
                    }
                }
            });
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    public boolean isAmountWithFeeExceedsBalance(double amount, double feeAmount) {
        mTotalAmount = amount + feeAmount;
        return accountFrom.balance < mTotalAmount;
    }

    public void initToolbar() {
        if (getArguments() != null) {
            String categoryName = "";
            try {
                categoryName = getArguments().getString(TRANSFER_NAME).replaceAll("\n", " ");
            } catch (Exception ignored) {
            }
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

    public boolean checkField() {
        boolean success = true;
        ArrayList<TextView> fields = new ArrayList<>();
        fields.add(edPurpose);
        fields.add(tvSpinnerKNP);
        fields.add(tvSpinnerBIC);
        fields.add(etReceiverName);
        fields.add(edSpinnerTo);
        fields.add(edAmount);
        fields.add(tvTransferType);

        if (accountFrom == null) {
            tvSpinnerFrom.setError(getResources().getString(R.string.error_empty));
            tvSpinnerFrom.requestFocus();
            success = false;
            return success;
        }

        for (TextView view : fields) {
            if (view.getText().toString().isEmpty()) {
                view.setError(getResources().getString(R.string.error_empty));
                view.requestFocus();
                success = false;
            }
        }

        if (edAmount.getText().toString().startsWith(".") || edAmount.getText().toString().startsWith(",")) {
            edAmount.setError(getResources().getString(R.string.error_wrong_format));
            edAmount.requestFocus();
            return false;
        }

        if (!edAmount.getText().toString().isEmpty() && accountFrom.balance < getDoubleType(edAmount.getText().toString()) &&
                accountFrom.currency.equalsIgnoreCase(currency)) {
            onError(getResources().getString(R.string.error_large_sum));
            return false;
        }

        if (tvSpinnerFrom.isShown()) {
            tvSpinnerFrom.setError(getResources().getString(R.string.error_empty));
            tvSpinnerFrom.requestFocus();
            success = false;
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
        return success;
    }

    public void setAccountSpinnerFrom(UserAccounts userAccounts) {
        if (userAccounts != null) {
            tvSpinnerFrom.setVisibility(View.GONE);
            linFromAccountInfo.setVisibility(View.VISIBLE);
            tvStatusFrom.setVisibility(View.GONE);
            tvFromAccountName.setText(userAccounts.name);
            tvFromAccountBalance.setText(userAccounts.getFormattedBalance(requireContext()));
            tvFromAccountNumber.setText(userAccounts.number);

            tvMultiFrom.setVisibility(View.GONE);
            if (userAccounts instanceof UserAccounts.Cards) {
                UserAccounts.Cards userCard = (UserAccounts.Cards) userAccounts;

                tvStatusFrom.setVisibility(View.GONE);
                byte[] min = userCard.getByteArrayMiniatureImg();
                if (min != null)
                    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, BitmapFactory.decodeByteArray(min, 0, min.length));//card.miniatureIm
                else
                    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left));

            } else if (userAccounts instanceof UserAccounts.DepositAccounts) {
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override
    public void afterTextChanged(Editable s) {
        if (isChange) {
            isChange = false;
            String dotAndCommaFormattedString = Utilities.dotAndComma(s.toString());
            Utilities.amountFormat(dotAndCommaFormattedString, edAmount);
        } else {
            isChange = true;
        }
    }

    @Override
    public void onConfirmTransferResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            Intent intent = new Intent(requireContext(), SmsConfirmActivity.class);
            double sumWithAmount = getDoubleType(response.feeAmount) + getDoubleType(edAmount.getText().toString());
            intent.putExtra("isTransfer", true);
            intent.putExtra("isSuccess", true);
            intent.putExtra("isTemplate", isTemplate);
            intent.putExtra("operationCurrency", currency);
            intent.putExtra("feeWithAmount", sumWithAmount);
            intent.putExtra("amount", getDoubleType(edAmount.getText().toString()));
            startActivity(intent);
            requireActivity().finish();
        } else {
            onError(errorMessage);
        }
    }

    public void actionBack() {
        btnTransfer.setText(getResources().getString(R.string.send_transfer));
        btnTransfer.setOnClickListener(this);
        isBackPressed = true;
        enableDisableView(linMain, true);
        feeLinear.setVisibility(View.GONE);
        tvFee.setText("");
        tvSumWithFee.setText("");
        mt100TransferBody = null;
        response = null;
    }

    public JSONObject getBody() {
        mt100TransferBody = new BodyModel.Mt100Transfer();
        if (accountFrom instanceof UserAccounts.Cards) {
            mt100TransferBody.accountCode = ((UserAccounts.Cards) accountFrom).cardAccountCode;
        } else {
            mt100TransferBody.accountCode = accountFrom.code;
            mt100TransferBody.accountNumber = accountFrom.number;
            mt100TransferBody.amount = getDoubleType(edAmount.getText().toString()).toString();
            mt100TransferBody.productType = Constants.TransferMoneyToAnotherBank;
            mt100TransferBody.currency = currency;
            mt100TransferBody.type = 1;
            mt100TransferBody.contragentName = etReceiverName.getText().toString();
            mt100TransferBody.contragentAccountNumber = edSpinnerTo.getText().toString();
            mt100TransferBody.purpose = edPurpose.getText().toString();
            mt100TransferBody.contragentResident = isResident;
            mt100TransferBody.transferType = transferTypeCode;
        }
        if (response != null) {
            mt100TransferBody.feeAmount = response.feeAmount;
            mt100TransferBody.feeCurrency = response.feeCurrency;
        }
        if (knp != null) {
            mt100TransferBody.operationKnp = knp.getCode();
        }
        if (bic != null) {
            mt100TransferBody.contragentBic = bic.getCode();
        }
        mt100TransferBody.requestId = requestId;
        return getFieldNamesAndValues(mt100TransferBody);
    }


    @Override
    public void onSmsTextResponse(int statusCode, String errorMessage, Integer errorCode) {
        if (statusCode == Constants.SUCCESS) {
            Intent intent = new Intent(requireContext(), SmsConfirmActivity.class);
            intent.putExtra("isTransferInterBank", true);
            intent.putExtra("amount", getDoubleType(edAmount.getText().toString()).toString());
            intent.putExtra("ContragentAccountNumber", edSpinnerTo.getText().toString());
            intent.putExtra("ProductType", Constants.TransferMoneyToAnotherBank);
            intent.putExtra("FeeCurrency", response.feeCurrency);
            intent.putExtra("ContragentCardBrandType", ((UserAccounts.CheckingAccounts) accountFrom).accountType);
            intent.putExtra("ContragentName", etReceiverName.getText().toString());
            intent.putExtra("OperationKnp", knp.getCode());
            intent.putExtra("Purpose", edPurpose.getText().toString());
            intent.putExtra("Currency", currency);
            intent.putExtra("ContragentBic", bic.getCode());
            intent.putExtra("TransferType", transferTypeCode);
            intent.putExtra("FeeAmount", response.feeAmount);
            intent.putExtra("AccountCode", accountFrom.code);
            intent.putExtra("Type", 1);
            intent.putExtra("AccountId", ((UserAccounts.CheckingAccounts) accountFrom).id);
            intent.putExtra("operationCode", operationCode);
            startActivity(intent);
            requireActivity().finish();
        }
    }
}
