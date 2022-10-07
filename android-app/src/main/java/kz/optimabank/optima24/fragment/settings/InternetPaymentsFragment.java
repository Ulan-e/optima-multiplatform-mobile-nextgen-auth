package kz.optimabank.optima24.fragment.settings;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.Limit;
import kz.optimabank.optima24.model.interfaces.InternetInterface;
import kz.optimabank.optima24.model.service.InternetImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.utility.Constants.DATE_PICKER_TAG;
import static kz.optimabank.optima24.utility.Constants.SUCCESS_STATUS_CODE;
import static kz.optimabank.optima24.utility.Constants.VIEW_DATE_FORMAT;
import static kz.optimabank.optima24.utility.Utilities.formatDate;
import static kz.optimabank.optima24.utility.Utilities.getFieldNamesAndValues;
import static kz.optimabank.optima24.utility.Utilities.getFormatForDate;

import com.fourmob.datetimepicker.date.DatePickerDialog;

/**
 * Created by Max on 08.11.2017.
 */

public class InternetPaymentsFragment extends ATFFragment implements InternetImpl.callbackCheck, InternetImpl.callbackSet,
         View.OnClickListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swEnabled)
    Switch swEnabled;
    @BindView(R.id.tvEnabled)
    TextView tvEnabled;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.tvSpinnerBegin)
    EditText tvSpinnerBegin;
    @BindView(R.id.tvSpinnerEnd)
    EditText tvSpinnerEnd;
    @BindView(R.id.linSpinnerBegin)
    LinearLayout linSpinnerBegin;
    @BindView(R.id.linSpinnerEnd)
    LinearLayout linSpinnerEnd;

    int code;
    boolean checkEnabledInter, isOK = false, isFirstDateSelected = false;
    InternetInterface internetInterface;
    String from = "T00:00:00", to = "T23:59:59";
    String fromCheck, toCheck;

    private DatePickerDialog dialogFrom;
    private DatePickerDialog dialogTo;
    private final Calendar calendar = Calendar.getInstance();
    private Calendar calendarFrom = Calendar.getInstance();
    private Calendar calendarTo = Calendar.getInstance();
    private SimpleDateFormat sdfFromServer = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    SimpleDateFormat dateFormat = getFormatForDate("yyyy-MM-dd'T'HH:mm:ss");
    Date startDate;
    Date endDate;
    TextWatcher textWatcher;

    ProgressDialog progressDialog;

    EditText view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_internet_payments, container, false);
        ButterKnife.bind(this, view);
        initToolBar();
        getBundle();
        swEnabled.setChecked(false);
        if (internetInterface == null) {
            internetInterface = new InternetImpl();
        }

        swEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setButtonUseable(isChecked != checkEnabledInter);
                if (isChecked) {
                    tvEnabled.setText(getString(R.string.allowed));
                    linSpinnerBegin.setVisibility(View.VISIBLE);
                    linSpinnerEnd.setVisibility(View.VISIBLE);
                } else {
                    tvEnabled.setText(getString(R.string.forbidden));
                    linSpinnerBegin.setVisibility(View.INVISIBLE);
                    linSpinnerEnd.setVisibility(View.INVISIBLE);
                }
            }
        });

        internetInterface.registerCheckInternetCallBack(this);
        internetInterface.registerSetInternetCallBack(this);
        internetInterface.checkInternet(getActivity(), code, true);
        if(progressDialog == null){
            progressDialog = Utilities.progressDialog(getActivity(), getActivity().getResources().getString(R.string.t_loading));
            progressDialog.show();
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        dialogFrom = DatePickerDialog.newInstance(this, year, month, day, false);
        dialogTo = DatePickerDialog.newInstance(this, year, month, day, false);
        dialogFrom.setStartDate(year, month, day);
        Log.i("DATELIMITIM", "year = " + year + "   month= " + month + "   day = " + day);
        Log.i("DATELIMITIM", "year = " + year + "   month= " + month + 10 + "   day = " + day);
        dialogFrom.setDateRange(year, month, day-1, year+10, month, day);
        dialogFrom.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                dialogTo.setStartDate(year, month, day+1);
                isFirstDateSelected = true;
            }
        });

        linSpinnerBegin.setOnClickListener(this);
        linSpinnerEnd.setOnClickListener(this);
        tvSpinnerEnd.setOnClickListener(this);
        tvSpinnerBegin.setOnClickListener(this);

        setButtonUseable(false);
        btnSave.setOnClickListener(this);

        return view;
    }

    public void setButtonNegIfLimitNotChange(){
        setButtonUseable(checkChangeLimit());
        Log.i("limit", "checkChangeLimit = " + checkChangeLimit());
    }

    public boolean checkChangeLimit(){
        //            Toast.makeText(getActivity(), getString(R.string.youNothingChoose), Toast.LENGTH_LONG).show();
        return !fromCheck.equals(tvSpinnerBegin.getText().toString()) || !toCheck.equals(tvSpinnerEnd.getText().toString());
    }

    private void setButtonUseable(boolean usable){
        if (usable){
            btnSave.setBackground(getResources().getDrawable(R.drawable.button_default));
            btnSave.setClickable(true);
        } else {
            btnSave.setBackground(getResources().getDrawable(R.drawable.button_not_active));
            btnSave.setClickable(false);
        }
    }

    private void initToolBar() {
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public void getBundle() {
        if (getArguments() != null) {
            code = getArguments().getInt("code");
        }
    }

    @Override
    public void jsonCheckInternetResponse(int statusCode, String errorMessage, Limit.Internet internet) {
        progressDialog.dismiss();
        if (statusCode == Constants.SUCCESS) {
            isOK = true;
            checkEnabledInter = internet.IsActive;
            swEnabled.setChecked(internet.IsActive);
            if (internet.IsActive) {
                tvEnabled.setText(getString(R.string.allowed));
                linSpinnerBegin.setVisibility(View.VISIBLE);
                linSpinnerEnd.setVisibility(View.VISIBLE);
                try {
                    if (internet.DateFrom != null && !internet.DateFrom.equals("")) {
                        startDate = dateFormat.parse(internet.DateFrom);
                        tvSpinnerBegin.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                        tvSpinnerBegin.setText(getFormatForDate("dd.MM.yyyy").format(startDate));
                        fromCheck = tvSpinnerBegin.getText().toString();
                        isFirstDateSelected = true;
                    } else {
                        fromCheck = "";
                        tvSpinnerBegin.setText("");
                    }
                    if (internet.DateTo != null && !internet.DateTo.equals("")) {
                        endDate = dateFormat.parse(internet.DateTo);
                        tvSpinnerEnd.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                        tvSpinnerEnd.setText(getFormatForDate("dd.MM.yyyy").format(endDate));
                        toCheck = tvSpinnerEnd.getText().toString();
                    } else {
                        toCheck = "";
                        tvSpinnerEnd.setText("");
                    }
                }catch (ParseException e){
                    e.printStackTrace();
                }
            } else {
                tvEnabled.setText(getString(R.string.forbidden));
                linSpinnerBegin.setVisibility(View.INVISIBLE);
                linSpinnerEnd.setVisibility(View.INVISIBLE);
                fromCheck = "";
                toCheck= "";
            }
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    setButtonNegIfLimitNotChange();
                }
            };
            tvSpinnerBegin.addTextChangedListener(textWatcher);
            tvSpinnerEnd.addTextChangedListener(textWatcher);
        }
    }

    @Override
    public void jsonSetInternetResponse(int statusCode, String errorMessage) {
        progressDialog.dismiss();
        if (statusCode == SUCCESS_STATUS_CODE) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setMessage(R.string.operation_success);
            builder.setPositiveButton(getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().onBackPressed();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setMessage(R.string.test_message);
            builder.setPositiveButton(getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().onBackPressed();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        GregorianCalendar date = new GregorianCalendar(year, month, day);
        setTimeToMidnight(date);
        if(date.compareTo(setTimeToMidnight(Calendar.getInstance())) > 0 || date.compareTo(setTimeToMidnight(Calendar.getInstance())) == 0){
            if (view != null) {
                if (view == tvSpinnerBegin) {
                 //   dialogTo.setStartDate(year, month, day + 1);
                    isFirstDateSelected = true;
                }
                view.setError(null);
                view.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
                view.setText(VIEW_DATE_FORMAT.format(date.getTime()));
            }
            calendarFrom = date;
            calendarTo = date;
            Log.i("calendarFrom", "calendarFrom = " + calendarFrom);
        } else {
            Toast.makeText(getActivity(), getString(R.string.choose_correct_date), Toast.LENGTH_LONG).show();
        }
    }

    public static Calendar setTimeToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linSpinnerBegin:
            case R.id.tvSpinnerBegin:
                view = tvSpinnerBegin;
                //createDatePickerDialog(dialogFrom);
                break;
            case R.id.linSpinnerEnd:
            case R.id.tvSpinnerEnd:
                if (isFirstDateSelected) {
                    view = tvSpinnerEnd;
                    //createDatePickerDialog(dialogTo);
                } else {
                    if (tvSpinnerBegin.getText().toString().isEmpty())
                        tvSpinnerBegin.setError(getString(R.string.error_empty));
                }
                break;
            case R.id.btnSave:
                checkParameters();
                break;
        }
    }

    private void checkParameters() {
        if (swEnabled.isChecked()) {
            if (tvSpinnerBegin.getText().toString().isEmpty()) {
                tvSpinnerBegin.setError(getString(R.string.error_empty));
                return;
            }
            if (tvSpinnerEnd.getText().toString().isEmpty()) {
                tvSpinnerEnd.setError(getString(R.string.error_empty));
                return;
            }
            if (checkEnabledInter == swEnabled.isChecked() && fromCheck.equals(tvSpinnerBegin.getText().toString()) && toCheck.equals(tvSpinnerEnd.getText().toString())) {
                Toast.makeText(getActivity(), getString(R.string.youNothingChoose), Toast.LENGTH_LONG).show();
                return;
            }
            internetInterface.setInternet(getActivity(), code, getBody());
            progressDialog.show();
        } else {
            if (isOK) {
                if (checkEnabledInter == swEnabled.isChecked() && fromCheck.equals(tvSpinnerBegin.getText().toString()) && toCheck.equals(tvSpinnerEnd.getText().toString())) {
                    Toast.makeText(getActivity(), getString(R.string.youNothingChoose), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            internetInterface.setInternet(getActivity(), code, getBody());
            progressDialog.show();        }
    }

    // TODO Datepicker
    /*private void createDatePickerDialog(DatePickerDialog dialog) {
        if (dialog != null) {
            dialog.setYearRange(2018,2030);
            dialog.show(getActivity().getSupportFragmentManager(), DATE_PICKER_TAG);
        }
    }*/

    public JSONObject getBody() {
        //JSONObject body = new JSONObject();
        Limit.Internet internetBody = new Limit.Internet();
        internetBody.IsActive = swEnabled.isChecked();
        internetBody.Code = code;
        internetBody.DateFrom = tvSpinnerBegin.getText().toString().isEmpty() ? "0001-01-01" + from : formatDate(true, tvSpinnerBegin.getText().toString()) + ".000Z";
        internetBody.DateTo = tvSpinnerEnd.getText().toString().isEmpty() ? "0001-01-01" + to : formatDate(true, tvSpinnerEnd.getText().toString()) + ".000Z";
        return getFieldNamesAndValues(internetBody);
    }

}
