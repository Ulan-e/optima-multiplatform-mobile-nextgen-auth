package kz.optimabank.optima24.activity;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;
import static kz.optimabank.optima24.activity.PaymentActivity.CATEGORY_NAME;
import static kz.optimabank.optima24.fragment.CustomListFragment.CUSTOM_LIST_EXTRA;
import static kz.optimabank.optima24.utility.Constants.DATE_TAG;
import static kz.optimabank.optima24.utility.Constants.HISTORY_KEY;
import static kz.optimabank.optima24.utility.Constants.TEMPLATE_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.CustomListFragment;
import kz.optimabank.optima24.fragment.ListFragment;
import kz.optimabank.optima24.fragment.WebFragment;
import kz.optimabank.optima24.fragment.confirm.SuccessOperation;
import kz.optimabank.optima24.fragment.history.PaymentHistoryDetailsFragment;
import kz.optimabank.optima24.fragment.history.SelectDateFragment;
import kz.optimabank.optima24.fragment.history.TransferHistoryDetailsFragment;
import kz.optimabank.optima24.fragment.news.FragmentNewsInside;
import kz.optimabank.optima24.fragment.news.FragmentNewsItem;
import kz.optimabank.optima24.fragment.payment.PaymentFragment;
import kz.optimabank.optima24.fragment.payment.invoice.FindInvoiceAccountFragment;
import kz.optimabank.optima24.fragment.rate.RateFragmentM;
import kz.optimabank.optima24.fragment.references.RequisitesFragment;
import kz.optimabank.optima24.fragment.requests.CancelFragment;
import kz.optimabank.optima24.fragment.requests.RequestsContainer;
import kz.optimabank.optima24.fragment.service_point.ListAndMapContainer;
import kz.optimabank.optima24.fragment.settings.CommunicationsFragment;
import kz.optimabank.optima24.fragment.transfer.InformationFragment;
import kz.optimabank.optima24.fragment.transfer.SelectCurrencyForConvertFragment;
import kz.optimabank.optima24.model.base.HistoryItem;
import kz.optimabank.optima24.model.base.Templates;
import kz.optimabank.optima24.model.gson.response.UserAccounts;

/**
 * Created by Timur on 31.05.2017.
 */

