package kz.optimabank.optima24.fragment.requests;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
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
import kz.optimabank.optima24.controller.adapter.ATMTabAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.DATE_TAG;
import static kz.optimabank.optima24.utility.Constants.SELECT_DATE;

public class RequestsContainer extends ATFFragment {
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.app_bar_layout)AppBarLayout appBarLayout;
    @BindView(R.id.tvTitle)TextView tvTitle;
    @BindView(R.id.tabs)TabLayout tabs;
    @BindView(R.id.viewPager)ViewPager viewPager;
    String fromDate, toDate,period;
    ATMTabAdapter atmTabAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        setViewPagerAdapter();
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
                fromDate = data.getStringExtra("fromDate");
                toDate = data.getStringExtra("toDate");
                period = data.getStringExtra("period");
                //getAccountOperation();
                if(period!=null && !period.isEmpty()) {
                    tvTitle.setText(period);
                } else {
                    period =getOperationDate(fromDate) + "-" +  getOperationDate(toDate);
                    tvTitle.setText(period);
                }
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

    private void initToolbar() {
        ((NavigationActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((NavigationActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle(null);
        tvTitle.setText(getString(R.string.requests));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private ArrayList<ATFFragment> getFragments(){
        ArrayList<ATFFragment> fragments = new ArrayList<>();
        fragments.add(new ApplFragment());
        fragments.add(new HistoryApplFragment());
        return fragments;
    }

    private ArrayList<String> getFragmentsTitle() {
        ArrayList<String> fragmentsTitle = new ArrayList<>();
        fragmentsTitle.add(getString(R.string.payments_list));
        fragmentsTitle.add(getString(R.string.payments_history));
        return fragmentsTitle;
    }

    private void setViewPagerAdapter() {
        if(isAdded()) {
            atmTabAdapter = new ATMTabAdapter(getChildFragmentManager(), getFragments(), getFragmentsTitle());
            viewPager.setAdapter(atmTabAdapter);
            tabs.setupWithViewPager(viewPager);
        }
    }
}
