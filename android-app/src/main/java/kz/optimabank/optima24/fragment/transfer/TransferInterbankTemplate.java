package kz.optimabank.optima24.fragment.transfer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import kg.optima.mobile.R;
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
import static kz.optimabank.optima24.utility.Constants.SELECT_BIC_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_KNP_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.TRANSFER_INTERBANK_TYPE_CODE;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

/**
 * Created by Timur on 12.06.2017.
 */

public class TransferInterbankTemplate extends TransferInterbank implements TransferTemplateOperationImpl.CallbackChangeTransfer, SmsWithTextImpl.SmsSendWithOperationCodeCallback, View.OnClickListener/*, DatePickerDialog.OnDateSetListener*/ {
    TemplateTransfer templateTransfer;
    int actionTag, time = 10, dayWeekMonth = 10;
    String[] mass;
    TransferTemplateOperation transferTemplateOperation;
    DictionaryController dictionaryController;
    String isTTF, date;
    String isTransferAtTempl;
    SmsWithTextImpl smsSend;
    AlertDialog.Builder builder;
    private Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    //DatePickerDialog dateBeginDialog = DatePickerDialog.newInstance(this, year, month, day, false);
    private int AutoPayTypeInt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        smsSend = new SmsWithTextImpl();
        smsSend.registerSmsWithOperationCodeCallBack(this);
        getBundle();
        transferTemplateOperation = new TransferTemplateOperationImpl();
        transferTemplateOperation.registerCallBackChange(this);
        dictionaryController = DictionaryController.getController();
       // dateBeginDialog.setStartDate(year, month, day);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setSwitchRegularListener() {
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


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isTemplate = true;
        if (actionTag == Constants.TAG_CHANGE) {
            setTemplateName();
            btnTransfer.setText(getString(R.string.save));
        }
        setAccountSpinnerFrom(accountFrom);
        setTemplateParams();
        checkForAutoPay();
        showOrHideAutoContainer();
        setSwitchRegularListener();
    }


    @Override
    public void initToolbar() {
        tvTitle.setText(templateTransfer.getName());
        toolbar.setTitle("");
        ((TemplateActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dictionaryController != null) {
            dictionaryController.close();
        }
    }

    private void getBundle() {
        if (getArguments() != null) {
            isTTF = getArguments().getString("isTTF");
            isTransferAtTempl = getArguments().getString("isTransferAtTempl");
            templateTransfer = (TemplateTransfer) getArguments().getSerializable("template");
            Log.d(TAG, "templateTransfer = " + templateTransfer);
            if (templateTransfer != null) {
                actionTag = getArguments().getInt("actionTag");
                super.accountFrom = GeneralManager.getInstance().getAccountByCode(templateTransfer.getSourceAccountId());
            }
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
                        dayWeekMonth = 1;
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
                        dayWeekMonth = 2;
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
                        dayWeekMonth = 3;
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
                    switch (dayWeekMonth) {
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

   /* private void createDatePickerDialog(DatePickerDialog datePickerDialog) {
        if (datePickerDialog != null) {
            datePickerDialog.show(requireActivity().getSupportFragmentManager(), DATE_PICKER_TAG);
        }
    }*/

    private void checkForAutoPay() {
        if (actionTag == Constants.TAG_CHANGE) {
            switchRegular.setClickable(true);
            if (templateTransfer.StandingInstruction) {
                linTimeBegin.setOnClickListener(this);
                linRePayTime.setOnClickListener(this);
                linRepeat.setOnClickListener(this);
                try {
                    autoLayoutWrapper.setVisibility(View.VISIBLE);
                    switchRegular.setChecked(templateTransfer.StandingInstructionStatus == 1);
                    Log.i("DATE", "DATE = templateTransfer.StandingInstructionDate = " + templateTransfer.StandingInstructionDate);
                    tvRePayTime.setText(templateTransfer.StandingInstructionTime.substring(0, 5));
                    AutoPayTypeInt = templateTransfer.StandingInstructionType;
                    mass = getResources().getStringArray(R.array.auto_payment_type);
                    dayWeekMonth = AutoPayTypeInt;
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
                    dayWeekMonth = AutoPayTypeInt;
                    date = (templateTransfer.StartPayDay);
                } catch (Exception ignored) {
                }

                if (btnTransfer.getText().equals(getString(R.string.save))) {
                    autoLayoutWrapper.setVisibility(View.VISIBLE);
                    linTimeBegin.setOnClickListener(this);
                    linRePayTime.setOnClickListener(this);
                    linRepeat.setOnClickListener(this);
                } else {
                    autoLayoutWrapper.setVisibility(View.GONE);
                }
            }
        } else if (templateTransfer.StandingInstruction) {
            switchRegular.setClickable(false);
            linTimeBegin.setOnClickListener(null);
            linRePayTime.setOnClickListener(null);
            linRepeat.setOnClickListener(null);
            try {
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
                checkAutoContainer();
            } catch (Exception ignore) {
            }
        } else {
            autoLayoutWrapper.setVisibility(View.GONE);
        }
    }

    private void checkAutoContainer() {
        if (btnTransfer.getText().toString().equals(getString(R.string.send_transfer))) {
            autoLayoutWrapper.setVisibility(View.GONE);
        } else {
            autoLayoutWrapper.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linSpinnerFrom:
                intent = new Intent(getActivity(), SelectAccountActivity.class);
                intent.putExtra("checkOnlyKZT", true);
                startActivityForResult(intent, SELECT_ACCOUNT_FROM_REQUEST_CODE);
                break;
            case R.id.linSpinnerKNP:
                intent = new Intent(getActivity(), SelectParameterActivity.class);
                intent.putExtra("parameterName", getResources().getString(R.string.penalty_knp));
                startActivityForResult(intent, SELECT_KNP_REQUEST_CODE);
                break;
            case R.id.linSpinnerBIC:
                intent = new Intent(getActivity(), SelectParameterActivity.class);
                intent.putExtra("parameterName", getResources().getString(R.string.text_bic));
                startActivityForResult(intent, SELECT_BIC_REQUEST_CODE);
                break;
            case R.id.btnTransfer:
                if (checkField()) {
                    if (actionTag == Constants.TAG_CHANGE) {
                        if (edTemplateName.isShown()) {
                            if (edTemplateName.getText().toString().isEmpty()) {
                                edTemplateName.setError(getString(R.string.error_empty));
                                return;
                            }
                        }
                        if (switchRegular.isChecked()) {
                            smsSend.sendSmsWithOperationCode(requireContext(), CREATE_TEMPLATE_OTP_KEY, "0", operationCode);
                        } else {
                            templateTransfer.setStartPayDay(null);
                            templateTransfer.createDate = null;
                            templateTransfer.setStandingInstructionTime(null);
                            templateTransfer.setStandingInstructionDate(null);
                            templateTransfer.setStandingInstructionType(0);
                            templateTransfer.setStandingInstruction(false);
                            transferTemplateOperation.changeTransferTemplate(requireContext(), getTemplateBody(), templateTransfer.getId());
                            GeneralManager.getInstance().setNeedToUpdatePayTempl(true);
                        }
                    } else {
                        transfer.checkMt100Transfer(requireContext(), getBody());
                    }
                }
                break;
            case R.id.tvTransferType:
                stringList.clear();
                stringList.add(getResources().getString(R.string.transfer_type_cleering));
                stringList.add(getResources().getString(R.string.transfer_type_gross));
                intent = new Intent(getActivity(), SelectAccountActivity.class);
                intent.putExtra("isTransferType", true);
                intent.putExtra("transferTypeList", stringList);
                startActivityForResult(intent, TRANSFER_INTERBANK_TYPE_CODE);
                break;
            case R.id.timeBegin_linear:
                // createDatePickerDialog(dateBeginDialog);
                break;
            case R.id.regular_pay_time_linear:
                buildAlert(1);
                break;
            case R.id.repeat_pay_linear:
                buildAlert(2);
                break;
        }
    }

    @Override
    public void onChangeTransferTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            getBundle();
            Intent intent = new Intent(getActivity(), SmsConfirmActivity.class);
            intent.putExtra("isTransfer", true);
            intent.putExtra("isSuccess", true);
            intent.putExtra("isTTF", isTTF);
            intent.putExtra("isTTA", 0);
            intent.putExtra("isTransferAtTempl", isTransferAtTempl);
            intent.putExtra("isChange", true);
            intent.putExtra("isTemplate", true);
            requireActivity().startActivity(intent);
            requireActivity().finish();
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void setTemplateName() {
        edTemplateName.setVisibility(View.VISIBLE);
        edTemplateName.setText(templateTransfer.getName());
    }

    private void setTemplateParams() {
        edAmount.setText(templateTransfer.getAmount());
        currency = templateTransfer.getCurrency();
        tvCurrency.setText(templateTransfer.getCurrency());
        etReceiverName.setText(templateTransfer.getContragentName());
        edSpinnerTo.setText(templateTransfer.getContragentAccountNumber());
        edPurpose.setText(templateTransfer.getOperationPurpose());
        if (templateTransfer.getTransferType() == 1) {
            tvTransferType.setText(getResources().getString(R.string.transfer_type_cleering));
            transferTypeCode = 1;
        } else {
            transferTypeCode = 2;
            tvTransferType.setText(getResources().getString(R.string.transfer_type_gross));
        }
        if (getKnp() != null) {
            tvSpinnerKNP.setText(getKnp().getDescription());
        }
        if (getBic() != null) {
            tvSpinnerBIC.setText(getBic().getDescription());
        }
        if (templateTransfer.getContragentResidency() == 0) {
            isResident = false;
        } else if (templateTransfer.getContragentResidency() == 1) {
            isResident = true;
        }
    }


    private Dictionary getKnp() {
        return knp = dictionaryController.getKnpByCode(String.valueOf(templateTransfer.getOperationKnp()));
    }

    private Dictionary getBic() {
        return bic = dictionaryController.getBicByCode(String.valueOf(templateTransfer.getContragentBic()));
    }

    private JSONObject getTemplateBody() {
        templateTransfer.code = -1000;
        templateTransfer.Name = edTemplateName.getText().toString();
        templateTransfer.Amount = getDoubleType(edAmount.getText().toString());
        templateTransfer.Currency = currency;
        mt100TransferBody = new BodyModel.Mt100Transfer();
        if (accountFrom instanceof UserAccounts.Cards) {
            mt100TransferBody.accountCode = ((UserAccounts.Cards) accountFrom).cardAccountCode;
        } else {
            mt100TransferBody.accountCode = accountFrom.code;
            templateTransfer.ProductType = Constants.TransferMoneyToAnotherBank;
            templateTransfer.ContragentAccountNumber = edSpinnerTo.getText().toString();
            templateTransfer.ContragentName = etReceiverName.getText().toString();
            templateTransfer.OperationPurpose = edPurpose.getText().toString();
        }
        if (knp != null) {
            templateTransfer.OperationKnp = knp.getCode();
        }
        if (bic != null) {
            templateTransfer.ContragentBic = bic.getCode();
        }
        return getFieldNamesAndValues(templateTransfer);
    }

/*
    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        GregorianCalendar date = new GregorianCalendar(year, month, day);
        CharSequence dateString = DAY_MONTH_YEAR_FORMAT.format(date.getTime());
        this.date = DATE_FORMAT_FOR_REQEST.format(date.getTime());
        tvTimeBegin.setError(null);
        tvTimeBegin.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
        tvTimeBegin.setText(dateString);
        calendar = date;
    }*/

    @Override
    public void onSmsOperationCodeResponse(int statusCode, String errorMessage, Integer errorCode) {
        @SuppressLint("SimpleDateFormat") String dateInString = new SimpleDateFormat(Constants.COMMON_DATE_FORMAT).format(new Date());
        if (statusCode == Constants.SUCCESS) {
            String knpVariable, bicVariable;
            Intent intent = new Intent(requireContext(), SmsConfirmActivity.class);
            intent.putExtra("isInterbankTemplate", true);
            intent.putExtra("templateName", edTemplateName.getText().toString());
            intent.putExtra("amount", getDoubleType(edAmount.getText().toString()));
            intent.putExtra("currency", currency);
            intent.putExtra("productType", Constants.TransferMoneyToAnotherBank);
            intent.putExtra("contragentAccountNumber", edSpinnerTo.getText().toString());
            intent.putExtra("contragentName", etReceiverName.getText().toString());
            intent.putExtra("operationPurpose", edPurpose.getText().toString());
            intent.putExtra("knp", knpVariable = knp.getCode() == null ? "null" : knp.getCode());
            intent.putExtra("contragentBic", bicVariable = bic.getCode() == null ? "null" : bic.getCode());
            intent.putExtra("standingInstruction", switchRegular.isChecked());
            intent.putExtra("startPayDay", date);
            intent.putExtra("standingInstructionTime", tvRePayTime.getText().toString());
            intent.putExtra("createDate", date);
            intent.putExtra("standingInstructionType", dayWeekMonth);
            intent.putExtra("standingInstuctionDate", dateInString);
            intent.putExtra("standingInstructionStatus", templateTransfer.getStandingInstructionStatus());
            intent.putExtra("templateId", templateTransfer.getId());
            intent.putExtra("UserId", templateTransfer.getUserId());
            intent.putExtra("DestinationAccountId", templateTransfer.getDestinationAccountId());
            intent.putExtra("TransferType", templateTransfer.getTransferType());
            intent.putExtra("sourceAccountId", templateTransfer.getSourceAccountId());
            intent.putExtra("ContragentResidency", templateTransfer.ContragentResidency);
            intent.putExtra("ContragentSeco", templateTransfer.ContragentSeco);
            intent.putExtra("operationCode", operationCode);
            startActivity(intent);
        } else {
            onError(errorMessage);
        }
    }
}
