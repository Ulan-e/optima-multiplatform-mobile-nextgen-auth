package kz.optimabank.optima24.fragment.payment;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.activity.PaymentActivity;
import kz.optimabank.optima24.activity.TemplateActivity;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.controller.adapter.TransferFragAdapter;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.confirm.SuccessOperation;
import kz.optimabank.optima24.fragment.payment.invoice.FindInvoiceAccountFragment;
import kz.optimabank.optima24.model.base.Templates;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.base.TransferModel;
import kz.optimabank.optima24.model.gson.response.Invoices;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.interfaces.TransferAndPayment;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.TransferAndPaymentImpl;

import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_NAME;
import static kz.optimabank.optima24.activity.TransfersActivity.TRANSFER_NAME;
import static kz.optimabank.optima24.model.manager.GeneralManager.isRegionSeleted;
import static kz.optimabank.optima24.model.manager.GeneralManager.setRegionSeleted;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;

public class PaymentAdapFragment extends ATFFragment implements TransferAndPaymentImpl.Callback, TextWatcher,
        SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.edSearch) EditText edSearch;
    @BindView(R.id.clearSearch) ImageView clearSearch;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    SuccessOperation successOperation = new SuccessOperation();

    PaymentContextController paymentContextController;
    ArrayList<Object> fullListForAdapter = new ArrayList<>();
    ArrayList<Object> items = new ArrayList<>();
    ArrayList<Object> streamList=  new ArrayList<>();
    ArrayList<String> mobileOp = new ArrayList<>();
    TransferFragAdapter adapter;
    TransferAndPayment transferAndPayment;
    PaymentContextController paymentController;
    long invoiceId;
    int paymentServiceId;
    //int regionID;
    String categoryName;
    boolean isPaymentContext = true, isPaymentSubscriptions = true, isTransferTemplate = true, isClickableRecyclerView;
    List<PaymentService> paymentServices = new ArrayList<>();
    List<PaymentCategory> paymentCategories = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //regionID = GeneralManager.getInstance().getRegionID();
        paymentContextController = PaymentContextController.getController();

        mobileOp.add("ДОС");

        //получение "чистых" списков категорий платежей и их сервисов (чистые == регион сервисов совпадает с регионом приложения)
        /*for (PaymentCategory paymentCategory : paymentContextController.getAllPaymentCategory()) {
            boolean isAdd = false;
            for (PaymentService paymentService : paymentContextController.getPaymentServiceByCategoryId(paymentCategory.getId())) {
                if (paymentService.containsRegion(regionID) || regionID == 0)
                    if (!mobileOp.contains(paymentService.name))
                        isAdd = paymentServices.add(paymentService);
            }
            if (isAdd)
                paymentCategories.add(paymentCategory);
        }*/
        getPymentCategories();
    }

    private void getPymentCategories(){
        paymentCategories.clear();
        for (PaymentCategory paymentCategory : paymentContextController.getAllPaymentCategory()) {
            /*for (PaymentService paymentService : paymentContextController.getPaymentServiceByCategoryId(paymentCategory.getId())) {
                paymentServices.add(paymentService);
            }*/
            ArrayList<PaymentService> paymentService = paymentContextController.getPaymentServiceByCategoryId(paymentCategory.getId());
            if(paymentService.size() > 0)
                paymentCategories.add(paymentCategory);
            paymentServices.addAll(paymentService);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_payment_tab, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        initSwipeRefreshLayout();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)){
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

        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setBackgroundColor(Color.WHITE);
        edSearch.addTextChangedListener(this);

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSearch.setText("");
            }
        });
		return view;
	}

    @Override
    public void onStart() {
        super.onStart();
        if(isRegionSeleted()){
            setAdapter();
        }
        if (GeneralManager.getInstance().isNeedToUpdatePayTempl()){
            request(true);
        }
        successOperation.isTempl = false;
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        if (GeneralManager.getInstance().getSessionId() != null) {
            transferAndPayment = new TransferAndPaymentImpl();
            transferAndPayment.registerCallBack(this);
            request(false);
        }
	}

    @Override
    public void onResume() {
        super.onResume();
        isClickableRecyclerView = true;
        if(GeneralManager.getInstance().isNeedToUpdateTemplate()) {
            setAdapter();
            GeneralManager.getInstance().setNeedToUpdateTemplate(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(paymentController!=null) {
            paymentController.close();
        }
    }

    @Override
    public void jsonPaymentContextResponse(int statusCode,String errorMessage) {
        Log.d(TAG,"jsonPaymentContextResponse isPaymentContext,isPaymentSubscriptions,isTransferTemplate = "
                + isPaymentContext + " " + isPaymentSubscriptions + " " + isTransferTemplate + "  "+ statusCode);
        if(statusCode==0) {
            isPaymentContext = true;
            if(isPaymentSubscriptions&&isTransferTemplate) {
                getPymentCategories();
                Log.d(TAG,"jsonPaymentContextResponse isPaymentSubscriptions&&isTransferTemplate = true");
                setAdapter();
            }
        } else if(statusCode!= CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void jsonTransferTemplateResponse(int statusCode, String errorMessage) {
        Log.d(TAG,"jsonTransferTemplateResponse isPaymentContext,isPaymentSubscriptions,isTransferTemplate = "
                + isPaymentContext + " " + isPaymentSubscriptions + " " + isTransferTemplate + "  "+ statusCode);
        if(statusCode==0) {
            isTransferTemplate = true;
            if(isPaymentSubscriptions && isPaymentContext) {
                setAdapter();
            }
        } else if(statusCode!= CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if(isAdded()&& adapter!=null) {
            if (editable.length() == 0) {
                adapter.updateList(fullListForAdapter);
                if (items != null) {
                    items.clear();
                }
            } else {
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    filterWithStream(editable.toString());
                } else {*/
                    filter(editable.toString());
                //}
            }
        }
    }

    @Override
    public void onRefresh() {
        request(true);
        edSearch.setText("");
    }

    void filter(String text) {
        items.clear();
        text = text.toLowerCase();

        for (TemplatesPayment templatesPayment : GeneralManager.getInstance().getTemplatesPayment()){
            if (templatesPayment.name.toLowerCase().contains(text))
                items.add(templatesPayment);
        }

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


    @Override
    public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {
        Log.d(TAG,"jsonPaymentSubscriptionsResponse isPaymentContext,isPaymentSubscriptions,isTransferTemplate = "
                + isPaymentContext + " " + isPaymentSubscriptions + " " + isTransferTemplate + "  "+ statusCode);
        if(statusCode==0) {
            isPaymentSubscriptions = true;
            if(isPaymentContext && isTransferTemplate) {
                setAdapter();
            }
        } else if(statusCode!= CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    void filterWithStream(String text) {
        streamList.clear();
        String finalText = text.toLowerCase();
        List<TemplatesPayment> filteredTemplates =
                GeneralManager.getInstance().getTemplatesPayment()
                        .stream()
                        .filter(template -> template.name.toLowerCase().contains(finalText))
                        .collect(Collectors.toList());

        List<PaymentService> filteredServices = paymentServices
                .stream()
                .filter(service -> service.name.toLowerCase().contains(finalText))
                .collect(Collectors.toList());

        List<PaymentCategory> filteredPaymentCategory = paymentCategories
                .stream()
                .filter(paymentCategory -> paymentCategory.getName().toLowerCase().contains(finalText))
                .collect(Collectors.toList());

        streamList.addAll(filteredTemplates);
        streamList.addAll(filteredServices);
        streamList.addAll(filteredPaymentCategory);
        adapter.updateList(streamList);
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

    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                ViewPropertyAnimatorListener animatorListener = new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {}
                    @Override
                    public void onAnimationEnd(View view) {
                        Log.d("view","view = " + view);
                        if(isClickableRecyclerView) {
                            Object object = null;
                             if(items!=null && !items.isEmpty()) {
                                 object =  items.get(position);
                             } else if(fullListForAdapter !=null && !fullListForAdapter.isEmpty()) {
                                 object =  fullListForAdapter.get(position);
                             }
                            clickAction(object);
                        }
                    }
                    @Override
                    public void onAnimationCancel(View view) {}
                };
                clickAnimation(view,animatorListener);
            }
        };
    }

    private void clickAction(Object object) {
        if(object!=null) {
            Log.i("ITINVO","clickPAF");
            Intent intent = null;
            if (object instanceof PaymentCategory) {
                Log.i("ITINVO","clickP");
                PaymentCategory paymentCategory = (PaymentCategory) object;
                Log.i(".getExternalId()",".getExternalId() = "+paymentCategory.getExternalId());
                intent = new Intent(getActivity(), PaymentActivity.class);
                intent.putExtra(PaymentActivity.CATEGORY_ID, paymentCategory.getId());

                intent.putExtra(PaymentActivity.EXTERNAL_ID, paymentCategory.getExternalId());
                intent.putExtra(PaymentActivity.CATEGORY_ALIAS, paymentCategory.getAlias());
                intent.putExtra(PaymentActivity.CATEGORY_NAME, paymentCategory.getName());
            } else if (object instanceof TransferModel) {
                Log.i("ITINVO", "clickT");
                TransferModel transferModel = (TransferModel) object;
                intent = new Intent(getActivity(), TransfersActivity.class);
                intent.putExtra(TRANSFER_NAME, transferModel.name);
            } else if (object instanceof PaymentService) {
                PaymentService paymentService = (PaymentService) object;
                if (paymentService!=null){
                    Log.d(TAG,"paymentService.id = " + paymentService.id);
                    ATFFragment fragment;
                    //final Bundle bundle = new Bundle();
                    //bundle.putString(CATEGORY_NAME, paymentService.name);
                    //bundle.putInt("paymentServiceId", paymentService.id);
                    if(paymentService.IsInvoiceable) {
                        fragment = new FindInvoiceAccountFragment();
                    } else {
                        fragment = new PaymentFragment();
                    }
                    //fragment.setArguments(bundle);
                    Log.i("PaymentAdap", "NAME = " + paymentService.name);
                    ((MenuActivity) getActivity()).navigateToPage(fragment,paymentService.name,paymentService.id);
                }

            } else if (object instanceof Templates) {
                Log.i("ITINVO","PayAdapFragment");
                Templates template = (Templates) object;
                intent = new Intent(getActivity(), TemplateActivity.class);
                int checkInvoice = checkInvoice(template);
                if(checkInvoice == 0) {
                    Log.i("ITINVO","PayAdapFragment isInvoice");
                    intent.putExtra("InvoiceId",invoiceId);
                    intent.putExtra("paymentServiceId",paymentServiceId);
                    intent.putExtra(CATEGORY_NAME,categoryName);
                    intent.putExtra("isInvoice",true);
                } else if(checkInvoice == 1) {
                    onError(getString(R.string.not_invoices));
                    return;
                }
                intent.putExtra("isMainPage", true);
                intent.putExtra("template", template);
            }
            if (intent != null) {
                getActivity().startActivity(intent);
            }
        }
    }

    private int checkInvoice(Templates template) {
        if(template instanceof TemplatesPayment) {
            TemplatesPayment templatesPayment = (TemplatesPayment) template;
            paymentController = PaymentContextController.getController();
            PaymentService paymentService = paymentController.getPaymentServiceById(templatesPayment.serviceId);
            if (paymentService!=null)
                if (paymentService.IsInvoiceable) {
                    for (Invoices invoice : GeneralManager.getInstance().getInvoices()) {
                        if (invoice.getSubscriptionId() == templatesPayment.id) {
                            categoryName = paymentService.name;
                            invoiceId = invoice.getInvoiceId();
                            paymentServiceId = paymentService.id;
                            //this invoice, there is a receipt
                            return 0;
                        }
                    }
                    //this invoice, There is no receipt
                    return 1;
                } else {
                    //This is not an invoice
                    return 2;
                }
        }
        //error
        return -1000;
    }

    private void request(boolean isRefresh) {
        if(isAttached() && transferAndPayment != null) {
            if (!isRefresh) {
                if (PaymentContextController.getController().getAllPaymentCategory().isEmpty() || GeneralManager.isLocaleChanged()) {
                    isPaymentContext = false;
                    transferAndPayment.getPaymentContext(getActivity());
                }
                if (GeneralManager.getInstance().getTemplatesPayment().isEmpty() || GeneralManager.isLocaleChanged()) {
                    isPaymentSubscriptions = false;
                    transferAndPayment.getPaymentSubscriptions(getActivity());
                }
//            if (GeneralManager.getInstance().getTemplatesTransfer().isEmpty() || GeneralManager.isLocaleChanged()) {
//                isTransferTemplate = false;
//                transferAndPayment.getTransferTemplate(getActivity());
//            }
                if (isPaymentContext && isPaymentSubscriptions && isTransferTemplate) {
                    setAdapter();
                }
            } else {
                isPaymentContext = false;
                isPaymentSubscriptions = false;
                isTransferTemplate = false;
                transferAndPayment.getTransferTemplate(getActivity());
                transferAndPayment.getPaymentContext(getActivity());
                transferAndPayment.getPaymentSubscriptions(getActivity());
            }
        }
    }

    private void initToolbar() {
        ((MenuActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        //toolbar.setNavigationOnClickListener(toolBarNavigationOnClick);
        tvTitle.setText(R.string.payments);
    }

    public void setAdapter() {
        Log.i("setAdapterISADAP", "setAdapter = "+isRegionSeleted());
        setRegionSeleted(false);
        if(isAdded()) {
            fullListForAdapter.clear();
            fullListForAdapter.addAll(GeneralManager.getInstance().getTwoPaymentsTemplateWithHeaders(parentActivity));
            fullListForAdapter.addAll(paymentCategories);
            Log.i("setAdapterISADAP", "paymentCategories = "+paymentCategories.size());
            adapter = new TransferFragAdapter(getActivity(), fullListForAdapter, setOnClick(), 1);
            adapter.setHasStableIds(true);
            recyclerView.setAdapter(adapter);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 0);
        }
    }
}
