package kz.optimabank.optima24.fragment.transfer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.snackbar.Snackbar;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import cards.pay.paycardsrecognizer.sdk.Card;
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.databinding.FragmentTransferVisaToVisaBinding;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.AccStatusResponse;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.SmsWithTextSend;
import kz.optimabank.optima24.model.interfaces.Transfers;
import kz.optimabank.optima24.model.service.SmsWithTextImpl;
import kz.optimabank.optima24.model.service.TransferImpl;
import kz.optimabank.optima24.scan.RPSScanCardIntent;
import kz.optimabank.optima24.utility.CardNumberUtils;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.activity.TransfersActivity.TRANSFER_NAME;
import static kz.optimabank.optima24.utility.Constants.ACCOUNT_KEY;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.IS_RECEIPT_TRANSFER;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_FEE;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_FROM;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_TO;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_TO_FULL_NAME;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_OWN_ACCOUNT_TO;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_TO_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_CURRENCY_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;
import static kz.optimabank.optima24.utility.Utilities.getConstraintLayoutParamsForImageSize;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;
import static kz.optimabank.optima24.utility.Utilities.hasInternetConnection;

public class TransferVisaToVisaFragment extends ATFFragment implements
        TextWatcher, TransferImpl.Callback, TransferImpl.CallbackConfirm, SmsWithTextImpl.SmsSendWithTextCallback {

    private static final String TAG_VISA_TO_VISA = "tag_visa_to_visa";
    private static final String REPLENISH = "Replenish";
    private static final String ACTO = "acTo";
    private static final String ACTOO = "acToO";
    private static final String TRANSFER_VISA_TO_VISA = "transferVisaToVisa";
    private static final String CURRENCY = "Currency";
    private static final String LIST_CURRENCY = "listCurrency";
    private static final String IS_TRANSFER = "isTransfer";
    private static final String IS_SUCCESS = "isSuccess";
    private static final String IS_TEMPLATE = "isTemplate";
    private static final String FEE_WITH_AMOUNT = "feeWithAmount";
    private static final String AMOUNT = "amount";
    private static final String OPERATION_CURRENCY = "operationCurrency";
    private static final String SOURCE_ACCOUNT_ID = "sourceAccountId";
    private static final String FEE = "fee";
    private static final String FEE_CURRENCY = "feeCurrency";
    private static final String MT100_TRANSFER_BODY = "mt100TransferBody";
    private static final String OPERATION_CODE = "operationCode";
    private static final String CARD_START_WITH_SYMBOL = "4";
    private static final String ELCARD_PREFIX = "9417";
    private static final int SNACKBAR_MAX_LINES = 5;

    protected int MY_SCAN_REQUEST_CODE = 5555;
    protected TransferAccountsFragment.CardTransferType cardTransferType;
    protected boolean isCard, needSendFeeRespAfterGetAccData, isGetAccountDataRequested, isCheking;

    private String operationCode = String.valueOf(System.currentTimeMillis());
    private String fee, feeCurrency, categoryName, templateAccToNumb, currency, EncryCardNumber = "";
    private long requestId;
    private double sumWithAmount;
    private boolean isBackPressed = true, isTemplate, Replenish, isAccountToEncrypted = true;
    private boolean isShowFeeInfo, successCardBrandOrAccountCheck, ifChange = true, isRightCard;

    private SmsWithTextSend smsWithTextSend;
    private Transfers transfer;
    private UserAccounts accountFrom, accountTo;
    private ArrayList<String> stringList = new ArrayList<>();
    private FragmentTransferVisaToVisaBinding binding;
    private BodyModel.Mt100Transfer mt100TransferBody;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTransferVisaToVisaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // генерируем уникальный идентификатор запроса платежа
        generateRequestId();

        setListeners();
        setNavigateBackOptions();
        getBundleArguments();
        initToolbar();
        setClickListeners();
        setEditTextFilterOptions();
        setListenerForCardNumber();
        setTypeFace();
        numberOfCardChangeListener();
    }

    public void numberOfCardChangeListener() {
        final MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "[0000] [0000] [0000] [0000]",
                true,
                binding.editTextRecipientCardNumber,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        if (!binding.editTextRecipientCardNumber
                                .getText().toString().startsWith(CARD_START_WITH_SYMBOL)
                                && !binding.editTextRecipientCardNumber.getText().toString().isEmpty()) {
                            binding.editTextRecipientCardNumber.getText().clear();
                            showSnackBar(getString(R.string.transfer_card_visa_to_visa_for_for_error));
                        }

                        if (binding.editTextRecipientCardNumber.getText().toString().replace(" ", "").length() == 16) {
                            fetchCardDetails();
                        }

                        char ch = 0000;
                        if (binding.editTextRecipientCardNumber.getText().toString().length() > 0) {
                            ch = binding.editTextRecipientCardNumber.getText().toString().toCharArray()[0];
                        }

                        if (Character.isDigit(ch)) {
                            isCard = binding.editTextRecipientCardNumber.getText().toString().replace(" ", "").length() == 16;
                            isGetAccountDataRequested = false;
                            successCardBrandOrAccountCheck = false;
                            needSendFeeRespAfterGetAccData = false;
                            isCheking = false;
                            setEdSpinnerToParams(true);

                            binding.editTextReceiverName.setVisibility(View.GONE);
                            binding.textHintFio.setVisibility(View.GONE);
                            binding.editTextReceiverName.getText().clear();
                            getSenderCardCurrency();

                        } else if (Character.isLetter(ch)) {
                            isCard = false;
                            setEdSpinnerToParams(false);
                            if (extractedValue.toString().replace(" ", "").length() == 20) {
                                binding.editTextRecipientCardNumber.setVisibility(View.VISIBLE);
                            } else {
                                binding.editTextRecipientCardNumber.setVisibility(View.GONE);
                            }
                        }

                        if (!binding.editTextRecipientCardNumber.getText().toString().replaceAll(" ", "").equals(templateAccToNumb)) {
                            binding.editTextReceiverName.getText().clear();
                        }
                    }
                }
        );
        binding.editTextRecipientCardNumber.addTextChangedListener(listener);
    }

    private void setListenerForCardNumber() {
        binding.editTextRecipientCardNumber.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                fetchCardDetails();
                hideSoftKeyBoard(requireContext());
                return true;
            }
            return false;
        });
    }


    @Override
    public void mastercardRegisterResponse(int statusCode, String errorMessage) {
    }

    @Override
    public void checkMt100TransferResponse(int statusCode, final CheckResponse response, String errorMessage) {
        if (statusCode == 0 && response != null) {
            sumWithAmount = getDoubleType(binding.editTextAmount.getText().toString());
            if (response.isNeedSmsConfirmation) {
                isBackPressed = false;
                enableDisableViews(false);
                binding.evaluateFeeLayout.getRoot().setVisibility(View.VISIBLE);
                fee = response.feeAmount;
                feeCurrency = response.feeCurrency;
                binding.evaluateFeeLayout.textFeeWithCurrency.setText(getFormattedBalance(getDoubleType(fee), feeCurrency));
                if (Double.parseDouble(fee) == 0) {
                    feeCurrency = accountFrom.currency;
                }
                binding.evaluateFeeLayout.textAmountPaymentWithCurrency.setText(getFormattedBalance(sumWithAmount, feeCurrency));
                if (getContext() != null) {
                    binding.btnTransfer.setText(getContext().getResources().getString(R.string.transfer_confirm));
                    isShowFeeInfo = true;
                }
                binding.btnTransfer.setOnClickListener(v -> smsWithTextSend.sendSmsWithText(
                        requireContext(),
                        Constants.TRANSFER_OTP_KEY,
                        getDoubleType(binding.editTextAmount.getText().toString()) + " " +
                                currency, operationCode));
            } else {
                isBackPressed = false;
                enableDisableViews(false);
                binding.evaluateFeeLayout.getRoot().setVisibility(View.VISIBLE);
                fee = response.feeAmount;
                feeCurrency = response.feeCurrency;

                if (!(response.confirmText.equals(""))) {
                    binding.evaluateFeeLayout.warningTextContent.setVisibility(View.VISIBLE);
                    binding.evaluateFeeLayout.btnDescription.setVisibility(View.VISIBLE);
                    binding.evaluateFeeLayout.textWarningMulti.setText(response.confirmText);
                    binding.evaluateFeeLayout.btnDescription.setOnClickListener(view -> showMultiplicatorDescription());
                } else {
                    binding.evaluateFeeLayout.warningTextContent.setVisibility(View.GONE);
                }
                binding.evaluateFeeLayout.textFeeWithCurrency.setText(getFormattedBalance(getDoubleType(fee), feeCurrency));
                binding.evaluateFeeLayout.textAmountPaymentWithCurrency.setText(getFormattedBalance(sumWithAmount, feeCurrency));
                binding.btnTransfer.setText(getResources().getString(R.string.transfer_confirm));
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
                intent.putExtra(IS_TRANSFER, true);
                intent.putExtra(IS_SUCCESS, true);
                intent.putExtra(IS_TEMPLATE, isTemplate);
                intent.putExtra(FEE_WITH_AMOUNT, sumWithAmount);
                intent.putExtra(AMOUNT, getDoubleType(binding.editTextAmount.getText().toString()));
                intent.putExtra(OPERATION_CURRENCY, currency);
                intent.putExtra(SOURCE_ACCOUNT_ID, accountFrom.code);

                intent.putExtra(RECEIPT_ACCOUNT_FROM, accountFrom.number);
                intent.putExtra(RECEIPT_ACCOUNT_FEE, binding.evaluateFeeLayout.textFeeWithCurrency.getText().toString());
                intent.putExtra(RECEIPT_OWN_ACCOUNT_TO, binding.layoutSenderCardDetails.cardNumberText.getText().toString());
                intent.putExtra(RECEIPT_ACCOUNT_TO, binding.editTextRecipientCardNumber.getText().toString());
                intent.putExtra(RECEIPT_ACCOUNT_TO_FULL_NAME, binding.editTextReceiverName.getText().toString());
                intent.putExtra(IS_RECEIPT_TRANSFER, true);
                startActivity(intent);
                requireActivity().finish();
            } else {
                errorDialog(errorMessage);
            }
        }
    }

    @Override
    public void checkCardBrandResponse(int statusCode, String errorMessage, String cardValue) {
    }

    @Override
    public void getAccDataResponse(int statusCode, String errorMessage, AccStatusResponse response) {
        if (statusCode == Constants.SUCCESS) {
            if (response != null) {
                String numberTo = binding.editTextRecipientCardNumber.getText().toString();
                // Карта АТФ не выводить поле для ввода и тд
                if (!isAtfCardNumber(numberTo)) {
                    // Наша Visa карта
                    if (response.ownerName != null && !response.ownerName.isEmpty()) {
                        binding.editTextReceiverName.setVisibility(View.VISIBLE);
                        binding.editTextReceiverName.setText(response.ownerName);
                        isRightCard = true;
                    } else if (numberTo.startsWith(CARD_START_WITH_SYMBOL) && response.ownerName == null) {
                        // Чужая Visa карта
                        if (currentCardIsElCart()) {
                            onError(getString(R.string.transfer_card_visa_to_visa_for_for_error));
                            binding.btnTransfer.setClickable(false);
                            isRightCard = false;
                        } else {
                            binding.editTextReceiverName.setVisibility(View.VISIBLE);
                            binding.textHintFio.setVisibility(View.VISIBLE);
                            isRightCard = true;
                        }
                    }
                }

                successCardBrandOrAccountCheck = true;
                binding.editTextRecipientCardNumber.setError(null);
            } else {
                binding.editTextRecipientCardNumber.requestFocus();
                if (binding.editTextRecipientCardNumber.getText().toString().replace(" ", "").length() == 16)
                    showSnackBar(getString(R.string.transfer_card_visa_to_visa_for_for_error));
                successCardBrandOrAccountCheck = false;
            }
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            successCardBrandOrAccountCheck = false;
            onError(errorMessage);
        }
    }

    @Override
    public void onSmsTextResponse(int statusCode, String errorMessage, Integer errorCode) {
        if (statusCode == Constants.SUCCESS) {
            Intent intent = new Intent(getActivity(), SmsConfirmActivity.class);
            intent.putExtra(IS_TRANSFER, true);
            intent.putExtra(OPERATION_CURRENCY, currency);
            intent.putExtra(FEE, fee);
            intent.putExtra(FEE_CURRENCY, feeCurrency);
            intent.putExtra(FEE_WITH_AMOUNT, sumWithAmount);
            intent.putExtra(AMOUNT, getDoubleType(binding.editTextAmount.getText().toString()));
            intent.putExtra(MT100_TRANSFER_BODY, mt100TransferBody);
            intent.putExtra(IS_TEMPLATE, isTemplate);
            intent.putExtra(OPERATION_CODE, operationCode);

            intent.putExtra(RECEIPT_ACCOUNT_FROM, accountFrom.number);
            intent.putExtra(RECEIPT_OWN_ACCOUNT_TO, binding.layoutSenderCardDetails.cardNumberText.getText().toString());
            intent.putExtra(RECEIPT_ACCOUNT_FEE, binding.evaluateFeeLayout.textFeeWithCurrency.getText().toString());
            intent.putExtra(RECEIPT_ACCOUNT_TO, binding.editTextRecipientCardNumber.getText().toString());
            intent.putExtra(RECEIPT_ACCOUNT_TO_FULL_NAME, binding.editTextReceiverName.getText().toString());
            intent.putExtra(IS_RECEIPT_TRANSFER, true);
            startActivity(intent);
            requireActivity().finish();
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private boolean isAtfCardNumber(String number) {
        List<String> atfCardList = new ArrayList<>();
        atfCardList.add("4052 55");
        atfCardList.add("405255");
        atfCardList.add("4052 56");
        atfCardList.add("405256");
        atfCardList.add("4052 57");
        atfCardList.add("405257");
        atfCardList.add("4052 58");
        atfCardList.add("405258");
        atfCardList.add("4052 63");
        atfCardList.add("405263");
        atfCardList.add("4312 53");
        atfCardList.add("431253");
        atfCardList.add("4318 79");
        atfCardList.add("431879");
        atfCardList.add("4841 97");
        atfCardList.add("484197");
        atfCardList.add("4266 68");
        atfCardList.add("426668");
        atfCardList.add("4232 08");
        atfCardList.add("423208");
        atfCardList.add("4971 49");
        atfCardList.add("462000");
        atfCardList.add("4620 00");
        atfCardList.add("497149");

        for (String card : atfCardList) {
            if (number.startsWith(card)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CommonStatusCodes.SUCCESS && data != null) {
            if (requestCode == SELECT_ACCOUNT_FROM_REQUEST_CODE) {
                binding.textSenderCardNumber.setError(null);
                accountFrom = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                if (accountTo != null) {
                    if (accountFrom != null && accountFrom.code == accountTo.code) {
                        resetRecipientData();
                    }
                }
                setSenderCardDetails(accountFrom);
                setEdSpinnerToParams();
                getSenderCardCurrency();
                getRecipientCardCurrency();
                setCurrency();

                binding.scanCardImageView.setVisibility(View.VISIBLE);
                needSendFeeRespAfterGetAccData = false;
            } else if (requestCode == SELECT_ACCOUNT_TO_REQUEST_CODE) {
                accountTo = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                isCheking = false;
                if (accountTo != null && accountTo.code == Constants.NEW_CARD_ID) {
                    resetRecipientData();
                    if (accountFrom instanceof UserAccounts.Cards) {
                        binding.scanCardImageView.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.scanCardImageView.setVisibility(View.GONE);
                    setRecipientCardDetails(accountTo);
                    getRecipientCardCurrency();
                    setCurrency();
                }
            } else if (requestCode == SELECT_CURRENCY_REQUEST_CODE) {
                this.currency = (String) data.getSerializableExtra(STRING_KEY);
                binding.currencyText.setText(currency);
                if (accountFrom != null && accountFrom instanceof UserAccounts.Cards && ((UserAccounts.Cards) accountFrom).isMultiBalance) {
                    for (UserAccounts.Cards.MultiBalanceList multi : ((UserAccounts.Cards) accountFrom).multiBalanceList) {
                        if (this.currency.equals(multi.currency)) {
                            String balance = getFormattedBalance(multi.amount, currency);
                            binding.layoutSenderCardDetails.cardBalanceText.setText(balance);
                        }
                    }
                }
            }
        }

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            Log.i(TAG_VISA_TO_VISA, "Joined to Scan");
            ifChange = true;
            if (resultCode == Activity.RESULT_OK) {
                Card card = data.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD);
                String cardData = null;
                if (card != null) {
                    cardData = "Card number: " + card.getCardNumber() + "\n"
                            + "Card holder: " + card.getCardHolderName() + "\n"
                            + "Card expiration date: " + card.getExpirationDate();
                }
                binding.editTextReceiverName.getText().clear();
                if (card != null) {
                    binding.editTextRecipientCardNumber.setText(card.getCardNumber());
                    if (binding.editTextReceiverName.getText().toString().isEmpty()) {
                        binding.editTextReceiverName.setText(card.getCardHolderName());
                    }
                }
                binding.editTextRecipientCardNumber.setSelection(binding.editTextRecipientCardNumber.getText().length());
                Log.i(TAG_VISA_TO_VISA, "Card info: " + cardData);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                binding.editTextRecipientCardNumber.getText().clear();
                binding.editTextRecipientCardNumber.requestFocus();
            } else {
                Log.i(TAG_VISA_TO_VISA, "Scan failed");
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
    public void afterTextChanged(Editable editable) {
//        if (binding.editTextRecipientCardNumber.getText().toString().replace(" ", "").equals(editable.toString().replace(" ", ""))) {
//            setEdSpinnerToParamsToNull();
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initToolbar() {
        binding.toolbarView.setTitle(categoryName);
        ((TransfersActivity) requireActivity()).setSupportActionBar(binding.toolbarView);
        binding.toolbarView.setNavigationOnClickListener(toolbar -> {
            if (isBackPressed) {
                requireActivity().onBackPressed();
                binding.btnTransfer.setClickable(true);
                binding.btnTransfer.setEnabled(true);
            } else {
                actionBack();
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
                    binding.btnTransfer.setEnabled(true);
                    binding.btnTransfer.setClickable(true);
                }
            }
        });
        builder.create();
        builder.show();
    }

    // генерируем уникальный идентификатор запроса платежа
    private void generateRequestId() {
        requestId = System.currentTimeMillis();
    }

    // ставим слушатели
    private void setListeners() {
        transfer = new TransferImpl();
        transfer.registerCallBack(this);
        transfer.registerCallbackConfirm(this);
        binding.editTextRecipientCardNumber.addTextChangedListener(this);
        binding.editTextAmount.addTextChangedListener(this);
        smsWithTextSend = new SmsWithTextImpl();
        smsWithTextSend.registerSmsWithTextCallBack(this);
    }

    //  ставим слушатели для кнопок
    private void setClickListeners() {
        clickSenderCardNumber();
        clickScanCard();
        clickRecipientCardNumber();
        clickSelectCurrency();
        clickBtnTransferMoney();
    }

    // ставим действие если нажимаем на кнопку назад в тулбаре
    private void setNavigateBackOptions() {
        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener((view1, keyCode, event) -> {
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
            });
        }
    }

    // ставим фильтры в ввода полей
    private void setEditTextFilterOptions() {
        setEdSpinnerToParams(false);
        binding.editTextAmount.setFilters(new InputFilter[]{new Utilities.DecimalDigitsInputFilter(2)});
    }

    private void setTypeFace() {
        Utilities.setRobotoTypeFaceToTextView(requireContext(), binding.currencyText);
        Utilities.setRobotoTypeFaceToTextView(requireContext(), binding.evaluateFeeLayout.textFeeWithCurrency);
        Utilities.setRobotoTypeFaceToTextView(requireContext(), binding.evaluateFeeLayout.textAmountPaymentWithCurrency);
    }

    // действие при клике на поел ввода карты получателя
    private void clickRecipientCardNumber() {
        binding.editTextRecipientCardNumber.setOnClickListener(editTextCardNumberTo -> {
            binding.textSenderCardNumber.setFocusable(true);
            binding.textSenderCardNumber.setFocusableInTouchMode(true);
            binding.textSenderCardNumber.requestFocus();
            binding.textSenderCardNumber.setError(getString(R.string.error_empty));
        });
    }

    private void clickSelectCurrency() {
        binding.currencyText.setOnClickListener(currencyTextView -> {
            Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
            intent.putExtra(CURRENCY, true);
            intent.putExtra(LIST_CURRENCY, stringList);
            startActivityForResult(intent, SELECT_CURRENCY_REQUEST_CODE);
        });
    }

    private void clickSenderCardNumber() {
        binding.textSenderCardNumber.setOnClickListener(textView -> {
            openSelectVisaCardScreen();
        });
        binding.layoutSenderCardDetails.getRoot().setOnClickListener(layout -> {
            openSelectVisaCardScreen();
        });
    }

    private void openSelectVisaCardScreen() {
        Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
        intent.putExtra(TRANSFER_VISA_TO_VISA, true);
        startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
    }

    private void clickScanCard() {
        binding.scanCardImageView.setOnClickListener(scanImageView -> {
            if (accountFrom != null) {
                if (accountFrom instanceof UserAccounts.Cards) {
                    Intent intent = new RPSScanCardIntent.Builder(getContext()).build();
                    startActivityForResult(intent, MY_SCAN_REQUEST_CODE);
                }
            }
        });
    }

    private void clickBtnTransferMoney() {
        binding.btnTransfer.setOnClickListener(btnTransfer -> {
            if (hasInternetConnection(requireContext())) {
                if ((accountFrom instanceof UserAccounts.Cards || accountFrom instanceof UserAccounts.CheckingAccounts) && accountTo == null) {
                    if (binding.editTextRecipientCardNumber.getText().toString().replaceAll(" ", "").replaceAll("[0-9]", "").length() <= 0) {
                        if (isCorrectInputData() && binding.btnTransfer.getText().toString().equals(getString(R.string.transfer_confirm))) {
                            transfer.registerCallbackConfirm(this);
                            transfer.confirmMt100Transfer(requireContext(), getBody());
                            binding.btnTransfer.setClickable(false);
                            binding.btnTransfer.setEnabled(false);
                        } else if (isCorrectInputData() && binding.btnTransfer.getText().toString().equals(getString(R.string.send_transfer))) {
                            transfer.registerCallbackConfirm(this);
                            transfer.checkMt100Transfer(requireContext(), getBody());
                        }
                    }
                } else {
                    if (isCorrectInputData() && isBackPressed) {
                        transfer.checkMt100Transfer(requireContext(), getBody());
                    } else if (!isBackPressed) {
                        transfer.registerCallbackConfirm(this);
                        transfer.confirmMt100Transfer(requireContext(), getBody());
                        binding.btnTransfer.setClickable(false);
                        binding.btnTransfer.setEnabled(false);
                    }
                }
            } else {
                showToast(getString(R.string.internet_switched_off));
            }
        });
    }

    private void getBundleArguments() {
        if (getArguments() != null && !isTemplate) {
            categoryName = getArguments().getString(TRANSFER_NAME);
            Replenish = getArguments().getBoolean(REPLENISH, false);
            UserAccounts account = (UserAccounts) getArguments().getSerializable(ACCOUNT);
            if (account instanceof UserAccounts.DepositAccounts || Replenish) {
                if (account instanceof UserAccounts.WishAccounts) {
                    if (getArguments().getBoolean(ACTO)) {
                        accountTo = account;
                    } else {
                        accountFrom = account;
                    }
                } else if (getArguments().getBoolean(ACTOO)) {
                    accountFrom = account;
                } else {
                    accountTo = account;
                }
            } else {
                accountFrom = account;
            }
        }
    }

    // получаем данные получаетеля по номеру карты
    public void fetchCardDetails() {
        if (binding.editTextRecipientCardNumber.getText().toString().replace(" ", "").length() == 16) {
            String cardNumberWithoutSpaces = binding.editTextRecipientCardNumber.getText().toString().replace(" ", "");
            if (CardNumberUtils.isCardNumberCorrectWithLuhn(cardNumberWithoutSpaces)) {
                if (binding.editTextRecipientCardNumber.getText().toString().replaceAll(" ", "").replaceAll("[0-9]", "").length() <= 0) {
                    transfer.getAccountData(parentActivity, binding.editTextRecipientCardNumber.getText().toString().replace(" ", ""));
                    isGetAccountDataRequested = true;
                    cardTransferType = null;
                    hideSoftKeyBoard(requireContext());
                }
            } else {
                showSnackBar(getString(R.string.incorrect_card_number));
            }
        }
    }

    // проверка правильно ли введены данные пользователя
    public boolean isCorrectInputData() {
        if (binding.editTextReceiverName.isShown()) {
            needSendFeeRespAfterGetAccData = binding.editTextReceiverName.getText().toString().isEmpty();
        }

        boolean success = true;
        if (accountFrom != null) {
            ArrayList<TextView> fields = new ArrayList<>();
            if (!binding.editTextAmount.getText().toString().isEmpty()) {
                if (binding.editTextAmount.getText().toString().startsWith(".") || binding.editTextAmount.getText().toString().startsWith(",")) {
                    binding.editTextAmount.setError(getResources().getString(R.string.error_wrong_format));
                    binding.editTextAmount.requestFocus();
                    return false;
                }
                if (!binding.editTextAmount.getText().toString().isEmpty() && accountFrom.currency.equalsIgnoreCase(currency) &&
                        accountFrom.balance < getDoubleType(binding.editTextAmount.getText().toString())) {
                    onError(getResources().getString(R.string.error_large_sum));
                    return false;
                }
                if (binding.editTextReceiverName.isShown()) {
                    fields.add(binding.editTextReceiverName);
                    if (binding.editTextReceiverName.getText().toString().split(" ").length < 2 && binding.editTextReceiverName.getText().toString().isEmpty()) {
                        binding.editTextReceiverName.setError(getString(R.string.input_first_last_name_error));
                        binding.editTextReceiverName.requestFocus();
                        success = false;
                    }
                }

                if (binding.editTextRecipientCardNumber.isShown()) {
                    fields.add(binding.editTextRecipientCardNumber);
                }
                fields.add(binding.editTextAmount);
            } else {
                showSnackBar(getString(R.string.input_sum));
                binding.editTextAmount.requestFocus();
                success = false;
            }

            if (binding.editTextRecipientCardNumber.getText().toString().isEmpty()) {
                showSnackBar(getString(R.string.input_receiver_card_number));
                binding.editTextAmount.requestFocus();
                success = false;
            }

            if (binding.editTextRecipientCardNumber.getText().toString().startsWith(ELCARD_PREFIX)) {
                binding.editTextRecipientCardNumber.requestFocus();
                showSnackBar(getString(R.string.transfer_card_visa_to_visa_for_for_error));
                success = false;
            }

            for (TextView view : fields) {
                if (view.getText().toString().isEmpty()) {
                    view.setError(getResources().getString(R.string.error_empty));
                    view.requestFocus();
                    success = false;
                }
            }

            if (success && binding.editTextRecipientCardNumber.isShown()) {
                if (binding.editTextRecipientCardNumber.getText().toString().replaceAll(" ", "").length() != 16) {
                    binding.editTextRecipientCardNumber.setError(getResources().getString(R.string.error_wrong_format));
                    binding.editTextRecipientCardNumber.requestFocus();
                    success = false;
                } else if (binding.editTextRecipientCardNumber.getText().toString().replaceAll(" ", "").length() == 16) {
                    String cardNumberWithoutSpaces = binding.editTextRecipientCardNumber.getText().toString().replace(" ", "");
                    if (!CardNumberUtils.isCardNumberCorrectWithLuhn(cardNumberWithoutSpaces)) {
                        showSnackBar(getString(R.string.incorrect_card_number));
                        success = false;
                    }
                }
            }

            if (binding.textSenderCardNumber.isShown()) {
                binding.textSenderCardNumber.setError(getResources().getString(R.string.error_empty));
                binding.textSenderCardNumber.requestFocus();
                success = false;
            }
        } else {
            binding.textSenderCardNumber.setError(getResources().getString(R.string.error_empty));
            binding.textSenderCardNumber.requestFocus();
            success = false;
        }
        return success;
    }

    public void setSenderCardDetails(UserAccounts userAccounts) {
        if (userAccounts != null) {
            if (userAccounts instanceof UserAccounts.Cards) {
                binding.textSenderCardNumber.setVisibility(View.GONE);
                binding.layoutSenderCardDetails.getRoot().setVisibility(View.VISIBLE);
                binding.layoutSenderCardDetails.cardStatusText.setVisibility(View.GONE);
                binding.layoutSenderCardDetails.cardNameText.setText(userAccounts.name);
                binding.layoutSenderCardDetails.cardBalanceText.setText(userAccounts.getFormattedBalance(getContext()));
                binding.layoutSenderCardDetails.cardNumberText.setText(userAccounts.number);

                if (accountTo == null || accountTo.code == Constants.NEW_CARD_ID) {
                    cardTransferType = null;
                    // binding.editTextReceiverName.setVisibility(View.GONE);
                    // binding.editTextReceiverName.getText().clear();
                    binding.btnTransfer.setText(getString(R.string.send_transfer));
                    binding.scanCardImageView.setVisibility(View.VISIBLE);
                } else {
                    if (binding.scanCardImageView.isShown())
                        binding.scanCardImageView.setVisibility(View.GONE);
                }

                binding.textSenderCardNumber.setVisibility(View.GONE);
                UserAccounts.Cards userCard = (UserAccounts.Cards) userAccounts;
                binding.layoutSenderCardDetails.cardImageView.setLayoutParams(getConstraintLayoutParamsForImageSize(requireContext(), 50, 55));
                byte[] min = userCard.getByteArrayMiniatureImg();
                if (min != null) {
                    Utilities.setCardToImageView(
                            userCard,
                            binding.layoutSenderCardDetails.cardImageView,
                            binding.layoutSenderCardDetails.titleMultiCard,
                            BitmapFactory.decodeByteArray(min, 0, min.length)
                    );
                } else {
                    Utilities.setCardToImageView(
                            userCard,
                            binding.layoutSenderCardDetails.cardImageView,
                            binding.layoutSenderCardDetails.titleMultiCard,
                            BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left)
                    );
                }
            }
        }
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT);
        View mySnackbarView = snackbar.getView();
        TextView tv = (TextView) mySnackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setMaxLines(SNACKBAR_MAX_LINES);
        snackbar.show();
    }

    public void setRecipientCardDetails(UserAccounts userAccounts) {
        if (userAccounts != null) {
            if (userAccounts instanceof UserAccounts.WishAccounts) {
                binding.layoutSenderCardDetails.cardNameText.setText(((UserAccounts.WishAccounts) userAccounts).wishName);
            } else {
                binding.layoutSenderCardDetails.cardNameText.setText(userAccounts.name);
            }
            binding.editTextReceiverName.setVisibility(View.GONE);
            binding.editTextRecipientCardNumber.setVisibility(View.GONE);
            binding.layoutSenderCardDetails.getRoot().setVisibility(View.VISIBLE);
            binding.layoutSenderCardDetails.cardStatusText.setVisibility(View.GONE);
            binding.layoutSenderCardDetails.cardBalanceText.setText(userAccounts.getFormattedBalance(getContext()));
            binding.layoutSenderCardDetails.cardNameText.setText(userAccounts.number);
        }
    }

    public void resetRecipientData() {
        binding.editTextReceiverName.setVisibility(View.GONE);
        binding.textSenderCardNumber.setVisibility(View.GONE);
        binding.editTextRecipientCardNumber.setVisibility(View.VISIBLE);
        binding.layoutSenderCardDetails.getRoot().setVisibility(View.GONE);
        accountTo = null;
        currency = accountFrom.currency;
        binding.currencyText.setText(accountFrom.currency);
        binding.editTextRecipientCardNumber.getText().clear();
    }

    public void setEdSpinnerToParams() {
        int maxLength;
        if (accountFrom instanceof UserAccounts.Cards) {
            maxLength = 19;
        } else {
            binding.editTextRecipientCardNumber.setFocusable(true);
            binding.editTextRecipientCardNumber.setFocusableInTouchMode(true);
            maxLength = 20;
        }
        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        fArray[1] = (src, start, end, dest, dstart, dend) -> {
            if (src.equals("")) { // for backspace
                return src;
            }
            if (src.toString().matches("[a-zA-Z0-9 ]+")) {
                return src;
            }
            return "";
        };
        binding.editTextRecipientCardNumber.setFilters(fArray);
    }

    public void setEdSpinnerToParams(boolean isNumberCard) {
        int maxLength;
        InputFilter[] fArray = new InputFilter[2];
        maxLength = 19;
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        fArray[1] = (src, start, end, dest, dstart, dend) -> {
            if (src.equals("")) { // for backspace
                return src;
            }
            if (src.toString().matches("[0-9 ]+")) {
                return src;
            }
            return "";
        };
        binding.editTextRecipientCardNumber.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_NUMBER_FLAG_SIGNED);
        binding.editTextRecipientCardNumber.setFilters(fArray);
        binding.editTextRecipientCardNumber.setFocusableInTouchMode(true);
    }

    public void setEdSpinnerToParamsToNull() {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = (src, start, end, dest, dstart, dend) -> {
            if (src.equals("")) {
                return src;
            }
            if (src.toString().matches("[a-zA-Z0-9 ]+")) {
                return src;
            }
            return "";
        };
        binding.editTextRecipientCardNumber.setFilters(fArray);
    }

    public void getSenderCardCurrency() {
        stringList.clear();
        if (accountFrom != null) {
            if (accountFrom instanceof UserAccounts.Cards) {
                UserAccounts.Cards card = (UserAccounts.Cards) accountFrom;
                if (((UserAccounts.Cards) accountFrom).isMultiBalance) {
                    setMultiCurrency(true);
                    for (UserAccounts.Cards.MultiBalanceList item : card.multiBalanceList) {
                        addItemInCurrencyList(item.currency);
                    }
                } else {
                    setMultiCurrency(false);
                    addItemInCurrencyList(accountFrom.currency);
                }
            } else {
                setMultiCurrency(false);
            }
        }
    }

    public void getRecipientCardCurrency() {
        if (accountTo != null) {
            getSenderCardCurrency();
            if (accountTo instanceof UserAccounts.Cards) {
                UserAccounts.Cards card = (UserAccounts.Cards) accountTo;
                if (card.isMultiBalance) {
                    for (UserAccounts.Cards.MultiBalanceList item : card.multiBalanceList) {
                        addItemInCurrencyList(item.currency);
                    }
                } else {
                    addItemInCurrencyList(accountTo.currency);
                }
            }
        }
        if (stringList.size() > 1) {
            setMultiCurrency(true);
        } else {
            setMultiCurrency(false);
        }
    }

    private void addItemInCurrencyList(String item) {
        if (!stringList.contains(item)) {
            stringList.add(item);
        }
    }

    public boolean currentCardIsElCart() {
        return ((accountFrom != null && accountFrom instanceof UserAccounts.Cards && ((UserAccounts.Cards) accountFrom).brandType == 1)
                || (accountTo != null && accountTo instanceof UserAccounts.Cards && ((UserAccounts.Cards) accountTo).brandType == 1));
    }

    public void setCurrency() {
        if ((isCheking || (accountTo != null && accountTo instanceof UserAccounts.CheckingAccounts)) &&
                ((accountFrom instanceof UserAccounts.Cards))) {
            if (accountTo != null) {
                binding.currencyText.setVisibility(View.VISIBLE);
                binding.currencyText.setText(accountTo.currency);
                currency = accountTo.currency;
            }
            setMultiCurrency(false);
        } else {
            if (accountFrom != null) {
                binding.currencyText.setVisibility(View.VISIBLE);
                binding.currencyText.setText(accountFrom.currency);
                currency = accountFrom.currency;
            }
        }
    }

    // Делаем кликабельным если это мульти валютная карта
    public void setMultiCurrency(boolean isMultiCurrency) {
        binding.currencyText.setEnabled(isMultiCurrency);
        if (isMultiCurrency) {
            binding.currencyText.setVisibility(View.VISIBLE);
            binding.currencyText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_button_dark_common_down, 0);
        } else {
            binding.scanCardImageView.setVisibility(View.GONE);
            binding.currencyText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    public String getNextPaymentAmount() {
        String amount = binding.editTextAmount.getText().toString();
        if (!amount.isEmpty()) {
            BigDecimal bd = BigDecimal.valueOf(getDoubleType(amount.replaceAll(" ", "")));
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');
            DecimalFormat formatter = new DecimalFormat("##0.00", symbols);
            return formatter.format(bd.doubleValue()) + "";
        }
        return "";
    }

    public JSONObject getBody() {
        mt100TransferBody = new BodyModel.Mt100Transfer();
        if (accountTo != null) {
            if (accountFrom instanceof UserAccounts.Cards && accountTo instanceof UserAccounts.Cards
                    || !(accountFrom instanceof UserAccounts.Cards) && !(accountTo instanceof UserAccounts.Cards)
                    || !(accountFrom instanceof UserAccounts.Cards)) {
                mt100TransferBody.accountCode = accountFrom.code;
            } else {
                mt100TransferBody.accountCode = ((UserAccounts.Cards) accountFrom).cardAccountCode;
            }
        } else {
            if (isCard && accountFrom instanceof UserAccounts.Cards || !isCard && !(accountFrom instanceof UserAccounts.Cards) || !(accountFrom instanceof UserAccounts.Cards)) {
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
            mt100TransferBody.contragentAccountNumber = binding.editTextRecipientCardNumber.getText().toString().replaceAll(" ", "");
            if (binding.editTextReceiverName.isShown())
                if (!binding.editTextReceiverName.getText().toString().isEmpty())
                    mt100TransferBody.contragentName = binding.editTextReceiverName.getText().toString();
        }
        if (!isBackPressed) {
            mt100TransferBody.feeAmount = fee;
            mt100TransferBody.feeCurrency = feeCurrency;
        }
        mt100TransferBody.requestId = requestId;
        return getFieldNamesAndValues(mt100TransferBody);
    }

    private void actionBack() {
        binding.btnTransfer.setText(getResources().getString(R.string.send_transfer));
        isBackPressed = true;
        enableDisableViews(true);
        binding.evaluateFeeLayout.getRoot().setVisibility(View.GONE);
        binding.evaluateFeeLayout.textFeeWithCurrency.setText("");
        binding.evaluateFeeLayout.textAmountPaymentWithCurrency.setText("");
        fee = null;
        feeCurrency = null;
        isShowFeeInfo = false;
    }

    private void enableDisableViews(boolean isEnable) {
        binding.editTextAmount.setEnabled(isEnable);
        binding.editTextRecipientCardNumber.setEnabled(isEnable);
        binding.editTextReceiverName.setEnabled(isEnable);
        binding.textSenderCardNumber.setEnabled(isEnable);
        binding.currencyText.setEnabled(isEnable);
        binding.layoutSenderCardDetails.getRoot().setEnabled(isEnable);
        binding.evaluateFeeLayout.getRoot().setEnabled(isEnable);
        binding.scanCardImageView.setEnabled(isEnable);
    }
}