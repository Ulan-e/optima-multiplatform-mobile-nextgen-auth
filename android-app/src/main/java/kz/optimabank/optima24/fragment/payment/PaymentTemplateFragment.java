package kz.optimabank.optima24.fragment.payment;

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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.activity.TemplateActivity;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.PaymentTemplateOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.PaymentTemplateOperationImpl;
import kz.optimabank.optima24.model.service.SmsWithTextImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.CREATE_TEMPLATE_OTP_KEY;
import static kz.optimabank.optima24.utility.Constants.CURRENCY_KGS;
import static kz.optimabank.optima24.utility.Constants.DATE_FORMAT_FOR_REQEST;
import static kz.optimabank.optima24.utility.Constants.DATE_PICKER_TAG;
import static kz.optimabank.optima24.utility.Constants.DAY_MONTH_YEAR_FORMAT;
import static kz.optimabank.optima24.utility.Constants.PAYMENT_OTP_KEY;
import static kz.optimabank.optima24.utility.Constants.SELECT_ACCOUNT_FROM_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

import com.fourmob.datetimepicker.date.DatePickerDialog;

public class PaymentTemplateFragment extends PaymentFragment implements View.OnClickListener,
        PaymentTemplateOperationImpl.CallbackChangePayment, PaymentTemplateOperationImpl.CallbackOperationPayment, DatePickerDialog.OnDateSetListener, SmsWithTextImpl.SmsSendWithOperationCodeCallback, SmsWithTextImpl.SmsSendWithTextForPaymentCallback {
    private static final String TAG = PaymentTemplateFragment.class.getSimpleName();

    TemplatesPayment templatesPayment;
    PaymentCategory paymentCategory;
    PaymentService paymentService;
    PaymentContextController paymentController;
    BodyModel.CreateTemplate createTemplateBody;
    PaymentTemplateOperation paymentTemplateOperation;
    int actionTag;
    int AutoPayTypeInt, time = -1, DWM = 10;
    boolean isMobile = false, processAfterSaving, isConfirm, isChange;
    String isTTF;
    String date;
    String[] mass;

    private Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
  //  DatePickerDialog dateBeginDialog = DatePickerDialog.newInstance(this, year, month, day, false);
    AlertDialog.Builder builder;

    SmsWithTextImpl smsSend;
    private String operationCode = String.valueOf(System.currentTimeMillis());
    private static final String IS_PAYMENT  = "isPayment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        paymentTemplateOperation = new PaymentTemplateOperationImpl();
        paymentTemplateOperation.registerCallBackChange(this);
        paymentTemplateOperation.registerCallBack(this);
     //   dateBeginDialog.setStartDate(year, month, day);

        smsSend = new SmsWithTextImpl();
        smsSend.registerSmsWithOperationCodeCallBack(this);
        smsSend.registerSmsWithTextForPayment(this);
        return super.onCreateView(inflater, container, savedInstanceState);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.paymentService = paymentService;
        if (templatesPayment != null) {
            if (isMobile) {
                isAutoPay(0);
                if (!templatesPayment.parameters.isEmpty() && !templatesPayment.parameters.get(0).getValue().equals("")) {
                    edMobileNum.setText(templatesPayment.parameters.get(0).getValue());
                }
            } else {
                if (paymentService != null) {
                    isAutoPay(0);
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
                    initView(linAddFields);
                    try {
                        for (int i = 0; i <= paymentService.parameters.size(); i++) {
                            if (paymentCategory.alias.equalsIgnoreCase("telecom") && paymentService.parameters.size() <= 1) {
                                paramEditTexts[i].edittext.setText("7" + templatesPayment.parameters.get(i).getValue());
                            } else {
                                paramEditTexts[i].edittext.setText(templatesPayment.parameters.get(i).getValue());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            edAmount.setText(templatesPayment.getAmount());

            if (actionTag == Constants.TAG_CHANGE) {
                isAutoPay(actionTag);
                btnPayment.setText(getString(R.string.save));
                processAfterSaving = false;
            } else {
                processAfterSaving = true;
            }
            setTemplateName();
        }
        if (getArguments() != null) {
            templatesPayment = (TemplatesPayment) getArguments().getSerializable("template");
        }
        checkAutoContainer();
        showOrHideAutoContainer();
        switchRegularListener();
    }

    private void switchRegularListener() {
        switchRegular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonsAutoLayoutWrapper.setVisibility(View.VISIBLE);
                } else {
                    buttonsAutoLayoutWrapper.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkAutoContainer() {
        if (btnPayment.getText().toString().equals(getString(R.string.pay_action))) {
            autoLayoutWrapper.setVisibility(View.GONE);
        } else {
            autoLayoutWrapper.setVisibility(View.VISIBLE);
        }
    }

    private void showOrHideAutoContainer() {
        if (switchRegular.isChecked()) {
            buttonsAutoLayoutWrapper.setVisibility(View.VISIBLE);
        } else {
            buttonsAutoLayoutWrapper.setVisibility(View.GONE);
        }
    }

    private void isAutoPay(int AT) {
        if (actionTag == Constants.TAG_CHANGE) {
            switchRegular.setClickable(true);
            linTemplateName.setVisibility(View.VISIBLE);
            if (templatesPayment.isAutoPay) {
                linTimeBegin.setOnClickListener(this);
                linRePayTime.setOnClickListener(this);
                linRepeat.setOnClickListener(this);

                try {
                    autoLayoutWrapper.setVisibility(View.VISIBLE);
                    switchRegular.setChecked(templatesPayment.autoPayStatus2 == 1);
                    tvRePayTime.setText(templatesPayment.AutoPayTime.substring(0, 5));
                    AutoPayTypeInt = templatesPayment.AutoPayType;
                    mass = getResources().getStringArray(R.array.auto_payment_type);
                    if (AutoPayTypeInt == 1) {
                        tvRepeat.setText(mass[0]);
                    } else if (AutoPayTypeInt == 2) {
                        tvRepeat.setText(mass[1]);
                    } else if (AutoPayTypeInt == 3) {
                        tvRepeat.setText(mass[2]);
                    }

                    DWM = AutoPayTypeInt;
                    date = (templatesPayment.StartPayDay);
                    tvTimeBegin.setText(DAY_MONTH_YEAR_FORMAT.format(DATE_FORMAT_FOR_REQEST.parse(templatesPayment.StartPayDay)));
                } catch (Exception ignore) {
                }
            } else {
                try {
                    DWM = AutoPayTypeInt;
                    date = (templatesPayment.StartPayDay);
                } catch (Exception ignored) {
                }

                autoLayoutWrapper.setVisibility(View.VISIBLE);
                linTimeBegin.setOnClickListener(this);
                linRePayTime.setOnClickListener(this);
                linRepeat.setOnClickListener(this);
            }
        } else if (AT == 0) {
            linTemplateName.setVisibility(View.GONE);
            if (templatesPayment.isAutoPay) {
                try {
                    switchRegular.setClickable(false);
                    linTimeBegin.setOnClickListener(null);
                    linRePayTime.setOnClickListener(null);
                    linRepeat.setOnClickListener(null);
                    switchRegular.setChecked(templatesPayment.autoPayStatus2 == 1);
                    tvRePayTime.setText(templatesPayment.AutoPayTime.substring(0, 5));
                    AutoPayTypeInt = templatesPayment.AutoPayType;
                    mass = parentActivity.getResources().getStringArray(R.array.auto_payment_type);
                    date = templatesPayment.StartPayDay;
                    checkAutoContainer();
                    if (AutoPayTypeInt == 1) {
                        tvRepeat.setText(mass[0]);
                    } else if (AutoPayTypeInt == 2) {
                        tvRepeat.setText(mass[1]);
                    } else if (AutoPayTypeInt == 3) {
                        tvRepeat.setText(mass[2]);
                    }
                    if (templatesPayment.StartPayDay != null) {
                        tvTimeBegin.setText(DAY_MONTH_YEAR_FORMAT.format(DATE_FORMAT_FOR_REQEST.parse(templatesPayment.StartPayDay)));
                    }
                } catch (Exception ignore) {
                }
            } else {
                autoLayoutWrapper.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (paymentController != null) {
            paymentController.close();
        }
    }

    /*private void createDatePickerDialog(DatePickerDialog datePickerDialog) {
        if (datePickerDialog != null) {
            datePickerDialog.show(requireActivity().getSupportFragmentManager(), DATE_PICKER_TAG);
        }
    }*/

    @Override
    public void onClick(View view) {
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
                if (checkFieldForChangeTemplate()) {
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
                        isChange = true;
                        Log.d(TAG, "getBody() = " + getBody(false));
                        if (switchRegular.isChecked()) {
                            smsSend.sendSmsWithOperationCode(
                                    requireContext(),
                                    CREATE_TEMPLATE_OTP_KEY,
                                    "0",
                                    operationCode);
                        } else {
                            paymentTemplateOperation.changePaymentTemplate(
                                    requireContext(),
                                    getBody(false),
                                    templatesPayment.id,
                                    processAfterSaving);
                        }
                        GeneralManager.getInstance().setNeedToUpdatePayTempl(true);
                    } else if (actionTag == Constants.CLICK_ITEM_TAG) {
                        if(checkField()){
                            if (isCheck) {
                                payments.checkPayments(
                                        requireContext(),
                                        false,
                                        getBody(false));
                            } else if (!isConfirmOnProcess) {
                                isConfirm = true;
                                isConfirmOnProcess = true;
                                if (mIsNeedSmsConfirmation) {
                                    GeneralManager
                                            .getInstance()
                                            .setPaymentParameters(getBodyParameters(paymentService));
                                    smsSend.sendSmsWithTextForPayment(
                                            requireContext(),
                                            PAYMENT_OTP_KEY,
                                            getDoubleType(edAmount.getText().toString())
                                                    + " " + CURRENCY_KGS, operationCode);
                                } else {
                                    payments.registerPaymentConfirmCallBack(this);
                                    payments.confirmPayments(requireContext(), getBodyForPay());
                                }
                                isConfirmOnProcess = true;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void buildAlert(final int is) {
        builder = new AlertDialog.Builder(requireContext());
        String[] mass = new String[24];

        View v = View.inflate(requireContext(), R.layout.auto_payments_alert, null);
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
    public void getBundle() {
        if (getArguments() != null) {
            isTTF = getArguments().getString("isTTF");
            templatesPayment = (TemplatesPayment) getArguments().getSerializable("template");
            actionTag = getArguments().getInt("actionTag");
            if (templatesPayment != null) {
                paymentController = PaymentContextController.getController();
                categoryName = templatesPayment.name;
                paymentService = paymentController.getPaymentServiceById(templatesPayment.serviceId);
                if (paymentService != null) {
                    paymentCategory = paymentController.getPaymentCategoryByServiceId(paymentService.paymentCategoryId);
                }
                if (paymentCategory != null) {
                    isMobile = paymentCategory.alias.equals("mobile");
                }
                UserAccounts account = GeneralManager.getInstance().getAccountByCode(templatesPayment.sourceAccountId);
                setAccountSpinnerFrom(account);
                super.accountFrom = account;
                super.paymentCategory = paymentCategory;
                super.isMobile = isMobile;
                super.isTemplate = true;
            }
        }
    }

    @Override
    public void initToolbar() {
        if (categoryName != null) {
            tvTitle.setText(categoryName);
        }
        toolbar.setTitle("");
        ((TemplateActivity) requireActivity()).setSupportActionBar(toolbar);
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

    public JSONObject getBodyForPay() {
        paymentCheckBody = new BodyModel.PaymentCheck();
        if (Float.valueOf(edAmount.getText().toString().isEmpty() ? "0" : getDoubleType(edAmount.getText().toString()).toString()) > 0) {
            paymentCheckBody.Amount = edAmount.getText().toString().isEmpty() ? "0" : getDoubleType(edAmount.getText().toString()).toString();
        }
        //paymentCheckBody.amount = edAmount.getText().toString().isEmpty() ? "0" : getDoubleType(edAmount.getText().toString()).toString();
        paymentCheckBody.isAutoPay = false;
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
        }
        if (!isCheck) {
            paymentCheckBody.AccountId = String.valueOf(accountFrom.code);
            paymentCheckBody.FixComm = fixComm;
            paymentCheckBody.PrcntComm = prcntComm;
            paymentCheckBody.MinComm = minComm;
            paymentCheckBody.ProvReference = provReference;
            paymentCheckBody.ConfirmCode = "";
        }
        return getFieldNamesAndValues(paymentCheckBody);
    }

    @Override
    public JSONObject getBody(boolean isCheckBalance) {
        if (isChange || isConfirm) {
            createTemplateBody = new BodyModel.CreateTemplate();
            createTemplateBody.Name = edTemplateName.getText().toString();

            createTemplateBody.IsAutoPay = switchRegular.isChecked();
            if (switchRegular.isChecked()) {
                createTemplateBody.AutoPayType = DWM != 10 ? String.valueOf(DWM) : null;
                createTemplateBody.StartPayDay = date;
                if (time == -1)
                    createTemplateBody.AutoPayTime = "";
                createTemplateBody.AutoPayTime = tvRePayTime.getText().toString() + ":00.0000000";
            }
            createTemplateBody.Amount = getDoubleType(edAmount.getText().toString()).toString();
            createTemplateBody.SourceAccountId = String.valueOf(accountFrom.code);
            createTemplateBody.ServiceId = String.valueOf(paymentService.id);
            createTemplateBody.Parameters = getBodyParameters(paymentService);

        } else {
            return super.getBody(isCheckBalance);
        }
        return getFieldNamesAndValues(createTemplateBody);
    }

    @Override
    public void onChangePaymentTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            getBundle();
            Intent intent = new Intent(requireActivity(), SmsConfirmActivity.class);
            try {
                if(actionTag == Constants.CHECK_TAG){
                    intent.putExtra(IS_PAYMENT, false);
                }else{
                    intent.putExtra(IS_PAYMENT, true);
                }
                intent.putExtra("isSuccess", true);
                intent.putExtra("isTTF", isTTF);
                intent.putExtra("isTTA", 1);
                intent.putExtra("isChange", isChange);
                intent.putExtra("isTemplate", isTemplate);
                intent.putExtra("feeWithAmount", sumWithAmount);
                intent.putExtra("paymentTitle", categoryName);
                intent.putExtra("operationCurrency", accountFrom.currency);
            } catch (Exception e) {
                Toast.makeText(requireContext(), getString(R.string.exception_data), Toast.LENGTH_LONG).show();
            }
            isConfirm = false;
            isChange = false;
            requireActivity().startActivity(intent);
            requireActivity().finish();
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void setTemplateName() {
        linTemplateName.setVisibility(View.VISIBLE);
        separatorTemplateName.setVisibility(View.VISIBLE);
        edTemplateName.setText(templatesPayment.name);
    }

    @Override
    public void deletePaymentTemplateResponse(int statusCode, String errorMessage) {

    }

    @Override
    public void quickPaymentResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.getInstance().setPaymentParameters(getBodyParameters(paymentService));
            Intent intent = new Intent(requireContext(), SmsConfirmActivity.class);
            try {
                intent.putExtra("isPayment", true);
                intent.putExtra("isSuccess", true);
                intent.putExtra("isTTF", isTTF);
                intent.putExtra("isTTA PTF", 1);
                Log.i("isTTF", "" + isTTF);
                intent.putExtra("isChange", isChange);
                intent.putExtra("isTemplate", isTemplate);
                intent.putExtra("feeWithAmount", sumWithAmount);
                intent.putExtra("paymentTitle", categoryName);
                intent.putExtra("operationCurrency", accountFrom.currency);
            } catch (Exception e) {
                Toast.makeText(requireContext(), getString(R.string.exception_data), Toast.LENGTH_LONG).show();
            }
            isConfirm = false;
            isChange = false;
            startActivity(intent);
            requireActivity().finish();
        } else {
            onError(errorMessage);
        }
    }

    @Override
    public void changeActivePaymentTemplate(int statusCode, String errorMessage) {

    }

    @Override
    public void onSmsOperationCodeResponse(int statusCode, String errorMessage, Integer errorCode) {
        if (statusCode == Constants.SUCCESS) {
            String pattern = "dd.MM.yyyy";
            @SuppressLint("SimpleDateFormat") String dateInString = new SimpleDateFormat(pattern).format(new Date());
            Intent intent = new Intent(requireContext(), SmsConfirmActivity.class);
            GeneralManager.getInstance().setPaymentParameters(getBodyParameters(paymentService));
            intent.putExtra("isPaymentTemplateChange", true);
            intent.putExtra("isAutoPay", switchRegular.isChecked());
            intent.putExtra("amount", edAmount.getText().toString());
            intent.putExtra("serviceId", paymentService.getId());
            intent.putExtra("startPayDay", String.valueOf(date));
            intent.putExtra("autoPayType", DWM != 10 ? String.valueOf(DWM) : null);
            intent.putExtra("autoPayTime", tvRePayTime.getText().toString());
            intent.putExtra("name", edTemplateName.getText().toString());
            intent.putExtra("sourceAccountId", createTemplateBody.SourceAccountId);
            intent.putExtra("autoPayDate", dateInString);
            intent.putExtra("templateId", templatesPayment.getId());
            intent.putExtra("processAfterSaving", true);
            intent.putExtra("operationCode", operationCode);
            startActivity(intent);
        } else {
            onError(errorMessage);
        }
    }


    @Override
    public void onSmsOperationForPaymentResponse(int statusCode, String errorMessage, Integer errorCode) {
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
            intent.putExtra("feeSum", tvFee.getText().toString().replaceAll(" ", ""));  // Коммиссия
            intent.putExtra("paymentFee", fee);
            intent.putExtra("sumWithAmount", tvSumWithFee.getText().toString());//Сумма с коммисией
            intent.putExtra("operationCode", operationCode);
            intent.putExtra("isTemplate", true);
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
}
