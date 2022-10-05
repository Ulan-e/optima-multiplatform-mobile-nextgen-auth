package kz.optimabank.optima24.fragment.template;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.interfaces.CreateTemplate;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.CreateTemplateImpl;
import kz.optimabank.optima24.model.service.SmsWithTextImpl;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.DATE_FORMAT_FOR_REQEST;
import static kz.optimabank.optima24.utility.Constants.DATE_PICKER_TAG;
import static kz.optimabank.optima24.utility.Utilities.getDoubleType;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

/**
 * Created by Timur on 19.05.2017.
 */

public class CreateTemplateFragment extends ATFFragment implements View.OnClickListener, CreateTemplateImpl.Callback,
        TransferAndPaymentImpl.UpdateCallback, DatePickerDialog.OnDateSetListener, SmsWithTextImpl.SmsSendWithOperationCodeCallback {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edTemplateName)
    EditText edTemplateName;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.switchRegular)
    Switch switchRegular;
    @BindView(R.id.switch_auto_layout_wrapper)
    LinearLayout linSwitch;
    @BindView(R.id.timeBegin_linear)
    LinearLayout linTimeBegin;
    @BindView(R.id.linSpinnerFrom)
    LinearLayout linSpinnerFrom;
    @BindView(R.id.repeat_pay_linear)
    LinearLayout linRepeat;
    @BindView(R.id.tvTimeBegin)
    TextView tvTimeBegin;
    @BindView(R.id.tvSpinnerFrom)
    TextView tvSpinnerFrom;
    @BindView(R.id.tvRepeat)
    TextView tvRepeat;

    boolean isTransfer, isPayment, isTransferInterbankSwift;
    int sourceAccountId, serviceId, time = -1, DWM = 10;
    private Calendar calendar = Calendar.getInstance();
    String date;
    DatePickerDialog dateBeginDialog;
    JSONArray parameters = new JSONArray();
    String amount, documentId;
    CreateTemplate createTemplate;
    BodyModel.CreateTemplate createTemplateBody;
    TransferAndPayment transferAndPayment;
    AlertDialog.Builder builder;
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    SmsWithTextImpl smsSend;
    private String operationCode = String.valueOf(System.currentTimeMillis());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_template, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        initToolbar();

        Button btnShowReceipt = getActivity().findViewById(R.id.btn_show_receipt);
        btnShowReceipt.setVisibility(View.GONE);

        smsSend = new SmsWithTextImpl();
        smsSend.registerSmsWithOperationCodeCallBack(this);
        btnSave.setOnClickListener(this);
        switchRegular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    linTimeBegin.setVisibility(View.GONE);
                    linSpinnerFrom.setVisibility(View.GONE);
                    linRepeat.setVisibility(View.GONE);
                    tvTimeBegin.setText("");
                    tvSpinnerFrom.setText("");
                    tvRepeat.setText("");
                } else {
                    linTimeBegin.setVisibility(View.VISIBLE);
                    linSpinnerFrom.setVisibility(View.VISIBLE);
                    linRepeat.setVisibility(View.VISIBLE);
                }
            }
        });
        linTimeBegin.setOnClickListener(this);
        linSpinnerFrom.setOnClickListener(this);
        linRepeat.setOnClickListener(this);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateBeginDialog = DatePickerDialog.newInstance(this, year, month, day, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createTemplate = new CreateTemplateImpl();
        createTemplate.registerCallBack(this);

        transferAndPayment = new TransferAndPaymentImpl();
        transferAndPayment.registerUpdateCallBack(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                if (isPayment) {
                    if (switchRegular.isChecked()) {// TODO: 12.02.2021 createTemplateForPayment
                        if (isFieldsOfRegularTemplateNotFilled()) {
                            onError(getString(R.string._t_fill_all_required_));
                        } else {
                            smsSend.sendSmsWithOperationCode(requireContext(), Constants.CREATE_TEMPLATE_OTP_KEY, "0", operationCode);
                        }
                    } else {
                        if (isFieldsOfStandardTemplateNotFilled()) {
                            onError(getString(R.string._t_fill_all_required_));
                        } else {
                            createTemplate.createPaymentTemplate(requireContext(), getBody());
                        }
                    }
                } else {
                    if (switchRegular.isChecked()) {// TODO: 15.02.2021 createTemplateForTransfer
                        if (isFieldsOfRegularTemplateNotFilled()) {
                            onError(getString(R.string._t_fill_all_required_));
                        } else {
                            smsSend.sendSmsWithOperationCode(requireContext(), Constants.CREATE_TEMPLATE_OTP_KEY, "0", operationCode);
                        }
                    } else {
                        if (isFieldsOfStandardTemplateNotFilled()) {
                            onError(getString(R.string._t_fill_all_required_));
                        } else {
                            createTemplate.createTransferTemplate(requireContext(), getBody());
                        }
                    }
                }
                break;
            case R.id.timeBegin_linear:
                createDatePickerDialog(dateBeginDialog);
                break;
            case R.id.linSpinnerFrom:
                buildAlert(1);
                break;
            case R.id.repeat_pay_linear:
                buildAlert(2);
                break;
        }
    }

    public boolean isFieldsOfRegularTemplateNotFilled() {
        return tvTimeBegin.getText().toString().isEmpty() || tvSpinnerFrom.getText().toString().isEmpty() || tvRepeat.getText().toString().isEmpty() || edTemplateName.getText().toString().isEmpty();
    }

    public boolean isFieldsOfStandardTemplateNotFilled() {
        return edTemplateName.getText().toString().isEmpty();
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
                            tvSpinnerFrom.setText(finalMass[0]);
                            break;
                        case 1:
                            tvSpinnerFrom.setText(finalMass[1]);
                            break;
                        case 2:
                            tvSpinnerFrom.setText(finalMass[2]);
                            break;
                        case 3:
                            tvSpinnerFrom.setText(finalMass[3]);
                            break;
                        case 4:
                            tvSpinnerFrom.setText(finalMass[4]);
                            break;
                        case 5:
                            tvSpinnerFrom.setText(finalMass[5]);
                            break;
                        case 6:
                            tvSpinnerFrom.setText(finalMass[6]);
                            break;
                        case 7:
                            tvSpinnerFrom.setText(finalMass[7]);
                            break;
                        case 8:
                            tvSpinnerFrom.setText(finalMass[8]);
                            break;
                        case 9:
                            tvSpinnerFrom.setText(finalMass[9]);
                            break;
                        case 10:
                            tvSpinnerFrom.setText(finalMass[10]);
                            break;
                        case 11:
                            tvSpinnerFrom.setText(finalMass[11]);
                            break;
                        case 12:
                            tvSpinnerFrom.setText(finalMass[12]);
                            break;
                        case 13:
                            tvSpinnerFrom.setText(finalMass[13]);
                            break;
                        case 14:
                            tvSpinnerFrom.setText(finalMass[14]);
                            break;
                        case 15:
                            tvSpinnerFrom.setText(finalMass[15]);
                            break;
                        case 16:
                            tvSpinnerFrom.setText(finalMass[16]);
                            break;
                        case 17:
                            tvSpinnerFrom.setText(finalMass[17]);
                            break;
                        case 18:
                            tvSpinnerFrom.setText(finalMass[18]);
                            break;
                        case 19:
                            tvSpinnerFrom.setText(finalMass[19]);
                            break;
                        case 20:
                            tvSpinnerFrom.setText(finalMass[20]);
                            break;
                        case 21:
                            tvSpinnerFrom.setText(finalMass[21]);
                            break;
                        case 22:
                            tvSpinnerFrom.setText(finalMass[22]);
                            break;
                        case 23:
                            tvSpinnerFrom.setText(finalMass[23]);
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

    private void createDatePickerDialog(DatePickerDialog datePickerDialog) {
        if (datePickerDialog != null) {
            datePickerDialog.setStartDate(year, month, day);
            datePickerDialog.show(requireActivity().getSupportFragmentManager(), DATE_PICKER_TAG);
        }
    }

    @Override
    public void onCreatePaymentTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            transferAndPayment.getPaymentSubscriptions(getActivity());
            onSuccess(requireActivity(), getResources().getString(R.string.template_create_successfully));
        } else {
            onError(errorMessage);
        }
    }

    @Override
    public void onCreateTransferTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            onSuccess(requireActivity(), getResources().getString(R.string.template_create_successfully));
            transferAndPayment.getTransferTemplate(getActivity());
        } else {
            onError(errorMessage);
        }
    }

    @Override
    public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.getInstance().setNeedToUpdatePayTempl(true);
        } else {
            onError(errorMessage);
        }
    }

    @Override
    public void jsonTransferSubscriptionsResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.getInstance().setNeedToUpdateTemplate(true);
        } else {
            onError(errorMessage);
        }
    }

    private void getBundle() {
        if (getArguments() != null) {
            isTransfer = getArguments().getBoolean("isTransfer", false);
            isPayment = getArguments().getBoolean("isPayment", false);
            isTransferInterbankSwift = getArguments().getBoolean("isTransferInterBankSwift", false);
            boolean isSwift = getArguments().getBoolean("isSwift", false);
            if (isPayment) {
                amount = getArguments().getString("amount");
                sourceAccountId = getArguments().getInt("sourceAccountId", -1000);
                serviceId = getArguments().getInt("serviceId", -1000);
                parameters = GeneralManager.getInstance().getPaymentParameters();
            } else if (isTransferInterbankSwift || isSwift) {
                linSwitch.setVisibility(View.GONE);
                documentId = GeneralManager.getInstance().getTransferDocumentId();
            } else {
                amount = getArguments().getString("amount");
                sourceAccountId = getArguments().getInt("sourceAccountId", -1000);
                serviceId = getArguments().getInt("serviceId", -1000);
                documentId = GeneralManager.getInstance().getTransferDocumentId();
            }
        }
    }

    private void initToolbar() {
        tvTitle.setText(getString(R.string.create_template_));
        toolbar.setTitle("");
        ((SmsConfirmActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
    }

    private JSONObject getBody() {
        createTemplateBody = new BodyModel.CreateTemplate();
        createTemplateBody.IsAutoPay = switchRegular.isChecked();
        if (switchRegular.isChecked()) {
            createTemplateBody.AutoPayType = DWM != 10 ? String.valueOf(DWM) : null;
            createTemplateBody.StartPayDay = date;
            if (time == -1)
                createTemplateBody.AutoPayTime = "";
            createTemplateBody.AutoPayTime = tvSpinnerFrom.getText().toString() + ":00.0000000";
        }
        createTemplateBody.Name = edTemplateName.getText().toString();
        if (isPayment) {
            createTemplateBody.Amount = getDoubleType(amount).toString();
            createTemplateBody.SourceAccountId = String.valueOf(sourceAccountId);
            createTemplateBody.ServiceId = String.valueOf(serviceId);
            createTemplateBody.Parameters = parameters;
        } else {
            createTemplateBody.documentId = documentId;
        }
        return getFieldNamesAndValues(createTemplateBody);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        GregorianCalendar date = new GregorianCalendar(year, month, day);
        CharSequence dateString = Constants.DAY_MONTH_YEAR_FORMAT.format(date.getTime());
        tvTimeBegin.setError(null);
        tvTimeBegin.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
        tvTimeBegin.setText(dateString);
        calendar = date;
        this.date = DATE_FORMAT_FOR_REQEST.format(date.getTime());
    }

    @Override
    public void onSmsOperationCodeResponse(int statusCode, String errorMessage, Integer errorCode) {
        if (statusCode == Constants.SUCCESS) {
            @SuppressLint("SimpleDateFormat") String dateInString = new SimpleDateFormat(Constants.COMMON_DATE_FORMAT).format(new Date());
            Intent intent = new Intent(requireContext(), SmsConfirmActivity.class);
            intent.putExtra("isCreateTemplate", true);
            intent.putExtra("isAutoPay", switchRegular.isChecked());
            intent.putExtra("amount", amount);
            intent.putExtra("serviceId", String.valueOf(serviceId));
            intent.putExtra("startPayDay", String.valueOf(date));
            intent.putExtra("autoPayType", DWM != 10 ? String.valueOf(DWM) : null);
            intent.putExtra("autoPayTime", tvSpinnerFrom.getText().toString());
            intent.putExtra("name", edTemplateName.getText().toString());
            intent.putExtra("sourceAccountId", sourceAccountId);
            intent.putExtra("documentId", documentId);
            intent.putExtra("autoPayDate", dateInString);
            intent.putExtra("parameters", parameters.toString());
            intent.putExtra("operationCode", operationCode);
            startActivity(intent);
            requireActivity().finish();
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        } else {
            onError(errorMessage);
        }
    }
}