package kz.optimabank.optima24.fragment.references;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.PaymentActivity;
import kz.optimabank.optima24.controller.adapter.TransferFragAdapter;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.payment.PaymentFragment;
import kz.optimabank.optima24.fragment.payment.ServiceListFragment;
import kz.optimabank.optima24.fragment.payment.invoice.FindInvoiceAccountFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;
import kz.optimabank.optima24.utility.Constants;
import kz.optimabank.optima24.utility.Utilities;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_ID;
import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_NAME;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;

public class PaymentListFragment extends ATFFragment implements TransferAndPaymentImpl.Callback, TextWatcher {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.edSearch)
    TextView edSearch;
    @BindView(R.id.clearSearch)
    ImageView clearSearch;
    private ProgressDialog progressDialog;

    UserAccounts account;
    //TransferAndPaymentAdapter adapter;
    TransferFragAdapter adapter;
    ArrayList<Object> fullListForAdapter = new ArrayList<>();
    ArrayList<Object> items = new ArrayList<>();
    ArrayList<String> mobileOp = new ArrayList<>();
    PaymentContextController paymentContextController;
    TransferAndPayment transferAndPayment;
    private String categoryAlias;
    //int regionID;
    boolean isPaymentContext = true, isPaymentSubscriptions = true, isTransferTemplate = true;
    List<PaymentService> paymentServices = new ArrayList<>();
    List<PaymentCategory> paymentCategories = new ArrayList<>();
    public static final String EXTERNAL_ID = "externalId";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payments_list, container, false);
        ButterKnife.bind(this, view);
        progressDialog = Utilities.progressDialog(parentActivity, getString(R.string.t_loading));
        initToolbar();
        getBundle();
        edSearch.addTextChangedListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //return adapter.getItemViewType(position);
                switch (adapter.getItemViewType(position)) {
                    case 1:
                        return 1;
                    case 4:
                        return 1;
                    case 11:
                        return 1;
                    default:
                        return 3;
                }
            }
        });
        paymentContextController = PaymentContextController.getController();
        recyclerView.setLayoutManager(gridLayoutManager);

        mobileOp.add("ДОС");
        paymentCategories.clear();
        for (PaymentCategory paymentCategory : paymentContextController.getAllPaymentCategory()) {
            ArrayList<PaymentService> paymentService = paymentContextController.getPaymentServiceByCategoryId(paymentCategory.getId());
            if (paymentService.size() > 0)
                paymentCategories.add(paymentCategory);
            paymentServices.addAll(paymentService);
        }

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSearch.setText("");
            }
        });
        //regionID = GeneralManager.getInstance().getRegionID();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (paymentContextController != null) {
            paymentContextController.close();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        transferAndPayment = new TransferAndPaymentImpl();
        transferAndPayment.registerCallBack(this);
        request();
    }

    @Override
    public void jsonPaymentContextResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            isPaymentContext = true;
            if (isPaymentSubscriptions && isTransferTemplate) {
                setAdapter();
            }
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            isPaymentSubscriptions = true;
            if (isPaymentContext && isTransferTemplate) {
                setAdapter();
            }
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void jsonTransferTemplateResponse(int statusCode, String errorMessage) {
        if (statusCode == Constants.SUCCESS) {
            isTransferTemplate = true;
            if (isPaymentSubscriptions && isPaymentContext) {
                setAdapter();
            }
        } else if (statusCode != CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    private void initToolbar() {
        ((PaymentActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((PaymentActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getBundle() {
        Log.d("Payamantlistfragment", " = " + getArguments().getString(EXTERNAL_ID));

        if (getArguments() != null) {
            Log.d("Payamantlistfragment", " = " + getArguments().getString(EXTERNAL_ID));

            account = (UserAccounts) getArguments().getSerializable(ACCOUNT);
        }
    }

    private void setAdapter() {
        if (isAdded()) {
            fullListForAdapter.clear();
            fullListForAdapter.addAll(paymentCategories);

            if (isAttached() && progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            adapter = new TransferFragAdapter(getActivity(), fullListForAdapter, setOnClick(), 1);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
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
                        Object object;

                        if (!items.isEmpty() && items != null) {
                            object = items.get(position);
                        } else {
                            object = fullListForAdapter.get(position);
                        }

                        if (object instanceof PaymentCategory) {
                            ATFFragment fragment;
                            Bundle bundle = new Bundle();
                            PaymentCategory paymentCategory = (PaymentCategory) object;
                            categoryAlias = paymentCategory.getAlias();
                            if (categoryAlias != null && categoryAlias.equals("mobile")) {
                                bundle.putBoolean("isMobile", true);
                                bundle.putString(EXTERNAL_ID, getArguments().getString(EXTERNAL_ID));
                                fragment = new PaymentFragment();
                            } else {
                                fragment = new ServiceListFragment();
                                bundle.putInt(CATEGORY_ID, paymentCategory.getId());
                                bundle.putBoolean("isAutoPayment", false);
                                bundle.putInt("state", 1);
                                bundle.putString(EXTERNAL_ID, getArguments().getString(EXTERNAL_ID));
                            }

                            bundle.putString(CATEGORY_NAME, paymentCategory.getName());
                            bundle.putSerializable(ACCOUNT, account);
                            bundle.putString(EXTERNAL_ID, getArguments().getString(EXTERNAL_ID));
                            fragment.setArguments(bundle);
                            ((PaymentActivity) getActivity()).navigateToPage(fragment, true);
                        } else if (object instanceof PaymentService) {
                            PaymentService paymentService = (PaymentService) object;
                            if (paymentService != null) {
                                Log.d(TAG, "paymentService.id = " + paymentService.id);
                                ATFFragment fragment;
                                //final Bundle bundle = new Bundle();
                                //bundle.putString(CATEGORY_NAME, paymentService.name);
                                //bundle.putInt("paymentServiceId", paymentService.id);
                                if (paymentService.IsInvoiceable) {
                                    fragment = new FindInvoiceAccountFragment();
                                } else {
                                    fragment = new PaymentFragment();
                                }
                                //fragment.setArguments(bundle);
                                ((PaymentActivity) getActivity()).navigateToPage(fragment, paymentService.name, paymentService.id);
                            }

                        }
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                    }
                };
                clickAnimation(view, animatorListener);
            }
        };
    }

    private void request() {
        if (PaymentContextController.getController().getAllPaymentCategory().isEmpty() || GeneralManager.isLocaleChanged()) {
            isPaymentContext = false;
            transferAndPayment.getPaymentContext(getActivity());
            progressDialog.show();
        }
        if (GeneralManager.getInstance().getTemplatesPayment().isEmpty() || GeneralManager.isLocaleChanged()) {
            isPaymentSubscriptions = false;
            transferAndPayment.getPaymentSubscriptions(getActivity());
            progressDialog.show();
        }
        if (GeneralManager.getInstance().getTemplatesTransfer().isEmpty() || GeneralManager.isLocaleChanged()) {
            isTransferTemplate = false;
            transferAndPayment.getTransferTemplate(getActivity());
            progressDialog.show();
        }
        if (isPaymentContext && isPaymentSubscriptions && isTransferTemplate) {
            setAdapter();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isAdded() && adapter != null) {
            if (editable.length() == 0) {
                adapter.updateList(fullListForAdapter);
                if (items != null) {
                    items.clear();
                }

            } else {
                filter(editable.toString());
            }
        }
    }

    void filter(String text) {
        items.clear();
        text = text.toLowerCase();

        for (PaymentService paymentService : paymentServices) {
            if (paymentService.name.toLowerCase().contains(text))
                items.add(paymentService);
        }

        for (PaymentCategory paymentCategory : paymentCategories) {
            if (paymentCategory.getName().toLowerCase().contains(text))
                items.add(paymentCategory);
        }
        adapter.updateList(items);
    }
}
