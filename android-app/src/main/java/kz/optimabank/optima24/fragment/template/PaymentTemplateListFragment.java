package kz.optimabank.optima24.fragment.template;

import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tubb.smrv.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.controller.adapter.TemplateSwipeAdapter;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;

public class PaymentTemplateListFragment extends ATFFragment implements TransferAndPaymentImpl.Callback,
        SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView) SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    TemplateSwipeAdapter adapter;
    TransferAndPayment transferAndPayment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_list, container, false);
        ButterKnife.bind(this, view);
        transferAndPayment = new TransferAndPaymentImpl();
        transferAndPayment.registerCallBack(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initSwipeRefreshLayout();
        setAdapter();
        return view;
    }

    @Override
    public void onStart() {
        if (GeneralManager.getInstance().isNeedToUpdatePayTempl()){
            transferAndPayment.getPaymentContext(getActivity());
            transferAndPayment.getPaymentSubscriptions(getActivity());
            transferAndPayment.getTransferTemplate(getActivity());
        }
        super.onStart();
    }

    private void initSwipeRefreshLayout() {
        int actionBarSize = getActionBarSize(getActivity());
        int progressViewStart = getResources().getDimensionPixelSize(R.dimen.search_app_bar_height) - actionBarSize;
        int progressViewEnd = progressViewStart + (int) (actionBarSize * 1.5f);
        swipeRefreshLayout.setProgressViewOffset(true, progressViewStart, progressViewEnd);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_color));

        swipeRefreshLayout.setRefreshing(true);
    }

    private void setAdapter() {
        ArrayList<Object> templatesPayment = new ArrayList<>();
        templatesPayment.addAll(GeneralManager.getInstance().getTemplatesPayment());
        Collections.reverse(templatesPayment);
        adapter = new TemplateSwipeAdapter(getActivity(), templatesPayment);
        recyclerView.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
        adapter.notifyDataSetChanged();
    }

    private void request() {
            //transferAndPayment.getPaymentContext(getActivity());
            transferAndPayment.getPaymentSubscriptions(getActivity());
            //transferAndPayment.getTransferTemplate(getActivity());
            setAdapter();
    }

    @Override
    public void jsonPaymentContextResponse(int statusCode, String errorMessage) {

    }

    @Override
    public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {
        if (statusCode==0){
            setAdapter();
        }
    }

    @Override
    public void jsonTransferTemplateResponse(int statusCode, String errorMessage) {

    }

    @Override
    public void onRefresh() {
        request();
    }
}
