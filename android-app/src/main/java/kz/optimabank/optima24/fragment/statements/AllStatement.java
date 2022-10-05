package kz.optimabank.optima24.fragment.statements;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
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
 * Created by Timur on 30.05.2017.
 */

public class AllStatement extends ATFFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.tvNotData)
    TextView tvNotData;

    ProgressDialog progressDialog;
    String fromDate, toDate, currency;
    boolean ifFirst = false, exceptStartAndEndBalance;
    public StatementsWithStats response;

    ArrayList<ATFStatement> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isAttached()) {
            getSortStatement();
            getBundle();
        }
    }

    public void setAdapter(ArrayList<ATFStatement> sortStatement) {
        if (sortStatement != null && !sortStatement.isEmpty()) {
            //sortStatement.clear();
            StatementAdapter adapter = new StatementAdapter(getActivity(), sortStatement, false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
            progress.setVisibility(View.GONE);
            tvNotData.setVisibility(View.GONE);

            if (!progress.isShown()) {
            } else {
                progress.setVisibility(View.GONE);
            }
        }
        progressDialog.dismiss();
    }

    private SimpleDateFormat getSimpleDateFormat() {
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

    @SuppressLint("StaticFieldLeak")
    public void getSortStatement() {
        new AsyncTask<Void, Void, ArrayList<ATFStatement>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setVisibility(View.VISIBLE);
                Log.i("progressSSS", "progressSSS VISIBLE");
            }

            @Override
            protected ArrayList<ATFStatement> doInBackground(Void... voids) {
                if (!data.isEmpty()) {
                    data.clear();
                }
                ArrayList<ATFStatement> statements = GeneralManager.getInstance().getStatementsAccount();
                if (statements != null) {
                    Collections.sort(statements, new Comparator<ATFStatement>() {
                        @Override
                        public int compare(ATFStatement o1, ATFStatement o2) {
                            return o2.operationDate.compareTo(o1.operationDate);
                        }
                    });

                    String operationDate = "operation date";
                    for (ATFStatement statement : statements) {
                        if (operationDate != null && !operationDate.equals(statement.getOperationDate())) {
                            data.add(new ATFStatement(Constants.HEADER_ID, Utilities.formatDateString(parentActivity, formatDate(false, statement.operationDate), false)));
                            data.add(statement);
                        } else {
                            data.add(statement);
                        }
                        operationDate = statement.getOperationDate();
                    }
                }
                return data;
            }

            @Override
            protected void onPostExecute(ArrayList<ATFStatement> statements) {
                super.onPostExecute(statements);
                new Gson().toJson(statements);
                try {
                    progressDialog = Utilities.progressDialog(getActivity(), requireActivity().getResources().getString(R.string.t_loading));
                } catch (Exception e) {
                }
                boolean maxF = true, minF = true;

                int size = statements.size();
                if (response != null && size != 0) {
                    if (!exceptStartAndEndBalance) {
                        ATFStatement startBalance = new ATFStatement(-6);
                        startBalance.amount = response.startBalance;
                        startBalance.currency = currency;
                        startBalance.name = requireActivity().getResources().getString(R.string.start_balance);
                        statements.add(startBalance);
                    }

                    if (response.totalIncome != null && !response.totalIncome.isEmpty()) {
                        for (StatementsWithStats.Data data : response.totalIncome) {
                            ATFStatement minStat = new ATFStatement(-6);
                            minStat.currency = data.currency;
                            minStat.amount = data.value;
                            if (maxF) {
                                minStat.name = requireActivity().getResources().getString(R.string.total_admission);
                                maxF = false;
                            } else {
                                minStat.name = "";
                            }
                            statements.add(minStat);
                        }
                    }

                    if (response.totalExpense != null && !response.totalExpense.isEmpty()) {
                        for (StatementsWithStats.Data data : response.totalExpense) {
                            ATFStatement minStat = new ATFStatement(-6);
                            minStat.currency = data.currency;
                            minStat.amount = data.value;
                            if (minF) {
                                minStat.name = requireActivity().getResources().getString(R.string.total_write_off);
                                minF = false;
                            } else {
                                minStat.name = "";
                            }
                            statements.add(minStat);
                        }
                    }

                    if (response.totalFee != null) {
                        ATFStatement minStat = new ATFStatement(-6);
                        minStat.currency = response.totalFee.currency;
                        minStat.amount = response.totalFee.value;
                        minStat.name = requireActivity().getResources().getString(R.string.total_fee);
                        statements.add(minStat);
                    }
                        if (!exceptStartAndEndBalance) {
                        ATFStatement endBalance = new ATFStatement(-6);
                        endBalance.amount = response.endBalance;
                        endBalance.currency = currency;
                        endBalance.name = requireActivity().getResources().getString(R.string.end_balance);
                        statements.add(endBalance);
                    }
                }
                Log.i("STATMETSSIZE", "newStatments = " + statements);

                if (size == 0)
                    statements.add(new ATFStatement(Constants.HEADER_ID,
                            requireActivity().getResources().getString(R.string.not_data_fmt, fromDate, toDate)));

                try {
                    setAdapter(statements);
                } catch (Exception ignored) {
                }
            }
        }.execute();
    }

    private void getBundle() {
        if (getArguments() != null) {
            fromDate = getArguments().getString("fromDate");
            toDate = getArguments().getString("toDate");
            ifFirst = getArguments().getBoolean("ifFirst");
            Log.e("ololo", "getBundle: "+exceptStartAndEndBalance ); //check boolean
        }
    }
}