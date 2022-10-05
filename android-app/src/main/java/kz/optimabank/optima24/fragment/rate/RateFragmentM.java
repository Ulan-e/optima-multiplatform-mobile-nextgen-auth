package kz.optimabank.optima24.fragment.rate;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.controller.adapter.RateAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.base.Rate;
import kz.optimabank.optima24.model.interfaces.Rates;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.RatesImpl;
import kz.optimabank.optima24.utility.Constants;

public class RateFragmentM extends ATFFragment implements RatesImpl.Callback,SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.buyRate) TextView buyRate;
    @BindView(R.id.saleRate) TextView saleRate;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.linAll) LinearLayout linAll;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    RateAdapter adapter;
    Rates rates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_m, container, false);
        ButterKnife.bind(this, view);
        initSwipeRefreshLayout();
        inittoolbar();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rates = new RatesImpl();
        request();
    }

    @Override
    public void onRefresh() {
        request();
    }

    private void inittoolbar() {
        toolbar.setTitle(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void jsonRatesResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            setAdapter();
        } else if (statusCode != Constants.CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_color));

        swipeRefreshLayout.setRefreshing(true);
    }

    private void request() {
        rates.registerCallBack(this);
        rates.getRates(getActivity(), false);
    }

    private void setAdapter() {
        if(isAdded()) {
            ArrayList<Rate> rates = new ArrayList<>();

            ArrayList<Rate> sortedListByTypeRates = new ArrayList<>();
            rates.addAll(GeneralManager.getInstance().getRate());
            Log.i("rates.size()", " = " + rates.size());
            for (int i = 0; i < rates.size(); i++){
                if(rates.get(i).ForeignCurrency.equals("GBP") && rates.get(i).BaseCurrency.equals("KGS")){
                    rates.remove(i);
                }
            }

            for(int i = 0; i<4; i++){
                Rate rateHeader = new Rate(Constants.HEADER_ID);
                rateHeader.type = i;
                sortedListByTypeRates.add(rateHeader);

                for(Rate rate: rates){
                    if(rate.type == i){
                        sortedListByTypeRates.add(rate);
                    }
                }
            }
            Log.i("sortedListByTypeRates", " = " + sortedListByTypeRates.size());

            sortedListByTypeRates.add(new Rate(Constants.FOOTER_ID));
            adapter = new RateAdapter(getActivity(),sortedListByTypeRates);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}