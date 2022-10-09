package kz.optimabank.optima24.activity;

import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_NAME;
import static kz.optimabank.optima24.utility.Constants.TAG;
import static kz.optimabank.optima24.utility.Constants.TransferMoneyToAnotherBank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import kg.optima.mobile.R;
import kz.optimabank.optima24.db.controllers.PaymentContextController;
import kz.optimabank.optima24.db.entry.PaymentService;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.confirm.ParentSmsConfirmFragment;
import kz.optimabank.optima24.fragment.confirm.SuccessOperation;
import kz.optimabank.optima24.fragment.payment.PaymentTemplateFragment;
import kz.optimabank.optima24.fragment.payment.invoice.ChangeInvoiceTemplate;
import kz.optimabank.optima24.fragment.template.TemplateContainer;
import kz.optimabank.optima24.fragment.transfer.TransferInterbankCurrencyTemplate;
import kz.optimabank.optima24.fragment.transfer.TransferInterbankTemplate;
import kz.optimabank.optima24.fragment.transfer.TransferTemplateFragment;
import kz.optimabank.optima24.model.base.TemplateTransfer;
import kz.optimabank.optima24.model.base.Templates;
import kz.optimabank.optima24.model.base.TemplatesPayment;
import kz.optimabank.optima24.model.gson.response.Invoices;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;

