package kz.optimabank.optima24.fragment.transfer;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.activity.TransfersActivity.TRANSFER_NAME;
import static kz.optimabank.optima24.utility.Constants.ACCOUNT_KEY;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Constants.IS_RECEIPT_TRANSFER;
import static kz.optimabank.optima24.utility.Constants.PHONE_NUMBER_PREFIX;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_FEE;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_FROM;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_TO;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_ACCOUNT_TO_FULL_NAME;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_OWN_ACCOUNT_TO;
import static kz.optimabank.optima24.utility.Constants.RECEIPT_PHONE_NUMBER;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_CURRENCY_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;
import static kz.optimabank.optima24.utility.Utilities.getConstraintLayoutParamsForImageSize;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;
import static kz.optimabank.optima24.utility.Utilities.hasInternetConnection;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.activity.contacts.SelectContactsActivity;
import kz.optimabank.optima24.activity.contacts.data.Contact;
import kz.optimabank.optima24.databinding.FragmentTransferPhoneNumberBinding;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.transfer.phoneNumberRequest.TransferByPhoneNumberAction;
import kz.optimabank.optima24.fragment.transfer.phoneNumberRequest.TransferByPhoneNumberActionImpl;
import kz.optimabank.optima24.model.base.AccountPhoneNumberResponse;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.AccStatusResponse;
import kz.optimabank.optima24.model.gson.response.CheckResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.SmsWithTextSend;
import kz.optimabank.optima24.model.interfaces.Transfers;
import kz.optimabank.optima24.model.service.SmsWithTextImpl;
import kz.optimabank.optima24.model.service.TransferImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

