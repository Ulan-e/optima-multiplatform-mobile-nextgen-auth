package kz.optimabank.optima24.fragment.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.optimabank.optima24.R;
import kz.optimabank.optima24.activity.SettingsActivity;
import kz.optimabank.optima24.controller.adapter.DictionaryAdapter;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.Country;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;

import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;

/**
  Created by Timur on 11.07.2017.
 */

public class SelectCountryFragment extends ATFFragment implements TransferAndPaymentImpl.Callback , TextWatcher {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.edSearch) EditText edSearch;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    PaymentContextController paymentContextController = PaymentContextController.getController();
    ArrayList<Object> countries;
    ArrayList<Object> items;
    DictionaryAdapter adapter;
    TransferAndPayment transferAndPayment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.regions_list, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        setAdapter();
        request();
        edSearch.addTextChangedListener(this);
        return view;
    }

    private void request() {
        if (paymentContextController.getAllPaymentCountryes().isEmpty() || GeneralManager.isLocaleChanged()) {
            transferAndPayment = new TransferAndPaymentImpl();
            transferAndPayment.registerCallBack(this);
            transferAndPayment.getPaymentContext(getActivity());
        }
    }

    private void setAdapter() {
        countries = new ArrayList<>();
        countries.addAll(paymentContextController.getAllPaymentCountryes());
        adapter = new DictionaryAdapter(countries,setOnClick());
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
                        Country country;
                        if(items!=null && !items.isEmpty()) {
                            country = (Country) items.get(position);
                        } else {
                            country = (Country) countries.get(position);
                        }
                        if(country!=null) {
                            getConfirmDialog(country);
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

    private void getConfirmDialog(Country country) {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OptimaBank.getContext());
        //SharedPreferences.Editor editor = preferences.edit();
        //editor.putInt(Constants.CHOSEN_REGION, country.getId());
        //editor.apply();
        new AlertDialog.Builder(requireActivity())
                .setMessage(getString(R.string.region_selected) + " " + country.getName())
                .setCancelable(false)
                .setIcon(R.drawable.ic_dialog_alert)
                .setTitle(R.string.t_attention_)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                getActivity().onBackPressed();
                            }
                        }).create().show();
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
            if (object instanceof Country) {
                Country country = (Country) object;
                if (country.getName().toLowerCase().contains(text.toLowerCase())) {
                    items.add(country);
                }
            }
        }
        adapter.updateList(items);
    }



}
