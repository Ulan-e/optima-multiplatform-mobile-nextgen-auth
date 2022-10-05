package kz.optimabank.optima24.fragment.transfer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.activity.MenuActivity;
import kz.optimabank.optima24.activity.PaymentActivity;
import kz.optimabank.optima24.activity.TemplateActivity;
import kz.optimabank.optima24.activity.TransfersActivity;
import kz.optimabank.optima24.controller.adapter.TransferFragAdapter;
import kz.optimabank.optima24.controller.adapter.TransferServiceAdapter;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentCategory;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.confirm.SuccessOperation;
import kz.optimabank.optima24.model.ServiceModel;
import kz.optimabank.optima24.model.TransferServiceModel;
import kz.optimabank.optima24.model.base.TemplateTransfer;
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
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;
import static kz.optimabank.optima24.utility.Utilities.clickAnimation;

public class TransfersFragment extends ATFFragment implements TransferAndPaymentImpl.Callback, TextWatcher,
        SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.recyclerView_transfer) RecyclerView transfer_recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.edSearch) EditText edSearch;
    @BindView(R.id.clearSearch) ImageView clearSearch;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    SuccessOperation successOperation = new SuccessOperation();

    ArrayList<Object> objects = new ArrayList<>();
    ArrayList<Object> items = new ArrayList<>();
    TransferFragAdapter adapter;
    TransferAndPayment transferAndPayment;
    PaymentContextController paymentController;
    long invoiceId;
    int paymentServiceId;
    String categoryName;
    boolean isPaymentContext = true, isPaymentSubscriptions = true, isTransferTemplate = true, isClickableRecyclerView;
    ArrayList<TransferServiceModel> serviceList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_payment_tab, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        initSwipeRefreshLayout();
        //setHasOptionsMenu(true);
        Log.e(TAG, "onCreateView: "+ getClass().getSimpleName().toUpperCase());

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSearch.setText("");
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //return adapter.getItemViewType(position);
                switch(adapter.getItemViewType(position)){
                    case 4:
                    case 11:
                        return 1;
                    default:
                        return 4;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setBackgroundColor(Color.WHITE);
        edSearch.addTextChangedListener(this);
//        initTransferAdapter();
        return view;
	}

    private void initTransferAdapter() {// TODO: 03.02.2021 build Adapter
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        transfer_recyclerView.setLayoutManager(gridLayoutManager);
        TransferServiceAdapter adapter = new TransferServiceAdapter();
        transfer_recyclerView.setAdapter(adapter);
        serviceList.add(new TransferServiceModel("https://img.flaticon.com/icons/png/512/1/1213.png?size=1200x630f&pad=10,10,10,10&ext=png&bg=FFFFFFFF", "Блокировать карту"));
        serviceList.add(new TransferServiceModel("https://previews.123rf.com/images/tatianasun/tatianasun1802/tatianasun180200037/95294165-contactless-credit-card-icon-card-with-radio-wave-sign-bank-card-payment-isolated-icon-vector-.jpg", "Заказать доп карту"));
        adapter.setServiceData(serviceList);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (GeneralManager.getInstance().isNeedToUpdateTemplate()){
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

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
        //inflater.inflate(R.menu.chart_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(getActivity(), NavigationActivity.class);
        intent.putExtra("isMap",true);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void jsonPaymentContextResponse(int statusCode,String errorMessage) {
        Log.d(TAG,"jsonPaymentContextResponse isPaymentContext,isPaymentSubscriptions,isTransferTemplate = "
                + isPaymentContext + " " + isPaymentSubscriptions + " " + isTransferTemplate);
        if(statusCode==0) {
            isPaymentContext = true;
            if(isPaymentSubscriptions&&isTransferTemplate) {
                setAdapter();
            }
        } else if(statusCode!= CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void jsonPaymentSubscriptionsResponse(int statusCode, String errorMessage) {
        Log.d(TAG,"jsonPaymentSubscriptionsResponse isPaymentContext,isPaymentSubscriptions,isTransferTemplate = "
                + isPaymentContext + " " + isPaymentSubscriptions + " " + isTransferTemplate);
        if(statusCode==0) {
            isPaymentSubscriptions = true;
            if(isPaymentContext && isTransferTemplate) {
                setAdapter();
            }
        } else if(statusCode!= CONNECTION_ERROR_STATUS) {
            onError(errorMessage);
        }
    }

    @Override
    public void jsonTransferTemplateResponse(int statusCode, String errorMessage) {
        Log.d(TAG,"jsonTransferTemplateResponse isPaymentContext,isPaymentSubscriptions,isTransferTemplate = "
                + isPaymentContext + " " + isPaymentSubscriptions + " " + isTransferTemplate);
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
                adapter.updateList(objects);
                if (items != null) {
                    items.clear();
                }
            } else {
                filter(editable.toString());
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
        ArrayList<TemplateTransfer> templateTransfers = GeneralManager.getInstance().getTemplatesTransfer();
        if (templateTransfers != null) {
            for (TemplateTransfer templateTransfer : templateTransfers) {
                if (templateTransfer.getName().toLowerCase().contains(text))
                    items.add(templateTransfer);
            }
        }
        for (TransferModel transferModel : GeneralManager.getInstance().getTransfersWithoutHead(parentActivity)) {
            if (transferModel.name.toLowerCase().contains(text))
                items.add(transferModel);
        }

        /*for(Object object : GeneralManager.getInstance().getTransfersAndTwoTransTemplate(getActivity())) {
            if(object instanceof Templates) {
                Templates templates = (Templates) object;
                if(templates instanceof TemplatesPayment &&
                        ((TemplatesPayment) templates).name.toLowerCase().contains(text.toLowerCase())) {
                    items.add(templates);
                } else if(templates instanceof TemplateTransfer &&
                        ((TemplateTransfer) templates).getName().toLowerCase().contains(text.toLowerCase())) {
                    items.add(templates);
                }
            } else if(object instanceof TransferModel) {
                TransferModel transfer = (TransferModel) object;
                if(transfer.name.toLowerCase().contains(text.toLowerCase())) {
                    items.add(transfer);
                }
            }
        }*/
        adapter.updateList(items);
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
                            Log.d(TAG,"items = " + items);
                             if(items!=null && !items.isEmpty()) {
                                 object =  items.get(position);
                             } else if(objects !=null && !objects.isEmpty()) {
                                 object =  objects.get(position);
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
            Log.i("11111mmmmmm","222222mmmmmm");
            Intent intent = null;
            if (object instanceof PaymentCategory) {
                Log.i("22222mmmmmm","333333mmmmmm");
                PaymentCategory paymentCategory = (PaymentCategory) object;
                intent = new Intent(getActivity(), PaymentActivity.class);
                intent.putExtra(PaymentActivity.CATEGORY_ID, paymentCategory.getId());
                intent.putExtra(PaymentActivity.CATEGORY_ALIAS, paymentCategory.getAlias());
                intent.putExtra(PaymentActivity.CATEGORY_NAME, paymentCategory.getName());
            } else if (object instanceof TransferModel) {
                Log.i("333333mmmmmm","4444444mmmmmm");
                TransferModel transferModel = (TransferModel) object;
                intent = new Intent(getActivity(), TransfersActivity.class);
                intent.putExtra(TRANSFER_NAME, transferModel.name);
            } else if (object instanceof Templates) {
                Log.i("mmmmmmmm","yyeeeeaaahhhhhhh");
                Templates template = (Templates) object;
                intent = new Intent(getActivity(), TemplateActivity.class);
                intent.putExtra("isTTA",0);
                int checkInvoice = checkInvoice(template);
                if(checkInvoice == 0) {
                    intent.putExtra("InvoiceId",invoiceId);
                    intent.putExtra("paymentServiceId",paymentServiceId);
                    intent.putExtra(CATEGORY_NAME,categoryName);
                    intent.putExtra("isInvoice",true);
                } else if(checkInvoice == 1) {
                    onError(getString(R.string.not_invoices));
                    return;
                }
                intent.putExtra("isMainPage", false);
                intent.putExtra("template", template);
            }
            if (intent != null) {
                Log.i("567567567","567567567");
                getActivity().startActivity(intent);
            }
        }
    }

    private int checkInvoice(Templates template) {
        if(template instanceof TemplatesPayment) {
            TemplatesPayment templatesPayment = (TemplatesPayment) template;
            paymentController = PaymentContextController.getController();
            PaymentService paymentService = paymentController.getPaymentServiceById(templatesPayment.serviceId);
            if(paymentService.IsInvoiceable) {
                for (Invoices invoice : GeneralManager.getInstance().getInvoices()) {
                    if(invoice.getSubscriptionId() == templatesPayment.id) {
                        categoryName = paymentService.name;
                        invoiceId = invoice.getInvoiceId();
                        paymentServiceId = paymentService.id;
                        //this invoice, there is a receipt
                        Log.i("this is invoise","this is invoise");
                        return 0;
                    }
                }Log.i("this is invoise1111","this is invoise1111");
                //this invoice, There is no receipt
                return 1;
            } else {
                Log.i("this is invoise2222","this is invoise2222");
                //This is not an invoice
                return 2;
            }
        }Log.i("this is NOTinvoise","this is NOTinvoise");
        //error
        return -1000;
    }

    private void request(boolean isRefresh) {
        if(!isRefresh) {
            if (transferAndPayment != null) {
                if (PaymentContextController.getController().getAllPaymentCategory().isEmpty() || GeneralManager.isLocaleChanged()) {
                    isPaymentContext = false;
                    transferAndPayment.getPaymentContext(getActivity());
                }
                if (GeneralManager.getInstance().getTemplatesTransfer().isEmpty() || GeneralManager.isLocaleChanged()) {
                    isTransferTemplate = false;
                    transferAndPayment.getTransferTemplate(getActivity());
                }
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
        tvTitle.setText(R.string.transfers);
    }

    public void setAdapter() {
        if(isAdded()) {
            objects = GeneralManager.getInstance().getTransfersAndTwoTransTemplate(getActivity());
            adapter = new TransferFragAdapter(getActivity(), objects, setOnClick(), 0);
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
