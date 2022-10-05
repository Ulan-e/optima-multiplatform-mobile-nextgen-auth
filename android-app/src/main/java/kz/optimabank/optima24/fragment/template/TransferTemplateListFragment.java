package kz.optimabank.optima24.fragment.template;

import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
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

public class TransferTemplateListFragment extends ATFFragment implements TransferAndPaymentImpl.Callback,
        SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView) SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    TemplateSwipeAdapter adapter;
    TransferAndPayment transferAndPayment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transferAndPayment = new TransferAndPaymentImpl();
        transferAndPayment.registerCallBack(this);
//        if(recyclerView!=null){
//            recyclerView.removeAllViews();
//        }
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

    @Override
    public void onStart() {
        if (GeneralManager.getInstance().isNeedToUpdatePayTempl()){
            transferAndPayment.getPaymentContext(getActivity());
            transferAndPayment.getPaymentSubscriptions(getActivity());
            transferAndPayment.getTransferTemplate(getActivity());
        }
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_list, container, false);

        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initSwipeRefreshLayout();
        setAdapter();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
//        recyclerView.removeAllViews();
    }

    private void setAdapter() {
        ArrayList<Object> templatesTransfer = new ArrayList<>();
        templatesTransfer.addAll(GeneralManager.getInstance().getTemplatesTransfer());
        Collections.reverse(templatesTransfer);
        Log.d("TAG","templatesTransfer = " + templatesTransfer);
        adapter = new TemplateSwipeAdapter(getActivity(),templatesTransfer);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 0);
    }

    private void request() {
        //transferAndPayment.getPaymentContext(getActivity());
        //transferAndPayment.getPaymentSubscriptions(getActivity());
        transferAndPayment.getTransferTemplate(getActivity());
        setAdapter();
    }

    @Override
    public void onRefresh() {
        request();
    }

    @Override
    public void jsonPaymentContextResponse(int statusCode, String errorMessage) {

    }

    @Override
    public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {

    }

    @Override
    public void jsonTransferTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode==0){
            setAdapter();
        }
    }
}
