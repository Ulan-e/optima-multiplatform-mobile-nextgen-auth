package kz.optimabank.optima24.activity;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.payment.PaymentFragment;
import kz.optimabank.optima24.fragment.payment.ServiceListFragment;
import kz.optimabank.optima24.fragment.references.PaymentListFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;

public class PaymentActivity extends OptimaActivity {
    public static final String CATEGORY_ID = "categoryId";
    public static final String EXTERNAL_ID = "externalId";
    public static final String CATEGORY_ALIAS = "mobile";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String REGION_ID = "regionId";

    public static int categoryId;
    static String categoryName;
    static String categoryAlias;
    static String categoryExternalId;
    boolean isAutoPayment, isReferences;
    UserAccounts account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_detail_activity);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        categoryId = getIntent().getIntExtra(CATEGORY_ID, 1);
        categoryExternalId = getIntent().getStringExtra(EXTERNAL_ID);
//        if(!getIntent().getStringExtra(EXTERNAL_ID).isEmpty()){
//                    Log.e("cExternalIdACTIVITY",getIntent().getStringExtra(EXTERNAL_ID));
//
//        }else{
//                    Log.e("cExternalIdACTIVITY","falseee");
//
//        }
        categoryName = getIntent().getStringExtra(CATEGORY_NAME);
        categoryAlias = getIntent().getStringExtra(CATEGORY_ALIAS);
        isAutoPayment = getIntent().getBooleanExtra(Constants.EXTRA_DATA, false);
        isReferences = getIntent().getBooleanExtra("isReferences", false);


        ATFFragment fragment;
        if (categoryAlias != null && categoryAlias.equals("mobile")) {
            if (isAutoPayment) {
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isMobile", true);
                bundle.putString(CATEGORY_NAME, categoryName);
                bundle.putString(EXTERNAL_ID, categoryExternalId);
                fragment = new PaymentFragment();
                fragment.setArguments(bundle);
                navigateToPage(fragment, false);
            }
        } else if (isReferences) {
            account = (UserAccounts) getIntent().getSerializableExtra(ACCOUNT);
            fragment = new PaymentListFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ACCOUNT, account);
            bundle.putString(EXTERNAL_ID, categoryExternalId);
            bundle.putString(CATEGORY_NAME, categoryName);
            fragment.setArguments(bundle);
            navigateToPage(fragment, false);
        } else {
            Log.d("TAG", "categoryId = " + categoryId);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new ServiceListFragment();
            fragment.setArguments(getBundle());
            ft.add(R.id.fragment_content, fragment);
            ft.commit();
        }
    }

    public void navigateToPage(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        ft.replace(R.id.fragment_content, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void navigateToPage(Fragment fragment, String name, int ID) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt("paymentServiceId", ID);
        bundle.putString(CATEGORY_NAME, name);
        bundle.putString(EXTERNAL_ID, categoryExternalId);
        bundle.putString(CATEGORY_NAME, categoryName);
        fragment.setArguments(bundle);
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        ft.replace(R.id.fragment_content, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void navigateToPage(Fragment fragment, boolean addToBackStack) {
//        if(isReferences) {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(ACCOUNT, account);
//            fragment.setArguments(bundle);
//        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fragment_content, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GeneralManager.getInstance().getSessionId() == null) {
            finish();
        }
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_NAME, categoryName);
        bundle.putInt(CATEGORY_ID, categoryId);
        bundle.putString(EXTERNAL_ID, categoryExternalId);
        bundle.putBoolean("isAutoPayment", isAutoPayment);
        bundle.putInt("state", 1);
        return bundle;
    }
}
