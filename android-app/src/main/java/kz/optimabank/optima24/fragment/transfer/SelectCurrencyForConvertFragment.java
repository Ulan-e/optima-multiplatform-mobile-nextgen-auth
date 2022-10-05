package kz.optimabank.optima24.fragment.transfer;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.controller.adapter.SelectCurrencyForConvertAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;

/**
  Created by Timur on 15.06.2017.
 */

public class SelectCurrencyForConvertFragment extends ATFFragment {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    //@BindView(R.id.imgWindowsClose) ImageView imgWindowsClose;
    @BindView(R.id.toolbar) Toolbar toolbar;

    UserAccounts.Cards convertCardFrom;
    SelectCurrencyForConvertAdapter adapter;
    String selectedCurrency;
    ArrayList<UserAccounts.Cards.MultiBalanceList> multiBalanceList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_currency_convert_fragment, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        getBundle();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void initToolbar() {
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
        /*imgWindowsClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });*/
    }

    private void getBundle() {
        if(getArguments()!=null) {
            convertCardFrom = (UserAccounts.Cards) getArguments().getSerializable("convertCard");
            selectedCurrency = getArguments().getString("selectedFromCurrency");
        }
    }

    private void setAdapter() {
        multiBalanceList = convertCardFrom.multiBalanceList;
        if (selectedCurrency !=null) {
            removeSelectedAccount();
        }
        adapter = new SelectCurrencyForConvertAdapter(getActivity(),multiBalanceList, setOnClick());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Intent intent = new Intent();
                intent.putExtra("multiBalanceItem", multiBalanceList.get(position));
                getActivity().setResult(CommonStatusCodes.SUCCESS, intent);
                getActivity().finish();
            }
        };
    }

    private void removeSelectedAccount() {
        Iterator<UserAccounts.Cards.MultiBalanceList> it = multiBalanceList.iterator();
        while (it.hasNext()) {
            UserAccounts.Cards.MultiBalanceList card = it.next();
            if (card.currency.equals(selectedCurrency)) {
                it.remove();
            }
        }
    }
}
