package kz.optimabank.optima24.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.WindowManager;

import kg.optima.mobile.R;
import kz.optimabank.optima24.fragment.settings.CardLimitFragment;
import kz.optimabank.optima24.fragment.settings.CardLockFragment;
import kz.optimabank.optima24.fragment.settings.SettingsFragment;
import kz.optimabank.optima24.model.gson.response.UserAccounts;
import kz.optimabank.optima24.model.manager.GeneralManager;

/**
 * Created by Timur on 04.07.2017.
 */

public class SettingsActivity extends OptimaActivity {

    UserAccounts.Cards ACCOUNT;
    boolean CardSett;
    boolean isSelectedDefaultCard;
    int code;

    private static final String CODE = "code";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACCOUNT = (UserAccounts.Cards) getIntent().getSerializableExtra("account");
        CardSett = getIntent().getBooleanExtra("CardSett", false);
        isSelectedDefaultCard = getIntent().getBooleanExtra("selectedDefaultCard", false);
        setContentView(R.layout.accounts_detail_activity);
        navigateToPage(new SettingsFragment());
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
        if (CardSett) {
            Bundle bundle = new Bundle();
            bundle.putInt(CODE, getIntent().getIntExtra(CODE, 0));
            bundle.putSerializable("account", ACCOUNT);
            bundle.putBoolean("CardSett", CardSett);
            bundle.putBoolean("selectedDefaultCard", isSelectedDefaultCard);
            fragment.setArguments(bundle);
        }
        if (fragment instanceof CardLockFragment)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void navigateToSettingsPage(Fragment fragment) {
        if (getIntent() != null) {
            code = getIntent().getIntExtra("code", -1);
            Bundle bundle = new Bundle();
            bundle.putSerializable("code", code);
            fragment.setArguments(bundle);
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        ft.replace(R.id.fragment_content, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void navigateToSettingsPage(int type) {
        Fragment fragment = new CardLimitFragment();
        if (getIntent() != null) {
            code = getIntent().getIntExtra("code", -1);
            Bundle bundle = new Bundle();
            bundle.putSerializable("code", code);
            bundle.putSerializable("type", type);
            fragment.setArguments(bundle);
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        ft.replace(R.id.fragment_content, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
