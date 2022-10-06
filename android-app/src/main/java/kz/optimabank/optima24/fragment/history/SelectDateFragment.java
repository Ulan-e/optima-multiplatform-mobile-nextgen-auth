package kz.optimabank.optima24.fragment.history;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.DATE_PICKER_TAG;
import static kz.optimabank.optima24.utility.Constants.DATE_TAG;
import static kz.optimabank.optima24.utility.Utilities.getFormatForDate;

/**
  Created by Timur on 08.06.2017.
 */

public class SelectDateFragment extends ATFFragment implements View.OnClickListener/*, DatePickerDialog.OnDateSetListener*/{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.btnShow) Button btnShow;

    @BindView(R.id.linSpinnerFrom) LinearLayout linSpinnerFrom;
    @BindView(R.id.linSpinnerTo) LinearLayout linSpinnerTo;
    @BindView(R.id.tvSpinnerFrom) TextView tvSpinnerFrom;
    @BindView(R.id.tvSpinnerTo) TextView tvSpinnerTo;

    @BindView(R.id.linForMonth) LinearLayout linForMonth;
    @BindView(R.id.linForThreeMonth) LinearLayout linForThreeMonth;
    @BindView(R.id.linForHalfYear) LinearLayout linForHalfYear;

    private Calendar calendar = Calendar.getInstance();
    private Calendar fromDate = Calendar.getInstance();
    private Calendar toDate = Calendar.getInstance();
    /*DatePickerDialog dateFromPickerDialog;
    DatePickerDialog dateToPickerDialog;*/
    String dateTag, period;
    int from;
    int day,month,year;
    boolean isFirstDateSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_date_fragment, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        initToolbar();
        linSpinnerFrom.setOnClickListener(this);
        linSpinnerTo.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        linForMonth.setOnClickListener(this);
        linForThreeMonth.setOnClickListener(this);
        linForHalfYear.setOnClickListener(this);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        dateFromPickerDialog = DatePickerDialog.newInstance(this, year, month, day, false);
//        dateFromPickerDialog.setYearRange(2010, year+5);  //В календаре от текущего кода отображать плюс 5
//        dateToPickerDialog = DatePickerDialog.newInstance(this, year, month, day, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    /*@Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        period = null;
        GregorianCalendar date = new GregorianCalendar(year, month, day);
        CharSequence dateString = Constants.DAY_MONTH_YEAR_FORMAT.format(date.getTime());
        if(from == 1) {
            Log.i("OnDateSetSDF","year = "+year+"  month = "+ month+"  day = "+day);
            this.year = year;
            this.month = month;
            this.day = day;
            isFirstDateSelected = true;
            tvSpinnerFrom.setError(null);
            tvSpinnerFrom.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
            tvSpinnerFrom.setText(dateString);
            fromDate = date;
            Log.i("OnDateSetSDF", "fromDate = "+ date);
        } else {
            tvSpinnerTo.setError(null);
            tvSpinnerTo.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
            tvSpinnerTo.setText(dateString);
            toDate = date;
            Log.i("OnDateSetSDF", "toDate = "+ date);
        }
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linSpinnerFrom:
                from = 1;
             //   createDatePickerDialog(dateFromPickerDialog);
                break;
            case R.id.linSpinnerTo:
                if (isFirstDateSelected) {
                    Log.i("OnDateSetSDF----","year = "+year+"  month = "+ month+"  day = "+day);
                    from = 2;
                   // dateToPickerDialog.setStartDate(year, month, day + 1);
                  //  createDatePickerDialog(dateToPickerDialog);
                } else {
                    //if (tvSpinnerFrom.getText().toString().isEmpty())
                        tvSpinnerFrom.setError(getString(R.string.error_empty));
                }
                break;
            case R.id.btnShow:
                Intent intent = new Intent();
                Log.i("OnDateSetSDF", "fromDate.getTime() = "+ fromDate.getTime());
                Log.i("OnDateSetSDF", "toDate.getTime() = "+ toDate.getTime());
                String dateFrom = getFormatForDate("yyyy-MM-dd'T'HH:mm:ss").format(fromDate.getTime());
                String dateTo = getFormatForDate("yyyy-MM-dd'T'HH:mm:ss").format(toDate.getTime());
                Log.i("OnDateSetSDF", "dateFrom = "+ dateFrom);
                Log.i("OnDateSetSDF", "dateTo = "+ dateTo);

                if(tvSpinnerFrom.getText().toString().equals(getString(R.string.from))) {
                    tvSpinnerFrom.setError(getString(R.string.error_empty));
                    return;
                } else if(tvSpinnerTo.getText().toString().equals(getString(R.string.to))){
                    tvSpinnerTo.setError(getString(R.string.error_empty));
                    return;
                }

                intent.putExtra("fromDate",dateFrom);
                intent.putExtra("toDate",dateTo);
                intent.putExtra("period",period);
                intent.putExtra("ifFirst",true);
                getActivity().setResult(CommonStatusCodes.SUCCESS, intent);
                getActivity().finish();
                break;
            case R.id.linForMonth:
                period = getString(R.string.for_month);
                setDate(1);
                break;
            case R.id.linForThreeMonth:
                period = getString(R.string.for_three_month);
                setDate(3);
                break;
            case R.id.linForHalfYear:
                period = getString(R.string.for_half_year);
                setDate(6);
                break;
        }
    }

    private void initToolbar() {
        toolbar.setTitle("");
        ((NavigationActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        if(dateTag.equals("PaymentHistory") || dateTag.equals("TransferHistory")) {
            tvTitle.setText(getString(R.string.history_title));
        } else if(dateTag.equals("Statement")){
            tvTitle.setText(getString(R.string.account_statement));
        }
    }

    private void getBundle() {
        if(getArguments()!=null){
            dateTag = getArguments().getString(DATE_TAG);
        }
    }

//    private void createDatePickerDialog(DatePickerDialog datePickerDialog) {
//        if(datePickerDialog!=null) {
//            datePickerDialog.show(getActivity().getSupportFragmentManager(), DATE_PICKER_TAG);
//        }
//    }

    private void setDate(int day) {
        Calendar curDate = GregorianCalendar.getInstance();
        Calendar fromDate = GregorianCalendar.getInstance();
        curDate.add(Calendar.DAY_OF_YEAR, -0);
        fromDate.add(Calendar.MONTH, -day);

        this.fromDate = fromDate;
        this.toDate = curDate;

        CharSequence dateString = Constants.DAY_MONTH_YEAR_FORMAT.format(curDate.getTime());
        CharSequence curDateString = Constants.DAY_MONTH_YEAR_FORMAT.format(fromDate.getTime());

        tvSpinnerFrom.setText(curDateString);
        tvSpinnerFrom.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
        tvSpinnerFrom.setError(null);

        tvSpinnerTo.setText(dateString);
        tvSpinnerTo.setTextColor(getResources().getColor(R.color.gray_black_56_56_56));
        tvSpinnerTo.setError(null);
    }
}