public class TemplateActivity extends OptimaActivity {
    boolean isMainPage, isInvoice;
    Templates template;
    ATFFragment fragment;
    PaymentContextController paymentController;
    SuccessOperation successOperation = new SuccessOperation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_detail_activity);

        Intent intentAtAdap = getIntent();
        Bundle bundleAtAdap = intentAtAdap.getBundleExtra("getBundle");
        String boolAtAdap = intentAtAdap.getStringExtra("isADAP");
        String isTTF = intentAtAdap.getStringExtra("isTTF");
        String stringAtAdap = intentAtAdap.getStringExtra("whatIs");
        String isTransferAtTempl = intentAtAdap.getStringExtra("isTransferAtTempl");
        isMainPage = intentAtAdap.getBooleanExtra("isMainPage", false);
        int isTTA = intentAtAdap.getIntExtra("isTTA",9);
        Log.i("bundleAtAdap","bundleAtAdap = " + bundleAtAdap);
        Log.i("boolAtAdap","boolAtAdap = " + boolAtAdap);
        Log.i("isTTF","isTTF = " + isTTF);

        //isMainPage = getIntent().getBooleanExtra("isMainPage",false);
        Log.i("isMainPage","isMainPage - TemplateActivity = "+isMainPage);
        isInvoice = getIntent().getBooleanExtra("isInvoice",false);

        //if (boolAtAdap!=null)
        if (boolAtAdap.equals("1")) {
            Log.i("stringAtAdap", "stringAtAdap = " + stringAtAdap);
            switch (stringAtAdap) {
                case "CIT":
                    fragment = new ChangeInvoiceTemplate();
                    break;
                case "PTF":
                    fragment = new PaymentTemplateFragment();
                    break;
                case "TIT":
                    fragment = new TransferInterbankTemplate();
                    break;
                case "TTF":
                    fragment = new TransferTemplateFragment();
                    break;
                case "TIC":
                    fragment = new TransferInterbankCurrencyTemplate();
                    break;
            }
            bundleAtAdap.putString("isTTF",isTTF);
            Log.i("isTTF",""+isTTF);
            fragment.setArguments(bundleAtAdap);
            navigateToPage(fragment, false);
        }else{
            Log.i("isMainPage","isMainPage = "+isMainPage);
            if (!isMainPage) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                fragment = new TemplateContainer();
                /*if (isTransferAtTempl!=null)
                    if (isTransferAtTempl.equals("1")){
                        Bundle isTAT = new Bundle();
                        isTAT.putString("isTransferAtTempl",isTransferAtTempl);
                        fragment.setArguments(isTAT);
                    }*/
                Log.i("isTTA","isTTA - TemplateActivity = "+isTTA);
                    if (isTTA==1){
                        Bundle isTAT = new Bundle();
                        isTAT.putInt("isTTA",1);
                        fragment.setArguments(isTAT);
                    }else if (isTTA == 0){
                        Bundle isTAT = new Bundle();
                        isTAT.putInt("isTTA",0);
                        fragment.setArguments(isTAT);
                    }
                ft.add(R.id.fragment_content, fragment);
                ft.commit();
            } else {
                template = (Templates) getIntent().getSerializableExtra("template");
                //Log.i("template","template = " + template);
                //Log.i("getIntent().getSeriali","getIntent().getSerializableExtra(template); = " + getIntent().getSerializableExtra("template"));
                if (template != null) {
                    if (template instanceof TemplatesPayment) {
                        if (isInvoice) {
                            Log.i("ITINVO","isTemplate Bil here TA");
                            Intent intent = new Intent(TemplateActivity.this, InvoiceAblePaymentActivity.class);
                            intent.putExtra("InvoiceId", getIntent().getLongExtra("InvoiceId", 0));
                            intent.putExtra("paymentServiceId", getIntent().getIntExtra("paymentServiceId", 0));

                            intent.putExtra(CATEGORY_NAME, getIntent().getStringExtra(CATEGORY_NAME));
                            intent.putExtra("isTemplate", true);
                            startActivity(intent);
                            return;
                        } else {
                            fragment = new PaymentTemplateFragment();
                        }
                    } else if (template instanceof TemplateTransfer) {
                        TemplateTransfer templatesTransfer = (TemplateTransfer) template;
                        Log.d(TAG, "templatesTransfer.getProductType() = " + templatesTransfer.getProductType());
                        if (templatesTransfer.getProductType() == TransferMoneyToAnotherBank) {
                            fragment = new TransferInterbankTemplate();
                        } else {
                            fragment = new TransferTemplateFragment();
                        }
                    }
                }
                if (fragment != null) {
                    fragment.setArguments(getBundle(Constants.CLICK_ITEM_TAG, template));
                    navigateToPage(fragment, false);
                }
            }
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        successOperation.isTempl = true;
        Log.i("Operation.isTempl","successOperation.isTempl = " + successOperation.isTempl);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GeneralManager.getInstance().getSessionId() == null) {
            finish();
        }
    }

    public void navigateToPage(Fragment fragment, boolean isNullAddToBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.fragment_content, fragment);
        if(isNullAddToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    private Bundle getBundle(int tag, Templates templates) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("template",templates);
        bundle.putInt("actionTag",tag);
        return bundle;
    }

    public int checkInvoice(Templates template, int tag) {
        if(template instanceof TemplatesPayment) {
            TemplatesPayment templatesPayment = (TemplatesPayment) template;
            paymentController = PaymentContextController.getController();
            PaymentService paymentService = paymentController.getPaymentServiceById(templatesPayment.serviceId);
            Log.d("TemplateActivity", "paymentService = " + paymentService);
            if (paymentService!=null)
            if(paymentService.IsInvoiceable) {
                if(tag == Constants.TAG_CHANGE) {
                    return 1;
                }
               //Log.i("templatesPayment","templatesPayment.id = "+templatesPayment.id);
               //Log.i("templatesPayment","templatesPayment.name = "+templatesPayment.name);
               //Log.i("templatesPayment","templatesPayment.serviceId = "+templatesPayment.serviceId);
               //Log.i("templatesPayment","templatesPayment.sourceAccountId = "+templatesPayment.sourceAccountId);
               //Log.i("templatesPayment","templatesPayment.amount = "+templatesPayment.amount);
                for (Invoices invoice : GeneralManager.getInstance().getInvoices()) {
                    //Log.i("-----------------","-----------------");
                    //Log.i("invoice","invoice.getSubscriptionId() = "+invoice.getSubscriptionId());
                    //Log.i("invoice","invoice = "+invoice);
                    //Log.i("invoice","invoice.getStatus = "+invoice.getStatus());
                    //Log.i("invoice","invoice.getInvoiceId = "+invoice.getInvoiceId());
                    //Log.i("invoice","invoice.amount = "+invoice.getAmount());
                    //Log.i("-----------------","-----------------");
                    if(invoice.getSubscriptionId() == templatesPayment.id) {
                        Intent intent = new Intent(TemplateActivity.this, InvoiceAblePaymentActivity.class);
                        intent.putExtra("InvoiceId",invoice.getInvoiceId());
                        intent.putExtra("isTemplate",true);
                        intent.putExtra("paymentServiceId", paymentService.id);
                        intent.putExtra(CATEGORY_NAME,paymentService.name);
                        startActivity(intent);
                        //this invoice, there is a receipt
                        return 0;
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.not_invoices));
                builder.setPositiveButton(getString(R.string.status_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishEvent(ParentSmsConfirmFragment.FinishActivityEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
