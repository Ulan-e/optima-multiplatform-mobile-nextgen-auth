package kz.optimabank.optima24.fragment.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ntb.NavigationTabBar;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.AccountDetailsActivity;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.controller.adapter.ScheduleAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.response.LoanScheduleResponse;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.AccountsVisibility;
import kz.optimabank.optima24.model.interfaces.LoanSchedule;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AccountVisibilityImpl;
import kz.optimabank.optima24.model.service.LoanScheduleImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.formatDate;
import static kz.optimabank.optima24.utility.Utilities.getFormattedBalance;

/**
 * Created by Timur on 01.06.2017.
 */

public class CreditDetailsFragment extends ATFFragment implements LoanScheduleImpl.Callback, SwipeRefreshLayout.OnRefreshListener,
        AccountVisibilityImpl.Callback {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.pieChart)
    PieChart mChart;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ntb_horizontal)
    NavigationTabBar navigationTabBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;


    LoanSchedule loanSchedule;
    UserAccounts.CreditAccounts creditAccounts;
    ArrayList<LoanScheduleResponse> data = new ArrayList<>();
    String dateFrom, dateTo;
    String newName;
    double remainPay;
    AccountsVisibility accountsVisibility;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.credit_details_fragment, container, false);
        ButterKnife.bind(this, view);
        getBundle();
        initToolbar();

        setTollBarScrollFlags(true);
        if (creditAccounts != null) {
            initChart();
        }
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_color));

        navigationTabBar.setModels(getModels());
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {
            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                Intent intent = null;
                if (index == 0) {
                    intent = new Intent(getActivity(), NavigationActivity.class);
                    intent.putExtra("isMap", true);
                }

//                else if(index == 2) {
//                    intent = new Intent(getActivity(), NavigationActivity.class);
//                    intent.putExtra("isMap",true);
//                }

                if (intent != null) {
                    intent.putExtra(ACCOUNT, creditAccounts);
                    startActivity(intent);
                }
            }
        });
        setHasOptionsMenu(true);
        accountsVisibility = new AccountVisibilityImpl();
        accountsVisibility.registerCallBack(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setRefreshing(true);
        Log.d(TAG, "code = " + creditAccounts.code);
        Log.d(TAG, "accountType = " + creditAccounts.accountType);
        Log.d(TAG, "number = " + creditAccounts.number);
        Log.d(TAG, "currency = " + creditAccounts.currency);
        Log.d(TAG, "balance = " + creditAccounts.balance);
        Log.d(TAG, "name = " + creditAccounts.name);
        Log.d(TAG, "isBlocked = " + creditAccounts.isBlocked);
        Log.d(TAG, "isVisible = " + creditAccounts.isVisible);
        Log.d(TAG, "isClosed = " + creditAccounts.isClosed);
        Log.d(TAG, "agreementNumber = " + creditAccounts.agreementNumber);
        Log.d(TAG, "agreementAmount = " + creditAccounts.agreementAmount);
        Log.d(TAG, "agreementDate = " + creditAccounts.agreementDate);
        Log.d(TAG, "agreementEndDate = " + creditAccounts.agreementEndDate);
        Log.d(TAG, "interestRate = " + creditAccounts.interestRate);
        Log.d(TAG, "nextPaymentDate = " + creditAccounts.nextPaymentDate);
        Log.d(TAG, "nextPaymentAmount = " + creditAccounts.nextPaymentAmount);
        Log.d(TAG, "getNextPaymentDate = " + creditAccounts.getNextPaymentDate());
        Log.d(TAG, "getAgreementDate = " + creditAccounts.getAgreementDate());
        Log.d(TAG, "getAgreementDate = " + creditAccounts.getNextPaymentDay());
        tvTitle.setText(creditAccounts.name);
        dateFrom = Utilities.getDate("yyyy-MM-dd'T'HH:mm:ss", 6);
        dateTo = Utilities.getCurrentDate("yyyy-MM-dd'T'HH:mm:ss");
        loanSchedule = new LoanScheduleImpl();
        loanSchedule.registerCallBack(this);
        loanSchedule.getLoanSchedule(getActivity(), creditAccounts.code);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            View v = View.inflate(getActivity(), R.layout.message_rename_acc, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //final Button cancel = (Button) v.findViewById(R.id.btnCancel);
            //final Button ok = (Button) v.findViewById(R.id.btnOk);
            final EditText name = (EditText) v.findViewById(R.id.name);
            final TextView title = (TextView) v.findViewById(R.id.title);
            title.setText(R.string.change_number_credit);
            name.setText(creditAccounts.getName());
            name.setSelection(name.getText().length());
            ImageView clearSearch = (ImageView) v.findViewById(R.id.clearSearch);
            clearSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name.setText("");
                }
            });

            builder.setView(v);
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(R.string.status_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!creditAccounts.getName().equals(name.getText().toString()) && !name.getText().toString().isEmpty()) {
                        accountsVisibility.setVisibilityAccounts(getActivity(), getBody(creditAccounts, name.getText()));
                        newName = name.getText().toString();
                    }
                    dialog.dismiss();
                }
            });
            builder.show();

        }
        return super.onOptionsItemSelected(item);
    }

    private JSONArray getBody(UserAccounts account, Editable text) {
        if (account == null) {
            return null;
        }
        JSONArray bodyList = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Code", account.getCode());
            jsonObject.put("Name", text);
            jsonObject.put("Visible", account.isVisible);
            bodyList.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bodyList;
    }

    @Override
    public void jsonLoanScheduleResponse(int statusCode, String errorMessage) {
        if (isAdded()) {
            swipeRefreshLayout.setRefreshing(false);
            if (statusCode == Constants.SUCCESS) {
                setAdapter();
            } else if (statusCode != CONNECTION_ERROR_STATUS) {
                onError(errorMessage);
            }
        }
    }

    @Override
    public void onRefresh() {
        if (creditAccounts != null && loanSchedule != null) {
            loanSchedule.getLoanSchedule(getActivity(), creditAccounts.code);
        }
    }

    private void setAdapter() {
        if (isAdded()) {
            setTollBarScrollFlags(false);
            ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), getSortStatement(), creditAccounts);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
    }

    private ArrayList<LoanScheduleResponse> getSortStatement() {
        if (!data.isEmpty()) {
            data.clear();
        }
        ArrayList<LoanScheduleResponse> statements = GeneralManager.getInstance().getLoanScheduleResponses();
//        Collections.sort(statements, new Comparator<LoanScheduleResponse>() {
//            @Override
//            public int compare(LoanScheduleResponse o1, LoanScheduleResponse o2) {
//                return o2.date.compareTo(o1.date);
//            }
//        });
        String header = getString(R.string.loan_graphic);
        data.add(new LoanScheduleResponse(Constants.CREDIT_HEADER));
        data.add(new LoanScheduleResponse(Constants.HEADER_ID, header));
        if (statements.size() > 0) {
            data.addAll(statements);
        } else {
            data.clear();
            data.add(new LoanScheduleResponse(Constants.CREDIT_HEADER));
            data.add(new LoanScheduleResponse(-1, getString(R.string.not_data_fmt, formatDate(false, dateFrom), formatDate(false, dateTo))));
        }
        return data;
    }

    private void setTollBarScrollFlags(boolean isEnterAlways) {
        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        if (isEnterAlways) {
            p.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        } else {
            p.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }
        collapsingToolbarLayout.setLayoutParams(p);
    }

    private ArrayList<NavigationTabBar.Model> getModels() {
        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        final int bgColor = Color.parseColor("#00000000");
//        models.add (
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_pay),
//                        bgColor)
//                        .title(getString(R.string.replenish))
//                        .build()
//        );
//        models.add (
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.ic_list),
//                        bgColor)
//                        .title(getString(R.string.account_statement))
//                        .build()
//        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_button_grey_topbar_map),//ic_local
                        bgColor)
                        .title(getString(R.string.t_branch))
                        .build()
        );
        return models;
    }

    private void initToolbar() {
        ((AccountDetailsActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AccountDetailsActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getBundle() {
        if (getArguments() != null) {
            UserAccounts userAccounts = (UserAccounts) getArguments().getSerializable(ACCOUNT);
            if (userAccounts != null && userAccounts instanceof UserAccounts.CreditAccounts) {
                creditAccounts = (UserAccounts.CreditAccounts) userAccounts;
            }
        }
    }

    private void initChart() {
        mChart.getDescription().setEnabled(false);
        remainPay = creditAccounts.balance;//creditAccounts.agreementAmount -
        mChart.setCenterText(TextUtils.concat(generateCenterSpannableTextInfo(), "\n", "\n",
                generateCenterSpannableTextInfo2(), "\n", "\n", generateCenterSpannableTextInfo3()));
        mChart.setHoleColor(getResources().getColor(R.color.transparent));
        mChart.setHoleRadius(85f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(-90);
        mChart.setDescription(null);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setTouchEnabled(false);
        setData();
        mChart.highlightValues(null);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setEnabled(false);
    }

    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        float f1 = 0.0f;
        float f2 = 0.0f;
        if (!creditAccounts.isClosed) {
            f1 = (float) (creditAccounts.agreementAmount - remainPay);
            f2 = (float) remainPay;
            Log.i("FF", "f1 = " + f1);
            Log.i("FF", "f2 = " + f2);
        } else {
            f1 = 0;
            f2 = 1;
        }
        entries.add(new PieEntry(f1, ""));
        entries.add(new PieEntry(f2, ""));
        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setDrawIcons(false);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.orange_246_121_37));
        colors.add(getResources().getColor(R.color.blue_178_198_216));
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        mChart.setData(data);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableTextInfo() {
        SpannableString s = new SpannableString(getString(R.string.remain_pay));
        s.setSpan(new RelativeSizeSpan(1.1f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_178_198_216)), 0, s.length(), 0);
        return s;
    }

    private SpannableString generateCenterSpannableTextInfo2() {
        SpannableString s;
        if (!creditAccounts.isClosed)
            s = new SpannableString(getFormattedBalance(remainPay, creditAccounts.currency));
        else
            s = new SpannableString(getFormattedBalance(0, creditAccounts.currency));
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, s.length(), 0);
        return s;
    }

    private SpannableString generateCenterSpannableTextInfo3() {
        SpannableString s = new SpannableString(getString(R.string.of) + " " +
                getFormattedBalance(creditAccounts.agreementAmount, creditAccounts.currency));
        s.setSpan(new RelativeSizeSpan(1.1f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_178_198_216)), 0, s.length(), 0);
        return s;
    }

    @Override
    public void jsonAccountsVisibilityResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            GeneralManager.setUpdateAccList(true);
            tvTitle.setText(newName);
        }
    }
}
