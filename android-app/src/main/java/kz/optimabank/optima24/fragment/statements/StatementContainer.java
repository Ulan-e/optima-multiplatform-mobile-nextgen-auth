package kz.optimabank.optima24.fragment.statements;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.NavigationActivity;
import kz.optimabank.optima24.activity.StatementActivity;
import kz.optimabank.optima24.controller.adapter.ATMTabAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.ATFStatement;
import kz.optimabank.optima24.model.base.StatementsWithStats;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.AccountOperation;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.AccountOperationImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.utility.Constants.DATE_TAG;
import static kz.optimabank.optima24.utility.Constants.SELECT_DATE;

/**
 Created by Timur on 29.05.2017.
 */

public class StatementContainer extends ATFFragment implements AccountOperationImpl.Callback {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.tvTitle) TextView tvTitle;

    UserAccounts userAccounts;
    ATMTabAdapter atmTabAdapter;
    AccountOperation accountOperation;
    String fromDate, toDate,period;
    boolean ifFirst = false;
    public static boolean isShow = false, exceptStartAndEndBalance;
    StatementsWithStats response;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statement_container_fragment, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        getBundle();
        fromDate = Utilities.formatDate(false, Utilities.getDate("yyyy-MM-dd'T'HH:mm:ss", 6));
        toDate = Utilities.formatDate(false, Utilities.getCurrentDate("yyyy-MM-dd'T'HH:mm:ss"));
        getAccountOperation();
        setViewPagerAdapter(0);
        viewPager.setOffscreenPageLimit(3);
        Log.d(TAG,"userAccounts balance = " + userAccounts.balance);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.data_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.calendar) {
            Intent intent = new Intent(getActivity(), NavigationActivity.class);
            intent.putExtra("selectDate",true);
            intent.putExtra(DATE_TAG,"Statement");
            startActivityForResult(intent,SELECT_DATE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CommonStatusCodes.SUCCESS && data!=null) {
            if (requestCode == SELECT_DATE) {
                isShow = true;
                fromDate = Utilities.formatDate(false, data.getStringExtra("fromDate"));
                toDate = Utilities.formatDate(false, data.getStringExtra("toDate"));
                period = data.getStringExtra("period");
                ifFirst = data.getBooleanExtra("ifFirst",false);
                getAccountOperation();
                if(period!=null && !period.isEmpty()) {
                    tvTitle.setText(period);
                } else {
                    period =getOperationDate(Utilities.formatDate(true, fromDate)) + " - " +  getOperationDate(Utilities.formatDate(true, toDate));
                    tvTitle.setText(period);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GeneralManager.getInstance().setStatementsAccount(new ArrayList<ATFStatement>());
    }

    @Override
    public void jsonAccountOperationsAndStatsResponse(int statusCode, String errorMessage, StatementsWithStats response) {
        this.response = response;
        setViewPagerAdapter(viewPager.getCurrentItem());
    }

    public void getAccountOperation() {
        accountOperation = new AccountOperationImpl();
        accountOperation.registerCallBack(this);
        accountOperation.getAccountOperationsAndStats(getActivity(), userAccounts.code, Utilities.formatDate(true, fromDate), Utilities.formatDate(true, toDate), false, true);
    }

    private void initToolbar() {
        ((StatementActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((StatementActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle(null);
        tvTitle.setText(getString(R.string.for_week));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private ArrayList<ATFFragment> getFragments() {
        ArrayList<ATFFragment> fragments = new ArrayList<>();
        AllStatement allStatement = new AllStatement();
        allStatement.response = response;
        allStatement.currency = userAccounts.currency;
        allStatement.exceptStartAndEndBalance = exceptStartAndEndBalance;
        fragments.add(allStatement);

        WriteOffStatement writeOffStatement = new WriteOffStatement();
        writeOffStatement.response = response;
        fragments.add(writeOffStatement);

        ReplenishmentStatement replenishmentStatement = new ReplenishmentStatement();
        replenishmentStatement.response = response;
        fragments.add(replenishmentStatement);

        Bundle bundle = new Bundle();
        bundle.putString("fromDate",fromDate);
        bundle.putString("toDate",toDate);
        bundle.putBoolean("ifFirst",ifFirst);
        fragments.get(0).setArguments(bundle);
        fragments.get(1).setArguments(bundle);
        fragments.get(2).setArguments(bundle);
        return fragments;
    }

    private ArrayList<String> getFragmentsTitle() {
        ArrayList<String> fragmentsTitle = new ArrayList<>();
        fragmentsTitle.add(getString(R.string.all_templates));
        fragmentsTitle.add(getString(R.string.debit));
        fragmentsTitle.add(getString(R.string.recharge));
        return fragmentsTitle;
    }

    private void setViewPagerAdapter(int pageNumber) {
        if(isAdded()) {
            atmTabAdapter = new ATMTabAdapter(getChildFragmentManager(), getFragments(), getFragmentsTitle());
            viewPager.setAdapter(atmTabAdapter);
            viewPager.setCurrentItem(pageNumber);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void getBundle() {
        if(getArguments()!=null) {
            userAccounts = (UserAccounts) getArguments().getSerializable(ACCOUNT);
            if(userAccounts instanceof UserAccounts.Cards && (((UserAccounts.Cards) userAccounts).isMultiBalance || ((UserAccounts.Cards) userAccounts).brandType == 1)){
                exceptStartAndEndBalance = true;
            }else {
                exceptStartAndEndBalance=false; // Если не мультивалютка перезаписываю булеан
            }
        }
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

    private SimpleDateFormat getSimpleDateFormat(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf;
    }
}