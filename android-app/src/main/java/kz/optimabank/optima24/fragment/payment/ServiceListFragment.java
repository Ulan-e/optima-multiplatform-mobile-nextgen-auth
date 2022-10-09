package kz.optimabank.optima24.fragment.payment;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.PaymentActivity;
import kz.optimabank.optima24.controller.adapter.ServiceListAdapter;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.fragment.payment.invoice.FindInvoiceAccountFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.manager.GeneralManager;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_ID;
import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_NAME;

public class ServiceListFragment extends ATFFragment implements TextWatcher {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.edSearch) EditText edSearch;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.clearSearch) ImageView clearSearch;

    int categoryId;
    int state;
    String categoryName;
    ArrayList<PaymentService> services;
    ArrayList<PaymentService> items;
    boolean isAutoPayment;
    ServiceListAdapter adapter;
    PaymentContextController paymentContextController;
    UserAccounts accounts;
    public static final String EXTERNAL_ID = "externalId";
    public static final int MOBILE_CATEGORY_ID = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.regions_list, container, false);
        ButterKnife.bind(this, view);
        getArgument();
        initToolbar();
        if (GeneralManager.getInstance().getSessionId() != null) {
            edSearch.addTextChangedListener(this);
        }
        paymentContextController = PaymentContextController.getController();

        clearSearch.setOnClickListener(v -> edSearch.setText(""));
        return view;
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (GeneralManager.getInstance().getSessionId() != null) {
            edSearch.setHint(getResources().getString(R.string.vendor_name));
            edSearch.setCursorVisible(true);
            edSearch.clearFocus();
            hideKeyboard();
            if (state == 1) {
                Log.d(TAG,"categoryId  = " + categoryId);
                if(categoryId == MOBILE_CATEGORY_ID){
                    GeneralManager.getInstance().setMobilePayment(true);
                }else{
                    GeneralManager.getInstance().setMobilePayment(false);
                }
                ArrayList<PaymentService> allServices = paymentContextController.getPaymentServiceByCategoryId(categoryId);
                Log.d(TAG,"allServices = " + allServices.size());
                services = new ArrayList<>();
                if(allServices!=null) {
                    for (int i = 0; i < allServices.size(); i++) {
                        PaymentService paymentService = allServices.get(i);
                        Log.d(TAG,"allServices paymentService = " + paymentService.name);
                        //int regionID = GeneralManager.getInstance().getRegionID();
                        //if (regionID==0){
                            services.add(paymentService);
                        /*}else
                            for (int j = 0; j < paymentService.regions.size(); j++) {
                                if (paymentService.regions.get(j).getId() == regionID || regionID==0) {
                                    services.add(paymentService);
                                }
                            }*/
                    }
                }
            }
            Log.d(TAG,"allServices services = " + services.size());
            if(services != null && !services.isEmpty()) {
                setAdapter(services);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(paymentContextController!=null) {
            paymentContextController.close();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable editable) {
        filter(editable.toString());
    }

    void filter(String text) {
        items = new ArrayList<>();
        for(PaymentService paymentService : services) {
            if(paymentService.name.toLowerCase().contains(text.toLowerCase())) {
                items.add(paymentService);
            }
        }
        if(adapter != null)
            adapter.updateList(items);
    }

    private void initToolbar() {
        tvTitle.setText(categoryName);

        toolbar.setTitle("");
        ((PaymentActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getArgument() {
        if(getArguments()!=null) {
            categoryName = getArguments().getString(CATEGORY_NAME);
            isAutoPayment = getArguments().getBoolean("isAutoPayment");
            state = getArguments().getInt("state");
            categoryId = getArguments().getInt(CATEGORY_ID);
            accounts = (UserAccounts) getArguments().getSerializable(ACCOUNT);
        }
    }

    private void setAdapter(ArrayList<PaymentService> services) {
        if(isAttached()) {
            adapter = new ServiceListAdapter(services, setOnClick());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
    }

    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                PaymentService paymentService = null;
                if(items!=null && !items.isEmpty()) {
                    paymentService = items.get(position);
                } else if(services!=null && !services.isEmpty()) {
                    paymentService = services.get(position);
                }
                if(paymentService!=null) {
                    Log.d(TAG,"paymentService.id = " + paymentService.id);
                    ATFFragment fragment;
                    final Bundle bundle = new Bundle();
                    bundle.putString(CATEGORY_NAME, paymentService.name);
                    bundle.putInt("paymentServiceId", paymentService.id);
                    bundle.putString(EXTERNAL_ID, getArguments().getString(EXTERNAL_ID));
                    if(paymentService.IsInvoiceable) {
                        fragment = new FindInvoiceAccountFragment();
                    } else {
                        if (accounts != null) {
                            bundle.putSerializable(ACCOUNT, accounts);
                        }

                        fragment = new PaymentFragment();
                    }
                    fragment.setArguments(bundle);
                    ((PaymentActivity) getActivity()).navigateToPage(fragment);
                }
            }
        };
    }
}
