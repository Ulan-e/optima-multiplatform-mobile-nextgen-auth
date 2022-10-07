package kz.optimabank.optima24.fragment.transfer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AlertDialog;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SelectParameterActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.activity.TemplateActivity;
import kz.optimabank.optima24.db.controllers.DictionaryController;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.model.base.TemplateTransfer;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.TransferTemplateOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.SmsWithTextImpl;
import kz.optimabank.optima24.model.service.TransferTemplateOperationImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.CREATE_TEMPLATE_OTP_KEY;
import static kz.optimabank.optima24.utility.Constants.DATE_FORMAT_FOR_REQEST;
import static kz.optimabank.optima24.utility.Constants.DATE_PICKER_TAG;
import static kz.optimabank.optima24.utility.Constants.DAY_MONTH_YEAR_FORMAT;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_CITIZENSHIP_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_CURRENCY_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_KNP_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.TransferMoneyMasterToMaster;
import static kz.optimabank.optima24.utility.Constants.TransferMoneyVisaToVisa;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;
import static kz.optimabank.optima24.utility.Utilities.hasInternetConnection;

import com.fourmob.datetimepicker.date.DatePickerDialog;

public class TransferTemplateFragment extends TransferAccountsFragment implements View.OnClickListener,
        TransferTemplateOperationImpl.CallbackChangeTransfer, DatePickerDialog.OnDateSetListener, SmsWithTextImpl.SmsSendWithOperationCodeCallback {
    private static final String TAG = TransferTemplateFragment.class.getSimpleName();

    TemplateTransfer templateTransfer;
    int actionTag, productType, AutoPayTypeInt, time = 10, DWM = 10;
    String title;
    String[] mass;
    DictionaryController dictionaryController;
    TransferTemplateOperation transferTemplateOperation;
    String isTTF, date;
    String isTransferAtTempl;
    private Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog dateBeginDialog = DatePickerDialog.newInstance(this, year, month, day, false);
    AlertDialog.Builder builder;
    SmsWithTextImpl smsSend;
    private String templateOperationCode = String.valueOf(System.currentTimeMillis());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getBundle();
        isTemplate = true;
        transferTemplateOperation = new TransferTemplateOperationImpl();
        transferTemplateOperation.registerCallBackChange(this);
        dateBeginDialog.setStartDate(year, month, day);
        smsSend = new SmsWithTextImpl();
        smsSend.registerSmsWithOperationCodeCallBack(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (templateTransfer != null) {
            if (accountFrom != null) {
                setAccountSpinnerFrom(accountFrom);
            }
            setEdSpinnerToParams();
            if (actionTag == Constants.TAG_CHANGE) {
                setTemplateName();
                btnTransfer.setText(getString(R.string.save));
            }
            isAutoPay();
            edAmount.setText(templateTransfer.getAmount() + "0");               //был баг(http://support.rdplus.kz/issues/16) - исправил с таким костылем
            edAmount.setText(templateTransfer.getAmount());
            currency = templateTransfer.getCurrency();
            tvCurrency.setText(templateTransfer.getCurrency());
            if (templateTransfer.getDestinationAccountId() != 0) {
                if (productType == TransferMoneyVisaToVisa || productType == TransferMoneyMasterToMaster) {
                    receiverForm.setVisibility(View.VISIBLE);
                    etReceiverName.setText(templateTransfer.getContragentName());
                }
                super.accountTo = GeneralManager.getInstance().getAccountByCode(templateTransfer.getDestinationAccountId());
                if (accountTo instanceof UserAccounts.EncryptedCard) {
                    setEncryptedCardTo(accountTo);
                } else {
                    setAccountSpinnerTo(accountTo);
                    if ((accountFrom != null && accountFrom instanceof UserAccounts.Cards && accountTo == null) || (accountFrom == null && accountTo == null)) {
                        scan.setVisibility(View.VISIBLE);
                    } else
                        scan.setVisibility(View.GONE);
                    getCurrencyAccountTo();
                    clickSpinnerTo();
                }
            } else if (productType == TransferMoneyVisaToVisa || productType == TransferMoneyMasterToMaster) {
                showVisaAndMasterForm();
            } else if (templateTransfer.getContragentAccountNumber() != null) {
                templateAccToNumb = templateTransfer.getContragentAccountNumber();
                edSpinnerTo.setText(templateTransfer.getContragentAccountNumber());
                if (accountFrom instanceof UserAccounts.CheckingAccounts ||
                        accountFrom instanceof UserAccounts.CardAccounts ||
                        accountFrom instanceof UserAccounts.WishAccounts) {
                    setInsideBankParams();
                }
            }
            if (templateTransfer.getContragentName() != null) {
                etReceiverName.setText(templateTransfer.getContragentName());
            }
            successCardBrandOrAccountCheck = true;
        }
        checkForAutoContainer();
        showOrHideAutoContainer();
        switchRegularListener();
    }

    private void switchRegularListener() {
        switchRegular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttons_auto_layout_wrapper.setVisibility(View.VISIBLE);
                } else {
                    buttons_auto_layout_wrapper.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showOrHideAutoContainer() {
        if (switchRegular.isChecked()) {
            buttons_auto_layout_wrapper.setVisibility(View.VISIBLE);
        } else {
            buttons_auto_layout_wrapper.setVisibility(View.GONE);
        }
    }


    private void checkForAutoContainer() {
        if (btnTransfer.getText().toString().equals(getString(R.string.send_transfer))) {
            autoLayoutWrapper.setVisibility(View.GONE);
        } else {
            autoLayoutWrapper.setVisibility(View.VISIBLE);
        }
    }

    private void isAutoPay() {
        if (actionTag == Constants.TAG_CHANGE) {
            switchRegular.setClickable(true);
            if (templateTransfer.StandingInstruction) {
                timeBeginLinear.setOnClickListener(this);
                regularPayTimeLinear.setOnClickListener(this);
                repeatPayLinear.setOnClickListener(this);
                try {
                    autoLayoutWrapper.setVisibility(View.VISIBLE);
                    switchRegular.setChecked(templateTransfer.StandingInstructionStatus == 1);
                    tvRePayTime.setText(templateTransfer.StandingInstructionTime.substring(0, 5));
                    AutoPayTypeInt = templateTransfer.StandingInstructionType;
                    mass = getResources().getStringArray(R.array.auto_payment_type);
                    DWM = AutoPayTypeInt;
                    date = (templateTransfer.StartPayDay);
                    if (AutoPayTypeInt == 1) {
                        tvRepeat.setText(mass[0]);
                    } else if (AutoPayTypeInt == 2) {
                        tvRepeat.setText(mass[1]);
                    } else if (AutoPayTypeInt == 3) {
                        tvRepeat.setText(mass[2]);
                    }
                    tvTimeBegin.setText(DAY_MONTH_YEAR_FORMAT.format(Objects.requireNonNull(DATE_FORMAT_FOR_REQEST.parse(templateTransfer.StartPayDay))));
                } catch (Exception ignore) {
                }
            } else {
                try {
                    DWM = AutoPayTypeInt;
                    date = (templateTransfer.StartPayDay);
                } catch (Exception ignored) {
                }

                if (btnTransfer.getText().equals(getString(R.string.save))) {
                    autoLayoutWrapper.setVisibility(View.VISIBLE);
                    timeBeginLinear.setOnClickListener(this);
                    regularPayTimeLinear.setOnClickListener(this);
                    repeatPayLinear.setOnClickListener(this);
                } else {
                    autoLayoutWrapper.setVisibility(View.GONE);
                }
            }
        } else if (templateTransfer.StandingInstruction) {
            switchRegular.setClickable(false);
            timeBeginLinear.setOnClickListener(null);
            regularPayTimeLinear.setOnClickListener(null);
            repeatPayLinear.setOnClickListener(null);
            try {
                autoLayoutWrapper.setVisibility(View.VISIBLE);
                switchRegular.setChecked(templateTransfer.StandingInstructionStatus == 1);
                tvRePayTime.setText(templateTransfer.StandingInstructionTime.substring(0, 5));
                AutoPayTypeInt = templateTransfer.StandingInstructionType;
                mass = getResources().getStringArray(R.array.auto_payment_type);
                date = templateTransfer.StartPayDay;
                if (AutoPayTypeInt == 1) {
                    tvRepeat.setText(mass[0]);
                } else if (AutoPayTypeInt == 2) {
                    tvRepeat.setText(mass[1]);
                } else if (AutoPayTypeInt == 3) {
                    tvRepeat.setText(mass[2]);
                }
                tvTimeBegin.setText(DAY_MONTH_YEAR_FORMAT.format(Objects.requireNonNull(DATE_FORMAT_FOR_REQEST.parse(templateTransfer.StartPayDay))));
            } catch (Exception ignore) {
            }
        } else {
            autoLayoutWrapper.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.information_menu, menu);
        if (productType == TransferMoneyVisaToVisa || productType == TransferMoneyMasterToMaster) {
            menu.findItem(R.id.information).setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onChangeTransferTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            getBundle();
            Intent intent = new Intent(getActivity(), SmsConfirmActivity.class);
            intent.putExtra("isTransfer", true);
            intent.putExtra("isSuccess", true);
            intent.putExtra("isChange", true);
            intent.putExtra("isTTF", isTTF);
            intent.putExtra("isTTA", 0);
            intent.putExtra("isTransferAtTempl", isTransferAtTempl);
            intent.putExtra("isTemplate", isTemplate);
            requireActivity().startActivity(intent);
            requireActivity().finish();
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void createDatePickerDialog(DatePickerDialog datePickerDialog) {
        if (datePickerDialog != null) {
            datePickerDialog.show(requireActivity().getSupportFragmentManager(), DATE_PICKER_TAG);
        }
    }

    private void buildAlert(final int is) {
        builder = new AlertDialog.Builder(getActivity());
        String[] mass = new String[24];

        View v = View.inflate(getActivity(), R.layout.auto_payments_alert, null);
        final CheckBox cb1 = v.findViewById(R.id.cb1);
        final CheckBox cb2 = v.findViewById(R.id.cb2);
        final CheckBox cb3 = v.findViewById(R.id.cb3);
        if (is == 1) {
            final CheckBox cb4 = v.findViewById(R.id.cb4);
            cb4.setVisibility(View.VISIBLE);
            final CheckBox cb5 = v.findViewById(R.id.cb5);
            cb5.setVisibility(View.VISIBLE);
            final CheckBox cb6 = v.findViewById(R.id.cb6);
            cb6.setVisibility(View.VISIBLE);
            final CheckBox cb7 = v.findViewById(R.id.cb7);
            cb7.setVisibility(View.VISIBLE);
            final CheckBox cb8 = v.findViewById(R.id.cb8);
            cb8.setVisibility(View.VISIBLE);
            final CheckBox cb9 = v.findViewById(R.id.cb9);
            cb9.setVisibility(View.VISIBLE);
            final CheckBox cb10 = v.findViewById(R.id.cb10);
            cb10.setVisibility(View.VISIBLE);
            final CheckBox cb11 = v.findViewById(R.id.cb11);
            cb11.setVisibility(View.VISIBLE);
            final CheckBox cb12 = v.findViewById(R.id.cb12);
            cb12.setVisibility(View.VISIBLE);
            final CheckBox cb13 = v.findViewById(R.id.cb13);
            cb13.setVisibility(View.VISIBLE);
            final CheckBox cb14 = v.findViewById(R.id.cb14);
            cb14.setVisibility(View.VISIBLE);
            final CheckBox cb15 = v.findViewById(R.id.cb15);
            cb15.setVisibility(View.VISIBLE);
            final CheckBox cb16 = v.findViewById(R.id.cb16);
            cb16.setVisibility(View.VISIBLE);
            final CheckBox cb17 = v.findViewById(R.id.cb17);
            cb17.setVisibility(View.VISIBLE);
            final CheckBox cb18 = v.findViewById(R.id.cb18);
            cb18.setVisibility(View.VISIBLE);
            final CheckBox cb19 = v.findViewById(R.id.cb19);
            cb19.setVisibility(View.VISIBLE);
            final CheckBox cb20 = v.findViewById(R.id.cb20);
            cb20.setVisibility(View.VISIBLE);
            final CheckBox cb21 = v.findViewById(R.id.cb21);
            cb21.setVisibility(View.VISIBLE);
            final CheckBox cb22 = v.findViewById(R.id.cb22);
            cb22.setVisibility(View.VISIBLE);
            final CheckBox cb23 = v.findViewById(R.id.cb23);
            cb23.setVisibility(View.VISIBLE);
            final CheckBox cb24 = v.findViewById(R.id.cb24);
            cb24.setVisibility(View.VISIBLE);
            final CheckBox[] arrayCB = new CheckBox[]{cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11, cb12, cb13, cb14, cb15, cb16, cb17, cb18, cb19, cb20, cb21, cb22, cb23, cb24};
            mass = getResources().getStringArray(R.array.auto_payment_times);
            if (tvRePayTime.getText().toString() != null || tvRePayTime.getText().toString() != "") {
                String selected = tvRePayTime.getText().toString();
                for (int i = 0; i < mass.length; i++) {
                    if (selected.equals(mass[i])) {
                        arrayCB[i].setChecked(true);
                    }
                }
            }
            cb1.setText(mass[0]);
            cb2.setText(mass[1]);
            cb3.setText(mass[2]);
            cb4.setText(mass[3]);
            cb5.setText(mass[4]);
            cb6.setText(mass[5]);
            cb7.setText(mass[6]);
            cb8.setText(mass[7]);
            cb9.setText(mass[8]);
            cb10.setText(mass[9]);
            cb11.setText(mass[10]);
            cb12.setText(mass[11]);
            cb13.setText(mass[12]);
            cb14.setText(mass[13]);
            cb15.setText(mass[14]);
            cb16.setText(mass[15]);
            cb17.setText(mass[16]);
            cb18.setText(mass[17]);
            cb19.setText(mass[18]);
            cb20.setText(mass[19]);
            cb21.setText(mass[20]);
            cb22.setText(mass[21]);
            cb23.setText(mass[22]);
            cb24.setText(mass[23]);
            for (int i = 0; i < arrayCB.length; i++) {
                int finalJ = i;
                arrayCB[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        for (int k = 0; k < arrayCB.length; k++) {
                            arrayCB[k].setChecked(false);
                        }
                        arrayCB[finalJ].setChecked(isChecked);
                        time = finalJ;
                    }
                });
            }
        }
        if (is == 2) {
            mass = getResources().getStringArray(R.array.auto_payment_type);
            cb1.setText(mass[0]);
            cb2.setText(mass[1]);
            cb3.setVisibility(View.VISIBLE);
            cb3.setText(mass[2]);

            cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    cb1.setChecked(isChecked);
                    if (is == 1) {
                        time = 1;
                    }
                    if (is == 2) {
                        DWM = 1;
                    }
                }
            });
            cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cb1.setChecked(false);
                    cb3.setChecked(false);
                    cb2.setChecked(isChecked);
                    if (is == 1) {
                        time = 2;
                    }
                    if (is == 2) {
                        DWM = 2;
                    }
                }
            });
            cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    cb3.setChecked(isChecked);
                    if (is == 1) {
                        time = 3;
                    }
                    if (is == 2) {
                        DWM = 3;
                    }
                }
            });
        }

        builder.setView(v);

        final String[] finalMass = mass;
        builder.setPositiveButton(getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (is == 1) {
                    switch (time) {
                        case 0:
                            tvRePayTime.setText(finalMass[0]);
                            break;
                        case 1:
                            tvRePayTime.setText(finalMass[1]);
                            break;
                        case 2:
                            tvRePayTime.setText(finalMass[2]);
                            break;
                        case 3:
                            tvRePayTime.setText(finalMass[3]);
                            break;
                        case 4:
                            tvRePayTime.setText(finalMass[4]);
                            break;
                        case 5:
                            tvRePayTime.setText(finalMass[5]);
                            break;
                        case 6:
                            tvRePayTime.setText(finalMass[6]);
                            break;
                        case 7:
                            tvRePayTime.setText(finalMass[7]);
                            break;
                        case 8:
                            tvRePayTime.setText(finalMass[8]);
                            break;
                        case 9:
                            tvRePayTime.setText(finalMass[9]);
                            break;
                        case 10:
                            tvRePayTime.setText(finalMass[10]);
                            break;
                        case 11:
                            tvRePayTime.setText(finalMass[11]);
                            break;
                        case 12:
                            tvRePayTime.setText(finalMass[12]);
                            break;
                        case 13:
                            tvRePayTime.setText(finalMass[13]);
                            break;
                        case 14:
                            tvRePayTime.setText(finalMass[14]);
                            break;
                        case 15:
                            tvRePayTime.setText(finalMass[15]);
                            break;
                        case 16:
                            tvRePayTime.setText(finalMass[16]);
                            break;
                        case 17:
                            tvRePayTime.setText(finalMass[17]);
                            break;
                        case 18:
                            tvRePayTime.setText(finalMass[18]);
                            break;
                        case 19:
                            tvRePayTime.setText(finalMass[19]);
                            break;
                        case 20:
                            tvRePayTime.setText(finalMass[20]);
                            break;
                        case 21:
                            tvRePayTime.setText(finalMass[21]);
                            break;
                        case 22:
                            tvRePayTime.setText(finalMass[22]);
                            break;
                        case 23:
                            tvRePayTime.setText(finalMass[23]);
                            break;
                    }
                }
                if (is == 2) {
                    switch (DWM) {
                        case 1:
                            tvRepeat.setText(finalMass[0]);
                            break;
                        case 2:
                            tvRepeat.setText(finalMass[1]);
                            break;
                        case 3:
                            tvRepeat.setText(finalMass[2]);
                            break;
                    }
                }
            }
        });
        builder.setNegativeButton(getString(R.string._no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.timeBegin_linear:
                //createDatePickerDialog(dateBeginDialog);
                break;
            case R.id.regular_pay_time_linear:
                buildAlert(1);
                break;
            case R.id.repeat_pay_linear:
                buildAlert(2);
                break;
            case R.id.linSpinnerFrom:
                intent = new Intent(getActivity(), SelectAccountActivity.class);
                if (accountTo != null && accountTo instanceof UserAccounts.CheckingAccounts) {
                    intent.putExtra("exceptElcart", true);
                }
                intent.putExtra("transferAccFrom", true);
                startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
                break;
            case R.id.imgSpinnerTo:
                actionForSpinnerTo();
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
                    if (btnTransfer.getText().toString().equals(getString(R.string.send_transfer))) {
                        fetchAccountData(); // TODO: 17.03.2021
                    } else if (btnTransfer.getText().toString().equals(getString(R.string.transfer_confirm))) {
                        btnTransfer.setClickable(false);
                        transfer.registerCallbackConfirm(this);
                        transfer.confirmMt100Transfer(requireContext(), addFieldTemplateIdToJson());
                        btnTransfer.setEnabled(false);
                        btnTransfer.setClickable(false);
                    }
                    if (checkField()) {
                        if (switchRegular.isChecked()) {
                            if (tvTimeBegin.getText().toString().isEmpty()) {
                                tvTimeBegin.setError(getResources().getString(R.string.error_empty));
                                tvTimeBegin.requestFocus();
                            }
                            if (tvRePayTime.getText().toString().isEmpty()) {
                                tvRePayTime.setError(getResources().getString(R.string.error_empty));
                                tvRePayTime.requestFocus();
                            }
                            if (tvRepeat.getText().toString().isEmpty()) {
                                tvRepeat.setError(getResources().getString(R.string.error_empty));
                                tvRepeat.requestFocus();
                            }
                        }
                        if (actionTag == Constants.TAG_CHANGE) {
                            if (edTemplateName.isShown()) {
                                if (edTemplateName.getText().toString().isEmpty()) {
                                    edTemplateName.setError(getString(R.string.error_empty));
                                    return;
                                }
                            }
                            if ((accountFrom instanceof UserAccounts.Cards || accountFrom instanceof UserAccounts.CheckingAccounts) && accountTo == null) {
                                if (edSpinnerTo.getText().toString().replaceAll(" ", "").replaceAll("[0-9]", "").length() <= 0) {
                                    if (switchRegular.isChecked()) {
                                        smsSend.sendSmsWithOperationCode(requireContext(), CREATE_TEMPLATE_OTP_KEY, "0", templateOperationCode);
                                    } else {
                                        transferTemplateOperation.changeTransferTemplate(requireContext(), getTemplateBody(), templateTransfer.getId());
                                    }
                                    GeneralManager.getInstance().setNeedToUpdatePayTempl(true);
                                } else if ((accountFrom instanceof UserAccounts.Cards || accountFrom instanceof UserAccounts.CheckingAccounts) && accountTo == null) {
                                    if (edSpinnerTo.getText().toString().replaceAll(" ", "").replaceAll("[0-9]", "").length() <= 0) {
                                        if (isBackPressed && !needSendFeeRespAfterGetAccData) {
                                            isAccountToEncrypted = edSpinnerTo.getText().toString().equals(EncryCardNumber);
                                            transfer.checkMt100Transfer(requireContext(), addFieldTemplateIdToJson());
                                        } else if (!isBackPressed) {
                                            btnTransfer.setClickable(false);
                                            transfer.registerCallbackConfirm(this);
                                            transfer.confirmMt100Transfer(requireContext(), addFieldTemplateIdToJson());
                                            btnTransfer.setEnabled(false);
                                            btnTransfer.setClickable(false);
                                        }
                                    }
                                } else if (isBackPressed) {
                                    isAccountToEncrypted = edSpinnerTo.getText().toString().equals(EncryCardNumber);
                                    transfer.checkMt100Transfer(requireContext(), addFieldTemplateIdToJson());
                                } else {
                                    btnTransfer.setClickable(false);
                                    transfer.registerCallbackConfirm(this);
                                    transfer.confirmMt100Transfer(requireContext(), addFieldTemplateIdToJson());
                                    btnTransfer.setEnabled(false);
                                    btnTransfer.setClickable(false);
                                }
                            } else if (accountTo != null && btnTransfer.getText().toString().equals(getString(R.string.save))) { // TODO: 17.03.2021 change transfer template
                                if (switchRegular.isChecked()) {
                                    smsSend.sendSmsWithOperationCode(requireContext(), CREATE_TEMPLATE_OTP_KEY, "0", templateOperationCode);
                                } else {
                                    transferTemplateOperation.changeTransferTemplate(requireContext(), getTemplateBody(), templateTransfer.getId());
                                }
                                GeneralManager.getInstance().setNeedToUpdatePayTempl(true);
                            }
                        }
                    }
                }
                break;
            case R.id.linSelectCountry:
                intent = new Intent(parentActivity, SelectParameterActivity.class);
                intent.putExtra("parameterName", getString(R.string.country));
                startActivityForResult(intent, SELECT_COUNTRY_FOR_REG_MASTERCARD_REQUEST_CODE);
                break;
        }
    }

    private JSONObject addFieldTemplateIdToJson() {
        JSONObject object = null;
        try {
            object = getBody(templateTransfer.getProductType()).put("TemplateId", 0);
        } catch (JSONException e) {
            Log.i("TranferTemplateFragment", "failed put = TemplateId " + templateTransfer.getId());
        }
        return object;
    }

    @Override
    public void initToolbar() {
        if (templateTransfer != null) {
            title = templateTransfer.getName();
        }
        tvTitle.setText(title);
        toolbar.setTitle("");
        ((TemplateActivity) requireContext()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {
            if (isBackPressed) {
                requireActivity().onBackPressed();
            } else {
                actionBack();
            }
        });
    }

    private void getBundle() {
        if (getArguments() != null) {
            isTTF = getArguments().getString("isTTF");
            isTransferAtTempl = getArguments().getString("isTransferAtTempl");
            templateTransfer = (TemplateTransfer) getArguments().getSerializable("template");
            if (templateTransfer != null) {
                actionTag = getArguments().getInt("actionTag");
                productType = templateTransfer.getProductType();
                switch (productType) {
                    case TransferMoneyVisaToVisa:
                        cardTransferType = CardTransferType.VISA;
                        break;
                    case TransferMoneyMasterToMaster:
                        cardTransferType = CardTransferType.MASTERCARD;
                        break;
                }
                super.accountFrom = GeneralManager.getInstance().getAccountByCode(templateTransfer.getSourceAccountId());
            }
        }
    }

    @Override
    protected void prepareLayout(CardTransferType cardTransferType) {
        super.prepareLayout(cardTransferType);
        if (actionTag == Constants.TAG_CHANGE) {
            btnTransfer.setText(getString(R.string.save));
        }
    }

    private void showVisaAndMasterForm() {
        receiverForm.setVisibility(View.VISIBLE);
        etReceiverName.setText(templateTransfer.getContragentName());
        templateAccToNumb = templateTransfer.getContragentAccountNumber();
        StringBuffer stringBuffer = new StringBuffer();
        String string = templateTransfer.getContragentAccountNumber().replaceAll(" ", "");
        stringBuffer.append(string);
        for (int i = 0; i < 4; i++) {
            int pos;
            pos = stringBuffer.lastIndexOf(" ");
            if (pos == -1) {
                if (edSpinnerTo.getText().length() > 4)
                    stringBuffer.insert(4, " ");
            } else {
                if (pos > 0) {
                    if (edSpinnerTo.getText().length() > pos + 5) {
                        stringBuffer.insert(pos + 5, " ");
                    }
                }
            }
        }
        edSpinnerTo.setText(stringBuffer);
        isClickableSpinnerCurrency(false);
    }

    private void setInsideBankParams() {
        etReceiverName.setText(templateTransfer.getContragentName());
        edReceiverIIN.setText(templateTransfer.getContragentIdn());
        if (templateTransfer.getContragentResidency() == 0) {
            isResident = false;
            tvSpinnerCitizenship.setText(getResources().getString(R.string.text_not_resident));
        } else if (templateTransfer.getContragentResidency() == 1) {
            isResident = true;
            tvSpinnerCitizenship.setText(getResources().getString(R.string.text_resident));
        }
        dictionaryController = DictionaryController.getController();
        edPurpose.setText(templateTransfer.getOperationPurpose());
        if (getKnp() != null) {
            tvSpinnerKNP.setText(getKnp().getDescription());
        }
    }

    private Dictionary getKnp() {
        return knp = dictionaryController.getKnpByCode(String.valueOf(templateTransfer.getOperationKnp()));
    }

    private void setTemplateName() {
        edTemplateName.setVisibility(View.VISIBLE);
        edTemplateName.setText(templateTransfer.getName());
    }

    private JSONObject getTemplateBody() {
        templateTransfer.code = -1000;
        templateTransfer.Name = edTemplateName.getText().toString();
        templateTransfer.Amount = getDoubleType(edAmount.getText().toString());
        templateTransfer.Currency = currency;
        templateTransfer.SourceAccountId = accountFrom.code;
        mt100TransferBody = new BodyModel.Mt100Transfer();
        templateTransfer.StandingInstruction = switchRegular.isChecked();
        if (switchRegular.isChecked()) {
            templateTransfer.StandingInstructionType = DWM != 10 ? DWM : 3;
            templateTransfer.StartPayDay = date;
            if (time == -1)
                templateTransfer.StandingInstructionTime = "";
            templateTransfer.StandingInstructionTime = tvRePayTime.getText().toString() + ":00.0000000";
        }
        if (accountTo != null && isAccountToEncrypted) {
            templateTransfer.DestinationAccountId = accountTo.code;
        } else {
            templateTransfer.ContragentAccountNumber = edSpinnerTo.getText().toString().replaceAll(" ", "");
            if (etReceiverName.isShown())
                if (!etReceiverName.getText().toString().isEmpty())
                    templateTransfer.ContragentName = etReceiverName.getText().toString();
        }
        return getFieldNamesAndValues(templateTransfer);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        GregorianCalendar date = new GregorianCalendar(year, month, day);
        CharSequence dateString = DAY_MONTH_YEAR_FORMAT.format(date.getTime());
        this.date = DATE_FORMAT_FOR_REQEST.format(date.getTime());
        tvTimeBegin.setError(null);
        tvTimeBegin.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
        tvTimeBegin.setText(dateString);
        calendar = date;
    }

    @Override
    public void onSmsOperationCodeResponse(int statusCode, String errorMessage, Integer errorCode) {
        @SuppressLint("SimpleDateFormat") String dateInString = new SimpleDateFormat(Constants.COMMON_DATE_FORMAT).format(new Date());
        if (statusCode == Constants.SUCCESS) {
            Intent intent = new Intent(getContext(), SmsConfirmActivity.class);
            intent.putExtra("isTransferTemplateChange", true);
            intent.putExtra("amount", edAmount.getText().toString());
            intent.putExtra("standingInstruction", switchRegular.isChecked());
            intent.putExtra("startPayDay", date);
            intent.putExtra("standingInstructionType", DWM);
            intent.putExtra("standingInstructionTime", tvRePayTime.getText().toString());
            intent.putExtra("productType", productType);
            intent.putExtra("name", edTemplateName.getText().toString());
            intent.putExtra("createDate", date);
            intent.putExtra("standingInstuctionDate", dateInString);
            intent.putExtra("currency", currency);
            intent.putExtra("standingInstructionStatus", 1);
            intent.putExtra("contragentAccountNumber", templateTransfer.getContragentAccountNumber());
            intent.putExtra("sourceAccountId", templateTransfer.getSourceAccountId());
            intent.putExtra("templateId", templateTransfer.getId());
            intent.putExtra("contragentBic", templateTransfer.ContragentBic);
            intent.putExtra("ContragentCardBrandType", templateTransfer.ContragentCardBrandType);
            intent.putExtra("ContragentIdn", templateTransfer.ContragentIdn);
            intent.putExtra("ContragentName", templateTransfer.getContragentName());
            intent.putExtra("ContragentResidency", templateTransfer.ContragentResidency);
            intent.putExtra("ContragentSeco", templateTransfer.ContragentSeco);
            intent.putExtra("DestinationAccountId", templateTransfer.getDestinationAccountId());
            intent.putExtra("id", templateTransfer.getId());
            intent.putExtra("OperationKnp", templateTransfer.OperationKnp);
            intent.putExtra("TransferType", templateTransfer.getTransferType());
            intent.putExtra("UserId", templateTransfer.getUserId());
            intent.putExtra("operationCode", templateOperationCode);
            startActivity(intent);
        } else {
            errorDialog(errorMessage);
        }
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
}
