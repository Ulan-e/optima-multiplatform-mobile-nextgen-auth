package kz.optimabank.optima24.fragment.statements;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.controller.adapter.StatementAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.ATFStatement;
import kz.optimabank.optima24.model.base.StatementsWithStats;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.utility.Utilities.formatDate;

/**
 Created by Timur on 30.05.2017.
 */

public class WriteOffStatement extends ATFFragment {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progress) ProgressBar progress;
    @BindView(R.id.tvNotData) TextView tvNotData;

    ArrayList<ATFStatement> data = new ArrayList<>();
    String fromDate,toDate;
    boolean ifFirst = false;
    public StatementsWithStats response = new StatementsWithStats();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            if (getActivity()!=null&&isAdded())
                getSortStatement();
        }catch (Exception ignored){}
        getBundle();
    }

    public void setAdapter(ArrayList<ATFStatement> sortStatement) {
        if(sortStatement!=null && !sortStatement.isEmpty() && sortStatement.size()!=0) {
            //sortStatement.clear();
            StatementAdapter adapter = new StatementAdapter(getActivity(), sortStatement, false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
            progress.setVisibility(View.GONE);
            tvNotData.setVisibility(View.GONE);

            if(!progress.isShown()) {
            }else{
                progress.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void getSortStatement() {
        new AsyncTask<Void, Void, ArrayList<ATFStatement>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected ArrayList<ATFStatement> doInBackground(Void... voids) {
                if(!data.isEmpty()) {
                    data.clear();
                }
                ArrayList<ATFStatement> statements;
                try {
                    statements = GeneralManager.getInstance().getStatementsAccount();
                    Collections.sort(statements, new Comparator<ATFStatement>() {
                        @Override
                        public int compare(ATFStatement o1, ATFStatement o2) {
                            return o2.operationDate.compareTo(o1.operationDate);
                        }
                    });


                    String operationDate = "operation date";
                    for(ATFStatement statement : statements) {
                        if(statement.amount < 0) {
                            if (operationDate != null && !operationDate.equals(statement.getOperationDate())) {
                                data.add(new ATFStatement(Constants.HEADER_ID, Utilities.formatDateString(parentActivity, formatDate(false, statement.operationDate), false)));
                                data.add(statement);
                            } else {
                                data.add(statement);
                            }
                            operationDate = statement.getOperationDate();
                        }
                    }
                }catch (Exception e){}
                return data;
            }

            @Override
            protected void onPostExecute(ArrayList<ATFStatement> statements) {
                super.onPostExecute(statements);
                boolean maxF = true;
                if(response!=null) {
                    if(response.totalExpense != null && !response.totalExpense.isEmpty()){
                        for(StatementsWithStats.Data data: response.totalExpense){
                            ATFStatement minStat = new ATFStatement(-6);
                            minStat.currency = data.currency;
                            minStat.amount = data.value;
                            if (maxF){
                                minStat.name = getString(R.string.total_write_off);
                                maxF = false;
                            }else{
                                minStat.name = "";
                            }
                            statements.add(minStat);
                        }
                    }
                    if(response.totalFee != null){
                        ATFStatement minStat = new ATFStatement(-6);
                        minStat.currency = response.totalFee.currency;
                        minStat.amount = response.totalFee.value;
                        minStat.name = getString(R.string.total_fee);
                        statements.add(minStat);
                    }
                }
                if(statements != null && statements.isEmpty()){
                    statements.add(new ATFStatement(Constants.HEADER_ID, getString(R.string.not_data_fmt, fromDate, toDate)));
                }

                try {
                    setAdapter(statements);
                }catch (Exception ignored){}
            }
        }.execute();
    }

    private SimpleDateFormat getSimpleDateFormat(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }

    public String getOperationDate(String date) {
        String stDate = null;
        SimpleDateFormat sdf = getSimpleDateFormat();
        try {
            stDate = Constants.API_DAY_MONTH_FORMAT.format(sdf.parse(date));
            TimeUnit.MILLISECONDS.toDays(sdf.parse(date).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stDate;
    }

    private void getBundle(){
        if (getArguments()!=null){
            fromDate = getArguments().getString("fromDate");
            toDate = getArguments().getString("toDate");
            ifFirst = getArguments().getBoolean("ifFirst");
        }
    }
}