public class NavigationActivity extends OptimaActivity {
    UserAccounts userAccounts;
    ATFFragment fragment;
    Templates template;
    HistoryItem historyItem;
    String dateTag, textInfo, selectedCurrency;
    private UserAccounts.Cards convertCard;
    boolean isMap, isHistoryTransfer, isHistoryPayment, selectDate, isInformation, isConvertCurrency, isNews, isNotice,
            isRequests, isContact, isUserAccounts, isNewsList, isRate, isToPayFrag, isToFindInvoiceAccountFragment, isShowWebView,
            isMessage, isCustomList, isApplication, isComment, isSucces, isPdfView, isContactlessLogin, selectDefaultCard, isFromUnAuthContacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_detail_activity);

        Intent intent = getIntent();
        isMap = intent.getBooleanExtra("isMap", false);
        isUserAccounts = intent.getBooleanExtra("UserAccountsB", false);
        Log.i("UserAccounts", "isUserAccounts = " + isUserAccounts);
        isRequests = intent.getBooleanExtra("isRequests", false);
        isToPayFrag = intent.getBooleanExtra("isToPayFrag", false);
        isToFindInvoiceAccountFragment = intent.getBooleanExtra("isToFindInvoiceAccountFragment", false);
        isHistoryTransfer = intent.getBooleanExtra("isHistoryTransfer", false);
        isHistoryPayment = intent.getBooleanExtra("isHistoryPayment", false);
        selectDate = intent.getBooleanExtra("selectDate", false);
        isContact = intent.getBooleanExtra("isContact", false);
        isInformation = intent.getBooleanExtra("isInformation", false);
        isConvertCurrency = intent.getBooleanExtra("isConvertCurrency", false);
        isNews = intent.getBooleanExtra("isNews", false);
        isNotice = intent.getBooleanExtra("isNotice", false);
        isNewsList = intent.getBooleanExtra("isNewsList", false);
        isRate = intent.getBooleanExtra("isRate", false);
        //isMessage = intent.getBooleanExtra(DetailsMessageActivity.IS_MESSAGE_EXTRA, false);
        isContactlessLogin = intent.getBooleanExtra("is_contactless_login", false);
        selectDefaultCard = intent.getBooleanExtra("selectDefaultCard", false);
        isCustomList = intent.getBooleanExtra("isCustomList", false);
        isApplication = intent.getBooleanExtra("isApplication", false);
        isComment = intent.getBooleanExtra("isComment", false);
        isSucces = intent.getBooleanExtra("isSucces", false);
        isShowWebView = intent.getBooleanExtra("isShowWebView", false);
        isPdfView = intent.getBooleanExtra("isPdfView", false);
        isFromUnAuthContacts = intent.getBooleanExtra("CONTACTS_FROM_UNAUTH", false);

        if (isMap) {
            fragment = new ListAndMapContainer();
        } else if (isNotice) {
            //fragment = new FragmentMessage();
        } else if (isToPayFrag) {
            fragment = new PaymentFragment();
        } else if (isToFindInvoiceAccountFragment) {
            fragment = new FindInvoiceAccountFragment();
        } else if (isRequests) {
            fragment = new RequestsContainer();
        } else if (isApplication) {
            fragment = new SuccessOperation();
        } else if (isSucces) {
            fragment = new SuccessOperation();
        } else if (isRate) {
            fragment = new RateFragmentM();
        } else if (isUserAccounts) {
            fragment = new ListFragment();
        } else if (isNewsList) {
            fragment = new FragmentNewsInside();
        } else if (isNews) {
            fragment = new FragmentNewsItem();
            //fragment = new WebFragment();
        } else if (isContact) {
            Log.e("c",""+isFromUnAuthContacts);
            if (isFromUnAuthContacts) {
                fragment = newInstance(true);
            } else {
                fragment = newInstance(false);
            }
        } else if (selectDate) {
            dateTag = intent.getStringExtra(DATE_TAG);
            fragment = new SelectDateFragment();
        } else if (isHistoryTransfer) {
            historyItem = (HistoryItem) intent.getSerializableExtra(HISTORY_KEY);
            fragment = new TransferHistoryDetailsFragment();
        } else if (isHistoryPayment) {
            historyItem = (HistoryItem) intent.getSerializableExtra(HISTORY_KEY);
            fragment = new PaymentHistoryDetailsFragment();
        } else if (isInformation) {
            textInfo = intent.getStringExtra("infoText");
            fragment = new InformationFragment();
        } else if (isConvertCurrency) {
            convertCard = (UserAccounts.Cards) intent.getSerializableExtra("convertCard");
            selectedCurrency = intent.getStringExtra("selectedFromCurrency");
            fragment = new SelectCurrencyForConvertFragment();
        } else if (isMessage) {
            //fragment = new DetailsMessageActivity();
        } else if (isCustomList) {
            fragment = new CustomListFragment();
        } else if (isComment) {
            fragment = new CancelFragment();
        } else if (isShowWebView) {
            fragment = new WebFragment();
        } else if (isPdfView) {
            // fragment = new PdfViewFragment();
        } else {
            userAccounts = (UserAccounts) intent.getSerializableExtra(ACCOUNT);
            template = (Templates) intent.getSerializableExtra(TEMPLATE_KEY);
            fragment = new RequisitesFragment();
        }
        fragment.setArguments(setBundle());
        navigateToPage(fragment, false);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!isNews && !isContactlessLogin && GeneralManager.getInstance().getSessionId() == null && !isCustomList && !isShowWebView && !isPdfView) {
//            finish();
//        }
    }

    public void navigateToPage(Fragment fragment, boolean addToBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fragment_content, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

/*    @Override
    public void onBackPressed() {
        if (isApplication){
            Intent intent = new Intent(this, NavigationActivity.class);
            intent.putExtra("isRequests", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        super.onBackPressed();
    }*/

    private Bundle setBundle() {
        Bundle bundle = new Bundle();
        if (userAccounts != null) {
            bundle.putString("numberFromAccountDetail", getIntent().getStringExtra("numberFromAccountDetail"));
            bundle.putString("isFrom", getIntent().getStringExtra("isFrom"));
            bundle.putSerializable(ACCOUNT, userAccounts);
        } else if (isUserAccounts) {
            Log.i("UserAccounts", "getBundle = " + getIntent().getStringExtra("UserAccounts"));
            bundle.putString("UserAccounts", getIntent().getStringExtra("UserAccounts"));
        } else if (template != null) {
            bundle.putSerializable(TEMPLATE_KEY, template);
        } else if (isToPayFrag || isToFindInvoiceAccountFragment) {
            bundle.putString(CATEGORY_NAME, getIntent().getStringExtra(CATEGORY_NAME));
            bundle.putInt("paymentServiceId", getIntent().getIntExtra("paymentServiceId", 0));
        } else if (isRate) {
            bundle.putSerializable("transform", true);
        } else if (isApplication) {
            bundle.putBoolean("isApplication", true);
        } else if (getIntent().getBooleanExtra("customText", false)) {
            bundle.putBoolean("customText", getIntent().getBooleanExtra("customText", false));
            bundle.putString("customTextS", getIntent().getStringExtra("customTextS"));
        } else if (historyItem != null) {
            bundle.putSerializable(HISTORY_KEY, historyItem);
        } else if (selectDate) {
            bundle.putString(DATE_TAG, dateTag);
        } else if (isInformation) {
            bundle.putString("infoText", textInfo);
        } else if (isConvertCurrency) {
            bundle.putSerializable("convertCard", convertCard);
            bundle.putString("selectedFromCurrency", selectedCurrency);
        } else if (isNews) {
            bundle.putSerializable("news", getIntent().getSerializableExtra("news"));
            bundle.putSerializable("HTML", getIntent().getSerializableExtra("HTML"));
        } else if (isMessage) {
            //bundle.putString(DetailsMessageActivity.ID_MESSAGE_EXTRA, getIntent().getStringExtra(DetailsMessageActivity.ID_MESSAGE_EXTRA));
        } else if (isCustomList) {
            bundle.putSerializable(CUSTOM_LIST_EXTRA, getIntent().getSerializableExtra(CUSTOM_LIST_EXTRA));
        } else if (isShowWebView) {
            bundle.putString("url", getIntent().getStringExtra("url"));
        } else {
            bundle.putBoolean("isMainPage", false);
        }
        return bundle;
    }

    public static CommunicationsFragment newInstance(boolean isFromUnAuthContacts) {
        CommunicationsFragment fragment = new CommunicationsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("CONTACTS_FROM_UNAUTH_ONBACKPRESED", isFromUnAuthContacts);
        fragment.setArguments(bundle);
        return fragment;
    }
}
