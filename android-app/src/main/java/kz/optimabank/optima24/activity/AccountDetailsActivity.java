package kz.optimabank.optima24.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.ATFFragment;
import kz.optimabank.optima24.fragment.account.AccountDetailsFragment;
import kz.optimabank.optima24.fragment.account.CardDetailsFragment;
import kz.optimabank.optima24.fragment.account.CreditDetailsFragment;
import kz.optimabank.optima24.fragment.account.DepositDetailsFragment;
import kz.optimabank.optima24.fragment.account.WishDetailsFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;
import kz.optimabank.optima24.utility.Constants;

public class AccountDetailsActivity extends OptimaActivity {
    public static final String ACCOUNT = "account";
    UserAccounts account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_detail_activity);
        Intent intent = getIntent();
        account = (UserAccounts) intent.getSerializableExtra(ACCOUNT);
        switchFragment();
    }

    public void switchFragment() {
        ATFFragment fragment = null;
        if(account instanceof UserAccounts.Cards) {
            fragment = new CardDetailsFragment();
        } else if(account instanceof UserAccounts.CardAccounts || account instanceof UserAccounts.CheckingAccounts) {
            fragment = new AccountDetailsFragment();
        } else if(account instanceof UserAccounts.DepositAccounts) {
            UserAccounts.DepositAccounts depositAccounts = (UserAccounts.DepositAccounts) account;
            if(depositAccounts instanceof UserAccounts.WishAccounts) {
                fragment = new WishDetailsFragment();
            } else {
                fragment = new DepositDetailsFragment();
            }
        } else if(account instanceof UserAccounts.CreditAccounts) {
            fragment = new CreditDetailsFragment();
        }
        if(fragment!=null) {
            fragment.setArguments(setBundle());
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_content, fragment);
            ft.commit();
        }
    }

    private Bundle setBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ACCOUNT,account);
        return bundle;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GeneralManager.getInstance().getSessionId() == null) {
            finish();
        }
    }

    public String dateFormat(String date) {
        String dateFormat = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            dateFormat = Constants.VIEW_DATE_FORMAT.format(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat;
    }

    public void navigateToSettingsPage(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        ft.replace(R.id.fragment_content, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
