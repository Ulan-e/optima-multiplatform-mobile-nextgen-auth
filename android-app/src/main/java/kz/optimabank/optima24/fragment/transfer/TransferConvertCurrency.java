package kz.optimabank.optima24.fragment.transfer;

import static kz.optimabank.optima24.activity.TransfersActivity.TRANSFER_NAME;
import static kz.optimabank.optima24.activity.TransfersActivity.USER_ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_TO_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.TransferConversionInsideMulticurrencyCard;
import static kz.optimabank.optima24.utility.Utilities.enableDisableView;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;
import static kz.optimabank.optima24.utility.Utilities.isNumeric;
import static kz.optimabank.optima24.utility.Utilities.setRobotoTypeFaceToTextView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.Rate;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.AccStatusResponse;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.Rates;
import kz.optimabank.optima24.model.interfaces.Transfers;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.RatesImpl;
import kz.optimabank.optima24.model.service.TransferImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

/**
 * Created by Timur on 06.09.2016.
 */
public class TransferConvertCurrency extends ATFFragment implements View.OnClickListener,
        RatesImpl.Callback, TextWatcher, TransferImpl.Callback, TransferImpl.CallbackConfirm
        /*, SmsSendImpl.Callback*/ {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnTransfer)
    Button btnTransfer;
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
    @BindView(R.id.tvCurrencyFrom)
    TextView tvCurrencyFrom;
    @BindView(R.id.edAmountFrom)
    EditText edAmountFrom;

    //account spinner to
    @BindView(R.id.linSpinnerTo)
    LinearLayout linSpinnerTo;
    @BindView(R.id.linToAccountInfo)
    LinearLayout linToAccountInfo;
    @BindView(R.id.tvSpinnerTo)
    TextView tvSpinnerTo;
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
    @BindView(R.id.edAmountTo)
    EditText edAmountTo;
    @BindView(R.id.tvCurrencyTo)
    TextView tvCurrencyTo;

    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvDate)
    TextView tvDate;

    //fee
//    @BindView(R.id.linFee) LinearLayout linFee;
    @BindView(R.id.linFeeConvert)
    LinearLayout linFeeConvert;
    //    @BindView(R.id.tvFee) TextView tvFee;
    @BindView(R.id.tvFeeConvert)
    TextView tvFeeConvert;
//    @BindView(R.id.tvSumWithFee) TextView tvSumWithFee;

    Rates rates;
    UserAccounts.Cards card;
    String categoryName, fee, feeCurrency;
    UserAccounts.Cards.MultiBalanceList multiBalanceItemFrom, multiBalanceItemTo;
    float sum, sellRateTo, buyRateFrom;
    double sumWithAmount;
    //    public SmsSend smsSend;
    Transfers transfer;
    BodyModel.Mt100Transfer mt100TransferBody;
    boolean isBackPressed = true, isEdChange = true;

    private long requestId;
    private static final int PASTE_TEXT_LIMITATION = 45;
    private static final int LAST_CHARACTERS_LENGTH = 2;
    private static final int START_SUBSTRING_FOR_LAST_CHARACTERS = 0;
    private static final int END_SUBSTRING_FOR_LAST_CHARACTERS = 3;
    private static final int LENGTH_OF_SPLIT_COMMA_DOT = 1;
    private static final int LAST_CHARACTERS_SPLIT_START = 1;
    private static final int FIRST_SYMBOL = 1;
    private static final int LENGTH_IS_ZERO = 0;
    private static final String REPLACE_INCORRECT_SYMBOLS = "x";


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_transfer_convert_currency,
                container,
                false);
        ButterKnife.bind(this, view);
        requestId = System.currentTimeMillis();

        getBundle();
        initToolbar();
        ratesRequest();
        linSpinnerFrom.setOnClickListener(this);
        linSpinnerTo.setOnClickListener(this);
        btnTransfer.setOnClickListener(this);
