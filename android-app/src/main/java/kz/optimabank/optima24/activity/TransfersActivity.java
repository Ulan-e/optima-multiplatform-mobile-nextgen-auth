package kz.optimabank.optima24.activity;

import static kz.optimabank.optima24.activity.AccountDetailsActivity.ACCOUNT;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.confirm.ParentSmsConfirmFragment;
import kz.optimabank.optima24.fragment.references.TransferList;
import kz.optimabank.optima24.fragment.references.TransferReferencesAccountsFragment;
import kz.optimabank.optima24.fragment.transfer.TransferAccountsFragment;
import kz.optimabank.optima24.fragment.transfer.TransferConvertCurrency;
import kz.optimabank.optima24.fragment.transfer.TransferElcartToElcartFragment;
import kz.optimabank.optima24.fragment.transfer.TransferInterbank;
import kz.optimabank.optima24.fragment.transfer.TransferInterbankCurrency;
import kz.optimabank.optima24.fragment.transfer.TransferPhoneNumberFragment;
import kz.optimabank.optima24.fragment.transfer.TransferVisaToVisaFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;

public class TransfersActivity extends OptimaActivity {
    public static final String TRANSFER_NAME = "transferName";
    public static final String TRANSFER_TYPE = "transfer";
    public static final String USER_ACCOUNT = "userAccount";
    UserAccounts account;
    String transferName;
    boolean isReferences, Replenish, acTo, acToO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.accounts_detail_activity);

        Intent intent = getIntent();
        transferName = intent.getStringExtra(TRANSFER_NAME);
        isReferences = intent.getBooleanExtra("isReferences", false);
        acTo = intent.getBooleanExtra("acTo", false);
        acToO = intent.getBooleanExtra("acToO", false);
        Replenish = intent.getBooleanExtra("Replenish", false);
        Log.d("TAG", "isReferences = " + isReferences);
        ATFFragment fragment = null;
        if (transferName != null) {
            if (transferName.equals(getResources().getString(R.string.transfer_card))) {
                fragment = new TransferAccountsFragment();
                fragment.setArguments(getBundle());
            } else if (transferName.equals(getResources().getString(R.string.transfer_swift_tenge)) || transferName.equals(getResources().getString(R.string.transfer_swift_tengeN))) {
                fragment = new TransferInterbank();
                fragment.setArguments(getBundle());
            } else if (transferName.equals(getResources().getString(R.string.transfer_swift))) {
                fragment = new TransferInterbankCurrency();
                fragment.setArguments(getBundle());
            } else if (transferName.equals(getResources().getString(R.string.transfer_card_visa_to_visa_for_item))) {
                fragment = new TransferVisaToVisaFragment();
                fragment.setArguments(getBundle());
            } else if (transferName.equals(getResources().getString(R.string.transfer_card_elcard_to_elcard_for_item))) {
                fragment = new TransferElcartToElcartFragment();
                fragment.setArguments(getBundle());
            } else if (transferName.equals(getResources().getString(R.string.transfer_phone_number))) {
                fragment = new TransferPhoneNumberFragment();
                fragment.setArguments(getBundle());
            } else if (transferName.equals(getResources().getString(R.string.transfer_multi_card))) {
                fragment = new TransferConvertCurrency();
                Bundle bundle = new Bundle();
                bundle.putString(TRANSFER_NAME, transferName);
                bundle.putSerializable(USER_ACCOUNT, intent.getSerializableExtra(USER_ACCOUNT));
                fragment.setArguments(bundle);
            }
        } else if (isReferences) {
            account = (UserAccounts) intent.getSerializableExtra(ACCOUNT);
            Log.d("TAG", "account instanceof  UserAccounts.Cards = " + account.getClass());

            if (Replenish) {
                fragment = new TransferReferencesAccountsFragment();
                transferName = getResources().getString(R.string.transfer_card);
            } else {
                if (account instanceof UserAccounts.Cards || account instanceof UserAccounts.DepositAccounts) {
                    Log.i("CardsOrDepositAccounts", "CardsOrDepositAccounts");
                    fragment = new TransferReferencesAccountsFragment();
                    transferName = getResources().getString(R.string.transfer_card);
                } else {
                    fragment = new TransferList();
                }
            }
            if (fragment != null) {
                fragment.setArguments(getBundle());
            }
        }
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_content, fragment);
            ft.commit();
        }


        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GeneralManager.getInstance().getSessionId() == null) {
            finish();
        }
    }

    public void navigateToPage(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fragment_content, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        if (isReferences) {
            bundle.putSerializable(ACCOUNT, account);
            bundle.putBoolean("Replenish", Replenish);
            Log.i("acTo11", "acToTA = " + acTo);
            bundle.putBoolean("acTo", acTo);
            bundle.putBoolean("acToO", acToO);
            Log.i("Replenish", "Replenish = " + Replenish);
        }
        bundle.putString(TRANSFER_NAME, transferName);
        return bundle;
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
