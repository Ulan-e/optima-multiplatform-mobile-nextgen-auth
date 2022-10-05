package kz.optimabank.optima24.activity;

import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_NAME;
import static kz.optimabank.optima24.utility.Constants.CONNECTION_ERROR_STATUS;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.widget.HorizontalGridView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kg.optima.mobile.R;
import kz.optimabank.optima24.controller.adapter.GridViewAdapter;
import kz.optimabank.optima24.fragment.payment.invoice.AlsekoOwnerInfoFragment;
import kz.optimabank.optima24.fragment.payment.invoice.CommonInvoiceFragment;
import kz.optimabank.optima24.fragment.payment.invoice.FixedInvoiceFragment;
import kz.optimabank.optima24.fragment.payment.invoice.InvoiceAblePaymentFragment;
import kz.optimabank.optima24.model.base.InvoiceContainerItem;
import kz.optimabank.optima24.model.interfaces.Invoice;
import kz.optimabank.optima24.model.interfaces.OnItemClickListener;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.model.service.InvoiceImpl;

/**
  Created by Timur on 12.07.2017.
 */

public class InvoiceAblePaymentActivity extends OptimaActivity implements InvoiceImpl.Callback {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvTitleDate) TextView tvTitleDate;

    @BindView(R.id.gridView) HorizontalGridView gridView;

    GridViewAdapter gridAdapter;
    InvoiceContainerItem invoiceItem;
    String categoryName;
    Invoice invoice;
    ArrayList<Object> items;
    long invoiceId = 0;
    int paymentServiceId = -1000;
    CommonInvoiceFragment fragment;
    boolean isTemplate;
    public static boolean isAllIsPayTrue=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_able_activity);
        ButterKnife.bind(this);
        getIntentData();
        invoiceRequest();
        initToolbar();
        try {
            Log.i("IIOI", "Name = " + invoiceItem.ownerInfo.Name);
            Log.i("IIOI", "Address = " + invoiceItem.ownerInfo.Address);
            Log.i("IIOI", "Name = " + invoiceItem.ownerInfo.Name);
            Log.i("IIOI", "Name = " + invoiceItem.ownerInfo.Name);
        }catch (Exception ignored){}
    }

    private void setTitleDate() {
        String date = invoiceItem.invoiceBody.ExpireDate;
        date = date.substring(5,7);
        switch (Integer.valueOf(date)){
            case 1:
                date = getResources().getStringArray(R.array.month)[0].toLowerCase();
                break;
            case 2:
                date = getResources().getStringArray(R.array.month)[1].toLowerCase();
                break;
            case 3:
                date = getResources().getStringArray(R.array.month)[2].toLowerCase();
                break;
            case 4:
                date = getResources().getStringArray(R.array.month)[3].toLowerCase();
                break;
            case 5:
                date = getResources().getStringArray(R.array.month)[4].toLowerCase();
                break;
            case 6:
                date = getResources().getStringArray(R.array.month)[5].toLowerCase();
                break;
            case 7:
                date = getResources().getStringArray(R.array.month)[6].toLowerCase();
                break;
            case 8:
                date = getResources().getStringArray(R.array.month)[7].toLowerCase();
                break;
            case 9:
                date = getResources().getStringArray(R.array.month)[8].toLowerCase();
                break;
            case 10:
                date = getResources().getStringArray(R.array.month)[9].toLowerCase();
                break;
            case 11:
                date = getResources().getStringArray(R.array.month)[10].toLowerCase();
                break;
            case 12:
                date = getResources().getStringArray(R.array.month)[11].toLowerCase();
                break;
        }
        tvTitleDate.setText(getString(R.string.receipt)+" "+getString(R.string.behind)+" "+date+ " " + invoiceItem.invoiceBody.ExpireDate.substring(0,4));//
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GeneralManager.getInstance().setInvoiceAccountFrom(null);
    }

    @Override
    public void jsonInvoiceResponse(int statusCode, String errorMessage, InvoiceContainerItem response) {
        if(statusCode == 200) {
            if(response != null) {
                this.invoiceItem = response;
                setTemplateGridAdapter();
            }
        } else if(statusCode != CONNECTION_ERROR_STATUS) {
            new AlertDialog.Builder(this)
                    .setMessage(errorMessage)
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_dialog_alert)
                    .setTitle(R.string.alert_error)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.cancel();
                                }
                            }).create().show();
        }
    }

    private void invoiceRequest() {
        if(invoiceId != 0) {
            invoice = new InvoiceImpl();
            invoice.registerCallBack(this);
            invoice.getInvoice(this, invoiceId);
        }
    }

    public void initToolbar() {
        toolbar.setTitle("");
        tvTitle.setText(categoryName);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GeneralManager.getInstance().isInvoiceCheckedStatus()){
                    if(fragment!=null) {
                        fragment.actionBack();
                    }
                } else {
                    onBackPressed();
                }
            }
        });
    }

    public void setTemplateGridAdapter() {
        gridAdapter = new GridViewAdapter(this,getGridItems(), setOnClick());
        gridView.setAdapter(gridAdapter);
        if(gridAdapter.getItemCount() > 0) {
            initFragment(0);
        }
    }

    private ArrayList<Object> getGridItems() {
       items = new ArrayList<>();
        if(invoiceItem != null && !invoiceItem.invoiceBody.getItems().isEmpty()) {
            items.add(new InvoiceContainerItem.BodyItem(getString(R.string.alseco_account)));
            setTitleDate();
            for (InvoiceContainerItem.BodyItem invoiceBodyItem : invoiceItem.invoiceBody.getItems()) {
                // set invoiceBodyItem params
                invoiceBodyItem.setUsePenalty(true);
                invoiceBodyItem.setUseDebt(true);
                invoiceBodyItem.setNotUseDebtForFixInvoice(false);
                invoiceBodyItem.setServiceSum(-1000);

                Log.i("isCounterService","isCounterService = " + invoiceBodyItem.isCounterService());
                Log.i("getServiceName","getServiceName = " + invoiceBodyItem.getServiceName());
                Log.i("getServiceId","getServiceId = " + invoiceBodyItem.getServiceId());
                if(invoiceBodyItem.isCounterService()) {
                    items.add(invoiceBodyItem);
                }
            }
            items.add(new InvoiceContainerItem.BodyItem(getString(R.string.fixed_payments)));
        }
        return items;
    }

    private OnItemClickListener setOnClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                gridView.getLayoutManager().scrollToPosition(position);
                initFragment(position);
            }
        };
    }

    private void initFragment(int position) {
        InvoiceContainerItem.BodyItem bodyItem = null;
        try {
            if(items!=null && !items.isEmpty()) {
                bodyItem = (InvoiceContainerItem.BodyItem) items.get(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("invoice",invoiceItem);
        bundle.putInt("itemPosition",position-1);
        bundle.putInt("paymentServiceId",paymentServiceId);
        bundle.putLong("InvoiceId",invoiceId);

        Log.i("ITINVO","isTemplate IAPA = "+isTemplate);
        bundle.putBoolean("isTemplate",isTemplate);
        bundle.putString(CATEGORY_NAME,categoryName);
        if(position == 0) {
            fragment = new AlsekoOwnerInfoFragment();
        } else if (bodyItem != null && !bodyItem.isCounterService()) {
            fragment = new FixedInvoiceFragment();
        } else {
            fragment = new InvoiceAblePaymentFragment();
        }
        fragment.setArguments(bundle);
        navigateToPage(fragment);
    }

    public void navigateToPage(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fragment_content, fragment);
        ft.commit();
    }

    private void getIntentData() {
        categoryName = getIntent().getStringExtra(CATEGORY_NAME);
        invoiceId = getIntent().getLongExtra("InvoiceId", 0);
        paymentServiceId = getIntent().getIntExtra("paymentServiceId", -1000);
        Log.i("ITINVO","isTemplate IAPAgetIntent = "+getIntent().getBooleanExtra("isTemplate",false));
        isTemplate = getIntent().getBooleanExtra("isTemplate",false);
    }
}
