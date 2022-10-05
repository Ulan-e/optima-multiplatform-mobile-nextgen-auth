package kz.optimabank.optima24.fragment.settings;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.app.OptimaBank;
import kz.optimabank.optima24.controller.adapter.DictionaryAdapter;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentRegions;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.model.manager.GeneralManager.setRegionSeleted;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;

/**
  Created by Timur on 11.07.2017.
 */

public class SelectRegionFragment extends ATFFragment implements TransferAndPaymentImpl.Callback , TextWatcher {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.edSearch) EditText edSearch;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.clearSearch) ImageView clearSearch;
    private ProgressDialog progressDialog;
    PaymentContextController paymentContextController = PaymentContextController.getController();
    ArrayList<Object> countries;
    ArrayList<Object> regions;
    ArrayList<Object> items;
    DictionaryAdapter adapter;
    TransferAndPayment transferAndPayment;

    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.regions_list, container, false);
        ButterKnife.bind(this, view);
        progressDialog = Utilities.progressDialog(parentActivity, getString(R.string.t_loading));
        initToolbar();
        prepareData();
        edSearch.addTextChangedListener(this);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSearch.setText("");
            }
        });
        return view;
    }

    private void prepareData() {
        if (paymentContextController.getAllPaymentRegions().isEmpty() || GeneralManager.isLocaleChanged()) {
            transferAndPayment = new TransferAndPaymentImpl();
            transferAndPayment.registerCallBack(this);
            transferAndPayment.getPaymentContext(getActivity());
            progressDialog.show();
        } else {
            setAdapter();
        }
    }

    private void setAdapter() {
        countries = new ArrayList<>();
        regions = new ArrayList<>();
        countries.addAll(paymentContextController.getAllPaymentRegions());

        for (Object ob : countries){
            PaymentRegions paymentRegions = (PaymentRegions) ob;
            //Log.i("id","id = "+paymentRegions.id);
            boolean be = false;
            for (Object obReg : regions){
                PaymentRegions paymentRegionsReg = (PaymentRegions) obReg;
                if(paymentRegionsReg.id == paymentRegions.id){
                    be = true;
                }
            }
            if (!be){
                regions.add(ob);
                be = false;
            }
        }
        regions.add(new PaymentRegions(0,getString(R.string.all_regions)));
        countries = regions;

        Log.i("SelectRegionFrag", "regions = " + regions);
        adapter = new DictionaryAdapter(regions,setOnClick());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                    }
                    @Override
                    public void onAnimationEnd(View view) {
                        PaymentRegions regions;
                        if(items!=null && !items.isEmpty()) {
                            regions = (PaymentRegions) items.get(position);
                        } else {
                            regions = (PaymentRegions) countries.get(position);
                        }
                        if(regions!=null) {
                            setRegionSeleted(true);
                            getConfirmDialog(regions);
                        }
                    }
                    @Override
                    public void onAnimationCancel(View view) {
                    }
                };
                clickAnimation(view,animatorListener);
            }
        };
    }

    private void getConfirmDialog(PaymentRegions regions) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OptimaBank.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constants.CHOSEN_REGION, regions.getId());
        editor.apply();
        if (alertDialog == null && isAttached()) {
            alertDialog = new AlertDialog.Builder(getActivity())
                    .setMessage(getString(R.string.region_selected) + " " + regions.getName())
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_dialog_alert)
                    .setTitle(R.string.t_attention_)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    getActivity().onBackPressed();
                                }
                            }).create();
            alertDialog.show();
        }
    }

    public void initToolbar() {
        toolbar.setTitle("");
        tvTitle.setText(getResources().getString(R.string.region));
        edSearch.setHint(getResources().getString(R.string.search_region));
        ((SettingsActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((SettingsActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(paymentContextController!=null) {
            paymentContextController.close();
        }
    }

    @Override
    public void jsonPaymentContextResponse(int statusCode, String errorMessage) {
        Log.i("statusCode","statusCode = " + statusCode);
        if (isAttached() && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if(statusCode==0) {
            setAdapter();
        } else if(statusCode!= CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {}

    @Override
    public void jsonTransferTemplateResponse(int statusCode, String errorMessage) {}


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(isAdded()&& adapter!=null) {
            if (editable.length() == 0) {
                adapter.updateList(countries);
                if (items != null) {
                    items.clear();
                }
            } else {
                filter(editable.toString());
            }
        }
    }

    void filter(String text) {
        items = new ArrayList<>();
        for(Object object : countries) {
            if (object instanceof PaymentRegions) {
                PaymentRegions paymentRegions = (PaymentRegions) object;
                if (paymentRegions.getName().toLowerCase().contains(text.toLowerCase())) {
                    items.add(paymentRegions);
                }
            }
        }
        adapter.updateList(items);
    }



}
