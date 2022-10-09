package kz.optimabank.optima24.fragment.template;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.TemplateActivity;
import kz.optimabank.optima24.app.NonSwipeableViewPager;
import kz.optimabank.optima24.controller.adapter.ATMTabAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;

/**
  Created by Timur on 27.04.2017.
 */

public class TemplateContainer extends ATFFragment {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewPager) NonSwipeableViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    ATMTabAdapter tabAdapter;
    String isTransferAtTempl;
    int isTTA = 9;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.template_contener, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        setViewPagerAdapter();
        return view;
    }

    private ArrayList<ATFFragment> getFragments() {
        ArrayList<ATFFragment> fragments = new ArrayList<>();
        fragments.add(new PaymentTemplateListFragment());
        fragments.add(new TransferTemplateListFragment());
        return fragments;
    }

    private ArrayList<String> getFragmentsTitle() {
        ArrayList<String> fragmentsTitle = new ArrayList<>();
        fragmentsTitle.add(getString(R.string.payments));
        fragmentsTitle.add(getString(R.string.transfer_accounts));
        return fragmentsTitle;
    }

    private void setViewPagerAdapter() {
        tabAdapter = new ATMTabAdapter(getChildFragmentManager(), getFragments(), getFragmentsTitle());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        getBundle();
        /*if (isTransferAtTempl!=null)
            if (isTransferAtTempl.equals("1")){
                viewPager.setCurrentItem(2);
            }*/
        Log.i("isTTA1","isTTA1 = "+isTTA);
            if (isTTA==1 || isTTA==9){
                viewPager.setCurrentItem(0);
            }else if (isTTA==0){
                viewPager.setCurrentItem(1);
            }
    }

    private void getBundle() {
        if(getArguments()!=null) {
            isTransferAtTempl = getArguments().getString("isTransferAtTempl");
            isTTA = getArguments().getInt("isTTA");
            Log.i("isTransferAtTempl8",""+isTransferAtTempl);
        }
    }

    private void initToolbar() {
        toolbar.setTitle("");
        ((TemplateActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}
