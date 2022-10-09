package kz.optimabank.optima24.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.controller.adapter.DictionaryAdapter;
import kz.optimabank.optima24.db.controllers.DictionaryController;
import kz.optimabank.optima24.db.entry.Dictionary;
import kz.optimabank.optima24.db.entry.ForeignBank;
import kz.optimabank.optima24.model.interfaces.DictionaryContext;
import kz.optimabank.optima24.model.interfaces.ForeignBankContext;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.service.DictionaryImpl;
import kz.optimabank.optima24.model.service.ForeignBankImpl;
import kz.optimabank.optima24.utility.Constants;

import static kz.optimabank.optima24.utility.Constants.DICTIONARY_KEY;
import static kz.optimabank.optima24.utility.Constants.STRING_KEY;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;

/**
  Created by Timur on 10.05.2017.
 */

public class SelectParameterActivity extends OptimaActivity implements DictionaryImpl.Callback, TextWatcher, ForeignBankImpl.Callback {
    public static final String IS_FOREIGN_BANK_EXTRA = "is_foreign_bank";

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.edSearch) EditText edSearch;
    @BindView(R.id.clearSearch) ImageView clearSearch;

    @BindView(R.id.no_data_view) View viewStub;

    DictionaryContext dictionary;
    DictionaryAdapter adapter;
    String parameterName;
    ArrayList<Object> dictionaries;
    ArrayList<Object> temp = new ArrayList<>();
    DictionaryController dictionaryController = DictionaryController.getController();
    ForeignBankContext foreignBankContext;
    boolean isForeignBank;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regions_list);
        ButterKnife.bind(this);
        parameterName = getIntent().getStringExtra("parameterName");
        isForeignBank = getIntent().getBooleanExtra(IS_FOREIGN_BANK_EXTRA, false);
        initToolbar();
        setHintSearch();
        edSearch.addTextChangedListener(this);
        if (dictionaryController.getDictionaryBic().isEmpty()) {
            dictionary = new DictionaryImpl();
            dictionary.registerCallBack(this);
            dictionary.getAllDictionary(this);
        } else {
            setAdapter();
        }

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSearch.setText("");
            }
        });
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
                        Intent intent = new Intent();
                        if (parameterName.equals(getString(R.string.receivers_country))) {
                            String country;
                            if (temp != null && !temp.isEmpty()) {
                                country = (String) temp.get(position);
                            } else {
                                country = (String) dictionaries.get(position);
                            }
                            intent.putExtra(STRING_KEY, country);
                        } else if (isForeignBank) {
                            ForeignBank foreignBank;
                            if(temp!=null && !temp.isEmpty()) {
                                foreignBank = (ForeignBank) temp.get(position);
                            } else {
                                foreignBank = (ForeignBank) dictionaries.get(position);
                            }
                            intent.putExtra(DICTIONARY_KEY, foreignBank);
                        } else {
                            Dictionary dictionary;
                            if(temp!=null && !temp.isEmpty()) {
                                dictionary = (Dictionary) temp.get(position);
                            } else {
                                dictionary = (Dictionary) dictionaries.get(position);
                            }
                            intent.putExtra(DICTIONARY_KEY, dictionary);
                        }
                        setResult(CommonStatusCodes.SUCCESS, intent);
                        finish();
                    }
                    @Override
                    public void onAnimationCancel(View view) {
                    }
                };
                clickAnimation(view,animatorListener);
            }
        };
    }

    private void setHintSearch() {
        if(parameterName!=null) {
            if(parameterName.equals(getResources().getString(R.string.citizenship)) || parameterName.equals(getString(R.string.country)) || parameterName.equals(getString(R.string.receivers_country))) {
                edSearch.setHint(getResources().getString(R.string.countries_name));
            }
//            else if(parameterName.equals(getResources().getString(R.string.text_seco_code))) {
//                edSearch.setHint(getResources().getString(R.string.seco_name));
//            }
            else if(parameterName.equals(getResources().getString(R.string.penalty_knp))) {
                edSearch.setHint(getResources().getString(R.string.knp_name));
            } else if(parameterName.equals(getResources().getString(R.string.text_bic))||parameterName.equals(getResources().getString(R.string.text_bic_mediator))) {
                edSearch.setHint(getResources().getString(R.string.bank_name));
            } else if(parameterName.equals(getResources().getString(R.string.vo_code))) {
                edSearch.setHint(getResources().getString(R.string.knp_name));
            }
        }
    }

    private void initToolbar() {
        tvTitle.setText(parameterName);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setAdapter() {
       dictionaries = new ArrayList<>();

//        if (parameterName.equals(getResources().getString(R.string.text_seco_code))) {
//            dictionaries.addAll(dictionaryController.getDictionarySeco());
//        }
//        else
            if (parameterName.equals(getResources().getString(R.string.penalty_knp))) {
            dictionaries.addAll(dictionaryController.getDictionaryKnp());
        } else if (parameterName.equals(getResources().getString(R.string.text_bic)) || parameterName.equals(getResources().getString(R.string.text_bic_mediator))) {
            if (isForeignBank) {
                dictionaries.addAll(dictionaryController.getForeignBanks());
            } else {
                dictionaries.addAll(dictionaryController.getDictionaryBic());
            }
        } else if (parameterName.equals(getResources().getString(R.string.vo_code))) {
            dictionaries.addAll(dictionaryController.getDictionaryVoCodes());
        } else if (parameterName.equals(getString(R.string.country))) {
            dictionaries.addAll(dictionaryController.getCountriesForRegMastercard());
        } else if (parameterName.equals(getString(R.string.receivers_country))) {
            dictionaries.addAll(getIntent().getStringArrayListExtra("customList"));
        }

        Log.i("dictionaries","dictionaries = "+dictionaries);
        Log.i("isForeignBank", "isForeignBank + " + isForeignBank);
        if (isForeignBank){
            adapter = new DictionaryAdapter(dictionaries, setOnClick(), isForeignBank);
        }else {
            adapter = new DictionaryAdapter(dictionaries, setOnClick());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void jsonDictionaryResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            setAdapter();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dictionaryController.close();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(final Editable editable) {
        if (isForeignBank) {
            if (editable.toString().isEmpty()) {
                dictionaries.clear();
                dictionaries.addAll(dictionaryController.getForeignBanks());
                adapter.updateList(dictionaries);
                prepareLayout(false);
                return;
            }
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            if (handler == null) {
                handler = new Handler();
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!editable.toString().isEmpty()) {
                        filterForForeignBanks(editable.toString());
                    }
                }
            }, 1000);
        } else {
            filter(editable.toString());
        }
    }

    private void filterForForeignBanks(String s) {
        if (foreignBankContext == null) {
            foreignBankContext = new ForeignBankImpl();
            foreignBankContext.registerCallBack(this);
        }
        foreignBankContext.filterForeignBanks(this, s);
    }

    void filter(String text) {
        //Log.i("temp","temp.size.befor = "+temp.size());
        Log.i("text","text = "+text);
        if (text.trim().isEmpty()){
            setAdapter();
            prepareLayout(false);
            return;
        }
        temp.clear();
        //Log.i("temp","temp.size.befor2 = "+temp.size());
        for(Object d : dictionaries) {
            if(d instanceof Dictionary){
                Dictionary dictionary = (Dictionary) d;
                if(dictionary.getDescription().toLowerCase().contains(text.toLowerCase()) || dictionary.getCode().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(dictionary);
                    //return;
                }
            } else if (d instanceof String) {
                if (((String) d).toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                }
            }
        }
        if (temp.isEmpty()) {
            prepareLayout(true);
        } else {
            adapter.updateList(temp);
            prepareLayout(false);
        }
        Log.i("temp","temp.size.after = "+temp.size());
        //setAdapter();
        //adapter.updateList(temp);
    }

    @Override
    public void jsonFilterForeignBankResponse(int statusCode, String errorMessage, ArrayList<ForeignBank> response) {
        if (statusCode == Constants.SUCCESS) {
            if (response != null && !response.isEmpty()) {
                dictionaries.clear();
                dictionaries.addAll(response);
                adapter.updateList(dictionaries);
                prepareLayout(false);
            } else {
                prepareLayout(true);
            }
        }
    }

    private void prepareLayout(boolean noData) {
        if (noData) {
            recyclerView.setVisibility(View.GONE);
            viewStub.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            viewStub.setVisibility(View.GONE);
        }
    }
}
