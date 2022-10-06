package kz.optimabank.optima24.fragment.settings;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.CardLimitActivity;
import kz.optimabank.optima24.activity.SelectAccountActivity;
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.CardLimit;
import kz.optimabank.optima24.model.base.Limit;
import kz.optimabank.optima24.model.interfaces.LimitInterface;
import kz.optimabank.optima24.model.interfaces.SetLimitInterface;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.LimitInterfaceImpl;
import kz.optimabank.optima24.model.service.SetLimitInterfaceImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.DATE_PICKER_TAG;
import static kz.optimabank.optima24.utility.Constants.SELECT_CITIZENSHIP_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.SELECT_LIMIT_REQUEST_CODE;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;
import static kz.optimabank.optima24.utility.Utilities.getFormatForDate;

/**
 * Created by Max on 29.08.2017.
 */

public class CardLimitFragment extends ATFFragment implements View.OnClickListener, /*DatePickerDialog.OnDateSetListener,*/
        LimitInterfaceImpl.Callback, SetLimitInterfaceImpl.Callback {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvSpinnerReg) TextView tvSpinnerReg;
    @BindView(R.id.linSpinnerReg) LinearLayout linSpinnerReg;
    @BindView(R.id.linChooseCountry) LinearLayout linChooseCountry;
    @BindView(R.id.firstLin) LinearLayout firstLin;
    @BindView(R.id.linLayF) LinearLayout linLayF;
    @BindView(R.id.regLiner) LinearLayout regLiner;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.tvTitle) TextView tvTitle;

    //ATM first
    @BindView(R.id.edSpinnerLimitDayATM) EditText edSpinnerLimitDayATM;
    @BindView(R.id.edSpinnerLimitDayATMHint) TextInputLayout edSpinnerLimitDayATMHint;
    @BindView(R.id.tvSpinnerBeginATMHint) TextInputLayout tvSpinnerBeginATMHint;
    @BindView(R.id.etReceiverCountATM) EditText etReceiverCountATM;
    @BindView(R.id.linSpinnerLimitDayATM) LinearLayout linSpinnerLimitDayATM;
    @BindView(R.id.linSpinnerBeginATM) LinearLayout linSpinnerBeginATM;
    @BindView(R.id.tvSpinnerBeginATM) TextView tvSpinnerBeginATM;
    @BindView(R.id.edMaxSumDayATMHint) TextInputLayout edMaxSumDayATMHint;
    @BindView(R.id.edMaxSumDayATM) EditText edMaxSumDayATM;
    @BindView(R.id.firstTV) TextView firstTV;
    @BindView(R.id.SWfirst) Switch SWfirst;

    @BindView(R.id.linLimitGeneral) LinearLayout linLimitGeneral;
    @BindView(R.id.linTypeLin) LinearLayout linTypeLin;
    @BindView(R.id.linSetLimitType) LinearLayout linSetLimitType;
    @BindView(R.id.tvLimitType) TextInputEditText tvLimitType;
    @BindView(R.id.tilLimitDaySOM) TextInputLayout tilLimitDaySOM;
    @BindView(R.id.edLimitDaySOM) TextInputEditText edLimitDaySOM;
    @BindView(R.id.linLimitStartDate) LinearLayout linLimitStartDate;
    @BindView(R.id.tvLimitStartDate) TextInputEditText tvLimitStartDate;
    @BindView(R.id.linLimitEndDate) LinearLayout linLimitEndDate;
    @BindView(R.id.tvLimitEndDate) TextInputEditText tvLimitEndDate;


    DatePickerDialog dateFromPickerDialog;
    DatePickerDialog dateToPickerDialog;
    private Calendar calendar = Calendar.getInstance();
    private Calendar fromDate = Calendar.getInstance();
    private Calendar toDate = Calendar.getInstance();

    String period;
    TextView view;
    int code;
    int type;
    boolean ATM_B;
    ArrayList<String> stringList = new ArrayList<>();
    ArrayList<String> limitTypeList = new ArrayList<>();
    private boolean isFirstRequest;
    private String selectedLimitType;
    private int positionOfLimit;
    private LimitInterface limitInterface;
    private SetLimitInterface setLimitInterface;
    private ArrayList<Limit> limitMass = new ArrayList<>();
    int year;
    int month;
    int day;
    int from;
    SimpleDateFormat dateFormat = getFormatForDate("yyyy-MM-dd'T'HH:mm:ss");
    Date startDate;
    Date endDate;

    JSONObject json = null;
    Limit limit;

    private static final int CASH_WITHDRAWAL_ATM_POS = 0;
    private static final int CASH_PURCHASES_THROUGH_POS = 2;
    private static final int INTERNET_SHOPPING = 3;
    private static final String CODE = "code";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_limit,container,false);
        ButterKnife.bind(this,view);

        getBundle();
        initToolbar();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        /*dateFromPickerDialog = DatePickerDialog.newInstance(this, year, month, day, false);
        dateFromPickerDialog.setYearRange(2000, year+5);//В календаре от текущего кода отображать плюс 5

        dateToPickerDialog = DatePickerDialog.newInstance(this, year, month, day, false);
        dateToPickerDialog.setYearRange(2000, year+5);//В календаре от текущего кода отображать плюс 5*/
        if (type == 4){
            linChooseCountry.setVisibility(View.GONE);
            regLiner.setVisibility(View.GONE);
            limitTypeList.add(getResources().getString(R.string.ATM_LIMIT_CASH_LIMIT));
            limitTypeList.add(getResources().getString(R.string.RETAIL_CARD_PRESENT));
            limitTypeList.add(getResources().getString(R.string.CARD_NOT_PRESENT));
        }

        firstLin.setVisibility(View.GONE);
        SWfirst.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                linLayF.setVisibility(View.VISIBLE);
                firstTV.setText(R.string.allowed);
                setButtonNegIfLimitNotChange();
            } else {
                linLayF.setVisibility(View.GONE);
                firstTV.setText(R.string.forbidden);
//                if (limit.getStatus()){
//                    setButtonUseable(true);
//                } else {
//                    setButtonUseable(false);
//                }
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                setButtonNegIfLimitNotChange();
            }
        };

        View.OnFocusChangeListener focusChangeListener = (v, hasFocus) -> {
            if (v instanceof EditText) {
                EditText editText = (EditText) v;
                if (hasFocus) {
                    if (editText.getText().toString().replaceAll("[0-9]", "").length() > 0) {
                        editText.setText(editText.getText().toString().replaceAll("[A-Za-zА-Яа-я]", "").replaceAll(" ", ""));
                        editText.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                    }
                } else {
                    if (editText.getText().toString().replaceAll("[A-Za-zА-Яа-я]", "").length() <= 0) {
                        if(type != 4){
                            editText.setText(getString(R.string.unlimit));
                        }
                        editText.setTextColor(getResources().getColor(R.color.gray_155_155_155));
                    }
                }
            }
        };

        edSpinnerLimitDayATM.addTextChangedListener(textWatcher);
        edMaxSumDayATM.addTextChangedListener(textWatcher);
        etReceiverCountATM.addTextChangedListener(textWatcher);
        tvSpinnerBeginATM.addTextChangedListener(textWatcher);
        edLimitDaySOM.addTextChangedListener(textWatcher);
        tvLimitStartDate.addTextChangedListener(textWatcher);
        tvLimitEndDate.addTextChangedListener(textWatcher);

        edSpinnerLimitDayATM.setOnFocusChangeListener(focusChangeListener);
        edMaxSumDayATM.setOnFocusChangeListener(focusChangeListener);
        etReceiverCountATM.setOnFocusChangeListener(focusChangeListener);
        tvSpinnerBeginATM.setOnFocusChangeListener(focusChangeListener);
        edLimitDaySOM.setOnFocusChangeListener(focusChangeListener);
        tvLimitStartDate.setOnFocusChangeListener(focusChangeListener);
        tvLimitEndDate.setOnFocusChangeListener(focusChangeListener);

        setLimitInterface = new SetLimitInterfaceImpl();
        setLimitInterface.registerCallBack(this);

            if (code != 0 && code != -1) {
                limitInterface = new LimitInterfaceImpl();
                limitInterface.registerCallBack(this);
                isFirstRequest = true;
//                limitInterface.getLimit(getActivity(), code, true);
            } else {
                Toast.makeText(parentActivity, getString(R.string.card_not_defined),Toast.LENGTH_LONG).show();
            }

        stringList.clear();
        if (tvTitle.getText().toString().equals(getString(R.string.Cash_withdrawal))){
            stringList.add(getResources().getString(R.string.all_country));
            stringList.add(getResources().getString(R.string.india));
            stringList.add(getResources().getString(R.string.indonesia));
            stringList.add(getResources().getString(R.string.nepal));

            tvSpinnerReg.setError(getString(R.string.choose_region));
            linSpinnerReg.setOnClickListener(this);
            linSpinnerReg.setVisibility(View.VISIBLE);
            regLiner.setVisibility(View.VISIBLE);
        }
        if (tvTitle.getText().toString().equals(getString(R.string.pos_terminal_pay))){
            stringList.add(getResources().getString(R.string.pos_terminal_pay));

            linSpinnerReg.setVisibility(View.GONE);
            regLiner.setVisibility(View.GONE);
        }
        btnSave.setOnClickListener(this);
        linLimitStartDate.setOnClickListener(this);
        tvLimitStartDate.setOnClickListener(this);
        tvLimitEndDate.setOnClickListener(this);
        linLimitEndDate.setOnClickListener(this);
        linSetLimitType.setOnClickListener(this);
        tvLimitType.setOnClickListener(this);
        tvLimitType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                tvLimitType.requestFocus();
            }
        });

