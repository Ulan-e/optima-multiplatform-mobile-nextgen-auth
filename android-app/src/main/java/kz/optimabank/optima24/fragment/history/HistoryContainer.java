package kz.optimabank.optima24.fragment.history;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.controller.adapter.ATMTabAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.manager.GeneralManager;

/**
  Created by Timur on 19.05.2017.
 */

public class HistoryContainer extends ATFFragment {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.tvTitle) TextView tvTitle;

    ATMTabAdapter atmTabAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_container, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(GeneralManager.getInstance().getSessionId()!=null) {
            setViewPagerAdapter();
        }
    }

    private void initToolbar() {
        ((MenuActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        //toolbar.setNavigationOnClickListener(toolBarNavigationOnClick);
    }

    private ArrayList<ATFFragment> getFragments(){
        ArrayList<ATFFragment> fragments = new ArrayList<>();
        fragments.add(new PaymentHistoryListFragment());
        fragments.add(new TransferHistoryListFragment());
        return fragments;
    }

    private ArrayList<String> getFragmentsTitle() {
        ArrayList<String> fragmentsTitle = new ArrayList<>();
        fragmentsTitle.add(getString(R.string.payments));
        fragmentsTitle.add(getString(R.string.transfers));
        return fragmentsTitle;
    }

    private void setViewPagerAdapter() {
        if(isAdded()) {
            atmTabAdapter = new ATMTabAdapter(getChildFragmentManager(), getFragments(), getFragmentsTitle());
            viewPager.setAdapter(atmTabAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }
}
