package kz.optimabank.optima24.fragment.payment.invoice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.activity.SmsConfirmActivity;
import kz.optimabank.optima24.activity.TemplateActivity;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.gson.BodyModel;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.PaymentTemplateOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.PaymentTemplateOperationImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.DATE_FORMAT_FOR_REQEST;
import static kz.optimabank.optima24.utility.Constants.DATE_PICKER_TAG;
import static kz.optimabank.optima24.utility.Constants.DAY_MONTH_YEAR_FORMAT;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;

/**
  Created by Timur on 04.08.2017.
 */

public class ChangeInvoiceTemplate extends ATFFragment implements View.OnClickListener,
        PaymentTemplateOperationImpl.CallbackChangePayment, DatePickerDialog.OnDateSetListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.edTemplateName)
    EditText edTemplateName;
    @BindView(R.id.edTemplateNumber)
    EditText edTemplateNumber;
    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.timeBegin_linear)
    LinearLayout linTimeBegin;
    @BindView(R.id.regular_pay_time_linear)
    LinearLayout linRePayTime;
    @BindView(R.id.repeat_pay_linear)
    LinearLayout linRepeat;

    @BindView(R.id.auto_layout_wrapper)
    LinearLayout isAuto;
    @BindView(R.id.switchRegular)
    Switch switchRegular;
    @BindView(R.id.tvTimeBegin)
    TextView tvTimeBegin;
    @BindView(R.id.tvRePayTime)
    TextView tvRePayTime;
    @BindView(R.id.tvRepeat)
    TextView tvRepeat;

    TemplatesPayment templatesPayment;
    PaymentTemplateOperation paymentTemplateOperation;
    BodyModel.CreateTemplate createTemplateBody;
    PaymentContextController paymentController;
    UserAccounts account;
    AlertDialog.Builder builder;
    private Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog dateBeginDialog = DatePickerDialog.newInstance(this, year, month, day, false);
    String isTTF;
    String date;
    String[] mass;
    int time=-1,DWM=10,AutoPayTypeInt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.change_invoice_template, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        if(templatesPayment!=null) {
            initToolbar();
            btnSave.setOnClickListener(this);

            edTemplateNumber.setFocusable(false);
            edTemplateNumber.setEnabled(false);
            edTemplateName.setText(templatesPayment.name);
            try {
                if (templatesPayment.parameters.size() > 0)
                    edTemplateNumber.setText(templatesPayment.parameters.get(0).getValue());
            }catch (Exception ignored){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.template_error);
                builder.setMessage(R.string.template_errorMessage);
                builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        getActivity().onBackPressed();
                    }
                });
                builder.create();
                builder.show();
            }
            try {
                if (templatesPayment.isAutoPay) {
                    switchRegular.setChecked(templatesPayment.autoPayStatus2 == 1);
                    tvTimeBegin.setText(DAY_MONTH_YEAR_FORMAT.format(DATE_FORMAT_FOR_REQEST.parse(templatesPayment.StartPayDay)));
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
//                    time = tvRePayTime.getText().toString().equals("09:00:00.0000000") ? 1 : 2;
                    date = templatesPayment.StartPayDay;
                    DWM = AutoPayTypeInt;
                }else{
                    switchRegular.setChecked(false);
                }
            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.template_error);
                builder.setMessage(R.string.template_errorMessage);
                builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        getActivity().onBackPressed();
                    }
                });
                builder.create();
                builder.show();
            }
        }
        dateBeginDialog.setStartDate(year, month, day);
        linTimeBegin.setOnClickListener(this);
        linRePayTime.setOnClickListener(this);
        linRepeat.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(paymentController!=null) {
            paymentController.close();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave:
                if (switchRegular.isChecked()) {
                    if (tvTimeBegin.getText().toString().isEmpty()) {
                        tvTimeBegin.setError(getString(R.string.error_empty));
                        tvTimeBegin.requestFocus();
                        return;
                    } else if (tvRePayTime.getText().toString().isEmpty()) {
                        tvRePayTime.setError(getString(R.string.error_empty));
                        tvRePayTime.requestFocus();
                        return;
                    } else if (tvRepeat.getText().toString().isEmpty()) {
                        tvRepeat.setError(getString(R.string.error_empty));
                        tvRepeat.requestFocus();
                        return;
                    }
                }
                if(paymentTemplateOperation == null){
                    paymentTemplateOperation = new PaymentTemplateOperationImpl();
                    paymentTemplateOperation.registerCallBackChange(this);
                }
                paymentTemplateOperation.changePaymentTemplate(getActivity(), getBody(),
                        templatesPayment.id, false);
                GeneralManager.getInstance().setNeedToUpdatePayTempl(true);
                break;
            case R.id.regular_pay_time_linear:
//                buildAlert(1);
                break;
            case R.id.repeat_pay_linear:
//                buildAlert(2);
                break;
            case R.id.timeBegin_linear:
                createDatePickerDialog(dateBeginDialog);
                break;
        }
    }

    private void createDatePickerDialog(DatePickerDialog datePickerDialog) {
        if(datePickerDialog!=null) {
            //datePickerDialog.setYearRange(2015, 2030);
            datePickerDialog.show(getActivity().getSupportFragmentManager(), DATE_PICKER_TAG);
        }
    }

    private void buildAlert(final int is){
        builder = new AlertDialog.Builder(getActivity());
        String[] mass = new String[24];

        View v = View.inflate(getActivity(),R.layout.auto_payments_alert,null);
        final CheckBox cb1 = v.findViewById(R.id.cb1);
        final CheckBox cb2 = v.findViewById(R.id.cb2);
        final CheckBox cb3 = v.findViewById(R.id.cb3);
        if (is==1){
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
            if(tvRePayTime.getText().toString() != null || tvRePayTime.getText().toString() != ""){
                String selected =  tvRePayTime.getText().toString();
                for(int i =0; i<mass.length; i++){
                    if(selected.equals(mass[i])){
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
            for(int i = 0; i<arrayCB.length; i++){
                int finalJ = i;
                arrayCB[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        for(int k = 0; k<=arrayCB.length; k++){
                            arrayCB[k].setChecked(false);
                        }
                        arrayCB[finalJ].setChecked(isChecked);
                        time = finalJ;
                    }
                });
            }
        }
        if (is==2) {
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
                    if (is==1) {
                        time = 1;
                    }
                    if (is==2){
                        DWM=1;
                    }
                }
            });
            cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cb1.setChecked(false);
                    cb3.setChecked(false);
                    cb2.setChecked(isChecked);
                    if (is==1) {
                        time = 2;
                    }
                    if (is==2){
                        DWM=2;
                    }
                }
            });
            cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    cb3.setChecked(isChecked);
                    if (is==1) {
                        time = 3;
                    }
                    if (is==2){
                        DWM=3;
                    }
                }
            });
        }

        builder.setView(v);

        final String[] finalMass = mass;
        builder.setPositiveButton(getString(R.string._yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (is==1) {
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
                if (is==2){
                    switch(DWM){
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
    public void onChangePaymentTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            getBundle();
            Intent intent = new Intent(requireContext(), SmsConfirmActivity.class);
            intent.putExtra("isPayment", true);
            intent.putExtra("isSuccess", true);
            intent.putExtra("isTTF", isTTF);
            Log.i("isTTF CIT", " " + isTTF);
            intent.putExtra("isTTA", 1);
            intent.putExtra("isChange", true);
            intent.putExtra("isTemplate", true);
            intent.putExtra("feeWithAmount", 0);
            intent.putExtra("paymentTitle", templatesPayment.name);
            if (account != null) {
                intent.putExtra("operationCurrency", account.currency);
            }
            getActivity().startActivity(intent);
            getActivity().finish();
        } else if(statusCode!= Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    public JSONObject getBody() {
        paymentController = PaymentContextController.getController();
        account = GeneralManager.getInstance().getAccountByCode(templatesPayment.sourceAccountId);
        PaymentService paymentService = paymentController.getPaymentServiceById(templatesPayment.serviceId);
        createTemplateBody = new BodyModel.CreateTemplate();
        createTemplateBody.Name = edTemplateName.getText().toString();

        createTemplateBody.IsAutoPay = switchRegular.isChecked();
        if (switchRegular.isChecked()) {
            createTemplateBody.AutoPayType = DWM != 10 ? String.valueOf(DWM) : null;
            createTemplateBody.StartPayDay = date;
//            createTemplateBody.autoPayTime = time == 1 ? "09:00:00.0000000" : time == 2 ? "13:00:00.0000000" : "";
            if(time == -1)
                createTemplateBody.AutoPayTime = "";
            createTemplateBody.AutoPayTime = tvRePayTime.getText().toString() + ":00.0000000";
        }

        createTemplateBody.Amount = "0";
        if(account!=null) {
            createTemplateBody.SourceAccountId = String.valueOf(account.code);
        }
        createTemplateBody.ServiceId = String.valueOf(paymentService.id);
        createTemplateBody.Parameters = getBodyParameters(paymentService);

        return getFieldNamesAndValues(createTemplateBody);
    }

    private void getBundle() {
        if(getArguments() != null) {
            isTTF = getArguments().getString("isTTF");
            templatesPayment = (TemplatesPayment) getArguments().getSerializable("template");
        }
    }

    private void initToolbar() {
        toolbar.setTitle("");
        tvTitle.setText(templatesPayment.name);
        ((TemplateActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    public JSONArray getBodyParameters(PaymentService paymentService) {
        JSONArray bodyList = new JSONArray();
        try {
            JSONObject bodyObjects = new JSONObject();
            bodyObjects.put("serviceParameterId",paymentService.parameters.get(0).id);
            bodyObjects.put("value", templatesPayment.parameters.get(0).getValue());
            Log.d(TAG,"templatesPayment.parameters.get(0).getValue() = " + templatesPayment.parameters.get(0).getValue());
            Log.d(TAG,"paymentService.parameters.get(0).id = " + paymentService.parameters.get(0).id);
            bodyList.put(bodyObjects);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bodyList;
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
}