public class TransferPhoneNumberFragment extends ATFFragment implements TextWatcher,
        TransferImpl.Callback, TransferImpl.CallbackConfirm,
        SmsWithTextImpl.SmsSendWithTextCallback, TransferByPhoneNumberAction.AccDataByPhoneNumberCallback {

    private static final String TAG_PHONE_NUMBER = "tag_elcart_to_elcart";
    private static final int START_INDEX = 0;
    private static final int SELECT_CONTACT_REQUEST = 1;
    private static final int SIZE_PHONE_NUMBER_WITH_SYMBOLS = 15;
    private static final String REPLENISH = "Replenish";
    private static final String ACTO = "acTo";
    private static final String ACTOO = "acToO";
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

    protected TransferAccountsFragment.CardTransferType cardTransferType;
    protected boolean needSendFeeRespAfterGetAccData, isGetAccountDataRequested, isCheking;

    private String operationCode = String.valueOf(System.currentTimeMillis());
    private String fee, feeCurrency, categoryName, templateAccToNumb, currency, EncryCardNumber = "";
    private long requestId;
    private double sumWithAmount;
    private boolean isBackPressed = true, isTemplate, Replenish, isAccountToEncrypted = true;
    private boolean isShowFeeInfo, successCardBrandOrAccountCheck, ifChange = true, isRightCard;

    private SmsWithTextSend smsWithTextSend;
    private Transfers transfer;
    private TransferByPhoneNumberAction transferByPhoneNumberAction;
    private UserAccounts accountFrom, accountTo;
    private ArrayList<String> stringList = new ArrayList<>();
    private FragmentTransferPhoneNumberBinding binding;
    private BodyModel.Mt100Transfer mt100TransferBody;
    private String contragentAccountNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTransferPhoneNumberBinding.inflate(inflater, container, false);
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
        setListenerForCardNumber();
        setCustomActionModeCallBackInputPhoneNumber();

        binding.currencyText.setVisibility(View.VISIBLE);
        binding.inputPhoneNumber.addTextChangedListener(this);
    }

    private void setCustomActionModeCallBackInputPhoneNumber() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.inputPhoneNumber.setCustomInsertionActionModeCallback(new ActionMode.Callback() {
                private static final String TAG = "StyleCallback";

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    Log.d(TAG, "onCreateActionMode");
                    menu.clear();
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.cut_copy_menu, menu);
                    menu.removeItem(android.R.id.selectAll);
                    return true;
                }

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    Log.d(TAG, String.format("onActionItemClicked item=%s/%d", item.toString(), item.getItemId()));

                    if (item.getItemId() == R.id.paste) {
                        ClipboardManager clipboardManager =
                                (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboardManager.hasPrimaryClip()) {
                            String newPasteData = "";
                            ClipData.Item clipDataItem = clipboardManager.getPrimaryClip().getItemAt(START_INDEX);
                            String pasteData = clipDataItem.getText().toString();
                            newPasteData = Utilities.getPhoneNumber(pasteData.trim());
                            binding.inputPhoneNumber.setText(newPasteData);
                        }
                        return true;
                    }
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {
                }
            });
        }
    }

    private void setListenerForCardNumber() {
        binding.inputPhoneNumber.setOnEditorActionListener((v, actionId, event) -> {
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
            sumWithAmount = getDoubleType(binding.inputAmount.getText().toString());
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
                        getDoubleType(binding.inputAmount.getText().toString()) + " " +
                                currency, operationCode));
            } else {
                isBackPressed = false;
                enableDisableViews(false);
                binding.evaluateFeeLayout.getRoot().setVisibility(View.VISIBLE);
                fee = response.feeAmount;
                feeCurrency = response.feeCurrency;
                binding.evaluateFeeLayout.textFeeWithCurrency.setText(getFormattedBalance(getDoubleType(fee), feeCurrency));
                binding.evaluateFeeLayout.textAmountPaymentWithCurrency.setText(getFormattedBalance(sumWithAmount, feeCurrency));
                binding.btnTransfer.setText(getResources().getString(R.string.transfer_confirm));
                isShowFeeInfo = true;
            }
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void checkCardBrandResponse(int statusCode, String errorMessage, String cardValue) {

    }

    @Override
    public void getAccDataResponse(int statusCode, String errorMessage, AccStatusResponse response) {

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
                intent.putExtra(AMOUNT, getDoubleType(binding.inputAmount.getText().toString()));
                intent.putExtra(OPERATION_CURRENCY, currency);
                intent.putExtra(SOURCE_ACCOUNT_ID, accountFrom.code);

                intent.putExtra(RECEIPT_PHONE_NUMBER, 1);
                intent.putExtra(RECEIPT_ACCOUNT_FROM, accountFrom.number);
                intent.putExtra(RECEIPT_ACCOUNT_FEE, binding.evaluateFeeLayout.textFeeWithCurrency.getText().toString());
                intent.putExtra(RECEIPT_OWN_ACCOUNT_TO, binding.layoutSenderCardDetails.cardNumberText.getText().toString());
                intent.putExtra(RECEIPT_ACCOUNT_TO, binding.inputPhoneNumber.getText().toString());
                intent.putExtra(RECEIPT_ACCOUNT_TO_FULL_NAME, binding.textReceiverName.getText().toString());
                intent.putExtra(IS_RECEIPT_TRANSFER, true);
                startActivity(intent);
                requireActivity().finish();
            } else {
              errorDialog(errorMessage);
            }
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
            intent.putExtra(AMOUNT, getDoubleType(binding.inputAmount.getText().toString()));
            intent.putExtra(MT100_TRANSFER_BODY, mt100TransferBody);
            intent.putExtra(IS_TEMPLATE, isTemplate);
            intent.putExtra(OPERATION_CODE, operationCode);

            intent.putExtra(RECEIPT_PHONE_NUMBER, 1);
            intent.putExtra(RECEIPT_ACCOUNT_FROM, accountFrom.number);
            intent.putExtra(RECEIPT_OWN_ACCOUNT_TO, binding.layoutSenderCardDetails.cardNumberText.getText().toString());
            intent.putExtra(RECEIPT_ACCOUNT_FEE, binding.evaluateFeeLayout.textFeeWithCurrency.getText().toString());
            intent.putExtra(RECEIPT_ACCOUNT_TO, binding.inputPhoneNumber.getText().toString());
            intent.putExtra(RECEIPT_ACCOUNT_TO_FULL_NAME, binding.textReceiverName.getText().toString());
            intent.putExtra(IS_RECEIPT_TRANSFER, true);
            startActivity(intent);
            requireActivity().finish();
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CommonStatusCodes.SUCCESS && data != null) {
            if (requestCode == SELECT_ACCOUNT_FROM_REQUEST_CODE) {
                Log.d(TAG, "select card");
                binding.textSenderCardNumber.setError(null);
                accountFrom = (UserAccounts) data.getSerializableExtra(ACCOUNT_KEY);
                if (accountTo != null) {
                    if (accountFrom != null && accountFrom.code == accountTo.code) {
                        resetRecipientData();
                    }
                }
                setSenderCardDetails(accountFrom);
                getSenderCardCurrency();
                setCurrency();
            } else if (requestCode == SELECT_CURRENCY_REQUEST_CODE) {
                Log.d(TAG, "select currency");
                this.currency = (String) data.getSerializableExtra(STRING_KEY);
                binding.currencyText.setText(currency);
                if (accountFrom != null && accountFrom instanceof UserAccounts.Cards && ((UserAccounts.Cards) accountFrom).isMultiBalance) {
                    for (UserAccounts.Cards.MultiBalanceList multi : ((UserAccounts.Cards) accountFrom).multiBalanceList) {
                        if (this.currency.equals(multi.currency)) {
                            String c = multi.amount + " " + currency;
                            binding.layoutSenderCardDetails.cardBalanceText.setText(c);
                        }
                    }
                }
            } else if (requestCode == SELECT_CONTACT_REQUEST) {
                Log.d(TAG, "select contact");
                Contact contact = data.getParcelableExtra("contact");
                setContactRecipientData(contact);
            }
        }
    }

    private void setContactRecipientData(Contact contact) {
        if (contact != null) {
            binding.inputPhoneNumber.setText(contact.getFormattedPhoneNumber());
            binding.contactFullName.setVisibility(View.VISIBLE);
            binding.contactFullName.setText(contact.getContactName());

            new Handler(Looper.getMainLooper()).postDelayed(this::fetchCardDetails, 300);
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
        if (binding.inputAmount.getText().toString().equals(String.valueOf(editable))) {
            boolean isReverse = false;
            StringBuffer stringBuffer = new StringBuffer();
            String string = binding.inputAmount.getText().toString().replaceAll(" ", "");
            string = Utilities.dotAndComma(string);
            if (ifChange) {
                ifChange = false;
                if (binding.inputAmount.getText().length() > 3) {
                    int pointPosition = string.indexOf(".");
                    if (pointPosition == 0 || pointPosition == -1) {
                        pointPosition = string.indexOf(",");
                    }
                    if (pointPosition == -1) {
                        pointPosition = string.length();
                        if (binding.inputAmount.getText().length() == 4) {
                            isReverse = true;
                            stringBuffer.delete(0, stringBuffer.length());
                            stringBuffer.append(string);
                            stringBuffer.reverse();
                            string = String.valueOf(stringBuffer);
                            string = string.substring(0, pointPosition - 1) + " " + string.substring(pointPosition - 1);
                        } else {
                            isReverse = false;
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
                if (isReverse) {
                    stringBuffer.delete(0, stringBuffer.length());
                    stringBuffer.append(string);
                    stringBuffer.reverse();
                    string = String.valueOf(stringBuffer);
                }
                int selection = binding.inputAmount.getSelectionStart();
                if ((binding.inputAmount.getText().toString().length() + 2) == string.length()) {
                    selection = selection + 2;
                } else if ((binding.inputAmount.getText().toString().length() + 1) == string.length()) {
                    selection = selection + 1;
                } else if ((binding.inputAmount.getText().toString().length() - 1) == string.length()) {
                    selection = selection - 1;
                } else if ((binding.inputAmount.getText().toString().length() - 2) == string.length()) {
                    selection = selection - 2;
                } else {
                    selection = binding.inputAmount.getSelectionStart();
                }

                binding.inputAmount.setText(string);
                try {
                    binding.inputAmount.setSelection(selection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
        transfer.registerCallbackConfirm(this);
        transfer.registerCallBack(this);
        smsWithTextSend = new SmsWithTextImpl();
        smsWithTextSend.registerSmsWithTextCallBack(this);
        transferByPhoneNumberAction = new TransferByPhoneNumberActionImpl();
        transferByPhoneNumberAction.registerAccDataPhoneNumberCallback(this);
    }

    //  ставим слушатели для кнопок
    private void setClickListeners() {
        clickSenderCardNumber();
        clickRecipientCardNumber();
        clickSelectCurrency();
        clickBtnTransferMoney();

        binding.contactsImageView.setOnClickListener(currencyTextView -> {
            Intent intent = new Intent(requireActivity(), SelectContactsActivity.class);
            startActivityForResult(intent, SELECT_CONTACT_REQUEST);
        });
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

    // действие при клике на поел ввода карты получателя
    private void clickRecipientCardNumber() {
        binding.inputPhoneNumber.setOnClickListener(editTextCardNumberTo -> {
            binding.textSenderCardNumber.setFocusable(true);
            binding.textSenderCardNumber.setFocusableInTouchMode(true);
            binding.textSenderCardNumber.requestFocus();
            binding.textSenderCardNumber.setError(getString(R.string.error_empty));
        });

        binding.inputPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    if (editable.length() < SIZE_PHONE_NUMBER_WITH_SYMBOLS) {
                        binding.contactFullName.setVisibility(View.GONE);
                        binding.textReceiverName.setVisibility(View.GONE);
                    }

                    if (editable.length() == SIZE_PHONE_NUMBER_WITH_SYMBOLS) {
                        fetchCardDetails();
                    }
                }
            }
        });

        binding.inputPhoneNumber.setOnClickListener(editTextCardNumberTo -> {
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
            openSelectCardScreen();
        });
        binding.layoutSenderCardDetails.getRoot().setOnClickListener(layout -> {
            openSelectCardScreen();
        });
    }

    private void openSelectCardScreen() {
        Intent intent = new Intent(getActivity(), SelectAccountActivity.class);
        intent.putExtra("isCards", true);
        startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
    }

    private void clickBtnTransferMoney() {
        binding.btnTransfer.setOnClickListener(btnTransfer -> {
            if (hasInternetConnection(requireContext())) {
                if (binding.btnTransfer.getText().toString().equals(getString(R.string.send_transfer))) {
                    fetchCardDetails();
                } else {
                    if (accountFrom instanceof UserAccounts.Cards) {
                        if (isCorrectInputData() && binding.btnTransfer.getText().toString().equals(getString(R.string.transfer_confirm))) {
                            binding.btnTransfer.setClickable(false);
                            transfer.registerCallbackConfirm(this);
                            transfer.confirmMt100Transfer(requireContext(), getBody());
                            binding.btnTransfer.setEnabled(false);
                        } else if (isCorrectInputData() && binding.btnTransfer.getText().toString().equals(getString(R.string.send_transfer))) {
                            transfer.registerCallbackConfirm(this);
                            transfer.checkMt100Transfer(requireContext(), getBody());
                        }
                    } else {
                        if (isCorrectInputData() && isBackPressed) {
                            transfer.checkMt100Transfer(requireContext(), getBody());
                        } else if (!isBackPressed) {
                            binding.btnTransfer.setClickable(false);
                            transfer.registerCallbackConfirm(this);
                            transfer.confirmMt100Transfer(requireContext(), getBody());
                            binding.btnTransfer.setEnabled(false);
                        }
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
        if (!binding.textReceiverName.isShown()) {
            String phoneNumberWithoutSpaces = PHONE_NUMBER_PREFIX + binding.inputPhoneNumber.getRawText();
            if (phoneNumberWithoutSpaces.length() == 12) {
                transferByPhoneNumberAction.getAccountDataByPhoneNumber(parentActivity, phoneNumberWithoutSpaces);
                isGetAccountDataRequested = true;
                cardTransferType = null;
                hideSoftKeyBoard(requireContext());
            }
        } else if (binding.btnTransfer.getText().toString().equals(getString(R.string.send_transfer))) {
            if (isCorrectInputData()) {
                transfer.checkMt100Transfer(requireContext(), getBody());
            }
        }
    }

    // проверка правильно ли введены данные пользователя
    public boolean isCorrectInputData() {
        if (binding.inputPhoneNumber.isShown()) {
            needSendFeeRespAfterGetAccData = binding.inputPhoneNumber.getText().toString().isEmpty();
        }

        boolean success = true;
        if (accountFrom != null) {
            ArrayList<TextView> fields = new ArrayList<>();
            if (!binding.inputAmount.getText().toString().isEmpty()) {
                if (binding.inputAmount.getText().toString().startsWith(".") || binding.inputAmount.getText().toString().startsWith(",")) {
                    binding.inputAmount.setError(getResources().getString(R.string.error_wrong_format));
                    binding.inputAmount.requestFocus();
                    return false;
                }
                if (!binding.inputAmount.getText().toString().isEmpty() && accountFrom.currency.equalsIgnoreCase(currency) &&
                        accountFrom.balance < getDoubleType(binding.inputAmount.getText().toString())) {
                    onError(getResources().getString(R.string.error_large_sum));
                    return false;
                }
                if (binding.textReceiverName.isShown()) {
                    fields.add(binding.textReceiverName);
                    if (binding.textReceiverName.getText().toString().split(" ").length < 2 && binding.textReceiverName.getText().toString().isEmpty()) {
                        binding.textReceiverName.setError(getString(R.string.input_first_last_name_error));
                        binding.textReceiverName.requestFocus();
                        success = false;
                    }
                }

                if (binding.inputPhoneNumber.isShown()) {
                    fields.add(binding.inputPhoneNumber);
                }
                fields.add(binding.inputAmount);
            } else {
                showSnackBar(getString(R.string.input_sum));
                binding.inputAmount.requestFocus();
                success = false;
            }

            if (binding.inputPhoneNumber.getText().toString().isEmpty()) {
                showSnackBar(getString(R.string.input_receiver_phone_number));
                binding.inputAmount.requestFocus();
                success = false;
            }

            for (TextView view : fields) {
                if (view.getText().toString().isEmpty()) {
                    view.setError(getResources().getString(R.string.error_empty));
                    view.requestFocus();
                    success = false;
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
                    binding.btnTransfer.setText(getString(R.string.send_transfer));
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
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    public void resetRecipientData() {
        binding.textReceiverName.setVisibility(View.VISIBLE);
        binding.textSenderCardNumber.setVisibility(View.GONE);
        binding.inputPhoneNumber.setVisibility(View.VISIBLE);
        binding.layoutSenderCardDetails.getRoot().setVisibility(View.GONE);
        accountTo = null;
        currency = accountFrom.currency;
        binding.currencyText.setText(accountFrom.currency);
        binding.inputPhoneNumber.getText().clear();
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

    private void addItemInCurrencyList(String item) {
        if (!stringList.contains(item)) {
            stringList.add(item);
        }
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
            binding.currencyText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    public String getNextPaymentAmount() {
        String amount = binding.inputAmount.getText().toString();
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
            if (accountFrom instanceof UserAccounts.Cards) {
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
            mt100TransferBody.contragentAccountNumber = contragentAccountNumber;
            if (binding.inputPhoneNumber.isShown())
                if (!binding.inputPhoneNumber.getText().toString().isEmpty())
                    mt100TransferBody.contragentName = binding.textReceiverName.getText().toString();
        }
        if (!isBackPressed) {
            mt100TransferBody.feeAmount = fee;
            mt100TransferBody.feeCurrency = feeCurrency;
        }
        mt100TransferBody.requestId = requestId;
        mt100TransferBody.receiverRequisites = PHONE_NUMBER_PREFIX + binding.inputPhoneNumber.getRawText();
        mt100TransferBody.transferMethod = "ByPhoneNumber";
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
        binding.contactsImageView.setEnabled(isEnable);
        binding.inputAmount.setEnabled(isEnable);
        binding.inputPhoneNumber.setEnabled(isEnable);
        binding.textReceiverName.setEnabled(isEnable);
        binding.textSenderCardNumber.setEnabled(isEnable);
        binding.currencyText.setEnabled(isEnable);
        binding.layoutSenderCardDetails.getRoot().setEnabled(isEnable);
        binding.evaluateFeeLayout.getRoot().setEnabled(isEnable);
    }

    @Override
    public void getAccDataByPhoneNumberResponse(int statusCode, String errorMessage, AccountPhoneNumberResponse response) {
        if (statusCode == Constants.SUCCESS) {
            if (response != null) {
                if (response.ownerName != null && !response.ownerName.isEmpty()) {
                    binding.textReceiverName.setVisibility(View.VISIBLE);
                    binding.textReceiverName.setText(response.ownerName);
                    contragentAccountNumber = response.getCardNumber();
                    isRightCard = true;
                }

                successCardBrandOrAccountCheck = true;
                binding.inputPhoneNumber.setError(null);
            } else {
                binding.inputPhoneNumber.requestFocus();
                Log.d("inputPhoneNumber", "Phone number has not found");
                successCardBrandOrAccountCheck = false;
            }
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            successCardBrandOrAccountCheck = false;
            onError(errorMessage);
        }
    }
}