//        setButtonUseable(false);
        return view;
    }

    private void setButtonNegIfLimitNotChange(){
        if (limit!= null) {
//            setButtonUseable(checkChangeLimit());
            Log.i("limit", "checkChangeLimit = " + checkChangeLimit());
        }
    }

    private boolean checkChangeLimit(){
        boolean isNotCheck = false;
        if (type == 4){
            String edLimitDaySOMStr, tvLimitStartDateStr, tvLimitEndDateStr, etReceiverCountATMStr;
            if(edLimitDaySOM.getText().toString().length() == 0){
                edLimitDaySOMStr = "";
            } else edLimitDaySOMStr = edLimitDaySOM.getText().toString();

            if(etReceiverCountATM.getText().toString().length() == 0){
                etReceiverCountATMStr = "";
            } else etReceiverCountATMStr = etReceiverCountATM.getText().toString();

            if(tvLimitStartDate.getText().toString().length() == 0){
                tvLimitStartDateStr = "";
            } else tvLimitStartDateStr = tvLimitStartDate.getText().toString();

            if(tvLimitEndDate.getText().toString().length() == 0){
                tvLimitEndDateStr = "";
            } else tvLimitEndDateStr = tvLimitEndDate.getText().toString();

            String formattedStartDate = null;
            String formattedEndtDate = null;
            if(startDate != null && endDate != null) {
                formattedStartDate = getFormatForDate("dd.MM.yyyy").format(startDate);
                formattedEndtDate = getFormatForDate("dd.MM.yyyy").format(endDate);
            } else {
                formattedStartDate = "";
                formattedEndtDate = "";
            }
            if((limit.Amount.equals(edLimitDaySOMStr)) && (limit.Number.equals(etReceiverCountATMStr))){
                Log.i("limit.StartDate", " = " + limit.StartDate);
                Log.i("limit.EndDate", " = " + limit.EndDate);
                if(limit.StartDate != null && !formattedStartDate.equals(tvLimitStartDateStr)){
                    return true;
                } else if ((limit.StartDate == null || limit.StartDate.equals(""))  && !tvLimitStartDateStr.isEmpty()){
                    return true;
                }
                if(limit.EndDate != null && !formattedEndtDate.equals(tvLimitEndDateStr)){
                    return true;
                } else if ((limit.EndDate == null || limit.StartDate.equals("")) && !tvLimitEndDateStr.isEmpty()){
                    return true;
                }
                isNotCheck = false;
            } else {
                isNotCheck = true;
            }

            return isNotCheck;

        }
        else {
            Log.i("limit", "limit.getStatus() = " + limit.getStatus());
            Log.i("limit", "limit.Amount = " + limit.Amount);
            Log.i("limit", "edSpinnerLimitDayATM.getText().toString() = " + edSpinnerLimitDayATM.getText().toString());
            Log.i("limit", "limit singleAmount = " + limit.SingleAmount);
            Log.i("limit", "edMaxSumDayATM.getText().toString() = " + edMaxSumDayATM.getText().toString());
            Log.i("limit", "limit.Number = " + limit.Number);
            Log.i("limit", "etReceiverCountATM.getText().toString() = " + etReceiverCountATM.getText().toString());
            Log.i("limit", "limit.Period = " + limit.Period);
            Log.i("limit", "tvSpinnerBeginATM.getText().toString() = " + tvSpinnerBeginATM.getText().toString());

            String edSpinnerLimitDayATMString,edMaxSumDayATMString,etReceiverCountATMString,tvSpinnerBeginATMString;
            edSpinnerLimitDayATMString = edSpinnerLimitDayATM.getText().toString();
            edMaxSumDayATMString = edMaxSumDayATM.getText().toString();
            etReceiverCountATMString = etReceiverCountATM.getText().toString();
            tvSpinnerBeginATMString = tvSpinnerBeginATM.getText().toString();

            if (limit.getStatus()==SWfirst.isChecked() &&
                    (limit.Amount.equals(edSpinnerLimitDayATMString) || edSpinnerLimitDayATMString.equals("0") || edSpinnerLimitDayATMString.equals(getString(R.string.unlimit))) &&
                    (limit.SingleAmount.equals(edMaxSumDayATMString) || edMaxSumDayATMString.equals("0") || edMaxSumDayATMString.equals(getString(R.string.unlimit))) &&
                    (limit.Number.equals(etReceiverCountATMString) || etReceiverCountATMString.equals("0") || etReceiverCountATMString.equals(getString(R.string.unlimit))) &&
                    (limit.Period.equals(tvSpinnerBeginATMString.replaceAll("[^0-9]", "")) || tvSpinnerBeginATMString.equals("0") || tvSpinnerBeginATMString.equals(getString(R.string.unlimit)))){
                isNotCheck = false;
            } else {
                isNotCheck = true;
            }
        }
        return isNotCheck;
    }