//        smsSend = new SmsSendImpl();
//        smsSend.registerSmsCallBack(this);   //#8236
        edAmountFrom.addTextChangedListener(this);
        edAmountTo.addTextChangedListener(this);
        edAmountFrom.setFilters(new InputFilter[]{new Utilities.DecimalDigitsInputFilter(2)});
        edAmountTo.setFilters(new InputFilter[]{new Utilities.DecimalDigitsInputFilter(2)});
        setRobotoTypeFaceToTextView(getContext(), tvCurrencyFrom);
        setRobotoTypeFaceToTextView(getContext(), tvRate);
        setRobotoTypeFaceToTextView(getContext(), tvFromAccountBalance);
        setRobotoTypeFaceToTextView(getContext(), tvToAccountBalance);
        setRobotoTypeFaceToTextView(getContext(), tvCurrencyTo);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        transfer = new TransferImpl();
        transfer.registerCallBack(this);
        Log.d(TAG, "card.multiBalanceList = " + card.multiBalanceList.get(0));
        if (card.multiBalanceList != null && !card.multiBalanceList.isEmpty()) {
            setMultiBalanceItem(card.multiBalanceList.get(0), card.multiBalanceList.get(1));

            setAccountSpinnerFrom(card, multiBalanceItemFrom.amount, multiBalanceItemFrom.currency);

            setAccountSpinnerTo(card, multiBalanceItemTo.amount, multiBalanceItemTo.currency);
//            tvCurrencyFrom.setText(getCurrencyBadge(getContext(), multiBalanceItemFrom.currency));
            tvCurrencyFrom.setText(multiBalanceItemFrom.currency);
//            tvCurrencyTo.setText(getCurrencyBadge(getContext(), multiBalanceItemTo.currency));
            tvCurrencyTo.setText(multiBalanceItemTo.currency);

            if (getView() != null) {
                getView().setFocusableInTouchMode(true);
                getView().requestFocus();
                Log.d("TAG", "getView() != null");
                getView().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int keyCode, KeyEvent event) {
                        Log.d("TAG", "event.getAction() = " + event.getAction());
                        if (event.getAction() == KeyEvent.ACTION_UP
                                && keyCode == KeyEvent.KEYCODE_BACK) {
//                            if(isBackPressed) {
                            getActivity().onBackPressed();
//                            } else {
//                                actionBack();
//                            }
                            return true;
                        }
                        return false;
                    }
                });
            } else {
                Log.d("TAG", "getView() == null");
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linSpinnerFrom:
                intent = new Intent(getActivity(), NavigationActivity.class);
                intent.putExtra("isConvertCurrency", true);
                intent.putExtra("convertCard", card);
                startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
                break;
            case R.id.linSpinnerTo:
                intent = new Intent(getActivity(), NavigationActivity.class);
                intent.putExtra("isConvertCurrency", true);
                intent.putExtra("convertCard", card);
                intent.putExtra("selectedFromCurrency", multiBalanceItemFrom.currency);
                startActivityForResult(intent, SELECT_ACCOUNT_TO_REQUEST_CODE);
                break;
            case R.id.btnTransfer:
                if (edAmountFrom.getText().toString().isEmpty()) {
                    edAmountFrom.setError(getString(R.string.error_empty));
                } else if (multiBalanceItemFrom.amount < getDoubleType(edAmountFrom.getText()
                        .toString())) {
                    onError(getResources().getString(R.string.error_large_sum));
                } else {
//                    Log.d(TAG,"getBody() = " + getBody());
//                    transfer.checkMt100Transfer(getActivity(), getBody());
                    if (isBackPressed) {
                        transfer.checkMt100Transfer(getActivity(), getBody());
                    } else {
                        transfer.registerCallbackConfirm(this);
                        transfer.confirmMt100Transfer(getActivity(), getBody());
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CommonStatusCodes.SUCCESS && data != null) {
            if (requestCode == SELECT_ACCOUNT_FROM_REQUEST_CODE) {
                UserAccounts.Cards.MultiBalanceList selectedMultiBalance =
                        (UserAccounts.Cards.MultiBalanceList)
                                data.getSerializableExtra("multiBalanceItem");
                if (selectedMultiBalance.currency.equals(multiBalanceItemTo.currency)) {
                    multiBalanceItemTo = multiBalanceItemFrom;
                    tvCurrencyTo.setText(multiBalanceItemTo.currency);
                    setAccountSpinnerTo(
                            card,
                            multiBalanceItemTo.amount,
                            multiBalanceItemTo.currency);
                }
                multiBalanceItemFrom = selectedMultiBalance;
                setAccountSpinnerFrom(card,
                        multiBalanceItemFrom.amount,
                        multiBalanceItemFrom.currency);

                tvCurrencyFrom.setText(multiBalanceItemFrom.currency);
                setRateInfo();
                changeAmountEditTextFrom(edAmountFrom, isEdChange, multiBalanceItemFrom.currency, edAmountTo);
            } else if (requestCode == SELECT_ACCOUNT_TO_REQUEST_CODE) {
                multiBalanceItemTo = (UserAccounts.Cards.MultiBalanceList) data.getSerializableExtra("multiBalanceItem");
                setAccountSpinnerTo(card, multiBalanceItemTo.amount, multiBalanceItemTo.currency);
                tvCurrencyTo.setText(multiBalanceItemTo.currency);
                setRateInfo();
                changeAmountEditTextTo(edAmountTo, isEdChange, multiBalanceItemTo.currency, edAmountFrom);
            }
        }
    }

    @Override
    public void checkMt100TransferResponse(int statusCode, CheckResponse response, String errorMessage) {
        if (statusCode == 0 && response != null) {
            sumWithAmount = getDoubleType(response.feeAmount) + getDoubleType(edAmountFrom.getText().toString());
            isBackPressed = false;
            enableDisableView(linMain, false);
            fee = response.feeAmount;
            feeCurrency = response.feeCurrency;
            btnTransfer.setText(getResources().getString(R.string.transfer_confirm));
            linFeeConvert.setVisibility(View.VISIBLE);
            String fee = response.feeAmount + response.feeCurrency.replace("KZT", getString(R.string.tenge_icon)).replace("USD", getString(R.string.USD))
                    .replace("EUR", getString(R.string.EUR)).replace("RUB", getString(R.string.RUB)).replace("GBP", getString(R.string.GBP))
                    .replace("KGS", getString(R.string.KGS))
                    .replace("CNY", getString(R.string.CNY));
            tvFeeConvert.setText(fee);

        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void onConfirmTransferResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            Intent intent = new Intent(requireContext(), SmsConfirmActivity.class);
            intent.putExtra("isTransfer", true);
            intent.putExtra("isSuccess", true);
            intent.putExtra("isTTF", "1");
            intent.putExtra("isTemplate", true);
            intent.putExtra("feeWithAmount", sumWithAmount);
            intent.putExtra("amount", getDoubleType(edAmountFrom.getText().toString()));
            intent.putExtra("operationCurrency", multiBalanceItemFrom.currency);
            startActivity(intent);
            requireActivity().finish();
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void checkCardBrandResponse(int statusCode, String errorMessage, String cardValue) {
    }

    @Override
    public void getAccDataResponse(int statusCode, String errorMessage, AccStatusResponse cardValue) {
    }

    @Override
    public void mastercardRegisterResponse(int statusCode, String errorMessage) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == edAmountFrom.getText()) {
            if (edAmountFrom.getText().toString().isEmpty()) {
                edAmountTo.getText().clear();
            }
            if (!edAmountFrom.getText().toString().isEmpty()) {

                if (edAmountFrom.getText().length() == FIRST_SYMBOL) {
                    if (edAmountFrom.getText().toString().equals(".")
                            || edAmountFrom.getText().toString().equals(",")) {
                        edAmountFrom.getText().clear();
                    }
                }

                String numericTextWithFirstLetter
                        = s.toString().replaceAll("[^\\d[.,]?]", REPLACE_INCORRECT_SYMBOLS);
                String numericText = s.toString().replaceAll("[^\\d[.,]?]", "");
                String numericTextForParse = numericText.replaceAll(",", ".");

                if (edAmountFrom.getText().length() < PASTE_TEXT_LIMITATION
                        && isNumeric(numericTextForParse)) {
                    String lastCharacters = "";

                    if (numericTextWithFirstLetter.contains(".")) {
                        if (numericTextWithFirstLetter.split("\\.")
                                .length > LENGTH_OF_SPLIT_COMMA_DOT) {
                            lastCharacters = numericTextWithFirstLetter
                                    .split("\\.")[LENGTH_OF_SPLIT_COMMA_DOT]
                                    .trim();
                        }

                        if (!lastCharacters.startsWith(REPLACE_INCORRECT_SYMBOLS)) {

                                if (Double.valueOf(numericTextForParse)
                                        > multiBalanceItemFrom.amount)

                                    edAmountFrom.setText(multiBalanceItemFrom.amount + "");
                                changeAmountEditTextFrom(edAmountFrom,
                                        isEdChange,
                                        multiBalanceItemFrom.currency,
                                        edAmountTo);
                        } else {
                            edAmountFrom.getText().clear();
                        }
                    } else if (numericText.contains(",")) {
                        if (numericTextWithFirstLetter
                                .split(",").length > LENGTH_OF_SPLIT_COMMA_DOT) {
                            lastCharacters = numericTextWithFirstLetter
                                    .split(",")[LENGTH_OF_SPLIT_COMMA_DOT]
                                    .trim();
                        }

                        if (!lastCharacters.startsWith(REPLACE_INCORRECT_SYMBOLS)) {

                                if (Double.valueOf(numericTextForParse)
                                        > multiBalanceItemFrom.amount)
                                    edAmountFrom.setText(multiBalanceItemFrom.amount + "");
                                changeAmountEditTextFrom(edAmountFrom,
                                        isEdChange,
                                        multiBalanceItemFrom.currency,
                                        edAmountTo);
                        } else {
                            edAmountFrom.getText().clear();
                        }
                    } else {

                            if (Double.valueOf(numericTextForParse)
                                    > multiBalanceItemFrom.amount)
                                edAmountFrom.setText(multiBalanceItemFrom.amount + "");
                            changeAmountEditTextFrom(edAmountFrom,
                                    isEdChange,
                                    multiBalanceItemFrom.currency,
                                    edAmountTo);
                    }
                } else {
                    edAmountFrom.setText("");
                    edAmountFrom.getText().clear();
                }
            }
        } else if (s == edAmountTo.getText()) {

            if (edAmountFrom.getText().length() == FIRST_SYMBOL) {
                if (edAmountTo.getText().toString().equals(".")
                        || edAmountTo.getText().toString().equals(",")) {
                    edAmountTo.getText().clear();
                }
            }

            String numericTextWithFirstLetter
                    = s.toString().replaceAll("[^\\d[.,]?]", REPLACE_INCORRECT_SYMBOLS);
            String numericText = s.toString().replaceAll("[^\\d[.,]?]", "");
            String numericTextForParse = numericText.replaceAll(",", ".");

            String lastCharacters = "";

            if (numericTextWithFirstLetter.contains(".")) {
                if (numericTextWithFirstLetter
                        .split("\\.").length > LENGTH_OF_SPLIT_COMMA_DOT) {
                    lastCharacters = numericTextWithFirstLetter
                            .split("\\.")[LENGTH_OF_SPLIT_COMMA_DOT]
                            .trim();
                }

                if (edAmountTo.getText().length() < PASTE_TEXT_LIMITATION
                        && isNumeric(numericTextForParse)) {
                    if (!lastCharacters.startsWith(REPLACE_INCORRECT_SYMBOLS)) {
                            if (!edAmountTo.getText().toString().isEmpty()) {
                                changeAmountEditTextTo(
                                        edAmountTo,
                                        isEdChange,
                                        multiBalanceItemTo.currency,
                                        edAmountFrom);
                            } else {
                                edAmountFrom.getText().clear();
                            }

                    } else {
                        edAmountTo.getText().clear();
                    }
                } else {
                    edAmountTo.getText().clear();
                }

            } else if (numericTextWithFirstLetter.contains(",")) {
                if (numericTextWithFirstLetter
                        .split(",").length > LENGTH_OF_SPLIT_COMMA_DOT) {
                    lastCharacters = numericTextWithFirstLetter
                            .split(",")[LENGTH_OF_SPLIT_COMMA_DOT]
                            .trim();
                }

                if (edAmountTo.getText().length() < PASTE_TEXT_LIMITATION
                        && isNumeric(numericTextForParse)) {
                    if (!lastCharacters.startsWith(REPLACE_INCORRECT_SYMBOLS)) {
                            if (!edAmountTo.getText().toString().isEmpty()) {
                                changeAmountEditTextTo(
                                        edAmountTo,
                                        isEdChange,
                                        multiBalanceItemTo.currency,
                                        edAmountFrom);
                            } else {
                                edAmountFrom.getText().clear();
                            }
                    } else {
                        edAmountTo.getText().clear();
                    }
                } else {
                    edAmountTo.getText().clear();
                }
            } else {

                if (edAmountTo.getText().length() < PASTE_TEXT_LIMITATION
                        && isNumeric(numericTextForParse)) {
                    if (!edAmountTo.getText().toString().isEmpty()) {
                        changeAmountEditTextTo(
                                edAmountTo,
                                isEdChange,
                                multiBalanceItemTo.currency,
                                edAmountFrom);
                    } else {
                        edAmountFrom.getText().clear();
                    }
                }else{
                    edAmountTo.getText().clear();
                }

            }
            //if (Double.valueOf(edAmountTo.getText().toString().replaceAll(",","."))
            //        > multiBalanceItemTo.amount)///косяк в том что multiBalanceItemTo.amount = 0 (хотя переводим мы куда а не откуда)
            //    edAmountTo.setText(multiBalanceItemTo.amount+"");
        }

    }

    /**
     * Метод который в зависимости от содержимого поля {@code nowTypingEditText} меняет второе поле {@code editTextForChange} для конвертации
     *
     * @param nowTypingEditText экземпляр {@code EditText} на котором вводится текст в данный момент
     * @param isChange          флажок для предотвращения переполнения стека {@code StackOverflowError}
     * @param currency          валюта по которому определяется логика конвертирования
     * @param editTextForChange собственно второе изменяеомое поле {@code EditText} для конвертации
     */
    private void changeAmountEditTextFrom(
            EditText nowTypingEditText,
            boolean isChange,
            String currency,
            EditText editTextForChange) {

        if (nowTypingEditText.getText().length() == LENGTH_IS_ZERO) {
            editTextForChange.getText().clear();
            return;
        }
        String amountText;
        String numericText
                = nowTypingEditText.getText()
                .toString().replaceAll("[^\\d[.,]?]", "");
        String numericTextForParse = numericText.replaceAll(",", "\\.");

        float amount = Float.parseFloat(numericTextForParse);

        int indexOfDot;
        int indexOfComma;
        String finalResult = numericText;

        if (numericText.contains(".")) {
            if (numericText.split("\\.").length > LENGTH_OF_SPLIT_COMMA_DOT) {
                String lastCharacters = numericText.split("\\.")[LENGTH_OF_SPLIT_COMMA_DOT]
                        .trim();

                if (lastCharacters.length() > LAST_CHARACTERS_LENGTH) {
                    indexOfDot = numericText.indexOf(".");
                    finalResult = numericText.substring(
                            START_SUBSTRING_FOR_LAST_CHARACTERS,
                            indexOfDot + END_SUBSTRING_FOR_LAST_CHARACTERS);
                }
            }
        } else if (numericText.contains(",")) {
            if (numericText.split(",").length > LENGTH_OF_SPLIT_COMMA_DOT) {
                String lastCharacters = numericText.split(",")[LENGTH_OF_SPLIT_COMMA_DOT]
                        .trim();

                if (lastCharacters.length() > LAST_CHARACTERS_LENGTH) {
                    indexOfComma = numericText.indexOf(",");
                    finalResult = numericText.substring(
                            START_SUBSTRING_FOR_LAST_CHARACTERS,
                            indexOfComma + END_SUBSTRING_FOR_LAST_CHARACTERS);
                }
            }
        }

        if (isChange) {
            this.isEdChange = false;
            nowTypingEditText.setText(Utilities.dotAndComma(finalResult));
            nowTypingEditText.setSelection(nowTypingEditText.length());
        } else {
            this.isEdChange = true;
        }
        if (currency.equals("KGS")) {
            amountText = String.format("%.3f", amount / sum).replace(".", ",");
        } else {
            // костыль
            if (!multiBalanceItemFrom.currency.equals("KGS")
                    && !multiBalanceItemTo.currency.equals("KGS")) {
                if (nowTypingEditText == edAmountTo) {
                    sum = sellRateTo / buyRateFrom;
                } else {
                    sum = buyRateFrom / sellRateTo;
                }
            }
            amountText = String.format("%.3f", amount * sum).replace(".", ",");
        }
        editTextForChange.removeTextChangedListener(this);
        editTextForChange.setText(amountText);
        editTextForChange.setSelection(amountText.length());
        editTextForChange.addTextChangedListener(this);
    }

    private void changeAmountEditTextTo(
            EditText nowTypingEditText,
            boolean isChange,
            String currency,
            EditText editTextForChange) {
        if (nowTypingEditText.getText().length() == LENGTH_IS_ZERO) {
            editTextForChange.getText().clear();
            return;
        }
        String amountText;

        String numericText =
                nowTypingEditText.getText().toString().replaceAll("[^\\d[.,]?]", "");
        String numericTextForParse = numericText.replaceAll(",", "\\.");

        float amount = Float.parseFloat(numericTextForParse);

        int indexOfDot;
        int indexOfComma;
        String finalResult = numericText;

        if (numericText.contains(".")) {
            if (numericText.split("\\.").length > LENGTH_OF_SPLIT_COMMA_DOT) {
                String lastCharacters = numericText.split("\\.")[LENGTH_OF_SPLIT_COMMA_DOT]
                        .trim();

                if (lastCharacters.length() > LAST_CHARACTERS_LENGTH) {
                    indexOfDot = numericText.indexOf(".");
                    finalResult = numericText.substring(START_SUBSTRING_FOR_LAST_CHARACTERS,
                            indexOfDot + END_SUBSTRING_FOR_LAST_CHARACTERS);
                }
            }
        } else if (numericText.contains(",")) {
            if (numericText.split(",").length > LENGTH_OF_SPLIT_COMMA_DOT) {
                String lastCharacters = numericText.split(",")[LENGTH_OF_SPLIT_COMMA_DOT]
                        .trim();

                if (lastCharacters.length() > LAST_CHARACTERS_LENGTH) {
                    indexOfComma = numericText.indexOf(",");
                    finalResult = numericText.substring(
                            START_SUBSTRING_FOR_LAST_CHARACTERS,
                            indexOfComma + END_SUBSTRING_FOR_LAST_CHARACTERS);
                }
            }
        }

        if (isChange) {
            this.isEdChange = false;
            nowTypingEditText.setText(Utilities.dotAndComma(finalResult));
            nowTypingEditText.setSelection(nowTypingEditText.length());
        } else {
            this.isEdChange = true;
        }
        if (currency.equals("KGS")) {
            amountText = String.format("%.3f", amount / sum).replace(".", ",");
        } else {
            // костыль
            if (!multiBalanceItemFrom.currency.equals("KGS")
                    && !multiBalanceItemTo.currency.equals("KGS")) {
                if (nowTypingEditText == edAmountTo) {
                    sum = sellRateTo / buyRateFrom;
                } else {
                    sum = buyRateFrom / sellRateTo;
                }
            }
            amountText = String.format("%.3f", amount * sum).replace(".", ",");
        }
        editTextForChange.removeTextChangedListener(this);
        editTextForChange.setText(amountText);
        editTextForChange.setSelection(amountText.length());

//        if (Double.valueOf(amountText.replaceAll(",","."))
//                > multiBalanceItemFrom.amount) {
//            editTextForChange.setText(multiBalanceItemFrom.amount + "");
//            editTextForChange.setSelection((multiBalanceItemFrom.amount + "").length());
//        }
//        else {
//            editTextForChange.setText(amountText);
//            editTextForChange.setSelection(amountText.length());
//        }

//        if (Double.valueOf(amountText.replaceAll(",", ".")) <= 0) {
//            nowTypingEditText.removeTextChangedListener(this);
//            nowTypingEditText.setText("0.0");
//            nowTypingEditText.setSelection(nowTypingEditText.length());
//            nowTypingEditText.addTextChangedListener(this);
//        } else {
////            ifMaxSetMax(edAmountFrom, multiBalanceItemFrom.currency);
//        }
        editTextForChange.addTextChangedListener(this);
    }

    private void ifMaxSetMax(EditText nowTypingEditText, String currency) {
        String amountText;
        float amount;
        if (!nowTypingEditText.getText().toString().isEmpty()) {
            amount = Float.parseFloat(nowTypingEditText.getText().toString().replaceAll(",", "\\."));
        } else
            amount = 0.0f;

        if (currency.equals("KGS")) {
            amountText = String.format("%.4f", amount / sum).replace(".", ",");
        } else {
            // костыль
            if (!multiBalanceItemFrom.currency.equals("KGS") && !multiBalanceItemTo.currency.equals("KGS")) {
                if (nowTypingEditText == edAmountTo) {
                    sum = sellRateTo / buyRateFrom;
                } else {
                    sum = buyRateFrom / sellRateTo;
                }
            }
            amountText = String.format("%.4f", amount * sum).replace(".", ",");
        }
//        if (amount >= multiBalanceItemFrom.amount &&
//                Double.valueOf(edAmountTo.getText().toString().replaceAll(",",".")) > Double.valueOf(amountText.replaceAll(",","."))) {      // хуйня какая то из АТФа
        edAmountTo.removeTextChangedListener(this);
        edAmountTo.setText(amountText);
        edAmountTo.setSelection(edAmountTo.length());
        edAmountTo.addTextChangedListener(this);
//        }
    }

    @Override
    public void jsonRatesResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            tvDate.setText(getString(R.string.data_time) + " " + Utilities.getCurrentDate("dd.MM.yyyy HH:mm:ss"));
            setRateInfo();
        } else if (statusCode == Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void setRateInfo() {
        String sRate = "";
        for (Rate rate : GeneralManager.getInstance().getRate()) {
            if (rate.type == 0) {
                if (!multiBalanceItemFrom.currency.equals("KGS") && multiBalanceItemTo.currency.equals("KGS")) {
                    if (rate.ForeignCurrency.equals(multiBalanceItemFrom.currency)) {
                        float buyRate = Float.parseFloat(rate.getBuyRate().replace(",", "."));
                        sRate = "1 " + multiBalanceItemFrom.currency + " = "
                                + String.format("%.4f", buyRate) + " " + multiBalanceItemTo.currency;
                        tvRate.setText(sRate);
                        sum = Float.parseFloat(rate.getBuyRate().replace(",", "."));
                    }
                } else {
                    if (multiBalanceItemFrom.currency.equals("KGS")) {
                        if (rate.ForeignCurrency.equals(multiBalanceItemTo.currency)) {
                            float sellRate = Float.parseFloat(rate.getSellRate().replace(",", "."));
                            sRate = "1 " + multiBalanceItemTo.currency + " = " +
                                    String.format("%.4f", sellRate) + " " + multiBalanceItemFrom.currency;
                            sum = Float.parseFloat(rate.getSellRate().replace(",", "."));
                        }
                    } else {
                        for (Rate rateSell : GeneralManager.getInstance().getRate()) {
                            if (rateSell.type == 0) {
                                if (rate.ForeignCurrency.equals(multiBalanceItemFrom.currency) &&
                                        rateSell.ForeignCurrency.equals(multiBalanceItemTo.currency)) {

                                    this.buyRateFrom = Float.parseFloat(rate.getBuyRate().replace(",", "."));
                                    this.sellRateTo = Float.parseFloat(rateSell.getSellRate().replace(",", "."));
                                    sRate = "1 " + multiBalanceItemFrom.currency + " = " +
                                            String.format("%.4f", buyRateFrom / sellRateTo) + " " + multiBalanceItemTo.currency;

                                    sum = buyRateFrom / sellRateTo;
                                }
                            }
                        }
                    }
                    tvRate.setText(sRate);
                }
            }
        }
    }

    private void initToolbar() {
        tvTitle.setText(categoryName);
        toolbar.setTitle("");
        ((TransfersActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(isBackPressed) {
                getActivity().onBackPressed();
//                } else {
//                    actionBack();
//                }
            }
        });
    }

    private void getBundle() {
        if (getArguments() != null) {
            card = (UserAccounts.Cards) getArguments().getSerializable(USER_ACCOUNT);
            Log.d(TAG, "card = " + card);
            categoryName = getArguments().getString(TRANSFER_NAME);
        }
    }

    private void ratesRequest() {
        rates = new RatesImpl();
        rates.registerCallBack(this);
        rates.getRates(getActivity(), true);
    }

    public void setAccountSpinnerFrom(UserAccounts userAccounts, double balance, String currency) {
        if (userAccounts != null) {
            tvSpinnerFrom.setVisibility(View.GONE);
            linFromAccountInfo.setVisibility(View.VISIBLE);
            tvStatusFrom.setVisibility(View.GONE);
            tvFromAccountName.setText(userAccounts.name);
            tvFromAccountBalance.setText(getFormattedBalance(balance, currency));
            tvFromAccountNumber.setText(userAccounts.number);
            if (userAccounts instanceof UserAccounts.Cards) {
                UserAccounts.Cards userCard = (UserAccounts.Cards) userAccounts;
                //Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, userCard.miniatureIm);
                /*try {
                    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, userCard.readObject(false));//card.miniatureIm
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, userCard.miniatureIm);
                }*/
                byte[] min = userCard.getByteArrayMiniatureImg();
                if (min != null)
                    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, BitmapFactory.decodeByteArray(min, 0, min.length));//card.miniatureIm
                else
                    Utilities.setCardToImageView(userCard, imgTypeFrom, tvMultiFrom, BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left));
            }
        }
    }

    public void setAccountSpinnerTo(UserAccounts userAccounts, double balance, String currency) {
        if (userAccounts != null) {
            tvSpinnerTo.setVisibility(View.GONE);
            linToAccountInfo.setVisibility(View.VISIBLE);
            tvStatusTo.setVisibility(View.GONE);
            tvToAccountName.setText(userAccounts.name);
            tvToAccountBalance.setText(getFormattedBalance(balance, currency));
            tvToAccountNumber.setText(userAccounts.number);
            if (userAccounts instanceof UserAccounts.Cards) {
                UserAccounts.Cards userCard = (UserAccounts.Cards) userAccounts;
                //Utilities.setCardToImageView(userCard, imgTypeTo, tvMultiTo, userCard.miniatureIm);
                /*try {
                    Utilities.setCardToImageView(userCard, imgTypeTo, tvMultiTo, userCard.readObject(false));//card.miniatureIm
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    Utilities.setCardToImageView(userCard, imgTypeTo, tvMultiTo, userCard.miniatureIm);
                }*/
                byte[] min = userCard.getByteArrayMiniatureImg();
                if (min != null)
                    Utilities.setCardToImageView(userCard, imgTypeTo, tvMultiTo, BitmapFactory.decodeByteArray(min, 0, min.length));//card.miniatureIm
                else
                    Utilities.setCardToImageView(userCard, imgTypeTo, tvMultiTo, BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left));
            }
        }
    }

    private void setMultiBalanceItem(UserAccounts.Cards.MultiBalanceList multiBalanceItemFrom,
                                     UserAccounts.Cards.MultiBalanceList multiBalanceItemTo) {
        this.multiBalanceItemFrom = multiBalanceItemFrom;
        this.multiBalanceItemTo = multiBalanceItemTo;
    }

    public JSONObject getBody() {
        mt100TransferBody = new BodyModel.Mt100Transfer();
        mt100TransferBody.accountCode = card.code;
        mt100TransferBody.accountNumber = card.number;
        mt100TransferBody.amount = getDoubleType(edAmountFrom.getText().toString()).toString();
        mt100TransferBody.productType = TransferConversionInsideMulticurrencyCard;
        mt100TransferBody.currency = multiBalanceItemFrom.currency;
        mt100TransferBody.type = 1;

        mt100TransferBody.contragentAccountCode = String.valueOf(card.code);
        mt100TransferBody.contragentCurrency = multiBalanceItemTo.currency;

        if (!isBackPressed) {
            mt100TransferBody.feeAmount = fee;
            mt100TransferBody.feeCurrency = feeCurrency;
        }
        mt100TransferBody.requestId = requestId;
        return getFieldNamesAndValues(mt100TransferBody);
    }

    public void actionBack() {
        btnTransfer.setText(getResources().getString(R.string.send_transfer));
        isBackPressed = true;
        enableDisableView(linMain, true);
        fee = null;
        feeCurrency = null;
    }

    /*@Override
    public void jsonSmsResponse(int statusCode, String errorMessage, Integer errorCode) {
        if (statusCode == 200 || errorCode != null && errorCode == -10004) {
            Intent intent = new Intent(getActivity(), SmsConfirmActivity.class);
            intent.putExtra("isTransfer", true);
            intent.putExtra("isSuccess",true);
            intent.putExtra("isTTF","1");
            intent.putExtra("isTemplate",true);
            intent.putExtra("feeWithAmount", sumWithAmount);
            intent.putExtra("amount", getDoubleType(edAmountFrom.getText().toString()));
            intent.putExtra("operationCurrency", multiBalanceItemFrom.currency);
            startActivity(intent);
            getActivity().finish();
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }*/

}