//    private void setButtonUseable(boolean usable){
//        if (usable){
//            btnSave.setBackground(getResources().getDrawable(R.drawable.button_default));
//            btnSave.setClickable(true);
//        } else {
//            btnSave.setBackground(getResources().getDrawable(R.drawable.button_not_active));
//            btnSave.setClickable(false);
//        }
//    }

    private void setToZero(){
        linLayF.setVisibility(View.GONE);
        edSpinnerLimitDayATM.setText(null);
        edMaxSumDayATM.setText(null);
        etReceiverCountATM.setText(null);
        tvSpinnerBeginATM.setText(null);
        edSpinnerLimitDayATM.setError(null);
        edMaxSumDayATM.setError(null);
        etReceiverCountATM.setError(null);
        tvSpinnerBeginATM.setError(null);
        SWfirst.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnSave:
                if (tvTitle.getText().toString().equals(getString(R.string.select_limit_type))){
                    if(parse(false)){
                        Limit limit = getCurrentChangedLimit(limitMass.get(0));
                        try {
                            json = addJSON(limit);                                          // 05.08.19-юзаю существующий метод, первый аргумент сдесь не нужен
                            setLimitInterface.setLimit(getActivity(), code, true, json);
                        }catch (org.json.JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case R.id.linSpinnerReg:
                setToZero();
                //((SettingsActivity) getActivity()).navigateToSettingsPage(new SelectCountryFragment());
                Intent intent;
                intent = new Intent(getActivity(),SelectAccountActivity.class);
                intent.putExtra("isLimit",true);
                intent.putExtra("citizenship",true);
                intent.putExtra("citizenshipList", stringList);
                startActivityForResult(intent, SELECT_CITIZENSHIP_REQUEST_CODE);
                break;

            case R.id.linSpinnerBeginATM:
                view = tvSpinnerBeginATM;
                ATM_B = true;
                createDatePickerDialog(dateFromPickerDialog);
                break;

            case R.id.tvSpinnerBeginATM:
                view = tvSpinnerBeginATM;
                ATM_B = true;
                createDatePickerDialog(dateFromPickerDialog);
                break;
            case R.id.linLimitStartDate:
            case R.id.tvLimitStartDate:
                from = 1;
                createDatePickerDialog(dateFromPickerDialog);
                break;
            case R.id.tvLimitEndDate:
            case R.id.linLimitEndDate:
                createDatePickerDialog(dateToPickerDialog);
                from = 2;
                break;
            case R.id.linSetLimitType:
            case R.id.tvLimitType:
//                intent = new Intent(getActivity(),SelectAccountActivity.class);
//                intent.putExtra("chooseLimit",true);
//                intent.putExtra("limitTypeList", limitTypeList);
//                startActivityForResult(intent, SELECT_LIMIT_REQUEST_CODE);
                break;

        }
    }

    private void createDatePickerDialog(DatePickerDialog datePickerDialog) {
        if(datePickerDialog!=null) {
//            datePickerDialog.setStartDate(year,month,day);
            //datePickerDialog.show(getActivity().getSupportFragmentManager(), DATE_PICKER_TAG);
        }
    }

    private boolean parse(boolean isSendSms) {

        if (tvTitle.getText().toString().equals(getString(R.string.select_limit_type))){
            if (TextUtils.isEmpty(edLimitDaySOM.getText().toString())) {
                edLimitDaySOM.setError(getString(R.string.the_value_should_be_number_up_to_19_digits));
                edLimitDaySOM.requestFocus();
                return false;
            }
            if (TextUtils.isEmpty(tvLimitStartDate.getText().toString())) {
                tvLimitStartDate.setError(getString(R.string._t_fill_));
                tvLimitStartDate.requestFocus();
                return false;
            }
            if (TextUtils.isEmpty(tvLimitEndDate.getText().toString())) {
                tvLimitEndDate.setError(getString(R.string._t_fill_));
                tvLimitEndDate.requestFocus();
                return false;
            }
            if (TextUtils.isEmpty(etReceiverCountATM.getText().toString())) {
                etReceiverCountATM.setError(getString(R.string.the_value_should_be_number_uper_to_0));
                etReceiverCountATM.requestFocus();
                return false;
            }
        } else {
            String begin;
            String massB[] = new String[3];
            String massE[] = new String[3];

            if (SWfirst.isChecked()) {

                if (!edSpinnerLimitDayATM.getText().toString().replaceAll("[^0-9]", "").isEmpty() &&
                        Double.valueOf(edSpinnerLimitDayATM.getText().toString().replaceAll("[^0-9]", "").replaceAll(",", "")) <= 0) {
                    edSpinnerLimitDayATM.setError(getString(R.string.the_value_should_be_number_uper_to_0));
                    edSpinnerLimitDayATM.requestFocus();
                    return false;
                }

                if (TextUtils.isEmpty(edSpinnerLimitDayATM.getText()) ||
                        edSpinnerLimitDayATM.getText().toString().replaceAll("[0-9]", "").replaceAll(",", "").length() > 0) {
                    edSpinnerLimitDayATM.setError(getString(R.string.the_value_should_be_number_up_to_19_digits));
                    edSpinnerLimitDayATM.requestFocus();
                    return false;
                }

                if (!edMaxSumDayATM.getText().toString().replaceAll("[^0-9]", "").isEmpty() &&
                        Double.valueOf(edMaxSumDayATM.getText().toString().replaceAll("[^0-9]", "").replaceAll(",", "")) <= 0) {
                    edMaxSumDayATM.setError(getString(R.string.the_value_should_be_number_uper_to_0));
                    edMaxSumDayATM.requestFocus();
                    return false;
                }

                if (TextUtils.isEmpty(edMaxSumDayATM.getText()) ||
                        edMaxSumDayATM.getText().toString().replaceAll("[0-9]", "").replaceAll(",", "").length() > 0) {
                    edMaxSumDayATM.setError(getString(R.string.the_value_should_be_number_up_to_19_digits));
                    edMaxSumDayATM.requestFocus();
                    return false;
                }

                if (!etReceiverCountATM.getText().toString().replaceAll("[^0-9]", "").isEmpty() &&
                        Double.valueOf(etReceiverCountATM.getText().toString().replaceAll("[^0-9]", "").replaceAll(",", "")) <= 0) {
                    etReceiverCountATM.setError(getString(R.string.the_value_should_be_number_uper_to_0));
                    etReceiverCountATM.requestFocus();
                    return false;
                }

                if (TextUtils.isEmpty(etReceiverCountATM.getText()) ||
                        etReceiverCountATM.getText().toString().replaceAll("[0-9]", "").replaceAll(",", "").length() > 0) {
                    etReceiverCountATM.setError(getString(R.string.the_value_should_be_number_up_to_19_digits).replace("1",""));
                    etReceiverCountATM.requestFocus();
                    return false;
                }

                if (!tvSpinnerBeginATM.getText().toString().replaceAll("[^0-9]", "").isEmpty() &&
                        Double.valueOf(tvSpinnerBeginATM.getText().toString().replaceAll("[^0-9]", "").replaceAll(",", "")) <= 0) {
                    tvSpinnerBeginATM.setError(getString(R.string.the_value_should_be_number_uper_to_0));
                    tvSpinnerBeginATM.requestFocus();
                    return false;
                }

                if (TextUtils.isEmpty(tvSpinnerBeginATM.getText()) ||
                        tvSpinnerBeginATM.getText().toString().replaceAll("[0-9]", "").replaceAll(",", "").length() > 0) {
                    tvSpinnerBeginATM.setError(getString(R.string.the_value_should_be_number_up_to_19_digits).replace("1",""));
                    tvSpinnerBeginATM.requestFocus();
                    return false;
                }
            }

        if (isSendSms) {

        }
            return true;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == SELECT_LIMIT_REQUEST_CODE && data != null){
//            if(selectedLimitType.equals(data.getSerializableExtra(STRING_KEY))){
//
//            } else {
//                if(selectedLimitType != null) {
//                    selectedLimitType = (String) data.getSerializableExtra(STRING_KEY);
//                    limit = getLimitByType(selectedLimitType);
//                    if (limit != null) {
//                        setLimit(limit);
//                    }
//                    tvLimitType.setText(selectedLimitType);
//                    tvLimitType.setError(null);
//                }
//            }
//        }
    }

    public void initToolbar() {
        Log.i("initToolbartype","type = "+type);
        if (type == 1)
            tvTitle.setText(R.string.Cash_withdrawal);
        if (type == 3)
            tvTitle.setText(R.string.pos_terminal_pay);
        if (type == 4)
            tvTitle.setText(R.string.select_limit_type);

        toolbar.setTitle("");
        ((CardLimitActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((CardLimitActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    /*@Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        period = null;
        GregorianCalendar date = new GregorianCalendar(year, month, day);
        CharSequence dateString = Constants.DAY_MONTH_YEAR_FORMAT.format(date.getTime());

        if(from == 1) {
            Log.i("OnDateSetSDF","year = "+year+"  month = "+ month+"  day = "+day);
            tvLimitStartDate.setError(null);
            tvLimitStartDate.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
            tvLimitStartDate.setText(getFormatForDate("dd.MM.yyyy").format(date.getTime()));
            fromDate = date;
            Log.i("OnDateSetSDF", "fromDate = "+ date);
        } else if (from == 2) {
            tvLimitEndDate.setError(null);
            tvLimitEndDate.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
            tvLimitEndDate.setText(getFormatForDate("dd.MM.yyyy").format(date.getTime()));
            toDate = date;
            Log.i("OnDateSetSDF", "toDate = "+ date);
        } else {
            if(view!=null) {
                view.setError(null);
                view.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                //view.setText(dateString);
                view.setText(getFormatForDate("yyyy-MM-dd").format(date.getTime()));
            }
            fromDate = date;
            toDate = date;
        }
    }*/

    public void getBundle() {
        if (getArguments()!=null){
            code = getArguments().getInt("code");
            type = getArguments().getInt("type");
            positionOfLimit = getArguments().getInt("position");
            limitMass.add(getArguments().getParcelable(STRING_KEY));
            setLimit(limitMass.get(0));

            //Log.i("code"," code ==== "+code);

//       selectedLimitType = (String) getArguments().getString(STRING_KEY);
//       Log.e("selectedLimitType22",selectedLimitType);
//       limit = getLimitByType(selectedLimitType);
//       if (limit != null) {
//       setLimit(limit);
//       }
//       tvLimitType.setText(selectedLimitType);
//       tvLimitType.setError(null);
        }
    }

    @Override
    public void jsonLimitResponse(int statusCode, String errorMessage) {
        if (statusCode==0){
            if(isAttached()){
//                limitMass = GeneralManager.getInstance().getLimit();
                if (tvTitle.getText().toString().equals(getString(R.string.select_limit_type))){
                    if (limitMass != null && !limitMass.isEmpty()){

//                        limit = limitMass.get(positionOfLimit);
//                        if(isFirstRequest){
//                            setButtonUseable(false);
//                            isFirstRequest = false;
//                        }
                    } else if(limitMass == null)
                        new AlertDialog.Builder(getActivity())
                                .setMessage(getString(R.string.test_message))
                                .setCancelable(false)
                                .setIcon(R.drawable.ic_dialog_alert)
                                .setTitle(R.string.alert_error)
                                .setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int whichButton) {
                                                dialog.cancel();
                                                getActivity().onBackPressed();
                                            }
                                        }).create().show();
                }
            }
        }
    }

    private void setLimit(Limit limit){
//        tvLimitType.requestFocus();
            if(limit.Type == CASH_WITHDRAWAL_ATM_POS){
                tvLimitType.setText(R.string.ATM_LIMIT_CASH_LIMIT);
                selectedLimitType = getResources().getString(R.string.ATM_LIMIT_CASH_LIMIT);
            } else if(limit.Type == CASH_PURCHASES_THROUGH_POS){
                tvLimitType.setText(R.string.RETAIL_CARD_PRESENT);
                selectedLimitType = getResources().getString(R.string.RETAIL_CARD_PRESENT);
            } else if(limit.Type == INTERNET_SHOPPING){
                tvLimitType.setText(R.string.CARD_NOT_PRESENT);
                selectedLimitType = getResources().getString(R.string.CARD_NOT_PRESENT);
            }
            edLimitDaySOM.setText(limit.Amount);
            etReceiverCountATM.setText(limit.Number);
            try {
                if(limit.StartDate != null && !limit.StartDate.equals("")){
                    startDate = dateFormat.parse(limit.StartDate);
                    tvLimitStartDate.setText(getFormatForDate("dd.MM.yyyy").format(startDate));
                } else {
                    tvLimitStartDate.setText("");
                }
                if(limit.EndDate != null && !limit.EndDate.equals("")){
                    endDate = dateFormat.parse(limit.EndDate);
                    tvLimitEndDate.setText(getFormatForDate("dd.MM.yyyy").format(endDate));
                } else {
                    tvLimitEndDate.setText("");
                }
            }catch (ParseException e){
                e.printStackTrace();
            }
    }

    private Limit getLimitByType(String limitByType){
        Limit limit = null;
        if (limitByType != null){
            if(limitByType.equals(getResources().getString(R.string.ATM_LIMIT_CASH_LIMIT))){
                limit = limitMass.get(0);
            } else if(limitByType.equals(getResources().getString(R.string.RETAIL_CARD_PRESENT))){
                limit = limitMass.get(1);
            } else if(limitByType.equals(getResources().getString(R.string.CARD_NOT_PRESENT))){
                limit = limitMass.get(2);
            }
        }
        return limit;
    }

    private Limit getCurrentChangedLimit(Limit limitByType){
        Limit limit = new Limit();
        if (limitByType != null){
            if(limitByType.Type == CASH_WITHDRAWAL_ATM_POS){
                limit = limitMass.get(0);
            } else if(limitByType.Type == CASH_PURCHASES_THROUGH_POS){
                limit = limitMass.get(0);
            } else if(limitByType.Type == INTERNET_SHOPPING){
                limit = limitMass.get(0);
            }
        }
        limit.Amount = edLimitDaySOM.getText().toString();
        limit.Number = etReceiverCountATM.getText().toString();
        limit.StartDate = tvLimitStartDate.getText().toString();
        limit.EndDate = tvLimitEndDate.getText().toString();

        return limit;
    }

    private JSONObject addJSON(Limit li) throws JSONException {
        JSONObject objectLimitList = null;
        if(li.StartDate != null && !li.StartDate.equals("")){
                String[] startDateArray = li.StartDate.split("[.]");
                li.StartDate = startDateArray[2] + "-" + startDateArray[1] + "-" + startDateArray[0] + "T00:00:00.000Z";
            } else {
                li.StartDate = "";
            }
            if(li.EndDate != null && !li.EndDate.equals("")){
                String[] endDateArray = li.EndDate.split("[.]");
                li.EndDate = endDateArray[2] + "-" + endDateArray[1] + "-" + endDateArray[0] + "T00:00:00.000Z";
            } else {
                li.EndDate = "";
            }
//        limit.StartDate = li.StartDate;
//            limit.EndDate = li.EndDate;

            objectLimitList = new JSONObject(new Gson().toJson(li));
        return objectLimitList;
    }

    @Override
    public void jsonSetLimitResponse(int statusCode, String errorMessage) {
        if (statusCode==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.operation_success);
            builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().onBackPressed();
                }
            });
            builder.create();
            builder.show();
        }else{
            Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_LONG).show();
        }
    }
}
